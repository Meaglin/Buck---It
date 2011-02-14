package org.buckit.access;

import org.buckit.Config;

public class AccessLevel {

    private int    id;
    private String name;
    private String usernameformat;

    private boolean canBuild, isAdmin, hasFullAccess;
    private Group[] childs;

    public AccessLevel(int id, Group[] childs, String name, String usernameformat, boolean canBuild, boolean isAdmin) {
        this.id = id;
        this.childs = childs;
        this.name = name;
        this.usernameformat = usernameformat;
        this.canBuild = canBuild;
        this.isAdmin = isAdmin;
        hasFullAccess = canUseCommand(Config.FULL_ACCESS_STRING);
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
     * @return the usernameformat
     */
    public String getUsernameformat() {
        return usernameformat;
    }

    /**
     * @return the canBuild
     */
    public boolean canBuild() {
        return canBuild;
    }

    /**
     * @return the isAdmin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * @return the childs
     */
    public Group[] getChilds() {
        return childs;
    }

    /**
     * @return the hasFullAccess
     */
    public boolean hasFullAccess() {
        return hasFullAccess;
    }

    public boolean canUseCommand(String command) {
        if (hasFullAccess())
            return true;

        for (Group g : getChilds())
            if (g.canUseCommand(command))
                return true;

        return false;
    }
}
