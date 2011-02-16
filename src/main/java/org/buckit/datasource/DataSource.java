package org.buckit.datasource;

import org.buckit.Config;
import org.buckit.datasource.mysql.*;
import org.buckit.datasource.flatfile.*;
import org.buckit.datasource.type.*;
import org.buckit.datasource.yaml.*;
import org.bukkit.Server;

public class DataSource {

    private UserDataSource    userdatasource;
    private WarpsDataSource   warpsdatasource;
    private KitsDataSource    kitsdatasource;
    private HomesDataSource   homesdatasource;
    private AccessDataSource  accessdatasource;
    private WhiteListDataSource whitelistdatasource;
    private ReserveListDataSource reservelistdatasource;

    private Server server;
    public DataSource(Server server) {
        this.server = server;
        
        load();
    }

    private void load() {
        switch (Config.DATA_SOURCE_TYPE) {
            case MYSQL:
                userdatasource = new MysqlUserDataSource(this);
                accessdatasource = new MysqlAccessDataSource(this);
                kitsdatasource = new MysqlKitsDataSource(this);
                homesdatasource = new MysqlHomesDataSource(this);
                warpsdatasource = new MysqlWarpsDataSource(this);
                whitelistdatasource = new MysqlWhiteListDataSource(this);
                reservelistdatasource = new MysqlReserveListDataSource(this);
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
            getWarpsDataSource().load(getServer());
        if (Config.HOMES_ENABLED)
            getHomesDataSource().load();
        if (Config.KITS_ENABLED)
            getKitsDataSource().load();
        if  (Config.WHITELIST_ENABLED)
            getWhiteListDataSource().load();
        if  (Config.RESERVELIST_ENABLED)
            getReserveListDataSource().load();
    }

    public DataType getType() {
        return DataType.MYSQL;
    }

    public String toString() {
        return "";
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
