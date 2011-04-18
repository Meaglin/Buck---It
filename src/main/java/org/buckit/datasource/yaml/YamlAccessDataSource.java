package org.buckit.datasource.yaml;

import java.util.Map;

import org.buckit.Config;
import org.buckit.access.AccessLevel;
import org.buckit.access.Group;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.AccessDataSource;

public class YamlAccessDataSource implements AccessDataSource{

    private Map<Integer, Group>       groupsint;
    private Map<String, Group>        groups;
    private Map<Integer, AccessLevel> accesslevelsint;
    private Map<String, AccessLevel>  accesslevels;

    private static final Group defaultGroup = new Group(-1,"default","","world");
    private static final AccessLevel defaultLevel = new AccessLevel(-1,new Group[]{defaultGroup},"default",null,false,false);
    
    private DataSourceManager datasource;

    
    public YamlAccessDataSource(DataSourceManager dataSource) {
        datasource = dataSource;
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
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
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

    @Override
    public DataSourceManager getDataSource() {
        return datasource;
    }
    
}
