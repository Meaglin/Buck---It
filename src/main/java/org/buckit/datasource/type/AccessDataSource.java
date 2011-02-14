package org.buckit.datasource.type;

import org.buckit.access.AccessLevel;
import org.buckit.access.Group;
import org.buckit.datasource.DataSource;

public interface AccessDataSource {

    public boolean load();
    public DataSource getDataSource();

    public AccessLevel getAccessLevel(int id);

    public AccessLevel getAccessLevel(String name);

    public Group getGroup(int id);

    public Group getGroup(String name);
}
