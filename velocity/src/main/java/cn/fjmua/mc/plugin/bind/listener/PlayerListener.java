package cn.fjmua.mc.plugin.bind.listener;

import cn.fjmua.mc.plugin.bind.UUIDHandler;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;

public class PlayerListener {

    @Subscribe(order = PostOrder.EARLY)
    public EventTask onLogin(LoginEvent event) {
        return EventTask.async(() ->
                UUIDHandler.tryRecord(event.getPlayer().getUniqueId()));
    }

}
