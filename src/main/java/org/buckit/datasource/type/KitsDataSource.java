package org.buckit.datasource.type;

import java.util.Collection;

import org.buckit.datasource.DataSourceManager;
import org.buckit.model.Kit;

public interface KitsDataSource {

    public boolean load();
    public DataSourceManager getDataSource();

    public Kit getKit(String name);
    public boolean setKit(Kit kit);
    
    public boolean setLastUsed(int userId, String kitname, int time);
    public int lastUsed(int userId, String kitname);
    
    public Collection<Kit> getKits();
}
