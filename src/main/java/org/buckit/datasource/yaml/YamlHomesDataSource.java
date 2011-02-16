package org.buckit.datasource.yaml;

import java.util.Collection;

import org.buckit.datasource.DataSource;
import org.buckit.datasource.type.HomesDataSource;
import org.buckit.model.Home;
import org.bukkit.Location;

public class YamlHomesDataSource implements HomesDataSource {

    public YamlHomesDataSource(DataSource dataSource) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteHome(int userId, String name) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataSource getDataSource() {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Home getHome(int userId, String name) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Home> getHomes(int userId) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean load() {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setHome(int userId, String username, String name, Location location) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
