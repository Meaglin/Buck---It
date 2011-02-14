package org.buckit.access;

import java.util.ArrayList;

import org.buckit.Config;

public class Group {
    private int               id;
    private String            name;
    private ArrayList<String> commands;

    public Group(int id, String name, String commands) {
        this.commands = new ArrayList<String>();
        for (String command : commands.split(Config.DATABASE_DELIMITER))
            this.commands.add(command);

        this.name = name;
        this.id = id;

    }

    public boolean canUseCommand(String command) {
        return commands.contains(command);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Group))
            return false;

        if (getId() == ((Group) object).getId())
            return true;

        return false;
    }
}
