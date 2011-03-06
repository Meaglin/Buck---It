package org.buckit.datasource.type;

public interface ReserveListDataSource {
    
    public boolean load();
    
    public boolean isReserveListed(int userid, String username);
    public boolean setReserveListed(int userid, String username, boolean reservelist);
    
}
