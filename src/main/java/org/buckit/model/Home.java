package org.buckit.model;

import org.bukkit.Location;

public class Home {

    private int      id;
    private String   name;
    private Location location;

    public Home(int id, String name, Location loc) {
        this.id = id;
        this.name = name;
        this.location = loc;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Home))
            return false;

        if (getId() == ((Home) object).getId())
            return true;

        return false;
    }
}
