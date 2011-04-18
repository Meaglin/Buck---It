package org.buckit.datasource.flatfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.buckit.Config;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.KitsDataSource;
import org.buckit.model.Kit;

public class FlatFileKitsDataSource implements KitsDataSource{

    private DataSourceManager datasource;
    
    private Map<String, Kit> kits;
    private Map<String, Integer> kitslast; //key=userid/kitname

    public FlatFileKitsDataSource(DataSourceManager dataSource) {
        datasource = dataSource;
    }
    
    public DataSourceManager getDataSource(){
        return datasource;
    }

    @Override
    public boolean load() {
        kits        = new HashMap<String, Kit>();
        kitslast    = new HashMap<String, Integer>();

        List<String> lines = FileHandler.getLines("kits");
        
        LineReader r;
        int delay, level;
        int[][] items;
        String name;
        for (int i=0; i<lines.size(); i++) {
            r = new LineReader(lines.get(i));
            
            name    = r.nextStr();
            items   = getItemArray(r.nextStr());
            delay   = r.nextInt();
            level   = r.nextInt();

            Kit kit = new Kit(-1, name, items, level, delay);
            kits.put(kit.getName(), kit);
        }
        
        FFLog.newInit("Kits", kits.size());
        
        List<String> lines2 = FileHandler.getLines("kitslast");
        
        int userid, time;
        String kitname;
        for (int i=0; i<lines2.size(); i++) {
            r = new LineReader(lines.get(i));
            
            userid      = r.nextInt();
            kitname     = r.nextStr();
            time        = r.nextInt();
            
            kitslast.put(userid+"/"+kitname, time);
        }
        
        FFLog.newInit("Kits-used", kitslast.size());
        
        return true;
    }
    
    public boolean setKit(Kit kit) {
        
        if (kits.containsKey(kit.getName())) {
            
            List<String> lines = FileHandler.getLines("kits");
            
            LineReader r;
            String name;
            for (int i=0; i<lines.size(); i++) {
                
                r = new LineReader(lines.get(i));
                
                name = r.nextStr();
                
                if (kit.getName().equals(name)) {
                    String str =    kit.getName() + FileHandler.sep1 + 
                                    kit.itemsToString() + FileHandler.sep1 + 
                                    kit.getDelay() + FileHandler.sep1 + 
                                    kit.getMinaccesslevel();
                    lines.set(i, str);
                    FFLog.newEdit("Kits", "edit kit '"+kit.getName()+"'");
                    break;
                }
            }
            kits.put(kit.getName(), kit);
            return FileHandler.writeFile("kits", lines);
        } else {
            FFLog.newEdit("Kits", "new kit '"+kit.getName()+"'");
            kits.put(kit.getName(), kit);
            return FileHandler.addLine("kits",  kit.getName() + FileHandler.sep1 + 
                                                kit.itemsToString() + FileHandler.sep1 + 
                                                kit.getDelay() + FileHandler.sep1 + 
                                                kit.getMinaccesslevel());
        }
    }
    
    @Override
    public Kit getKit(String name) {
        return kits.get(name);
    }

    @Override
    public Collection<Kit> getKits() {
        return kits.values();
    }
    

    @Override
    public boolean setLastUsed(int userid, String kitname, int time) {
        boolean exists = false;

        kitslast.put(userid+"/"+kitname, time);
        
        List<String> lines = FileHandler.getLines("kitslast");
        
        LineReader r;
        int useridL;
        String kitnameL;
        for (int i=0; i<lines.size(); i++) {
            r = new LineReader(lines.get(i));
            
            useridL     = r.nextInt();
            kitnameL    = r.nextStr();
            
            if (userid == useridL && kitname.equals(kitnameL)) {
                lines.set(i,    userid + FileHandler.sep1 + 
                                kitname + FileHandler.sep1 + 
                                time);
                exists = true;
                FFLog.newEdit("Kits-used", "kit '"+kitname+"' reused by '"+userid+"'");
                break;
            }
        }
        
        if (!exists) {
            lines.add(  userid + FileHandler.sep1 + 
                        kitname + FileHandler.sep1 + 
                        time);
            FFLog.newEdit("Kits-used", "kit '"+kitname+"' used by '"+userid+"'");
        }
        
        return FileHandler.writeFile("kitslast", lines);
    }
    
    
    @Override
    public int lastUsed(int userid, String kitname) {
        if (kitslast.get(userid+"/"+kitname) == null)
            return 0;
        else
            return kitslast.get(userid+"/"+kitname);
    }
    
    private static int[][] getItemArray(String str) {
        String[] split = str.split(Config.DATABASE_SEPERATOR);
        int[][] rt = new int[split.length][3];
        String[] parts;
        for (int i = 0; i < split.length; i++) {
            parts = split[i].split(Config.DATABASE_DELIMITER);
            try {
                rt[i][0] = Integer.parseInt(parts[0]);
                rt[i][1] = Integer.parseInt(parts[1]);
                rt[i][2] = Integer.parseInt(parts[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rt;
    }
}
