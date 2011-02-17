package org.buckit.datasource.flatfile;

import java.util.List;

import org.buckit.datasource.DataSource;
import org.buckit.datasource.type.ReserveListDataSource;

//USERID:LISTED
public class FlatFileReserveListDataSource implements ReserveListDataSource {

    public FlatFileReserveListDataSource(DataSource dataSource) {
    }

    @Override
    public boolean isReserveListed(int userid) {
        boolean ret = false;
        List<String> lines = FileHandler.getLines("reservelist");
        
        for (int i=0; i<lines.size(); i++) {
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            int useridR;  try { useridR = Integer.parseInt(entry[0]); } catch (Exception e) { return false; }
            boolean isR;  try { isR = Boolean.parseBoolean(entry[1]); } catch (Exception e) { return false; }
            
            if (userid==useridR)
                ret = isR;
        }
        
        return ret;
    }

    @Override
    public boolean load() {
        return true;
    }

    @Override
    public boolean setReserveListed(int userid, boolean reservelist) {
        List<String> lines = FileHandler.getLines("reservelist");
        
        for (int i=0; i<lines.size(); i++) {
            String[] entry = lines.get(i).split(FileHandler.sep1);
            
            int useridR;  try { useridR = Integer.parseInt(entry[0]); } catch (Exception e) { return false; }
            
            if (userid==useridR) {
                lines.set(i, useridR + FileHandler.sep1 + reservelist);
            }
        }
        
        return FileHandler.writeFile("reservelist", lines);
    }

}
