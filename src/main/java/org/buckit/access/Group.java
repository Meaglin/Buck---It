package org.buckit.access;

import java.util.Arrays;
import java.util.List;

import org.buckit.Config;

public class Group {
    private int               id;
    private String            name;
    private List<String> commands;
    private List<String> worlds;

    public Group(int id, String name, String commands, String worlds) {

        this.commands = Arrays.asList(commands.split(Config.DATABASE_DELIMITER));
        this.worlds = Arrays.asList(worlds.split(Config.DATABASE_DELIMITER));
        
        this.name = name;
        this.id = id;

    }

    public boolean canUseCommand(String command, String world) {
        return commands.contains(command) && (worlds.contains(world) || worlds.contains(Config.FULL_ACCESS_STRING));
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
