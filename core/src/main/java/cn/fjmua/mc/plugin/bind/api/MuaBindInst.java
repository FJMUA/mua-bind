package cn.fjmua.mc.plugin.bind.api;

import cn.fjmua.mc.plugin.bind.config.ConfigFile;
import cn.fjmua.mc.plugin.bind.dal.StorageType;
import cn.fjmua.mc.plugin.bind.dal.driver.MySQLDriver;
import cn.fjmua.mc.plugin.bind.dal.driver.SQLiteDriver;
import cn.fjmua.mc.plugin.bind.dal.impl.PlayerBindDAOImpl;
import cn.fjmua.mc.plugin.bind.pojo.ConfigEntity;
import cn.fjmua.mc.plugin.bind.util.Assert;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
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

    @NotNull
    Type getType();

    /**
     * 初始化数据库配置
     * @apiNote 插件加载时自动调用
     * */
    default void initJDBC() {
        ConfigEntity.Storage storage = Configuration.getReadOnlyConfig().getStorage();
        String typeStr = storage.getType();
        StorageType type = StorageType.parse(typeStr);
        Assert.notNull(type, "Unknown storage type: %s", typeStr);
        DataSource dataSource = null;
        if (type == StorageType.SQLITE) {
            dataSource = SQLiteDriver.getDataSource(MuaBindStatic.getInstance().getPluginDirPath());
        } else if (type == StorageType.MYSQL) {
            dataSource = MySQLDriver.getDataSource(storage.getMysql());
        }
        PlayerBindDAOImpl playerBindDAO = new PlayerBindDAOImpl(dataSource);
        MuaBindStatic.setPlayerBindDAO(playerBindDAO);
        playerBindDAO.tryCreateTable(type);
    }

    enum Type {

        VELOCITY,

        BUKKIT;

    }

}
