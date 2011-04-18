package org.buckit.datasource.type;

import org.buckit.datasource.DataSource;

public interface WhiteListDataSource extends DataSource {
    
    public boolean isWhiteListed(int userid, String username);
    public boolean setWhiteListed(int userid, String username, boolean whitelisted);
    
}
