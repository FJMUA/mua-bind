package cn.fjmua.mc.plugin.bind;

import cn.fjmua.mc.plugin.bind.api.MuaBindStatic;
import cn.fjmua.mc.plugin.bind.hook.LuckPermsHook;
import cn.fjmua.mc.plugin.bind.pojo.PlayerBind;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@AllArgsConstructor
public class UUIDLoadTask implements Runnable {

    private final UUID uuid;

    @Override
    public void run() {
        try {
            PlayerBind playerBind = UUIDCache.getCache().get(uuid);
            if (MuaBindStatic.isLuckPermsHook()) {
                try {
                    LuckPermsHook.setPermission(uuid, playerBind);
                } catch (InterruptedException | TimeoutException e) {
                    log.warn("玩家 (uuid: {}) 权限数据加载超时", uuid);
                }
            }
        } catch (ExecutionException e) {
            log.warn("玩家数据加载失败", e);
        }
    }

}
