package cn.fjmua.mc.plugin.bind;

import cn.fjmua.mc.plugin.bind.api.MuaBindInst;
import cn.fjmua.mc.plugin.bind.api.MuaBindStatic;
import cn.fjmua.mc.plugin.bind.cmd.CommandHelper;
import cn.fjmua.mc.plugin.bind.config.ConfigFile;
import cn.fjmua.mc.plugin.bind.hook.PlaceholderAPIHook;
import cn.fjmua.mc.plugin.bind.listener.PlayerListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

@Slf4j
@Getter
public class MuaBindBukkit extends JavaPlugin implements MuaBindInst {

    @Getter
    private static MuaBindBukkit instance;
    private static PlaceholderAPIHook papiHook;

    private Path pluginDirPath;
    private ConfigFile configFile;

    @Override
    public void onEnable() {
        MuaBindStatic.setInstance(instance = this);
        pluginDirPath = getDataFolder().toPath();
        configFile = new ConfigFile("config.yml", getPluginDirPath());
        initJDBC();
        loadHooks();
        PluginCommand command = getServer().getPluginCommand("MuaBind");
        CommandHelper helper = new CommandHelper();
        command.setTabCompleter(helper);
        command.setExecutor(helper);
        getServer().getPluginManager().registerEvents(new PlayerListener(), instance);
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, UUIDCache::refreshCache, 0L, 60 * 20L);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(instance);
        unloadHooks();
        HandlerList.unregisterAll(instance);
        MuaBindStatic.setInstance(instance = null);
    }

    protected void loadHooks() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiHook = new PlaceholderAPIHook();
            papiHook.register();
            log.info("检测到 PlaceholderAPI, 已挂钩");
        } else {
            log.info("未检测到 PlaceholderAPI");
        }
        if (getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            MuaBindStatic.setLuckPermsHook(true);
            log.info("检测到 LuckPerms, 已挂钩");
        } else {
            log.info("未检测到 LuckPerms");
        }
    }

    protected void unloadHooks() {
        if (papiHook != null) {
            papiHook.unregister();
        }
    }

    @Override
    public @NotNull Type getType() {
        return Type.BUKKIT;
    }

}
