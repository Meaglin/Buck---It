package org.buckit.datasource.database;

import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.WhiteListDataSource;

public class DatabaseWhiteListDataSource implements WhiteListDataSource, DataSource {

    public DatabaseWhiteListDataSource(DataSourceManager dataSource) {
        // do nothing
    }

    @Override
    public boolean isWhiteListed(int userid, String username) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean load() {
        // do nothing
        // we chose for a non caching setting for this situation.
        return true;
    }

    @Override
    public boolean setWhiteListed(int userid, String username, boolean whitelisted) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
