package org.buckit.datasource.flatfile;

import java.util.List;

import org.buckit.Config;
import org.buckit.access.AccessLevel;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.UserDataSource;
import org.buckit.model.UserDataHolder;

public class FlatFileUserDataSource implements UserDataSource{

    private DataSourceManager datasource;
    private int lastId=0;
    
    public FlatFileUserDataSource(DataSourceManager dataSource) {
        datasource = dataSource;
    }
    public DataSourceManager getDataSource(){
        return datasource;
    }

    @Override
    public boolean load() {
        List<String> lines = FileHandler.getLines("user");
        
        LineReader r;
        int id;
        for (int i=0; i<lines.size(); i++) {
            r = new LineReader(lines.get(i));
            
            id = r.nextInt();
            
            if (id > lastId)
                lastId = id;
        }
        
        FFLog.newFound("Userdata", lines.size());
        
        return true;
    }
    
    @Override
    public UserDataHolder getUserData(String username) {
        username = username.toLowerCase();
        boolean exists = false;
        
        UserDataHolder user = null;
        
        List<String> lines = FileHandler.getLines("user");
        
        LineReader r;
        int id, firstlogin, uptime, ipbantime, bantime, mutetime, aLevel;
        String name, format, commands, ip;
        Boolean canbuild,isadmin;
        AccessLevel level;
        for (int i=0; i<lines.size(); i++) {
            r = new LineReader(lines.get(i));
            
            id          = r.nextInt();
            name        = r.nextStr();
            format      = r.nextStr();
            firstlogin  = r.nextInt();
                        r.skip();
            uptime      = r.nextInt();
            ipbantime   = r.nextInt();
            bantime     = r.nextInt();
            mutetime    = r.nextInt();
            commands    = r.nextStr();
            canbuild    = r.nextBool();
            isadmin     = r.nextBool();
            aLevel      = r.nextInt();
            level       = getDataSource().getAccessDataSource().getAccessLevel(aLevel);
            ip          = r.nextStr();
            if (username.equals(name)) {
                user = new UserDataHolder(id, username, format, isadmin, canbuild, commands, firstlogin, currentTime(), uptime, ipbantime, bantime, mutetime, level,ip);
                exists = true;
                break;
            }
        }
        
        if (!exists) {
            user = new UserDataHolder((lastId+1), username.toLowerCase(), Config.DEFAULT_USER_FORMAT, false, false, "", currentTime(), currentTime(), 0, 0, 0, 0, getDataSource().getAccessDataSource().getAccessLevel(Config.DEFAULT_ACCESS_LEVEL),"0.0.0.0");
        	lastId++;
        	
        	lines.add(user.getId()+FileHandler.sep1+
                    user.getUsername().toLowerCase()+FileHandler.sep1+
                    user.getUsernameformat()+FileHandler.sep1+
                    user.getFirstlogin()+FileHandler.sep1+
                    user.getLastlogin()+FileHandler.sep1+
                    user.getUptime()+FileHandler.sep1+
                    user.getIpBantime()+FileHandler.sep1+
                    user.getBantime()+FileHandler.sep1+
                    user.getMutetime()+FileHandler.sep1+
                    user.getCommands()+FileHandler.sep1+
                    user.canbuild()+FileHandler.sep1+
                    user.isAdmin()+FileHandler.sep1+
                    Config.DEFAULT_ACCESS_LEVEL+FileHandler.sep1+
                    user.getIp());
        	
        	FFLog.newEdit("Userdata", "new user '"+username+"' with id "+user.getId());
        	
        	FileHandler.writeFile("user", lines);
        }
        
        return user;
    }


    @Override
    public boolean updateUser(UserDataHolder holder) {
        List<String> lines = FileHandler.getLines("user");
        
        LineReader r;
        int id;
        for (int i=0; i<lines.size(); i++) {
            r = new LineReader(lines.get(i));
            
            id = r.nextInt();
            
            if (holder.getId() == id) {
                lines.set(i, holder.getId()+FileHandler.sep1+
                            holder.getUsername().toLowerCase()+FileHandler.sep1+
                            holder.getUsernameformat()+FileHandler.sep1+
                            holder.getFirstlogin()+FileHandler.sep1+
                            holder.getLastlogin()+FileHandler.sep1+
                            holder.getUptime()+FileHandler.sep1+
                            holder.getIpBantime()+FileHandler.sep1+
                            holder.getBantime()+FileHandler.sep1+
                            holder.getMutetime()+FileHandler.sep1+
                            holder.getCommands()+FileHandler.sep1+
                            holder.canbuild()+FileHandler.sep1+
                            holder.isAdmin()+FileHandler.sep1+
                            holder.getAccessLevel().getId()+FileHandler.sep1+
                            holder.getIp());
                break;
            }
        }

        FFLog.newEdit("Userdata", "update user '"+holder.getUsername()+"'");
        
        return FileHandler.writeFile("user", lines);
    }

    @Override
    public boolean updateUserBanTime(UserDataHolder holder) {
        return updateUser(holder);
    }

    @Override
    public void updateUserDataOnDisconnect(UserDataHolder holder) {
        holder.setUptime(holder.getUptime() + (currentTime() - holder.getLastlogin()));
        updateUser(holder);
    }

    @Override
    public boolean updateUserMuteTime(UserDataHolder holder) {
        return updateUser(holder);
    }
    
    private static int currentTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }
}
