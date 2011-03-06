package org.buckit.datasource.flatfile;

import java.util.List;

import org.buckit.access.AccessLevel;
import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.UserDataSource;
import org.buckit.model.UserDataHolder;

//ID:USERNAME:USERNAMEFORMAT:FIRSTLOGIN:LASTLOGIN:ONLINETIME:BANTIME:MUTETIME:COMMANDS:CANBUILD:ISADMIN:ACCESSLEVEL
public class FlatFileUserDataSource implements UserDataSource, DataSource {

    private DataSourceManager datasource;
    public FlatFileUserDataSource(DataSourceManager dataSource) {
        datasource = dataSource;
    }
    public DataSourceManager getDataSource(){
        return datasource;
    }

    @Override
    public UserDataHolder getUserData(String username) {
        UserDataHolder user = null;
        
        List<String> lines = FileHandler.getLines("user");
        
        for (int i=0; i<lines.size(); i++) {
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            int     id;             try { id = Integer.parseInt(entry[0]); } catch (Exception e) { return user; }
            String  usernamename    = entry[1];
            String  usernameformat  = entry[2];
            int     firstlogin;     try { firstlogin = Integer.parseInt(entry[3]); } catch (Exception e) { return user; }
            int     uptime;         try { uptime = Integer.parseInt(entry[5]); } catch (Exception e) { return user; }
            int     bantime;        try { bantime = Integer.parseInt(entry[6]); } catch (Exception e) { return user; }
            int     mutetime;       try { mutetime = Integer.parseInt(entry[7]); } catch (Exception e) { return user; }
            String  commands        = entry[8];
            Boolean canbuild;       try { canbuild = Boolean.parseBoolean(entry[9]); } catch (Exception e) { return user; }
            Boolean isadmin;        try { isadmin = Boolean.parseBoolean(entry[10]); } catch (Exception e) { return user; }
            AccessLevel level       = getDataSource().getAccessDataSource().getAccessLevel(entry[11]);
            
            if (username.equals(usernamename))
                user = new UserDataHolder(id, username, usernameformat, isadmin, canbuild, commands, firstlogin, currentTime(), uptime, bantime, mutetime, level);
        }
        
        return user;
    }

    @Override
    public boolean load() {
        return true;
    }

    @Override
    public boolean updateUser(UserDataHolder holder) {
        List<String> lines = FileHandler.getLines("user");
        
        for (int i=0; i<lines.size(); i++) {
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            int     id;             try { id = Integer.parseInt(entry[0]); } catch (Exception e) { return false; }

          //ID:USERNAME:USERNAMEFORMAT:FIRSTLOGIN:LASTLOGIN:ONLINETIME:BANTIME:MUTETIME:COMMANDS:CANBUILD:ISADMIN:ACCESSLEVEL
            
            if (holder.getId() == id)
                lines.set(i, holder.getId()+FileHandler.sep1+
                            holder.getUsername()+FileHandler.sep1+
                            holder.getUsernameformat()+FileHandler.sep1+
                            holder.getFirstlogin()+FileHandler.sep1+
                            holder.getLastlogin()+FileHandler.sep1+
                            holder.getUptime()+FileHandler.sep1+
                            holder.getBantime()+FileHandler.sep1+
                            holder.getMutetime()+FileHandler.sep1+
                            holder.getCommands()+FileHandler.sep1+
                            holder.canbuild()+FileHandler.sep1+
                            holder.isAdmin()+FileHandler.sep1+
                            holder.getAccessLevel());
        }
        
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
