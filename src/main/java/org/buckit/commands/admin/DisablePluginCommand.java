package org.buckit.commands.admin;

import org.buckit.Config;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class DisablePluginCommand extends Command {

    private final Server server;

    public DisablePluginCommand(String name, Server server) {
        super(name);
        this.description = "Disables specified plugin.";
        this.usageMessage = "Usage: /disableplugin [plugin name]";
        this.accessname = "buckit.admin.disableplugin";
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
        Plugin plugin = m.getPlugin(name);
        if(plugin == null){
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No active plugin found with name " + name + ".");
            return true;
        }
        if(!plugin.isEnabled()){
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "This plugin is already disabled.");
            return true;
        }
        
        m.disablePlugin(plugin);
        sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Plugin disabled.");
        
        return true;
    }

}
