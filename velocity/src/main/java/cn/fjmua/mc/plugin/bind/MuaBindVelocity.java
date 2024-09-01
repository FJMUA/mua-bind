package cn.fjmua.mc.plugin.bind;

import cn.fjmua.mc.plugin.bind.api.MuaBindInst;
import cn.fjmua.mc.plugin.bind.api.MuaBindStatic;
import cn.fjmua.mc.plugin.bind.cmd.CommandHelper;
import cn.fjmua.mc.plugin.bind.config.ConfigFile;
import cn.fjmua.mc.plugin.bind.listener.PlayerListener;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;

@Slf4j
@Getter
@Plugin(
        id = "mua-bind",
        name = "MuaBind",
        version = MuaBindStatic.VERSION,
        dependencies = {
                @Dependency(id = "luckperms", optional = true)
        },
        authors = {"IllTamer"},
        url = "github.com/FJMUA/mua-bind"
)
public class MuaBindVelocity implements MuaBindInst {

    @Getter
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
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        MuaBindStatic.setInstance(instance = this);
        configFile = new ConfigFile("config.yml", getPluginDirPath());
        initJDBC();
        loadHooks();
        CommandManager cmdManager = server.getCommandManager();
        CommandMeta cmdMeta = cmdManager.metaBuilder("MuaBind")
                .aliases("muabind", "mb")
                .plugin(instance)
                .build();
        cmdManager.register(cmdMeta, new CommandHelper());
        server.getEventManager().register(instance, new PlayerListener());
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        server.getEventManager().unregisterListeners(instance);
        MuaBindStatic.setInstance(instance = null);
    }

    protected void loadHooks() {
        if (getServer().getPluginManager().getPlugin("LuckPerms").isPresent()) {
            MuaBindStatic.setLuckPermsHook(true);
            log.info("检测到 LuckPerms, 已挂钩");
        } else {
            log.info("未检测到 LuckPerms");
        }
    }

    @Override
    public @NotNull Type getType() {
        return Type.VELOCITY;
    }

}
