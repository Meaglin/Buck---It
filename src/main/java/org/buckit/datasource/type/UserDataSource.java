package org.buckit.datasource.type;

import org.buckit.datasource.DataSourceManager;
import org.buckit.model.UserDataHolder;

public interface UserDataSource {

    public boolean load();
    public DataSourceManager getDataSource();

    public UserDataHolder getUserData(String username);

    public void updateUserDataOnDisconnect(UserDataHolder holder);

    public boolean updateUserBanTime(UserDataHolder holder);

    public boolean updateUserMuteTime(UserDataHolder holder);

    public boolean updateUser(UserDataHolder holder);
}
