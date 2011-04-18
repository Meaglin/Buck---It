package org.buckit.datasource.flatfile;

import java.util.List;

import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.WhiteListDataSource;

public class FlatFileWhiteListDataSource implements WhiteListDataSource{

    public FlatFileWhiteListDataSource(DataSourceManager dataSource) {
    }

    @Override
    public boolean load() {
        return true;
    }

    @Override
    public boolean setWhiteListed(int userid, String username, boolean whitelisted) {
        
        if (whitelisted == true) {
            FFLog.newEdit("reservelist", "new player '"+username+"' added");
            return FileHandler.addLine("whitelist",   userid + FileHandler.sep1 + 
                                                        username);
        } else {
            List<String> lines = FileHandler.getLines("whitelist");
            
            LineReader r;
            int useridR;
            for (int i=0; i<lines.size(); i++) {
                r = new LineReader(lines.get(i));
                
                useridR = r.nextInt();
                
                if (userid==useridR) {
                    lines.remove(i);
                    FFLog.newEdit("reservelist", "player '"+username+"' removed");
                    break;
                }
            }
            
            return FileHandler.writeFile("whitelist", lines);
        }
    }
    
    @Override
    public boolean isWhiteListed(int userid, String username) {
        boolean rt = false;
        
        List<String> lines = FileHandler.getLines("whitelist");
        
        LineReader r;
        int useridR;
        for (int i=0; i<lines.size(); i++) {
            r = new LineReader(lines.get(i));
            
            useridR = r.nextInt();
            
            if (userid==useridR) {
                rt = true;
                break;
            }
        }
        
        return rt;
    }
}
