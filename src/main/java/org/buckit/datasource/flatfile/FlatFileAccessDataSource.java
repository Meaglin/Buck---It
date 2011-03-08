package org.buckit.datasource.flatfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.buckit.Config;
import org.buckit.access.AccessLevel;
import org.buckit.access.Group;
import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.AccessDataSource;

public class FlatFileAccessDataSource implements AccessDataSource, DataSource {

    private Map<Integer, Group>       groupsint;
    private Map<String, Group>        groups;
    private Map<Integer, AccessLevel> accesslevelsint;
    private Map<String, AccessLevel>  accesslevels;
    
    private static final Group defaultGroup = new Group(0,"default","");
    private static final AccessLevel defaultLevel = new AccessLevel(0,new Group[]{defaultGroup},"default",null,false,false);
    
    private DataSourceManager datasource;
    
    public FlatFileAccessDataSource(DataSourceManager dataSource) {
        datasource = dataSource;
    }
    
    public DataSourceManager getDataSource() {
        return datasource;
    }
    
    @Override
    public boolean load() {
        groups          = new HashMap<String, Group>();
        groupsint       = new HashMap<Integer, Group>();
        accesslevels    = new HashMap<String, AccessLevel>();
        accesslevelsint = new HashMap<Integer, AccessLevel>();
          
        List<String> lines = FileHandler.getLines("groups");
        
        Group group;
        LineReader r;
        int id;
        String name, commands;
        for (int i=0; i<lines.size(); i++) {
            
            r = new LineReader(lines.get(i));
            id          = r.nextInt();
            name        = r.nextStr();
            commands    = r.nextStr();

            group = new Group(
                        id, 
                        name, 
                        commands
                    );
            
            groups.put(group.getName(), group);
            groupsint.put(group.getId(), group);
        }

        FFLog.newInit("Accessgroups", groups.size());
        
        List<String> lines2 = FileHandler.getLines("accesslevels");
        AccessLevel access;
        String  usernameformat, accessgroups;
        Boolean admingroup, canbuild;
        for (int i=0; i<lines2.size(); i++) {
            
            r = new LineReader(lines2.get(i));
            id              = r.nextInt();
            name            = r.nextStr();
            usernameformat  = r.nextStr();
            accessgroups    = r.nextStr();
            admingroup      = r.nextBool();
            canbuild        = r.nextBool();
            
            access = new AccessLevel(
                        id, 
                        getGroups(accessgroups), 
                        name, 
                        usernameformat, 
                        canbuild, 
                        admingroup
                     );
            
            accesslevels.put(access.getName(), access);
            accesslevelsint.put(access.getId(), access);
        }
        
        FFLog.newInit("Accesslevels", accesslevels.size());
        
        return true;
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
            }
        }

        return group;
    }

}
