package cn.fjmua.mc.plugin.bind.dal.driver;

import cn.fjmua.mc.plugin.bind.api.Configuration;
import cn.fjmua.mc.plugin.bind.pojo.ConfigEntity;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDriver {

    public static Connection getConnection() throws SQLException {
        ConfigEntity.Storage.Mysql config = Configuration.getReadOnlyConfig().getStorage().getMysql();
        String host = config.getHost();
        int port = config.getPort();
        String db = config.getDb();
        String username = config.getUsername();
        String password = config.getPassword();
        return DriverManager.getConnection(String.format(
                "jdbc:mysql://%s:%d/%s?useSSL=false",
                host, port, db
        ), username, password);
    }

    public static DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(10);
        ConfigEntity.Storage.Mysql mysql = Configuration.getReadOnlyConfig().getStorage().getMysql();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s?useSSL=false", mysql.getHost(), mysql.getPort(), mysql.getDb()));
        config.setUsername(mysql.getUsername());
        config.setPassword(mysql.getPassword());
        return new HikariDataSource(config);
    }

}
