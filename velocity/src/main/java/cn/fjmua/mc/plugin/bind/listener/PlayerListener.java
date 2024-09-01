package cn.fjmua.mc.plugin.bind.listener;

import cn.fjmua.mc.plugin.bind.MuaBindVelocity;
import cn.fjmua.mc.plugin.bind.UUIDLoadTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;

public class PlayerListener {

    @Subscribe(order = PostOrder.FIRST)
    public void onLogin(LoginEvent event) {
        MuaBindVelocity instance = MuaBindVelocity.getInstance();
        instance.getServer().getScheduler().buildTask(instance,
                new UUIDLoadTask(event.getPlayer().getUniqueId())).schedule();
    }

}
