package cn.fjmua.mc.plugin.bind.dal.driver;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.nio.file.Path;

@Slf4j
public class SQLiteDriver {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            log.error("SQLite driver init error", e);
        }
    }

    public static DataSource getDataSource(Path path) {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(5);
        String dirPath = path.toAbsolutePath().toString();
        config.setJdbcUrl(String.format("jdbc:sqlite:%s/sqlite.db", dirPath));
        return new HikariDataSource(config);
    }

}
