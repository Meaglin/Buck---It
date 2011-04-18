package org.buckit.datasource;

import org.buckit.Config;
import org.buckit.datasource.database.DatabaseAccessDataSource;
import org.buckit.datasource.database.DatabaseFactory;
import org.buckit.datasource.database.DatabaseHomesDataSource;
import org.buckit.datasource.database.DatabaseKitsDataSource;
import org.buckit.datasource.database.DatabaseReserveListDataSource;
import org.buckit.datasource.database.DatabaseUserDataSource;
import org.buckit.datasource.database.DatabaseWarpsDataSource;
import org.buckit.datasource.database.DatabaseWhiteListDataSource;
import org.buckit.datasource.flatfile.FlatFileAccessDataSource;
import org.buckit.datasource.flatfile.FlatFileHomesDataSource;
import org.buckit.datasource.flatfile.FlatFileKitsDataSource;
import org.buckit.datasource.flatfile.FlatFileReserveListDataSource;
import org.buckit.datasource.flatfile.FlatFileUserDataSource;
import org.buckit.datasource.flatfile.FlatFileWarpsDataSource;
import org.buckit.datasource.flatfile.FlatFileWhiteListDataSource;
import org.buckit.datasource.type.AccessDataSource;
import org.buckit.datasource.type.HomesDataSource;
import org.buckit.datasource.type.KitsDataSource;
import org.buckit.datasource.type.ReserveListDataSource;
import org.buckit.datasource.type.UserDataSource;
import org.buckit.datasource.type.WarpsDataSource;
import org.buckit.datasource.type.WhiteListDataSource;
import org.buckit.datasource.yaml.YamlAccessDataSource;
import org.buckit.datasource.yaml.YamlHomesDataSource;
import org.buckit.datasource.yaml.YamlKitsDataSource;
import org.buckit.datasource.yaml.YamlReserveListDataSource;
import org.buckit.datasource.yaml.YamlUserDataSource;
import org.buckit.datasource.yaml.YamlWarpsDataSource;
import org.buckit.datasource.yaml.YamlWhiteListDataSource;
import org.bukkit.Server;

public class DataSourceManager {

    private UserDataSource    userdatasource;
    private WarpsDataSource   warpsdatasource;
    private KitsDataSource    kitsdatasource;
    private HomesDataSource   homesdatasource;
    private AccessDataSource  accessdatasource;
    private WhiteListDataSource whitelistdatasource;
    private ReserveListDataSource reservelistdatasource;

    private RequestDataSource requestdatasource;
    
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
        
        requestdatasource = new RequestDataSource(this);
        
        getRequestDataSource().load();
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

    /**
     * @return the requestdatasource
     */
    public RequestDataSource getRequestDataSource() {
        return requestdatasource;
    }

    public Server getServer(){
        return server;
    }
}
