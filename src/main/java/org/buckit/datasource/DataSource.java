package org.buckit.datasource;

import org.buckit.Config;
import org.buckit.datasource.mysql.MysqlAccessDataSource;
import org.buckit.datasource.mysql.MysqlHomesDataSource;
import org.buckit.datasource.mysql.MysqlKitsDataSource;
import org.buckit.datasource.mysql.MysqlUserDataSource;
import org.buckit.datasource.mysql.MysqlWarpsDataSource;
import org.buckit.datasource.type.*;
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
                break;
            case FLATFILE:

                break;
            case YAML:

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
    public WhiteListDataSource getWhitelistDataSource() {
        return whitelistdatasource;
    }

    /**
     * @return the reservelistdatasource
     */
    public ReserveListDataSource getReservelistDataSource() {
        return reservelistdatasource;
    }

    public Server getServer(){
        return server;
    }
}
