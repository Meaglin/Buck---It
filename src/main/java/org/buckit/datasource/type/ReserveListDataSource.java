package org.buckit.datasource.type;

import org.buckit.datasource.DataSource;

public interface ReserveListDataSource  extends DataSource  {
    
    public boolean isReserveListed(int userid, String username);
    public boolean setReserveListed(int userid, String username, boolean reservelist);
    
}
