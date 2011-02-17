package org.buckit.datasource.flatfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.buckit.Config;
import org.buckit.access.AccessLevel;
import org.buckit.access.Group;
import org.buckit.datasource.DataSource;
import org.buckit.datasource.type.AccessDataSource;


//groups: ID:NAME:COMMANDS
//levels: ID:NAME:USERNAMEFORMAT:ACCESSGROUPS:ADMINGROUP:CANBUILD
public class FlatFileAccessDataSource implements AccessDataSource {

    private Map<Integer, Group>       groupsint;
    private Map<String, Group>        groups;
    private Map<Integer, AccessLevel> accesslevelsint;
    private Map<String, AccessLevel>  accesslevels;
    
    private DataSource datasource;
    
    public FlatFileAccessDataSource(DataSource dataSource) {
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

        //groups: ID:NAME:COMMANDS
        //levels: ID:NAME:USERNAMEFORMAT:ACCESSGROUPS:ADMINGROUP:CANBUILD
          
        List<String> lines = FileHandler.getLines("groups");
        Group group;
        for (int i=0; i<lines.size(); i++) {
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            int     id;             try { id = Integer.parseInt(entry[0]); } catch (Exception e) { return false; }
            String  name            = entry[1];
            String  commands        = entry[2];
            
            group = new Group(id, name, commands);
            groups.put(group.getName(), group);
            groupsint.put(group.getId(), group);
        }
        
        List<String> lines2 = FileHandler.getLines("accesslevels");
        AccessLevel access;
        for (int i=0; i<lines2.size(); i++) {
            String[] entry = lines2.get(i).split(FileHandler.sep1);
            
            int     id;             try { id = Integer.parseInt(entry[0]); } catch (Exception e) { return false; }
            String  name            = entry[1];
            String  usernameformat  = entry[2]; 
            String  accessgroups    = entry[3];
            Boolean admingroup;     try { admingroup = Boolean.parseBoolean(entry[4]); } catch (Exception e) { return false; }
            Boolean canbuild;       try { canbuild = Boolean.parseBoolean(entry[5]); } catch (Exception e) { return false; }
            
            access = new AccessLevel(id, getGroups(accessgroups), name, usernameformat, canbuild, admingroup);
            accesslevels.put(access.getName(), access);
            accesslevelsint.put(access.getId(), access);
        }
        
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
