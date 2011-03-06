package org.buckit.datasource.flatfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.WarpsDataSource;
import org.buckit.model.Warp;
import org.bukkit.Location;

//id,name,groupname,world,x,y,z,rotX,rotY,minaccesslevel
public class FlatFileWarpsDataSource implements WarpsDataSource, DataSource {


    //key = groupname/name
    private Map<String, Warp> warps;
    private DataSourceManager datasource;
    private int nextId = 0;
    
    public FlatFileWarpsDataSource(DataSourceManager dataSource) {
        datasource = dataSource;
    }
    public DataSourceManager getDataSource(){
        return datasource;
    }

    @Override
    public boolean addWarp(String groupname, String name, Location l, int minaccesslevel) {
        nextId++;
        
        Warp warp = new Warp(nextId, name, groupname, new Location(l.getWorld(), l.getX(), l.getY(), l.getZ(), l.getPitch(), l.getYaw()), minaccesslevel);
        warps.put(groupname+"/"+name, warp);
        
        return FileHandler.addLine("warps", nextId+FileHandler.sep1+
                                    name+FileHandler.sep1+
                                    groupname+FileHandler.sep1+
                                    l.getWorld()+FileHandler.sep1+
                                    l.getX()+FileHandler.sep1+
                                    l.getY()+FileHandler.sep1+
                                    l.getZ()+FileHandler.sep1+
                                    l.getPitch()+FileHandler.sep1+
                                    l.getYaw()+FileHandler.sep1+
                                    minaccesslevel);
    }

    @Override
    public Collection<Warp> getAllWarps() {
        return warps.values();
    }

    @Override
    public Warp getWarp(String groupname, String name) {
        return warps.get(groupname+"/"+name);
    }

    @Override
    public Collection<Warp> getWarps(String groupname) {
        Collection<Warp> warpsOut = new ArrayList<Warp>();
        
        for (String key : warps.keySet()) {
            if (groupname.equals(key.split("/")[0]))
                warpsOut.add(warps.get(key));
        }
        
        return warpsOut;
    }

    @Override
    public boolean load() {
        warps = new HashMap<String, Warp>();
        
        List<String> lines = FileHandler.getLines("warps");
        
        Warp warp;
        for (int i=0; i<lines.size(); i++) {
            //id,name,groupname,world,x,y,z,rotX,rotY,minaccesslevel
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            int     id;  try { id = Integer.parseInt(entry[0]); } catch (Exception e) { return false; }
            String  name = entry[1];
            String  groupname = entry[2];
            String  world    = entry[3];
            double  x;       try { x = Double.parseDouble(entry[4]); } catch (Exception e) { return false; }
            double  y;       try { y = Double.parseDouble(entry[5]); } catch (Exception e) { return false; }
            double  z;       try { z = Double.parseDouble(entry[6]); } catch (Exception e) { return false; }
            float   rotX;    try { rotX = Float.parseFloat(entry[7]); } catch (Exception e) { return false; }
            float   rotY;    try { rotY = Float.parseFloat(entry[8]); } catch (Exception e) { return false; }  
            int     level;   try { level = Integer.parseInt(entry[4]); } catch (Exception e) { return false; }
            
            warp = new Warp(id, name, groupname, new Location(datasource.getServer().getWorld(world), x, y, z, rotX, rotY), level);
            warps.put(groupname+"/"+name, warp);
            
            if (id>nextId)
                nextId=id;
        }
        
        return true;
    }

    @Override
    public boolean removeWarp(Warp warp) {
        
        List<String> lines = FileHandler.getLines("warps");
        
        for (int i=0; i<lines.size(); i++) {
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            int     id;  try { id = Integer.parseInt(entry[0]); } catch (Exception e) { return false; }
            
            if (warp.getId() == id) {
                warps.remove(warp.getGroup()+"/"+warp.getName());
                lines.remove(i);
            }
        }
        
        return FileHandler.writeFile("warps", lines);
    }
}
