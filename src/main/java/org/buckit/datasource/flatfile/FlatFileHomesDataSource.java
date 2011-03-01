package org.buckit.datasource.flatfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.buckit.Config;
import org.buckit.datasource.DataSource;
import org.buckit.datasource.type.HomesDataSource;
import org.buckit.model.Home;
import org.bukkit.Location;
import org.bukkit.Server;

// ID:NAME:USERID:USERNAME:WORLD:X:Y:Z:rotX:rotY
public class FlatFileHomesDataSource implements HomesDataSource {

    private DataSource datasource;
    private Server      server;
    
    public FlatFileHomesDataSource(DataSource dataSource) {
        datasource = dataSource;
        server = datasource.getServer();
    }
    public DataSource getDataSource(){
        return datasource;
    }
    
    @Override
    public boolean deleteHome(int userId, String name) {
        List<String> lines = FileHandler.readFile(new File(Config.FLATFILE_HOMES_DIRECTORY+userId+".txt"));
        
        for (int i=0; i<lines.size(); i++) {
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            int homeUserid;
            try {
                homeUserid = Integer.parseInt(entry[2]);
            } catch (Exception e) {
                return false;
            }
            
            String homeName = entry[1];
            
            if (userId==homeUserid && name.equals(homeName)) {
                lines.remove(i);
            }
        }
        
        return FileHandler.writeFile("homes", lines);
    }

    @Override
    public Home getHome(int userId, String name) {
        Home home = null;

        List<String> lines = FileHandler.readFile(new File(Config.FLATFILE_HOMES_DIRECTORY+userId+".txt"));
        for (int i=0; i<lines.size(); i++) {
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            int     homeId;  try { homeId = Integer.parseInt(entry[0]); } catch (Exception e) { return home; }
            String  homeName = entry[1];
            int     homeUserId;  try { homeUserId = Integer.parseInt(entry[2]); } catch (Exception e) { return home; }
            String  world    = entry[4];
            double  x;       try { x = Double.parseDouble(entry[5]); } catch (Exception e) { return home; }
            double  y;       try { y = Double.parseDouble(entry[6]); } catch (Exception e) { return home; }
            double  z;       try { z = Double.parseDouble(entry[7]); } catch (Exception e) { return home; }
            float   rotX;    try { rotX = Float.parseFloat(entry[8]); } catch (Exception e) { return home; }
            float   rotY;    try { rotY = Float.parseFloat(entry[9]); } catch (Exception e) { return home; }            
         // ID:NAME:USERID:USERNAME:WORLD:X:Y:Z:rotX:rotY
            
            if (userId==homeUserId && name.equals(homeName)) {
                home = new Home(homeId, name, new Location(server.getWorld(world), x, y, z, rotX, rotY));
            }
        }
        
        return home;
    }

    @Override
    public List<Home> getHomes(int userId) {
        List<Home> home = new ArrayList<Home>();

        List<String> lines = FileHandler.readFile(new File(Config.FLATFILE_HOMES_DIRECTORY+userId+".txt"));
        for (int i=0; i<lines.size(); i++) {
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            int     homeId;  try { homeId = Integer.parseInt(entry[0]); } catch (Exception e) { return home; }
            String  homeName = entry[1];
            String  world    = entry[4];
            double  x;       try { x = Double.parseDouble(entry[5]); } catch (Exception e) { return home; }
            double  y;       try { y = Double.parseDouble(entry[6]); } catch (Exception e) { return home; }
            double  z;       try { z = Double.parseDouble(entry[7]); } catch (Exception e) { return home; }
            float   rotX;    try { rotX = Float.parseFloat(entry[8]); } catch (Exception e) { return home; }
            float   rotY;    try { rotY = Float.parseFloat(entry[9]); } catch (Exception e) { return home; }            
         // ID:NAME:USERID:USERNAME:WORLD:X:Y:Z:rotX:rotY
            
            home.add(new Home(homeId, homeName, new Location(server.getWorld(world), x, y, z, rotX, rotY)));
        }
        
        return home;       
    }

    @Override
    public boolean load() {
        return true;
        // do nothing
    }

    @Override
    public boolean setHome(int userId, String username, String name, Location home) {

        List<String> lines = FileHandler.readFile(new File(Config.FLATFILE_HOMES_DIRECTORY+userId+".txt"));
        for (int i=0; i<lines.size(); i++) {
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            int     homeId;  try { homeId = Integer.parseInt(entry[0]); } catch (Exception e) { return false; }
            String  homeName = entry[1];
            int     homeUserId;  try { homeUserId = Integer.parseInt(entry[2]); } catch (Exception e) { return false; }
            String  homeUsername = entry[3];
         // ID:NAME:USERID:USERNAME:WORLD:X:Y:Z:rotX:rotY
            
            if (name==homeName) {
                lines.set(i, homeId+FileHandler.sep1+
                            homeName+FileHandler.sep1+
                            homeUserId+FileHandler.sep1+
                            homeUsername+FileHandler.sep1+
                            home.getWorld()+FileHandler.sep1+
                            home.getX()+FileHandler.sep1+
                            home.getY()+FileHandler.sep1+
                            home.getZ()+FileHandler.sep1+
                            home.getPitch()+FileHandler.sep1+
                            home.getYaw());
            }
        }

        return FileHandler.writeFile(new File(Config.FLATFILE_HOMES_DIRECTORY+userId+".txt"), lines);
    }

}