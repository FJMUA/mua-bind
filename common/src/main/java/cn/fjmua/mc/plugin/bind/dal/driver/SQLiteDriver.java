package cn.fjmua.mc.plugin.bind.dal.driver;

import cn.fjmua.mc.plugin.bind.api.MuaBindStatic;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDriver {

    public static Connection getConnection() throws SQLException {
        Path path = MuaBindStatic.getInstance().getPluginDirPath();
        String dirPath = path.toAbsolutePath().toString();
        return DriverManager.getConnection(String.format("jdbc:sqlite:%s/sqlite.db", dirPath));
    }

    public static DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(5);
        Path path = MuaBindStatic.getInstance().getPluginDirPath();
        String dirPath = path.toAbsolutePath().toString();
        config.setJdbcUrl(String.format("jdbc:sqlite:%s/sqlite.db", dirPath));
        return new HikariDataSource(config);
    }

}
