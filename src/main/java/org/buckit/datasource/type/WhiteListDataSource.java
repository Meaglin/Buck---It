package org.buckit.datasource.type;

public interface WhiteListDataSource {

    public boolean load();
    
    public boolean isWhiteListed(int userid, String username);
    public boolean setWhiteListed(int userid, String username, boolean whitelisted);
    
}
