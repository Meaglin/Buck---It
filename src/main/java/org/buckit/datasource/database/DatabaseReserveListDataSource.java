package org.buckit.datasource.database;

import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.ReserveListDataSource;

public class DatabaseReserveListDataSource implements ReserveListDataSource, DataSource {

    public DatabaseReserveListDataSource(DataSourceManager dataSource) {
        // do nothing
    }

    @Override
    public boolean isReserveListed(int userid, String username) {
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
    public boolean setReserveListed(int userid, String username, boolean reservelist) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
