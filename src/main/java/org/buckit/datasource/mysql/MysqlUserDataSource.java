package org.buckit.datasource.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.buckit.Config;
import org.buckit.datasource.DataSource;
import org.buckit.datasource.type.UserDataSource;
import org.buckit.model.UserDataHolder;

public class MysqlUserDataSource implements UserDataSource {

    
    private static String SELECT_USER           = "SELECT * FROM " + Config.DATABASE_USERS_TABLE + " WHERE name = ?";
    private static String INSERT_USER           = "INSERT INTO " + Config.DATABASE_USERS_TABLE + " (username,usernameformat,firstlogin,lastlogin) VALUES (?,?,?,?) ";

    private static String UPDATE_USER_LASTLOGIN = "UPDATE " + Config.DATABASE_USERS_TABLE + " SET lastlogin = ? WHERE id = ?";
    private static String UPDATE_USER_UPTIME    = "UPDATE " + Config.DATABASE_USERS_TABLE + " SET onlinetime = ? WHERE id = ?";
    private static String UPDATE_USER_BANTIME   = "UPDATE " + Config.DATABASE_USERS_TABLE + " SET bantime = ? WHERE id = ?";
    private static String UPDATE_USER_MUTETIME  = "UPDATE " + Config.DATABASE_USERS_TABLE + " SET mutetime = ? WHERE id = ?";
    private static String UPDATE_USER           = "UPDATE " + Config.DATABASE_USERS_TABLE + "SET username = ?, usernameformat = ?, firstlogin = ?,lastlogin = ?,onlinetime = ?,bantime = ?,mutetime = ?,commands = ?,canbuild = ?,isadmin = ?,accesslevel = ? WHERE id = ?";

    
    private DataSource datasource;
    public MysqlUserDataSource(DataSource dataSource) {
        datasource = dataSource;
    }
    public DataSource getDataSource(){
        return datasource;
    }
    
    @Override
    public UserDataHolder getUserData(String username) {

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        UserDataHolder rt = null;
        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(SELECT_USER);
            st.setString(1, username);
            rs = st.executeQuery();
            if (!rs.first()) {
                st.close();
                rs.close();
                st = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
                st.setString(1, username);
                st.setString(2, Config.DEFAULT_USER_FORMAT);
                st.setInt(2, currentTime());
                st.setInt(3, currentTime());
                st.execute();

                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    rt = new UserDataHolder(rs.getInt(1), username, Config.DEFAULT_USER_FORMAT, false, false, null, currentTime(), currentTime(), 0, 0, 0, getDataSource().getAccessDataSource().getAccessLevel(0));
                }
            } else {
                rt = new UserDataHolder(rs.getInt("id"), username, rs.getString("usernameformat"), rs.getBoolean("isadmin"), rs.getBoolean("canbuild"), rs.getString("commands"), rs.getInt("firstlogin"), currentTime(), rs.getInt("uptime"), rs.getInt("bantime"), rs.getInt("mutetime"), getDataSource().getAccessDataSource().getAccessLevel(rs.getInt("accesslevel")));
                st.close();
                st = conn.prepareStatement(UPDATE_USER_LASTLOGIN);
                st.setInt(1, rt.getLastlogin());
                st.setInt(3, rt.getId());
                st.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (st != null)
                    st.close();
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
            }
        }
        return rt;
    }

    @Override
    public void updateUserDataOnDisconnect(UserDataHolder holder) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(UPDATE_USER_UPTIME);
            st.setInt(1, holder.getUptime() + (currentTime() - holder.getLastlogin()));
            st.setInt(2, holder.getId());
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (st != null)
                    st.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public boolean updateUserBanTime(UserDataHolder holder) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(UPDATE_USER_BANTIME);
            st.setInt(1, holder.getBantime());
            st.setInt(2, holder.getId());
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (st != null)
                    st.close();
            } catch (Exception e) {
            }
        }
        return true;
    }

    @Override
    public boolean updateUserMuteTime(UserDataHolder holder) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(UPDATE_USER_MUTETIME);
            st.setInt(1, holder.getMutetime());
            st.setInt(2, holder.getId());
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (st != null)
                    st.close();
            } catch (Exception e) {
            }
        }
        return true;
    }

    private static int currentTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    @Override
    public boolean load() {
        return true;
        // do nothing
    }

    @Override
    public boolean updateUser(UserDataHolder holder) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(UPDATE_USER);
            st.setString(1, holder.getUsername());
            st.setString(2, holder.getUsernameformat());
            st.setInt(3, holder.getFirstlogin());
            st.setInt(4, holder.getLastlogin());
            st.setInt(5, holder.getUptime());
            st.setInt(6, holder.getBantime());
            st.setInt(7, holder.getMutetime());
            st.setString(8, holder.getCommands());
            st.setBoolean(9, holder.canbuild());
            st.setBoolean(10, holder.isAdmin());
            st.setInt(11, holder.getAccessLevel().getId());
            st.setInt(12, holder.getId());
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (st != null)
                    st.close();
            } catch (Exception e) {
            }
        }
        return true;
    }

}
