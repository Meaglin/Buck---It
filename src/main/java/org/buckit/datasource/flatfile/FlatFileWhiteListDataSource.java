package org.buckit.datasource.flatfile;

import java.util.List;

import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.WhiteListDataSource;

//USERID:USERNAME
public class FlatFileWhiteListDataSource implements WhiteListDataSource, DataSource {

    public FlatFileWhiteListDataSource(DataSourceManager dataSource) {
    }

    @Override
    public boolean isWhiteListed(int userid, String username) {
        boolean ret = false;
        
        List<String> lines = FileHandler.getLines("whitelist");
        for (int i=0; i<lines.size(); i++) {
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            int useridR;  try { useridR = Integer.parseInt(entry[0]); } catch (Exception e) { return false; }
            
            if (userid==useridR)
                ret = true;
        }
        
        return ret;
    }

    @Override
    public boolean load() {
        return true;
    }

    @Override
    public boolean setWhiteListed(int userid, String username, boolean whitelisted) {
    	
    	if (whitelisted == true) {
    		return FileHandler.addLine("whitelist", userid + FileHandler.sep1 + username);
    	} else {
    		List<String> lines = FileHandler.getLines("whitelist");
    		
    		for (int i=0; i<lines.size(); i++) {
                String[] entry = lines.get(i).split(FileHandler.sep1);
                
                int useridR;  try { useridR = Integer.parseInt(entry[0]); } catch (Exception e) { return false; }
                
                if (userid==useridR)
                	lines.remove(i);
            }
    		
    		return FileHandler.writeFile("whitelist", lines);
    	}
    }

}
