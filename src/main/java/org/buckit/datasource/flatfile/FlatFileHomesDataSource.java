package org.buckit.datasource.flatfile;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.buckit.Config;
import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.HomesDataSource;
import org.buckit.model.Home;
import org.bukkit.Location;
import org.bukkit.Server;

public class FlatFileHomesDataSource implements HomesDataSource, DataSource {

    private DataSourceManager datasource;
    private Server server;
    
    private int lastId=0;    
    
    public FlatFileHomesDataSource(DataSourceManager dataSource) {
        datasource  = dataSource;
        server      = datasource.getServer();
    }
    
    public DataSourceManager getDataSource(){
        return datasource;
    }
    
    @Override
    public boolean load() {
        File dir = new File(Config.FLATFILE_HOMES_DIRECTORY);
        String[] files = dir.list();
        
        int linestotal=0;
        if (files != null) {
            List<String> lines;
            for (String f : files) {
                File file = new File(Config.FLATFILE_HOMES_DIRECTORY+f);
                lines = FileHandler.readFile(file);
                
                LineReader r;
                int homeId;
                
                for (int i=0; i<lines.size(); i++) {
                    r = new LineReader(lines.get(i));
                    
                    homeId      = r.nextInt();
                    
                    if (homeId > lastId) 
                        lastId = homeId;
                    
                    linestotal++;
                }
            }
        }
        
        FFLog.newFound("Homes", linestotal);
        
        return true;
    }

    @Override
    public boolean setHome(int userId, String username, String name, Location home) {
        boolean exists = false;
        
        File f = new File(Config.FLATFILE_HOMES_DIRECTORY+userId+".txt");
        
        if (!f.exists()) {
            new File(f.getParent()).mkdirs();
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FFLog.newFile(f.getPath());
        }
        
        LineReader r;
        List<String> lines = FileHandler.readFile(f);
        int homeId;
        String homeName;
        for (int i=0; i<lines.size(); i++) {
            r = new LineReader(lines.get(i));
            
            homeId      = r.nextInt();
            homeName    = r.nextStr();
            
            if (name.equals(homeName)) {
                lines.set(i, homeId+FileHandler.sep1+
                            homeName+FileHandler.sep1+
                            userId+FileHandler.sep1+
                            username+FileHandler.sep1+
                            home.getWorld().getName()+FileHandler.sep1+
                            home.getX()+FileHandler.sep1+
                            home.getY()+FileHandler.sep1+
                            home.getZ()+FileHandler.sep1+
                            home.getPitch()+FileHandler.sep1+
                            home.getYaw());
                exists = true;
                FFLog.newEdit("Homes ("+username+")", "edit home '"+name+"'");
                break;
            }
        }
        
        if (!exists) {
            lines.add((lastId+1)+FileHandler.sep1+
                name+FileHandler.sep1+
                userId+FileHandler.sep1+
                username+FileHandler.sep1+
                home.getWorld().getName()+FileHandler.sep1+
                home.getX()+FileHandler.sep1+
                home.getY()+FileHandler.sep1+
                home.getZ()+FileHandler.sep1+
                home.getPitch()+FileHandler.sep1+
                home.getYaw());
            lastId++;
            
            FFLog.newEdit("Homes ("+username+")", "new home '"+name+"'");
        }

        return FileHandler.writeFile(f, lines);
    }

    @Override
    public Home getHome(int userId, String name) {
        File f = new File(Config.FLATFILE_HOMES_DIRECTORY+userId+".txt");
        List<String> lines = FileHandler.readFile(f);
        
        LineReader r;
        Home home = null;
        int homeId;
        String homeName, world;
        double x,y,z;
        float rotY, rotX;
        
        for (int i=0; i<lines.size(); i++) {
            
            r = new LineReader(lines.get(i));
            
            homeId      = r.nextInt();
            homeName    = r.nextStr();
            r.skip();
            r.skip();
            world       = r.nextStr();
            x           = r.nextDouble();
            y           = r.nextDouble();
            z           = r.nextDouble();
            rotY        = r.nextFloat();
            rotX        = r.nextFloat();    
            
            if (name.equals(homeName)) {
                home = new Home(homeId, name, new Location(server.getWorld(world), x, y, z, rotX, rotY));
                break;
            }
        }
        
        return home;
    }

    @Override
    public Collection<Home> getHomes(int userId) {
        File f = new File(Config.FLATFILE_HOMES_DIRECTORY+userId+".txt");
        List<String> lines = FileHandler.readFile(f);

        LineReader r;
        Map<String, Home> homes = new LinkedHashMap<String, Home>();
        int homeId;
        String homeName, world;
        double x,y,z;
        float rotY, rotX;
        
        for (int i=0; i<lines.size(); i++) {
            r = new LineReader(lines.get(i));
            
            homeId      = r.nextInt();
            homeName    = r.nextStr();
            r.skip();
            r.skip();
            world       = r.nextStr();
            x           = r.nextDouble();
            y           = r.nextDouble();
            z           = r.nextDouble();
            rotY        = r.nextFloat();
            rotX        = r.nextFloat();    
            
            homes.put(homeName, new Home(homeId, homeName, new Location(server.getWorld(world), x, y, z, rotX, rotY)));
        }
        
        return homes.values();
    }
    
    @Override
    public boolean deleteHome(int userId, String name) {
        File f = new File(Config.FLATFILE_HOMES_DIRECTORY+userId+".txt");
        List<String> lines = FileHandler.readFile(f);
        
        LineReader r;
        String homeName;
        
        for (int i=0; i<lines.size(); i++) {
            r = new LineReader(lines.get(i));
            homeName = r.getStr(1);
            
            if (name.equals(homeName)) {
                lines.remove(i);
                FFLog.newEdit("Homes ("+userId+")", "delete home '"+name+"'");
                break;
            }
        }
        
        return FileHandler.writeFile("homes", lines);
    }
}
