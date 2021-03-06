package org.buckit.datasource.type;

import java.util.Collection;

import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.model.Home;
import org.bukkit.Location;

public interface HomesDataSource extends DataSource  {

    public DataSourceManager getDataSource();
    
    public Home getHome(int userId, String name);

    public Collection<Home> getHomes(int userId);

    public boolean setHome(int userId, String username, String name, Location location);

    public boolean deleteHome(int userId, String name);
}
