package org.buckit.datasource.yaml;

import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.WhiteListDataSource;

public class YamlWhiteListDataSource implements WhiteListDataSource, DataSource {

    public YamlWhiteListDataSource(DataSourceManager dataSource) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWhiteListed(int userid, String username) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean load() {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setWhiteListed(int userid, String username, boolean whitelisted) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
