package cn.fjmua.mc.plugin.bind.dal.driver;

import cn.fjmua.mc.plugin.bind.pojo.ConfigEntity;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

@Slf4j
public class MySQLDriver {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error("MySQL driver init error", e);
        }
    }

    public static DataSource getDataSource(ConfigEntity.Storage.Mysql mysql) {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(10);
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s?useSSL=false", mysql.getHost(), mysql.getPort(), mysql.getDb()));
        config.setUsername(mysql.getUsername());
        config.setPassword(mysql.getPassword());
        return new HikariDataSource(config);
    }

}
