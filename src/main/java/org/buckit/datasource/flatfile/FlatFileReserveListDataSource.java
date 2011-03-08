package org.buckit.datasource.flatfile;

import java.util.List;

import org.buckit.datasource.DataSource;
import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.ReserveListDataSource;

public class FlatFileReserveListDataSource implements ReserveListDataSource, DataSource {

    public FlatFileReserveListDataSource(DataSourceManager dataSource) {
    }

    @Override
    public boolean load() {
        return true;
    }

    @Override
    public boolean setReserveListed(int userid, String username, boolean reservelisted) {
        
        if (reservelisted == true) {
            FFLog.newEdit("reservelist", "new player '"+username+"' added");
            return FileHandler.addLine("reservelist",   userid + FileHandler.sep1 + 
                                                        username);
        } else {
            List<String> lines = FileHandler.getLines("reservelist");
            
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
            
            return FileHandler.writeFile("reservelist", lines);
        }
    }
    
    @Override
    public boolean isReserveListed(int userid, String username) {
        boolean rt = false;
        
        List<String> lines = FileHandler.getLines("reservelist");
        
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
