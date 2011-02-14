package org.buckit.datasource.type;

import org.buckit.datasource.DataSource;
import org.buckit.model.UserDataHolder;

public interface UserDataSource {

    public boolean load();
    public DataSource getDataSource();

    public UserDataHolder getUserData(String username);

    public void updateUserDataOnDisconnect(UserDataHolder holder);

    public boolean updateUserBanTime(UserDataHolder holder);

    public boolean updateUserMuteTime(UserDataHolder holder);

    public boolean updateUser(UserDataHolder holder);
}
