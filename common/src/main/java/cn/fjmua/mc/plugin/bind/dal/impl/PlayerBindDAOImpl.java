package cn.fjmua.mc.plugin.bind.dal.impl;

import cn.fjmua.mc.plugin.bind.dal.StorageType;
import cn.fjmua.mc.plugin.bind.dal.PlayerBindDAO;
import cn.fjmua.mc.plugin.bind.pojo.PlayerBind;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class PlayerBindDAOImpl implements PlayerBindDAO {

    private final DataSource dataSource;

    public PlayerBindDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public Integer insert(PlayerBind playerBind) {
        try (Connection conn = getConn()) {
            try (PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT)) {
                pstmt.setString(1, playerBind.getUuid());
                pstmt.setString(2, playerBind.getEmailDomain());
                pstmt.executeUpdate();
                return playerBind.getId();
            }
        } catch (SQLException e) {
            log.error("DAO invoke error", e);
        }
        return null;
    }

    @Override
    public void updateById(PlayerBind playerBind) {
        try (Connection conn = getConn()) {
            try (PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_BY_ID)) {
                pstmt.setString(1, playerBind.getUuid());
                pstmt.setString(2, playerBind.getEmailDomain());
                pstmt.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
                pstmt.setInt(4, playerBind.getId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            log.error("DAO invoke error", e);
        }

    }

    @Override
    public void deleteByUUID(String uuid) {
        try (Connection conn = getConn()) {
            try (PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_BY_UUID)) {
                pstmt.setString(1, uuid);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            log.error("DAO invoke error", e);
        }

    }

    @Override
    public PlayerBind selectByUUID(String uuid) {
        try (Connection conn = getConn()) {
            try (PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_UUID)) {
                pstmt.setString(1, uuid);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    PlayerBind playerBind = new PlayerBind();
                    playerBind.setId(rs.getInt("id"));
                    playerBind.setUuid(rs.getString("uuid"));
                    playerBind.setEmailDomain(rs.getString("email_domain"));
                    playerBind.setCreateTime(rs.getDate("create_time"));
                    playerBind.setModifiedTime(rs.getDate("modified_time"));
                    return playerBind;
                }
            }
        }catch (SQLException e) {
            log.error("DAO invoke error", e);
        }
        return null;
    }

    @Override
    public List<PlayerBind> selectAll() {
        List<PlayerBind> playerBinds = new LinkedList<>();
        try (Connection conn = getConn()) {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {
                while (rs.next()) {
                    PlayerBind playerBind = new PlayerBind();
                    playerBind.setId(rs.getInt("id"));
                    playerBind.setUuid(rs.getString("uuid"));
                    playerBind.setEmailDomain(rs.getString("email_domain"));
                    playerBind.setCreateTime(rs.getDate("create_time"));
                    playerBind.setModifiedTime(rs.getDate("modified_time"));
                    playerBinds.add(playerBind);
                }
            }
        }catch (SQLException e) {
            log.error("DAO invoke error", e);
        }
        return playerBinds;
    }

    @Override
    public void tryCreateTable(@NotNull StorageType type) {
        String sql;
        if (type == StorageType.MYSQL) {
            sql = SQL_CREATE_MYSQL_TABLE;
        } else if (type == StorageType.SQLITE) {
            sql = SQL_CREATE_SQLITE_TABLE;
        } else {
            return;
        }

        try (Connection conn = getConn()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(String.format(sql, TABLE_NAME));
            }
        } catch (SQLException e) {
            log.error("DAO invoke error", e);
        }
    }

    protected Connection getConn() throws SQLException {
        return dataSource.getConnection();
    }

}
