package org.buckit.datasource.flatfile;

import java.util.Collection;

import org.buckit.datasource.DataSource;
import org.buckit.datasource.type.WarpsDataSource;
import org.buckit.model.Warp;
import org.bukkit.Location;
import org.bukkit.Server;

public class FlatFileWarpsDataSource implements WarpsDataSource {

    public FlatFileWarpsDataSource(DataSource dataSource) {
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
    public DataSource getDataSource() {
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
    public boolean load(Server server) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeWarp(Warp warp) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
