package org.buckit.datasource.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.buckit.Config;
import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.WarpsDataSource;
import org.buckit.model.Warp;
import org.bukkit.Location;
import org.bukkit.Server;

public class DatabaseWarpsDataSource implements WarpsDataSource, DataSource {

    private static String           INSERT_WARP  = "REPLACE INTO " + Config.DATABASE_WARPS_TABLE + " (name,groupname,world,x,y,z,rotX,rotY) VALUES (?,?,?,?,?,?,?,?)";
    private static String           DELETE_WARP  = "DELETE FROM " + Config.DATABASE_WARPS_TABLE + " WHERE id = ?";
    private static String           SELECT_WARPS = "SELECT id,name,groupname,world,x,y,z,rotX,rotY,minaccesslevel FROM " + Config.DATABASE_WARPS_TABLE;

    private Map<String, Warp>       warps;
    private Map<String, List<Warp>> warpgroups;

    private DataSourceManager datasource;
    public DatabaseWarpsDataSource(DataSourceManager dataSource) {
        datasource = dataSource;
    }
    public DataSourceManager getDataSource(){
        return datasource;
    }
    
    @Override
    public boolean addWarp(String groupname, String name, Location warp, int minaccesslevel) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(INSERT_WARP, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, name);
            st.setString(2, groupname);
            st.setString(3, warp.getWorld().getName());
            st.setDouble(4, warp.getX());
            st.setDouble(5, warp.getY());
            st.setDouble(6, warp.getZ());
            st.setFloat(7, warp.getPitch());
            st.setFloat(8, warp.getYaw());
            st.execute();
            rs = st.getGeneratedKeys();
            if (rs.next()) {
                addWarp(new Warp(rs.getInt("id"), name, groupname, warp, minaccesslevel));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
        return true;
    }

    @Override
    public boolean removeWarp(Warp warp) {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(DELETE_WARP);
            st.setInt(1, warp.getId());
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
    public Collection<Warp> getAllWarps() {
        return warps.values();
    }

    @Override
    public Warp getWarp(String groupname, String name) {
        return warps.get(groupname + "." + name);
    }

    @Override
    public Collection<Warp> getWarps(String groupname) {
        return warpgroups.get(groupname);
    }

    @Override
    public boolean load() {
        warps = new HashMap<String, Warp>();
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(SELECT_WARPS);
            rs = st.executeQuery();
            Warp warp;
            while (rs.next()) {
                warp = new Warp(rs.getInt("id"), rs.getString("name"), rs.getString("groupname"), new Location(datasource.getServer().getWorld(rs.getString("world")), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("rotX"), rs.getFloat("rotY")), rs.getInt("minaccesslevel"));
                addWarp(warp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
        return true;
    }

    private void addWarp(Warp warp) {
        warps.put(warp.getGroup() + "." + warp.getName(), warp);
        if (warpgroups.containsKey(warp.getGroup())) {
            if (!warpgroups.get(warp.getGroup()).contains(warp)) {
                warpgroups.get(warp.getGroup()).add(warp);
            }
        } else {
            ArrayList<Warp> list = new ArrayList<Warp>();
            list.add(warp);
            warpgroups.put(warp.getGroup(), list);
        }
    }
}
