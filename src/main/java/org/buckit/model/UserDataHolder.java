package org.buckit.model;

import java.util.ArrayList;

import org.buckit.Config;
import org.buckit.access.AccessLevel;

public class UserDataHolder {

    private int         id, firstlogin, lastlogin, uptime, bantime, mutetime;
    private String      username, usernameformat, commands;
    private boolean     admin, canbuild;
    private AccessLevel accesslevel;
    private ArrayList<String> commandArray;
    private boolean canuseall = false;
    
    public UserDataHolder(int id, String username, String usernameformat, boolean admin, boolean canbuild, String commands, int firstlogin, int lastlogin, int uptime, int bantime, int mutetime, AccessLevel accesslevel) {
        this.id = id;
        this.username = username;
        this.usernameformat = usernameformat;
        this.admin = admin;
        this.canbuild = canbuild;
        this.commands = commands;
        this.firstlogin = firstlogin;
        this.lastlogin = lastlogin;
        this.uptime = uptime;
        this.bantime = bantime;
        this.mutetime = mutetime;
        this.accesslevel = accesslevel;
        
        this.commandArray = new ArrayList<String>();
        for (String command : commands.split(Config.DATABASE_DELIMITER))
            this.commandArray.add(command);
        
        canuseall = commandArray.contains(Config.FULL_ACCESS_STRING);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the firstlogin
     */
    public int getFirstlogin() {
        return firstlogin;
    }

    /**
     * @return the lastlogin
     */
    public int getLastlogin() {
        return lastlogin;
    }

    /**
     * @return the uptime
     */
    public int getUptime() {
        return uptime;
    }

    /**
     * @return the bantime
     */
    public int getBantime() {
        return bantime;
    }

    /**
     * @return the mutetime
     */
    public int getMutetime() {
        return mutetime;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the usernameformat
     */
    public String getUsernameformat() {
        return usernameformat;
    }

    /**
     * @return the commands
     */
    public String getCommands() {
        return commands;
    }

    /**
     * @return the admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * @return the canbuild
     */
    public boolean canbuild() {
        return canbuild;
    }

    /**
     * @return the accesslevel
     */
    public AccessLevel getAccessLevel() {
        return accesslevel;
    }

    /**
     * @return the muted
     */
    public boolean isMuted() {
        if((System.currentTimeMillis()/1000) < getMutetime())
            return true;
        
        return getMutetime() == -1;
    }

    public boolean isBanned() {
        if((System.currentTimeMillis()/1000) < getBantime())
            return true;
        
        return (getBantime() == -1);
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param firstlogin
     *            the firstlogin to set
     */
    public void setFirstlogin(int firstlogin) {
        this.firstlogin = firstlogin;
    }

    /**
     * @param lastlogin
     *            the lastlogin to set
     */
    public void setLastlogin(int lastlogin) {
        this.lastlogin = lastlogin;
    }

    /**
     * @param uptime
     *            the uptime to set
     */
    public void setUptime(int uptime) {
        this.uptime = uptime;
    }

    /**
     * @param bantime
     *            the bantime to set
     */
    public void setBantime(int bantime) {
        this.bantime = bantime;
    }

    /**
     * @param mutetime
     *            the mutetime to set
     */
    public void setMutetime(int mutetime) {
        this.mutetime = mutetime;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param usernameformat
     *            the usernameformat to set
     */
    public void setUsernameformat(String usernameformat) {
        this.usernameformat = usernameformat;
    }

    /**
     * @param commands
     *            the commands to set
     */
    public void setCommands(String commands) {
        this.commands = commands;
        
        this.commandArray = new ArrayList<String>();
        for (String command : commands.split(Config.DATABASE_DELIMITER))
            this.commandArray.add(command);
        
        canuseall = commandArray.contains(Config.FULL_ACCESS_STRING);
    }

    /**
     * @param admin
     *            the admin to set
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * @param canbuild
     *            the canbuild to set
     */
    public void setCanbuild(boolean canbuild) {
        this.canbuild = canbuild;
    }

    /**
     * @param accesslevel
     *            the accesslevel to set
     */
    public void setAccesslevel(AccessLevel accesslevel) {
        this.accesslevel = accesslevel;
    }

    
    public boolean canUseCommand(String command) {
        return canuseall || commandArray.contains(command) || getAccessLevel().canUseCommand(command);
    }
}
