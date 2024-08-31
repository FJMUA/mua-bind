package cn.fjmua.mc.plugin.bind.api;

import cn.fjmua.mc.plugin.bind.config.ConfigFile;
import cn.fjmua.mc.plugin.bind.dal.StorageType;
import cn.fjmua.mc.plugin.bind.dal.driver.MySQLDriver;
import cn.fjmua.mc.plugin.bind.dal.driver.SQLiteDriver;
import cn.fjmua.mc.plugin.bind.util.Assert;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface MuaBindInst {

    /**
     * 获取插件配置文件夹
     * */
    @NotNull
    Path getPluginDirPath();

    /**
     * 获取配置文件
     * */
    @NotNull
    ConfigFile getConfigFile();

    /**
     * 初始化数据库配置
     * @apiNote 插件加载时自动调用
     * */
    default void initJDBC() {
        String typeStr = Configuration.getReadOnlyConfig().getStorage().getType();
        StorageType type = StorageType.parse(typeStr);
        Assert.notNull(type, "Unknown storage type: %s", typeStr);
        if (type == StorageType.SQLITE) {
            MuaBindStatic.setDataSource(SQLiteDriver.getDataSource());
        } else if (type == StorageType.MYSQL) {
            MuaBindStatic.setDataSource(MySQLDriver.getDataSource());
        }
    }

}
