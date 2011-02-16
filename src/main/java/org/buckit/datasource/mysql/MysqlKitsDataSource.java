package org.buckit.datasource.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.buckit.Config;
import org.buckit.datasource.DataSource;
import org.buckit.datasource.type.KitsDataSource;
import org.buckit.model.Kit;

public class MysqlKitsDataSource implements KitsDataSource {

    private Map<String, Kit> kits;

    private static String       SELECT_KITS = "SELECT id,name,items,minaccesslevel,delay FROM " + Config.DATABASE_KITS_TABLE;
    private static String       INSERT_KIT = "REPLACE INTO " + Config.DATABASE_KITS_TABLE + " (name,items,minaccesslevel,delay) VALUES (?,?,?,?)";
    
    private DataSource datasource;
    public MysqlKitsDataSource(DataSource dataSource) {
        datasource = dataSource;
    }
    public DataSource getDataSource(){
        return datasource;
    }
    
    @Override
    public Kit getKit(String name) {
        return kits.get(name);
    }

    @Override
    public Collection<Kit> getKits() {
        return kits.values();
    }

    @Override
    public boolean load() {
        kits = new HashMap<String, Kit>();

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(SELECT_KITS);
            rs = st.executeQuery();
            Kit kit;
            while (rs.next()) {
                kit = new Kit(rs.getInt("id"), rs.getString("name"), getItemArray(rs.getString("items")), rs.getInt("minaccesslevel"), rs.getInt("delay"));
                kits.put(kit.getName(), kit);
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
        // TODO: print result.
        return true;
    }

    private static int[][] getItemArray(String str) {
        String[] split = str.split(Config.DATABASE_SEPERATOR);
        int[][] rt = new int[split.length][3];
        String[] parts;
        for (int i = 0; i < split.length; i++) {
            parts = split[i].split(Config.DATABASE_DELIMITER);
            try {
                rt[i][0] = Integer.parseInt(parts[0]);
                rt[i][1] = Integer.parseInt(parts[1]);
                rt[i][2] = Integer.parseInt(parts[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rt;
    }
    
    public boolean setLastUsed(int userId, String kitname, int time) {
        return false;
    }
    
    
    @Override
    public int lastUsed(int userId, String kitname) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public boolean setKit(Kit kit) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(INSERT_KIT, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, kit.getName());
            st.setString(2, kit.itemsToString());
            st.setInt(3, kit.getMinaccesslevel());
            st.setInt(4, kit.getDelay());
            st.execute();
            rs = st.getGeneratedKeys();
            if (rs.next()) {
                kits.put(kit.getName(), new Kit(rs.getInt("id"), kit.getName(), kit.getItemsArray(), kit.getMinaccesslevel(), kit.getDelay()));
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

}
