package org.buckit.datasource.type;

import java.util.Collection;

import org.buckit.datasource.DataSourceManager;
import org.buckit.model.Warp;
import org.bukkit.Location;

public interface WarpsDataSource {

    public boolean load();
    public DataSourceManager getDataSource();

    public boolean addWarp(String groupname, String name, Location warp, int minaccesslevel);

    public boolean removeWarp(Warp warp);

    public Warp getWarp(String groupname, String name);

    public Collection<Warp> getWarps(String groupname);

    public Collection<Warp> getAllWarps();
}
