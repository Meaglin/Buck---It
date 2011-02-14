package org.buckit.datasource.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.buckit.Config;
import org.buckit.datasource.DataSource;
import org.buckit.datasource.type.HomesDataSource;
import org.buckit.model.Home;
import org.bukkit.Location;
import org.bukkit.Server;

public class MysqlHomesDataSource implements HomesDataSource {

    private static String SELECT_HOME  = "SELECT id,x,y,z,rotX,rotY,world FROM " + Config.DATABASE_HOMES_TABLE + " WHERE userid = ? AND name = ? LIMIT 1";
    private static String INSERT_HOME  = "INSERT INTO " + Config.DATABASE_HOMES_TABLE + " (name,userid,username,world,x,y,z,rotX,rotY) VALUES (?,?,?,?,?,?,?,?,?)";
    private static String DELETE_HOME  = "DELETE FROM " + Config.DATABASE_HOMES_TABLE + " WHERE userid = ? AND name = ?";
    private static String SELECT_HOMES = "SELECT id,name,x,y,z,rotX,rotY,world FROM " + Config.DATABASE_HOMES_TABLE + " WHERE userid = ?";

    private DataSource datasource;
    private Server      server;
    public MysqlHomesDataSource(DataSource dataSource) {
        datasource = dataSource;
        server = datasource.getServer();
    }
    public DataSource getDataSource(){
        return datasource;
    }
    
    @Override
    public boolean deleteHome(int userId, String name) {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(DELETE_HOME);
            st.setInt(1, userId);
            st.setString(2, name);
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
    public Home getHome(int userId, String name) {

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Home home = null;
        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(SELECT_HOME);
            st.setInt(1, userId);
            st.setString(2, name);
            rs = st.executeQuery();
            if (rs.first()) {
                home = new Home(rs.getInt("id"), name, new Location(server.getWorld(rs.getString("world")), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("rotX"), rs.getFloat("rotY")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return home;
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
        return home;
    }

    @Override
    public List<Home> getHomes(int userId) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Home> homes = new ArrayList<Home>();
        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(SELECT_HOMES);
            st.setInt(1, userId);
            rs = st.executeQuery();
            while (rs.next()) {
                homes.add(new Home(rs.getInt("id"), rs.getString("name"), new Location(server.getWorld(rs.getString("world")), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("rotX"), rs.getFloat("rotY"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return homes;
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
        return homes;
    }

    @Override
    public boolean load() {
        return true;
        // do nothing
    }

    @Override
    public boolean setHome(int userId, String username, String name, Location home) {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(INSERT_HOME);
            st.setString(1, name);
            st.setInt(2, userId);
            st.setString(3, username);
            st.setString(4, home.getWorld().getName());
            st.setDouble(5, home.getX());
            st.setDouble(6, home.getY());
            st.setDouble(7, home.getZ());
            st.setFloat(8, home.getPitch());
            st.setFloat(9, home.getYaw());
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
