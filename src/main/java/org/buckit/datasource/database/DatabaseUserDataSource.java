package org.buckit.datasource.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.buckit.Config;
import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.UserDataSource;
import org.buckit.model.UserDataHolder;

public class DatabaseUserDataSource implements UserDataSource, DataSource {

    
    private static String SELECT_USER           = "SELECT * FROM " + Config.DATABASE_USERS_TABLE + " WHERE username = ?";
    private static String INSERT_USER           = "INSERT INTO " + Config.DATABASE_USERS_TABLE + " (username,usernameformat,firstlogin,lastlogin) VALUES (?,?,?,?) ";

    private static String UPDATE_USER_LASTLOGIN = "UPDATE " + Config.DATABASE_USERS_TABLE + " SET lastlogin = ? WHERE id = ?";
    private static String UPDATE_USER_UPTIME    = "UPDATE " + Config.DATABASE_USERS_TABLE + " SET onlinetime = ? WHERE id = ?";
    private static String UPDATE_USER_BANTIME   = "UPDATE " + Config.DATABASE_USERS_TABLE + " SET bantime = ? WHERE id = ?";
    private static String UPDATE_USER_MUTETIME  = "UPDATE " + Config.DATABASE_USERS_TABLE + " SET mutetime = ? WHERE id = ?";
    private static String UPDATE_USER           = "UPDATE " + Config.DATABASE_USERS_TABLE + " SET username = ? , usernameformat = ? , firstlogin = ? , lastlogin = ? , onlinetime = ? , ipbantime = ?, bantime = ? , mutetime = ? , commands = ? , canbuild = ? , isadmin = ? , accesslevel = ? , ip = ? WHERE id = ?";

    
    private DataSourceManager datasource;
    public DatabaseUserDataSource(DataSourceManager dataSource) {
        datasource = dataSource;
    }
    public DataSourceManager getDataSource(){
        return datasource;
    }
    
    @Override
    public UserDataHolder getUserData(String username) {

        username = username.toLowerCase();
        
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        UserDataHolder rt = null;
        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(SELECT_USER);
            st.setString(1, username);
            rs = st.executeQuery();
            if (!rs.next()) {
                st.close();
                rs.close();
                st = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
                st.setString(1, username);
                st.setString(2, Config.DEFAULT_USER_FORMAT);
                st.setInt(3, currentTime());
                st.setInt(4, currentTime());
                st.execute();

                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    rt = new UserDataHolder(rs.getInt(1), username, Config.DEFAULT_USER_FORMAT, false, false, null, currentTime(), currentTime(), 0, 0, 0, 0, getDataSource().getAccessDataSource().getAccessLevel(Config.DEFAULT_ACCESS_LEVEL),"0.0.0.0");
                }
            } else {
                rt = new UserDataHolder(rs.getInt("id"), username, rs.getString("usernameformat"), rs.getBoolean("isadmin"), rs.getBoolean("canbuild"), rs.getString("commands"), rs.getInt("firstlogin"), currentTime(), rs.getInt("onlinetime"),rs.getInt("ipbantime"), rs.getInt("bantime"), rs.getInt("mutetime"), getDataSource().getAccessDataSource().getAccessLevel(rs.getInt("accesslevel")),rs.getString("ip"));
                st.close();
                st = conn.prepareStatement(UPDATE_USER_LASTLOGIN);
                st.setInt(1, rt.getLastlogin());
                st.setInt(2, rt.getId());
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
            st.setInt(6, holder.getIpBantime());
            st.setInt(7, holder.getBantime());
            st.setInt(8, holder.getMutetime());
            st.setString(9, holder.getCommands());
            st.setBoolean(10, holder.canbuild());
            st.setBoolean(11, holder.isAdmin());
            st.setInt(12, holder.getAccessLevel().getId());
            st.setString(13, holder.getIp());
            st.setInt(14, holder.getId());
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
