package org.buckit.datasource.yaml;

import java.util.Collection;

import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.KitsDataSource;
import org.buckit.model.Kit;

public class YamlKitsDataSource implements KitsDataSource{

    public YamlKitsDataSource(DataSourceManager dataSource) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataSourceManager getDataSource() {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Kit getKit(String name) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Kit> getKits() {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int lastUsed(int userId, String kitname) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean load() {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setKit(Kit kit) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setLastUsed(int userId, String kitname, int time) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
