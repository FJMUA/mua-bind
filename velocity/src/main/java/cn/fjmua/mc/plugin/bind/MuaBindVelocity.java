package cn.fjmua.mc.plugin.bind;

import cn.fjmua.mc.plugin.bind.listener.PlayerListener;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "mua-bind",
        name = "MuaBind",
        version = "1.0.0",
        authors = {"IllTamer"},
        url = "github.com/FJMUA/mua-bind"
)
public class MuaBindVelocity {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDir;

    @Inject
    public MuaBindVelocity(
            ProxyServer server,
            Logger logger,
            @DataDirectory Path dataDir
    ) {
        this.server = server;
        this.logger = logger;
        this.dataDir = dataDir;

        logger.info("Hello there! I made my first plugin with Velocity.");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new PlayerListener());
    }

}
