package cn.fjmua.mc.plugin.bind.dal;

import cn.fjmua.mc.plugin.bind.pojo.PlayerBind;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class PlayerBindDAOImpl implements PlayerBindDAO {

    private final Connection conn;

    public PlayerBindDAOImpl(Connection conn) {
        this.conn = conn;
    }
    
    @Override
    public Integer insert(PlayerBind playerBind) {
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT)) {
            pstmt.setString(1, playerBind.getUuid());
            pstmt.setString(2, playerBind.getEmailDomain());
            pstmt.setDate(3, new java.sql.Date(playerBind.getCreateTime().getTime()));
            pstmt.setDate(4, new java.sql.Date(playerBind.getModifiedTime().getTime()));
            pstmt.executeUpdate();
            return playerBind.getId();
        } catch (SQLException e) {
            log.error("DAO error invoke error", e);
        }
        return null;
    }

    @Override
    public void updateById(PlayerBind playerBind) {
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_BY_ID)) {
            pstmt.setString(1, playerBind.getUuid());
            pstmt.setString(2, playerBind.getEmailDomain());
            pstmt.setDate(3, new java.sql.Date(playerBind.getModifiedTime().getTime()));
            pstmt.setInt(4, playerBind.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("DAO error invoke error", e);
        }
    }

    @Override
    public void deleteByUUID(String uuid) {
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_BY_UUID)) {
            pstmt.setString(1, uuid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("DAO error invoke error", e);
        }
    }

    @Override
    public PlayerBind selectByUUID(String uuid) {
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
        } catch (SQLException e) {
            log.error("DAO error invoke error", e);
        }
        return null;
    }

    @Override
    public List<PlayerBind> findAll() {
        List<PlayerBind> playerBinds = new LinkedList<>();
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
        } catch (SQLException e) {
            log.error("DAO error invoke error", e);
        }
        return playerBinds;
    }

    @Override
    public void tryCreateTable(@NotNull DataSourceType type) {
        String sql;
        if (type == DataSourceType.MYSQL) {
            sql = SQL_CREATE_MYSQL_TABLE;
        } else if (type == DataSourceType.SQLITE) {
            sql = SQL_CREATE_SQLITE_TABLE;
        } else {

            return;
        }

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            log.error("DAO error invoke error", e);
        }
    }

}
