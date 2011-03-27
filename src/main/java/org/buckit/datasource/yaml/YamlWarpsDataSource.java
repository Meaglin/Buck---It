package org.buckit.datasource.yaml;

import java.util.Collection;

import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.WarpsDataSource;
import org.buckit.model.Warp;
import org.bukkit.Location;

public class YamlWarpsDataSource implements WarpsDataSource, DataSource {

    public YamlWarpsDataSource(DataSourceManager dataSource) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addWarp(String groupname, String name, Location warp, int minaccesslevel) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Warp> getAllWarps() {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataSourceManager getDataSource() {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Warp getWarp(String groupname, String name) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Warp> getWarps(String groupname) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean load() {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeWarp(Warp warp) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
