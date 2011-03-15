package org.buckit.datasource.flatfile;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.WarpsDataSource;
import org.buckit.model.Warp;
import org.bukkit.Location;

public class FlatFileWarpsDataSource implements WarpsDataSource, DataSource {

    private DataSourceManager datasource;

    private Map<String, Warp> warps; //key = groupname/name
    private int lastId = 0;
    
    public FlatFileWarpsDataSource(DataSourceManager dataSource) {
        datasource = dataSource;
    }
    
    public DataSourceManager getDataSource(){
        return datasource;
    }

    @Override
    public boolean load() {
        warps = new LinkedHashMap<String, Warp>();
        
        List<String> lines = FileHandler.getLines("warps");
        
        LineReader r;
        int id, level;
        String name, groupname, world;
        double x,y,z;
        float rotX, rotY;
        Warp warp;
        for (int i=0; i<lines.size(); i++) {
            r = new LineReader(lines.get(i));
            
            id = r.nextInt();
            name = r.nextStr();
            groupname = r.nextStr();
            world = r.nextStr();
            x = r.nextDouble();
            y = r.nextDouble();
            z = r.nextDouble();
            rotX = r.nextFloat();
            rotY = r.nextFloat();
            level = r.nextInt();
            
            warp = new Warp(id, name, groupname, new Location(datasource.getServer().getWorld(world), x, y, z, rotX, rotY), level);
            
            warps.put(groupname+"/"+name, warp);
            
            if (id > lastId)
                lastId=id;
        }
        
        FFLog.newInit("Warps", warps.size());
        
        return true;
    }
    
    @Override
    public boolean addWarp(String groupname, String name, Location l, int minaccesslevel) {
        
        Warp warp = new Warp((lastId+1), name, groupname, new Location(l.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch()), minaccesslevel);
        lastId++;
        
        String checkName = groupname+"/"+name;
        
        if (!warps.containsKey(checkName)) {
            warps.put(checkName, warp);
            
            FFLog.newEdit("Warps", "new warp '"+name+"'");
            
            return FileHandler.addLine("warps", warp.getId()+FileHandler.sep1+
                                                name+FileHandler.sep1+
                                                groupname+FileHandler.sep1+
                                                l.getWorld().getName()+FileHandler.sep1+
                                                l.getX()+FileHandler.sep1+
                                                l.getY()+FileHandler.sep1+
                                                l.getZ()+FileHandler.sep1+
                                                l.getYaw()+FileHandler.sep1+
                                                l.getPitch()+FileHandler.sep1+
                                                minaccesslevel);
            
        } else {
            return false;
        }
    }

    @Override
    public boolean removeWarp(Warp warp) {
        
        warps.remove(warp.getGroup()+"/"+warp.getName());
        
        List<String> lines = FileHandler.getLines("warps");
        
        LineReader r;
        int id;
        for (int i=0; i<lines.size(); i++) {
            r = new LineReader(lines.get(i));
            
            id = r.nextInt();
            
            if (warp.getId() == id) {
                lines.remove(i);
                FFLog.newEdit("Warps", "removed warp '"+warp.getName()+"'");
                break;
            }
        }
        
        return FileHandler.writeFile("warps", lines);
    }
    
    @Override
    public Warp getWarp(String groupname, String name) {
        return warps.get(groupname+"/"+name);
    }

    @Override
    public Collection<Warp> getWarps(String groupname) {
        Collection<Warp> warpsOut = new LinkedList<Warp>();
        
        for (String key : warps.keySet()) {
            if (groupname.equals(warps.get(key).getGroup()))
                warpsOut.add(warps.get(key));
        }
        
        return warpsOut;
    }

    @Override
    public Collection<Warp> getAllWarps() {
        return warps.values();
    }
}
