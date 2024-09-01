package cn.fjmua.mc.plugin.bind.hook;

import cn.fjmua.mc.plugin.bind.api.Configuration;
import cn.fjmua.mc.plugin.bind.pojo.ConfigEntity;
import cn.fjmua.mc.plugin.bind.pojo.Pair;
import cn.fjmua.mc.plugin.bind.pojo.PlayerBind;
import lombok.extern.slf4j.Slf4j;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.node.ScopedNode;
import net.luckperms.api.util.Tristate;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class LuckPermsHook {

    private static final LuckPerms api = LuckPermsProvider.get();
    
    public static void setPermission(UUID uuid, PlayerBind playerBind) throws ExecutionException, InterruptedException, TimeoutException {
        User user = getUser(uuid);
        Pair<Node, Node> nodes = getExpectNodes(playerBind);

        boolean changed = false;
        if (!user.data().contains(nodes.k(), NodeEqualityPredicate.ONLY_KEY).asBoolean()) {
            user.data().add(nodes.k());
            changed = true;
        }
        if (!user.data().contains(nodes.v(), NodeEqualityPredicate.ONLY_KEY).asBoolean()) {
            user.data().add(nodes.v());
            changed = true;
        }
        if (changed) {
            api.getUserManager().saveUser(user).thenRun(() ->
                    log.info("玩家 (uuid: {}) 权限设置成功", uuid));
        }
    }

    public static User getUser(UUID uuid) throws ExecutionException, InterruptedException, TimeoutException {
        UserManager userManager = api.getUserManager();
        User user = userManager.getUser(uuid);
        if (user == null) {
            user = userManager.loadUser(uuid).get(3, TimeUnit.SECONDS);
        }
        return user;
    }

    private static Pair<Node, Node> getExpectNodes(PlayerBind playerBind) {
        ConfigEntity config = Configuration.getReadOnlyConfig();
        String prefix = config.getHook().getPermissionNodePrefix();
        String emailDomain = playerBind.getEmailDomain();
        String school = config.getSchoolMap().get(emailDomain);
        if (school == null) {
            school = config.getHook().getDefaultSchool();
        }

        ScopedNode<?, ?> titleNode1 = Node.builder(prefix + emailDomain).value(true).build();
        ScopedNode<?, ?> titleNode2 = Node.builder(prefix + school).value(true).build();
        return new Pair<>(titleNode1, titleNode2);
    }

}
