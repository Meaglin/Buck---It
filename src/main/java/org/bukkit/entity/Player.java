
package org.bukkit.entity;

import java.net.InetSocketAddress;

import org.buckit.access.AccessLevel;
import org.buckit.model.UserDataHolder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

/**
 * Represents a player, connected or not
 *
 */
public interface Player extends HumanEntity, CommandSender {
    /**
     * Checks if this player is currently online
     *
     * @return true if they are online
     */
    public boolean isOnline();

    /**
     * Gets the "friendly" name to display of this player. This may include color.
     *
     * Note that this name will not be displayed in game, only in chat and places
     * defined by plugins
     *
     * @return String containing a color formatted name to display for this player
     */
    public String getDisplayName();

    /**
     * Sets the "friendly" name to display of this player. This may include color.
     *
     * Note that this name will not be displayed in game, only in chat and places
     * defined by plugins
     *
     * @return String containing a color formatted name to display for this player
     */
    public void setDisplayName(String name);

    /**
     * Set the target of the player's compass.
     *
     * @param loc
     */
    public void setCompassTarget(Location loc);

    /**
     * Gets the socket address of this player
     * @return the player's address
     */
    public InetSocketAddress getAddress();

    /**
     * Kicks player with custom kick message.
     *
     * @return
     */
    public void kickPlayer(String message);

    /**
     * Makes the player perform the given command
     *
     * @param command Command to perform
     * @return true if the command was successful, otherwise false
     */
    public boolean performCommand(String command);

    /**
     * Returns if the player is in sneak mode
     * @return true if player is in sneak mode
     */
    public boolean isSneaking();

    /**
     * Sets the sneak mode the player
     * @param sneak true if player should appear sneaking
     */
    public void setSneaking(boolean sneak);

    /**
     * Forces an update of the player's entire inventory.
     *
     * @return
     *
     * @deprecated This method should not be relied upon as it is a temporary work-around for a larger, more complicated issue.
     */
    public void updateInventory();
    
    
    /**
     * Buck - It function.
     * 
     * @param command
     * @return true if the user can use the command
     */
    public boolean canUseCommand(String command);
    
    /**
     * Buck - It function.
     * 
     * @return true if the user is admin
     */
    public boolean isAdmin();

    /**
     * Buck - It function.
     * 
     * @return true if the user is allowed to build.
     */
    public boolean canBuild();
    
    /**
     * Buck - It function.
     * 
     * @return players accesslevel
     */
    public AccessLevel getAccessLevel();
    
    /**
     * Buck - It function.
     * 
     * @return player id
     */
    public int getPlayerId();
    
    /**
     * Buck - It function.
     * 
     * @return userdataholder
     */
    public UserDataHolder getUserDataHolder();
}
