package cn.fjmua.mc.plugin.bind.listener;

import cn.fjmua.mc.plugin.bind.MuaBindBukkit;
import cn.fjmua.mc.plugin.bind.UUIDLoadTask;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(MuaBindBukkit.getInstance(),
                new UUIDLoadTask(event.getUniqueId()), 10L);
    }

}
