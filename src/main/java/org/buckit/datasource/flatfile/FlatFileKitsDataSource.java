package org.buckit.datasource.flatfile;

import java.util.List;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.buckit.Config;
import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.flatfile.FileHandler;
import org.buckit.datasource.type.KitsDataSource;
import org.buckit.model.Kit;

// kits: NAME:IDs:DELAY:ACCESSLEVEL
// kitslast: USERID:KITNAME:TIME

public class FlatFileKitsDataSource implements KitsDataSource, DataSource {

    private Map<String, Kit> kits;
    private Map<String, Integer> kitslast; //key=userid/kitname
    

    private DataSourceManager datasource;
    public FlatFileKitsDataSource(DataSourceManager dataSource) {
        datasource = dataSource;
    }
    public DataSourceManager getDataSource(){
        return datasource;
    }
    
    @Override
    public Kit getKit(String name) {
        return kits.get(name);
    }

    @Override
    public Collection<Kit> getKits() {
        return kits.values();
    }
    
    public boolean setKit(Kit kit) {

        kits.put(kit.getName(), kit);
    	
    	if (kits.containsKey(kit.getName())) {
        	List<String> lines = FileHandler.getLines("kits");
        	for (int i=0; i<lines.size(); i++) {
        		String[] entry = lines.get(i).split(FileHandler.sep1);
               
        		String name = entry[0];
                
                if (kit.getName().equals(name)) {
                	String str = kit.getName() + FileHandler.sep1 + 
                    				kit.itemsToString() + FileHandler.sep1 + 
                    				kit.getDelay() + FileHandler.sep1 + 
                    				kit.getMinaccesslevel();
                	lines.set(i, str);
                }
        	}
        	
            return FileHandler.writeFile("kits", lines);
    		
    	} else {
            return FileHandler.addLine("kits", kit.getName() + FileHandler.sep1 + 
                                                kit.itemsToString() + FileHandler.sep1 + 
                                                kit.getDelay() + FileHandler.sep1 + 
                                                kit.getMinaccesslevel());
    	}
    }

    @Override
    public boolean load() {
        kits = new HashMap<String, Kit>();
        kitslast = new HashMap<String, Integer>();

        List<String> lines = FileHandler.getLines("kits");
        
        int id = 0;
        for (int i=0; i<lines.size(); i++) {
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            String name = entry[0];
            int[][] items = getItemArray(entry[1]);
            
            int delay = 0;
            try {
                delay = Integer.parseInt(entry[2]);
            } catch(Exception e) { return false; }
            
            int accesslevel = 0;
            try {
                accesslevel = Integer.parseInt(entry[3]);
            } catch(Exception e) { return false; }

            Kit kit = new Kit(id, name, items, accesslevel, delay);
            kits.put(kit.getName(), kit);
            
            id++;
        }
        
        List<String> lines2 = FileHandler.getLines("kitslast");
        
        for (int i=0; i<lines2.size(); i++) {
            String[] entry = lines2.get(i).split(FileHandler.sep1);
            
            int     useridL;    try { useridL = Integer.parseInt(entry[0]); } catch (Exception e) { return false; }
            String  kitnameL    = entry[1];
            int     timeL;      try { timeL = Integer.parseInt(entry[2]); } catch (Exception e) { return false; }
            
            kitslast.put(useridL+"/"+kitnameL, timeL);
        }
        
        return true;
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

 // kitslast: USERID:KITNAME:TIME
    @Override
    public boolean setLastUsed(int userid, String kitname, int time) {
        List<String> lines = FileHandler.getLines("kitslast");
        
        boolean renewed = false;
        for (int i=0; i<lines.size(); i++) {
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            int     useridL;    try { useridL = Integer.parseInt(entry[0]); } catch (Exception e) { return false; }
            String  kitnameL    = entry[1];
            
            if (userid == useridL && kitname.equals(kitnameL)) {
                lines.set(i, userid + FileHandler.sep1 + kitname + FileHandler.sep1 + time);
                renewed = true;
            }
        }
        
        if (!renewed) {
            lines.add(userid + FileHandler.sep1 + kitname + FileHandler.sep1 + time);
        }
        
        kitslast.put(userid+"/"+kitname, time);
        
        return FileHandler.writeFile("kitslast", lines);
    }
    
    
    @Override
    public int lastUsed(int userid, String kitname) {
        return kitslast.get(userid+"/"+kitname);
    }
}
