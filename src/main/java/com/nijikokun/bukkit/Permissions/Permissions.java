package com.nijikokun.bukkit.Permissions;

import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.util.config.Configuration;

import com.nijiko.Misc;
import com.nijiko.permissions.PermissionHandler;

/**
 * Permissions 2.x
 * Copyright (C) 2011  Matt 'The Yeti' Burnett <admin@theyeticave.net>
 * Original Credit & Copyright (C) 2010 Nijikokun <nijikokun@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Permissions Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Permissions Public License for more details.
 *
 * You should have received a copy of the GNU Permissions Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class Permissions implements Plugin {

    public static Logger log = Logger.getLogger("Minecraft");
    public static Plugin instance;
    public static Server Server = null;
    public File directory;
    public static String name = "Permissions";
    public static String version = "1.0";
    public static String codename = "Buck - It Emu";
    public static PluginDescriptionFile description = new PluginDescriptionFile(name,version,"com.nijikokun.bukkit.Permissions.Permissions");
    
    private boolean enabled = false;

    /**
     * Controller for permissions and security.
     */
    public static PermissionHandler Security;

    /**
     * Miscellaneous object for various functions that don't belong anywhere else
     */
    public static Misc Misc = new Misc();

    public Permissions(Server server) {
        Server = server;
        log.info("[Permissions] (" + codename + ") was Initialized.");
    }
    
    public void onLoad() {
    }

    public void onDisable() {
    	enabled = false;
    }

    /**
     * Alternative method of grabbing Permissions.Security
     * <br /><br />
     * <blockquote><pre>
     * Permissions.getHandler()
     * </pre></blockquote>
     *
     * @return PermissionHandler
     */
    public PermissionHandler getHandler() {
        return Permissions.Security;
    }

    public void setupPermissions() {
        
    }

    @Override
    public void onEnable() {
    	instance = this;
    	enabled = true;
    	Security = new PermissionsEmu(Server);
    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public File getDataFolder() {
        return null;
    }

    @Override
    public PluginDescriptionFile getDescription() {
        return description;
    }

    @Override
    public PluginLoader getPluginLoader() {
        return null;
    }

    @Override
    public Server getServer() {
        return Server;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
