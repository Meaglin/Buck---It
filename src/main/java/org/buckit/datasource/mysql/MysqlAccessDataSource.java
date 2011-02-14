package org.buckit.datasource.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.buckit.Config;
import org.buckit.access.AccessLevel;
import org.buckit.access.Group;
import org.buckit.datasource.DataSource;
import org.buckit.datasource.type.AccessDataSource;

public class MysqlAccessDataSource implements AccessDataSource {

    private Map<Integer, Group>       groupsint;
    private Map<String, Group>        groups;
    private Map<Integer, AccessLevel> accesslevelsint;
    private Map<String, AccessLevel>  accesslevels;

    private static String             SELECT_GROUPS       = "SELECT id,name,commands FROM " + Config.DATABASE_ACCESSGROUPS_TABLE;
    private static String             SELECT_ACCESSLEVELS = "SELECT id,name,usernameformat,accessgroups,admingroup,canbuild FROM " + Config.DATABASE_ACCESS_TABLE;

    
    private DataSource datasource;
    public MysqlAccessDataSource(DataSource dataSource) {
        datasource = dataSource;
    }
    public DataSource getDataSource(){
        return datasource;
    }
    
    @Override
    public AccessLevel getAccessLevel(int id) {
        return accesslevelsint.get(id);
    }

    @Override
    public AccessLevel getAccessLevel(String name) {
        return accesslevels.get(name);
    }

    @Override
    public Group getGroup(int id) {
        return groupsint.get(id);
    }

    @Override
    public Group getGroup(String name) {
        return groups.get(name);
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
                group = new Group(rs.getInt("id"), rs.getString("name"), rs.getString("commands"));
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
