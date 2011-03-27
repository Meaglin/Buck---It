package org.buckit.access;

public class AccessLevel {

    private int    id;
    private String name;
    private String usernameformat;

    private boolean canBuild, isAdmin;
    private Group[] childs;

    public AccessLevel(int id, Group[] childs, String name, String usernameformat, boolean canBuild, boolean isAdmin) {
        this.id = id;
        this.childs = childs;
        this.name = name;
        this.usernameformat = usernameformat;
        this.canBuild = canBuild;
        this.isAdmin = isAdmin;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    
    public int getLevel() {
        return getId();
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

    public boolean canUseCommand(String command, String world) {
        if(getChilds() == null)
            return false;
        
        for (Group g : getChilds())
            if (g.canUseCommand(command, world))
                return true;

        return false;
    }
}
