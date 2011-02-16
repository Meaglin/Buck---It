package org.buckit.datasource.type;

public interface ReserveListDataSource {
    
    public boolean load();
    
    public boolean isReserveListed(int userid);
    public boolean setReserveListed(int userid, boolean reservelist);
    
}
