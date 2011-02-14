package org.buckit.model;

import org.bukkit.Location;

public class Warp {

    private int      id, minaccesslevel;
    private String   name, group;
    private Location location;

    public Warp(int id, String name, String group, Location loc, int minaccesslevel) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.location = loc;
        this.minaccesslevel = minaccesslevel;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the minimum access level
     */
    public int getMinAccessLevel() {
        return minaccesslevel;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Warp))
            return false;

        if (getId() == ((Warp) object).getId())
            return true;

        return false;
    }
}
