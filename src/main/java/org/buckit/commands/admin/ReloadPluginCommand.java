package org.buckit.commands.admin;

import java.io.File;
import java.util.Arrays;

import org.buckit.Config;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginManager;

public class ReloadPluginCommand extends Command {

    private final Server server;

    public ReloadPluginCommand(String name, Server server) {
        super(name);
        this.tooltip = "Reloads specified plugin.";
        this.usageMessage = "Usage: /reloadplugin [plugin name]";
        this.accessname = "buckit.admin.reloadplugin";
        this.setAliases(Arrays.asList("enableplugin"));
        this.server = server;
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        
        if(args.length == 0) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Insufficient arguments specified.");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
            return true;
        }
        
        String name = args[0].toLowerCase();
        PluginManager m = server.getPluginManager();
        if(m.isPluginEnabled(name)){
            m.disablePlugin(m.getPlugin(name));
        }
        try {
            m.loadPlugin(new File(name+".jar"));
        } catch (InvalidPluginException e) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + e.getMessage());
            e.printStackTrace();
            return true;
        } catch (InvalidDescriptionException e) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + e.getMessage());
            e.printStackTrace();
            return true;
        }
        sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Plugin " + (currentAlias.equalsIgnoreCase("enableplugin") ? "enabled" : "reloaded") + ".");
        return true;
    }

}
