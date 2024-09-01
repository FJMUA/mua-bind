package cn.fjmua.mc.plugin.bind.cmd;

import cn.fjmua.mc.plugin.bind.MuaBindVelocity;
import cn.fjmua.mc.plugin.bind.UUIDLoadTask;
import cn.fjmua.mc.plugin.bind.api.Configuration;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommandHelper implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!invocation.source().hasPermission("muabind.admin")) {
            source.sendMessage(Component.text("无权限"));
            return;
        }
        if (args.length == 1) {
            if ("reload".equals(args[0])) {
                Configuration.reload();
                source.sendMessage(Component.text("配置文件重载成功"));
            }
        } else if (args.length == 2) {
            if ("load".equals(args[0])) {
                UUID uuid = null;
                Optional<Player> player = MuaBindVelocity.getInstance().getServer().getPlayer(args[1]);
                if (player.isPresent()) {
                    uuid = player.get().getUniqueId();
                }
                if (uuid == null) {
                    try {
                        uuid = UUID.fromString(args[1]);
                    } catch (Exception e) {
                        source.sendMessage(Component.text("未知的玩家 / 错误的uuid: " + args[1]));
                        return;
                    }
                }
                MuaBindVelocity instance = MuaBindVelocity.getInstance();
                instance.getServer().getScheduler()
                        .buildTask(instance, new UUIDLoadTask(uuid)).schedule();
                source.sendMessage(Component.text("发起异步任务加载玩家数据, uuid: " + uuid));
            }
        }
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("muabind.admin");
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            if (!invocation.source().hasPermission("muabind.admin")) {
                return List.of();
            }
            String[] args = invocation.arguments();
            if (args.length == 0) {
                return List.of("reload", "load");
            } else if (args.length == 1) {
                if ("load".equals(args[0])) {
                    return List.of("<playerName or uuid>");
                }
            }
            return List.of();
        });
    }

}
