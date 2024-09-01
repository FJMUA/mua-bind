package cn.fjmua.mc.plugin.bind;

import cn.fjmua.mc.plugin.bind.api.API;
import cn.fjmua.mc.plugin.bind.api.Configuration;
import cn.fjmua.mc.plugin.bind.api.MuaBindInst;
import cn.fjmua.mc.plugin.bind.api.MuaBindStatic;
import cn.fjmua.mc.plugin.bind.dal.PlayerBindDAO;
import cn.fjmua.mc.plugin.bind.dto.QueryEmailResponse;
import cn.fjmua.mc.plugin.bind.pojo.PlayerBind;
import cn.fjmua.mc.plugin.bind.util.Assert;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UUIDCache {

    @Getter
    private static final LoadingCache<UUID, PlayerBind> cache;

    static {
        cache = CacheBuilder.newBuilder()
                // 定期刷新缓存
                .refreshAfterWrite(6, TimeUnit.SECONDS)
                .build(new CacheLoader<>() {
                    @Override
                    public @NotNull PlayerBind load(@NotNull UUID uuid) throws Exception {
                        PlayerBindDAO dao = MuaBindStatic.getPlayerBindDAO();
                        String uuidStr = uuid.toString();

                        MuaBindInst.Type type = MuaBindStatic.getInstance().getType();
                        Boolean velocity = Configuration.getReadOnlyConfig().getVelocity();
                        // bukkit-velocity mode
                        if (type == MuaBindInst.Type.BUKKIT && Boolean.TRUE.equals(velocity)) {
                            PlayerBind playerBind = dao.selectByUUID(uuidStr);
                            Assert.notNull(playerBind, "数据库无记录，请确认 storage 配置是否正常");
                            return playerBind;
                        }

                        PlayerBind playerBind = dao.selectByUUID(uuidStr);
                        if (playerBind != null) {
                            return playerBind;
                        }

                        QueryEmailResponse response = API.QUERY_EMAIL_BY_UUID.invoke(QueryEmailResponse.class, uuidStr.replace("-", ""));
                        playerBind = new PlayerBind();
                        playerBind.setUuid(uuidStr);
                        playerBind.setEmailDomain(response.getEmailDomain());
                        dao.insert(playerBind);
                        return playerBind;
                    }
                });
    }

    /**
     * 全量刷新所有缓存
     * @apiNote 代理端无需全量刷新
     * */
    public static void refreshCache() {
        Map<UUID, PlayerBind> map = doLoadAll();
        cache.invalidateAll();
        cache.putAll(map);
    }

    // TODO 增量刷新缓存

    private static Map<UUID, PlayerBind> doLoadAll() {
        PlayerBindDAO dao = MuaBindStatic.getPlayerBindDAO();
        return new HashMap<>(dao.selectAll().stream()
                .collect(Collectors.toMap(
                        pd -> UUID.fromString(pd.getUuid()),
                        pd -> pd
                )));
    }

}
