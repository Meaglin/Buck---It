package com.nijikokun.bukkit.Permissions;

import java.util.Map;
import java.util.logging.Logger;

import org.buckit.access.AccessLevel;
import org.buckit.datasource.DataSourceManager;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;

public class PermissionsEmu extends PermissionHandler {

    private Server server;
    private final Logger log = Logger.getLogger("Minecraft");
    public PermissionsEmu(Server server) {
        this.server = server;
    }
    
    @Override
    public void addGroupInfo(String world, String group, String node, Object data) {}

    @Override
    public void addUserPermission(String world, String user, String node) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean canGroupBuild(String world, String group) {
        AccessLevel level = server.getDataSourceManager().getAccessDataSource().getAccessLevel(group);
        if(level != null) {
            return level.canBuild();
        } else
            return false;
    }

    @Override
    public boolean checkWorld(String world) {
        return loadWorld(world);
    }

    @Override
    public void clearAllCache() {}

    @Override
    public void clearCache(String world) {}

    @Override
    public void forceLoadWorld(String world) {}

    @Override
    public Map<String, Boolean> getCache(String world) {return null;}

    @Override
    public boolean getCacheItem(String world, String player, String permission) { return false;}

    @Override
    public String getGroup(String world, String name) {
        if(server.getDataSourceManager().getAccessDataSource().getAccessLevel(name) != null)
            return server.getDataSourceManager().getAccessDataSource().getAccessLevel(name).getName();
        else
            return null;
    }

    @Override
    public boolean getGroupPermissionBoolean(String world, String group, String permission) {return false;}

    @Override
    public double getGroupPermissionDouble(String world, String group, String permission) {return 0;}

    @Override
    public int getGroupPermissionInteger(String world, String group, String permission) {return 0;}

    @Override
    public String getGroupPermissionString(String world, String group, String permission) {return null;}

    @Override
    public String getGroupPrefix(String world, String group) {
        AccessLevel level = server.getDataSourceManager().getAccessDataSource().getAccessLevel(group);
        if(level == null) return null;
        
        String str = level.getUsernameformat();
        if(str.contains("{$username}")) {
            String[] split = str.split("{$username}");
            return split[0];
        } else
            return str;
    }

    @Override
    public String getGroupSuffix(String world, String group) {
        AccessLevel level = server.getDataSourceManager().getAccessDataSource().getAccessLevel(group);
        if(level == null) return null;
        
        String str = level.getUsernameformat();
        if(str.contains("{$username}")) {
            String[] split = str.split("{$username}");
            return split[1];
        } else
            return str;
    }

    @Override
    public String[] getGroups(String world, String name) {
        
        return null;
    }

    @Override
    public boolean getPermissionBoolean(String world, String name, String permission) {return false;}

    @Override
    public double getPermissionDouble(String world, String name, String permission) {return 0;}

    @Override
    public int getPermissionInteger(String world, String name, String permission) {return 0;}

    @Override
    public String getPermissionString(String world, String name, String permission) {return null;}

    @Override
    public boolean getUserPermissionBoolean(String world, String name, String permission) {return false;}

    @Override
    public double getUserPermissionDouble(String world, String name, String permission) {return 0;}

    @Override
    public int getUserPermissionInteger(String world, String name, String permission) {return 0;}

    @Override
    public String getUserPermissionString(String world, String name, String permission) {return null;}

    @Override
    public boolean has(Player player, String permission) {
        return permission(player,permission);
    }

    @Override
    public boolean inGroup(String world, String name, String group) {
        Player player = server.getPlayer(name);
        if(player == null){
            log.info("[PermissionsEmu]inGroup - Player not found " + name);
            return false;
        }
        
        AccessLevel level = server.getDataSourceManager().getAccessDataSource().getAccessLevel(group);
        if(level == null)return false;
        
        return level.getId() >= player.getAccessLevel().getId();
    }

    @Override
    public boolean inSingleGroup(String world, String name, String group) {
        Player player = server.getPlayer(name);
        if(player == null){
            log.info("[PermissionsEmu]SingleGroup - Player not found " + name);
            return false;
        }
        return player.getAccessLevel().getName().equalsIgnoreCase(group);
    }

    @Override
    public void load() {}

    @Override
    public void load(String world, Configuration config) {}

    @Override
    public boolean loadWorld(String world) {
        return server.getWorld(world) != null;
    }

    @Override
    public boolean permission(Player player, String permission) {
        return player.canUseCommand(permission);
    }

    @Override
    public void reload() {
        DataSourceManager manager = server.getDataSourceManager();
        manager.getAccessDataSource().load();
    }

    @Override
    public boolean reload(String world) {
        if(server.getWorld(world) != null) {
            reload();
            return true;
        } else
            return false;
    }

    @Override
    public void removeCachedItem(String world, String player, String permission) {}

    @Override
    public void removeGroupInfo(String world, String group, String node) {}

    @Override
    public void removeUserPermission(String world, String user, String node) {}

    @Override
    public void setCache(String world, Map<String, Boolean> Cache) {}

    @Override
    public void setCacheItem(String world, String player, String permission, boolean data) {}

    @Override
    public void setDefaultWorld(String world) {}

}
