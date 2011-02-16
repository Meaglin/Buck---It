package org.buckit.datasource.type;

public interface WhiteListDataSource {

    public boolean load();
    
    public boolean isWhiteListed(int userid);
    public boolean setWhiteListed(int userid, boolean whitelisted);
    
}
