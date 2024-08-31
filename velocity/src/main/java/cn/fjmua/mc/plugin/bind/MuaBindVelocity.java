package cn.fjmua.mc.plugin.bind;

import cn.fjmua.mc.plugin.bind.api.MuaBindInst;
import cn.fjmua.mc.plugin.bind.api.MuaBindStatic;
import cn.fjmua.mc.plugin.bind.config.ConfigFile;
import cn.fjmua.mc.plugin.bind.listener.PlayerListener;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;

@Getter
@Plugin(
        id = "mua-bind",
        name = "MuaBind",
        version = "1.0.0",
        authors = {"IllTamer"},
        url = "github.com/FJMUA/mua-bind"
)
public class MuaBindVelocity implements MuaBindInst {

    private static MuaBindVelocity instance;
    private final ProxyServer server;
    private final Logger logger;

    private final Path pluginDirPath;
    private ConfigFile configFile;

    @Inject
    public MuaBindVelocity(
            ProxyServer server,
            Logger logger,
            @DataDirectory Path pluginDirPath
    ) {
        this.server = server;
        this.logger = logger;
        this.pluginDirPath = pluginDirPath;

        logger.info("Hello there! I made my first plugin with Velocity.");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        MuaBindStatic.setInstance(instance = this);
        configFile = new ConfigFile("config.yml", getPluginDirPath());
        initJDBC();
        server.getEventManager().register(instance, new PlayerListener());
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        server.getEventManager().unregisterListeners(instance);
        MuaBindStatic.setInstance(instance = null);
    }

}
