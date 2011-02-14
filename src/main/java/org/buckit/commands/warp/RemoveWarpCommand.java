package org.buckit.commands.warp;

import java.util.Arrays;

import org.buckit.Config;
import org.buckit.datasource.type.WarpsDataSource;
import org.buckit.model.Warp;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveWarpCommand extends Command {

    private final WarpsDataSource datasource;
    public RemoveWarpCommand(String name, Server server){
        super(name);
        this.datasource = server.getDataSource().getWarpsDataSource();
        this.tooltip = "Removes an existing warp.";
        
        if(Config.WARPS_GROUPS_ENABLED)
            this.usageMessage = "Usage: /removewarp [warp name] <group name>";
        else
            this.usageMessage = "Usage: /removewarp [warp name]";
            
        this.accessname = "buckit.admin.removewarp";
        this.setAliases(Arrays.asList("delwarp"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if(!(sender instanceof Player))
            return false;
        
        if(args.length < 2){
            sender.sendMessage(ChatColor.RED + "No warp name specified.");
            sender.sendMessage(ChatColor.RED + getUsage());
            return true;
        }
        
        String name = args[1].toLowerCase();
        String group = Config.WARPS_DEFAULT_GROUP_NAME;
        
        if(args.length > 2){
            group = args[2].toLowerCase();
        }
        
        Warp warp = datasource.getWarp(group, name);
        if(warp == null){
            sender.sendMessage(ChatColor.RED + "No warp found with name '" + name + "' " + (Config.WARPS_GROUPS_ENABLED ? "in group '" + group + "'" : "") + " !");
            return true;
        }
        
        if(datasource.removeWarp(warp)){
            sender.sendMessage(ChatColor.GREEN + "Succefully removed warp '" + name + "' " + (Config.WARPS_GROUPS_ENABLED ? "from group '" + group + "'" : "") + " !");
        } else {
            sender.sendMessage(ChatColor.RED + "Unable to remove warp, please report the error in the console to Buck - It!");
        }
        
        return true;
    }

}
