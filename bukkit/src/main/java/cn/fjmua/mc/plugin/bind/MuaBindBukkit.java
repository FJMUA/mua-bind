package cn.fjmua.mc.plugin.bind;

import cn.fjmua.mc.plugin.bind.api.MuaBindInst;
import cn.fjmua.mc.plugin.bind.api.MuaBindStatic;
import cn.fjmua.mc.plugin.bind.config.ConfigFile;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

@Getter
public class MuaBindBukkit extends JavaPlugin implements MuaBindInst {

    private static MuaBindBukkit instance;

    private Path pluginDirPath;
    private ConfigFile configFile;

    @Override
    public void onEnable() {
        MuaBindStatic.setInstance(instance = this);
        pluginDirPath = getDataFolder().toPath();
        configFile = new ConfigFile("config.yml", getPluginDirPath());
        initJDBC();
//        getServer().getPluginManager().registerEvents();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(instance);
        MuaBindStatic.setInstance(instance = null);
    }

}
