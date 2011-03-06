package org.buckit.datasource;

import org.buckit.Config;
import org.buckit.datasource.database.*;
import org.buckit.datasource.flatfile.*;
import org.buckit.datasource.type.*;
import org.buckit.datasource.yaml.*;
import org.bukkit.Server;

public class DataSourceManager {

    private UserDataSource    userdatasource;
    private WarpsDataSource   warpsdatasource;
    private KitsDataSource    kitsdatasource;
    private HomesDataSource   homesdatasource;
    private AccessDataSource  accessdatasource;
    private WhiteListDataSource whitelistdatasource;
    private ReserveListDataSource reservelistdatasource;

    private Server server;
    private DataType type;
    public DataSourceManager(Server server) {
        this.server = server;
        
        load();
    }

    private void load() {
        type = Config.DATA_SOURCE_TYPE;
        switch (Config.DATA_SOURCE_TYPE) {
            case DATABASE:
                userdatasource = new DatabaseUserDataSource(this);
                accessdatasource = new DatabaseAccessDataSource(this);
                kitsdatasource = new DatabaseKitsDataSource(this);
                homesdatasource = new DatabaseHomesDataSource(this);
                warpsdatasource = new DatabaseWarpsDataSource(this);
                whitelistdatasource = new DatabaseWhiteListDataSource(this);
                reservelistdatasource = new DatabaseReserveListDataSource(this);
                break;
            case FLATFILE:
                userdatasource = new FlatFileUserDataSource(this);
                accessdatasource = new FlatFileAccessDataSource(this);
                kitsdatasource = new FlatFileKitsDataSource(this);
                homesdatasource = new FlatFileHomesDataSource(this);
                warpsdatasource = new FlatFileWarpsDataSource(this);
                whitelistdatasource = new FlatFileWhiteListDataSource(this);
                reservelistdatasource = new FlatFileReserveListDataSource(this);
                break;
            case YAML:
                userdatasource = new YamlUserDataSource(this);
                accessdatasource = new YamlAccessDataSource(this);
                kitsdatasource = new YamlKitsDataSource(this);
                homesdatasource = new YamlHomesDataSource(this);
                warpsdatasource = new YamlWarpsDataSource(this);
                whitelistdatasource = new YamlWhiteListDataSource(this);
                reservelistdatasource = new YamlReserveListDataSource(this);
                break;
        }
        
        getAccessDataSource().load();
        getUserDataSource().load();
        if (Config.WARPS_ENABLED)
            getWarpsDataSource().load();
        if (Config.HOMES_ENABLED)
            getHomesDataSource().load();
        if (Config.KITS_ENABLED)
            getKitsDataSource().load();
        if  (Config.WHITELIST_ENABLED)
            getWhiteListDataSource().load();
        if  (Config.RESERVELIST_ENABLED)
            getReserveListDataSource().load();
    }

    public void reload() {
        try {
            load();
            
            if(Config.DATA_SOURCE_TYPE == DataType.DATABASE)
                DatabaseFactory.getInstance().reload();
                
        } catch(Throwable t){
            server.getLogger().warning("Error reloading DataSources, error:");
            t.printStackTrace();
        }
    }
    
    public DataType getType() {
        return type;
    }

    public String toString() {
        return "DataSourceManage,Type:" + type.name().toLowerCase();
    }

    public UserDataSource getUserDataSource() {
        return userdatasource;
    }

    public WarpsDataSource getWarpsDataSource() {
        return warpsdatasource;
    }

    public KitsDataSource getKitsDataSource() {
        return kitsdatasource;
    }

    public HomesDataSource getHomesDataSource() {
        return homesdatasource;
    }

    public AccessDataSource getAccessDataSource() {
        return accessdatasource;
    }
    /**
     * @return the whitelistdatasource
     */
    public WhiteListDataSource getWhiteListDataSource() {
        return whitelistdatasource;
    }

    /**
     * @return the reservelistdatasource
     */
    public ReserveListDataSource getReserveListDataSource() {
        return reservelistdatasource;
    }

    public Server getServer(){
        return server;
    }
}
