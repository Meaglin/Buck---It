package org.buckit.datasource.yaml;

import org.buckit.datasource.DataSourceManager;
import org.buckit.datasource.type.UserDataSource;
import org.buckit.model.UserDataHolder;

public class YamlUserDataSource implements UserDataSource{

    public YamlUserDataSource(DataSourceManager dataSource) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataSourceManager getDataSource() {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserDataHolder getUserData(String username) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean load() {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateUser(UserDataHolder holder) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateUserBanTime(UserDataHolder holder) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateUserDataOnDisconnect(UserDataHolder holder) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateUserMuteTime(UserDataHolder holder) {
        // TODO: Implement.
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
