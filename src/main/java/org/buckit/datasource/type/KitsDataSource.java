package org.buckit.datasource.type;

import java.util.Collection;

import org.buckit.datasource.DataSource;
import org.buckit.model.Kit;

public interface KitsDataSource {

    public boolean load();
    public DataSource getDataSource();

    public Kit getKit(String name);
    public boolean setKit(Kit kit);
    
    public boolean setLastUsed(int userId, String kitname, int time);
    public int lastUsed(int userId, String kitname);
    
    public Collection<Kit> getKits();
}
