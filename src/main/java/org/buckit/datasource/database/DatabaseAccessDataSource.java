package org.buckit.datasource.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.buckit.Config;
import org.buckit.access.AccessLevel;
import org.buckit.access.Group;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.AccessDataSource;

public class DatabaseAccessDataSource implements AccessDataSource {

    private Map<Integer, Group>       groupsint;
    private Map<String, Group>        groups;
    private Map<Integer, AccessLevel> accesslevelsint;
    private Map<String, AccessLevel>  accesslevels;

    private static String             SELECT_GROUPS       = "SELECT id,name,commands,worlds FROM " + Config.DATABASE_ACCESSGROUPS_TABLE;
    private static String             SELECT_ACCESSLEVELS = "SELECT id,name,usernameformat,accessgroups,admingroup,canbuild FROM " + Config.DATABASE_ACCESS_TABLE;

    private static final Group defaultGroup = new Group(-1,"default","","world");
    private static final AccessLevel defaultLevel = new AccessLevel(-1,new Group[]{defaultGroup},"default",null,false,false);
    
    private DataSourceManager datasource;
    public DatabaseAccessDataSource(DataSourceManager dataSource) {
        datasource = dataSource;
    }
    public DataSourceManager getDataSource(){
        return datasource;
    }
    
    @Override
    public AccessLevel getAccessLevel(int id) {
        if(accesslevelsint.containsKey(id))
            return accesslevelsint.get(id);
        else
            return defaultLevel;
    }

    @Override
    public AccessLevel getAccessLevel(String name) {
        if(accesslevels.containsKey(name))
            return accesslevels.get(name);
        else
            return defaultLevel;
    }

    @Override
    public Group getGroup(int id) {
        if(groupsint.containsKey(id))
            return groupsint.get(id);
        else
            return defaultGroup;
    }

    @Override
    public Group getGroup(String name) {
        if(groups.containsKey(name))
            return groups.get(name);
        else
            return defaultGroup;
    }

    @Override
    public boolean load() {
        groups = new HashMap<String, Group>();
        groupsint = new HashMap<Integer, Group>();

        accesslevels = new HashMap<String, AccessLevel>();
        accesslevelsint = new HashMap<Integer, AccessLevel>();

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn = DatabaseFactory.getInstance().getConnection();
            st = conn.prepareStatement(SELECT_GROUPS);
            rs = st.executeQuery();
            Group group;
            while (rs.next()) {
                group = new Group(rs.getInt("id"), rs.getString("name"), rs.getString("commands"), rs.getString("worlds"));
                groups.put(group.getName(), group);
                groupsint.put(group.getId(), group);
            }
            rs.close();
            st.close();
            st = conn.prepareStatement(SELECT_ACCESSLEVELS);
            rs = st.executeQuery();
            AccessLevel access;
            while (rs.next()) {
                access = new AccessLevel(rs.getInt("id"), getGroups(rs.getString("accessgroups")), rs.getString("name"), rs.getString("usernameformat"), rs.getBoolean("canbuild"), rs.getBoolean("admingroup"));
                accesslevels.put(access.getName(), access);
                accesslevelsint.put(access.getId(), access);
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

    private Group[] getGroups(String list) {
        if(list == null || list.equals(""))
            return null;
        
        String[] split = list.split(Config.DATABASE_DELIMITER);
        Group[] group = new Group[split.length];
        for (int i = 0; i < split.length; i++) {
            try {
                group[i] = getGroup(Integer.parseInt(split[i]));
            } catch (NumberFormatException e) {
                group[i] = getGroup(split[i]);
            }
            if (group[i] == null) {
                // TODO: print error.
            }
        }

        return group;
    }

}
