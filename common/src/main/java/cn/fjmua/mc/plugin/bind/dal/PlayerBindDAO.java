package cn.fjmua.mc.plugin.bind.dal;

import cn.fjmua.mc.plugin.bind.pojo.PlayerBind;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PlayerBindDAO {

    String TABLE_NAME = "player_bind";
    String SQL_INSERT = String.format("INSERT INTO %s (uuid, email_domain, create_time, modified_time) VALUES (?, ?, ?, ?)", TABLE_NAME);
    String SQL_UPDATE_BY_ID = String.format("UPDATE %s SET uuid = ?, email_domain = ?, modified_time = ? WHERE id = ?", TABLE_NAME);
    String SQL_DELETE_BY_UUID = String.format("DELETE FROM %s WHERE uuid = ?", TABLE_NAME);
    String SQL_SELECT_BY_UUID = String.format("SELECT id, uuid, email_domain, create_time, modified_time FROM %s WHERE uuid = ?", TABLE_NAME);
    String SQL_SELECT_ALL = String.format("SELECT id, uuid, email_domain, create_time, modified_time FROM %s", TABLE_NAME);
    String SQL_CREATE_MYSQL_TABLE = """
                CREATE TABLE IF NOT EXISTS player_bind (
                    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                    uuid VARCHAR(255) NOT NULL,
                    email_domain VARCHAR(64),
                    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                    modified_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    UNIQUE INDEX uniq_uuid (uuid)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;""";
    String SQL_CREATE_SQLITE_TABLE = """
                CREATE TABLE IF NOT EXISTS player_bind (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    uuid TEXT NOT NULL,
                    email_domain TEXT,
                    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                    modified_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                    UNIQUE (uuid)
                );""";


    Integer insert(PlayerBind playerBind);

    void updateById(PlayerBind playerBind);

    void deleteByUUID(String uuid);

    PlayerBind selectByUUID(String uuid);

    List<PlayerBind> findAll();

    void tryCreateTable(@NotNull StorageType type);

}
