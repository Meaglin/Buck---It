package org.buckit.commands.warp;

import java.util.Arrays;

import org.buckit.Config;
import org.buckit.datasource.type.WarpsDataSource;
import org.buckit.model.Warp;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveWarpCommand extends Command {

    private final WarpsDataSource datasource;
    public RemoveWarpCommand(String name, Server server){
        super(name);
        this.datasource = server.getDataSourceManager().getWarpsDataSource();
        this.description = "Removes an existing warp.";
        
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
        
        if(args.length < 1){
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No warp name specified.");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
            return true;
        }
        
        String name = args[0].toLowerCase();
        String group = Config.WARPS_DEFAULT_GROUP_NAME;
        
        if(args.length > 1){
            group = args[1].toLowerCase();
        }
        
        Warp warp = datasource.getWarp(group, name);
        if(warp == null){
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No warp found with name '" + name + "' " + (Config.WARPS_GROUPS_ENABLED ? "in group '" + group + "'" : "") + " !");
            return true;
        }
        
        if(datasource.removeWarp(warp)){
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Succefully removed warp '" + name + "' " + (Config.WARPS_GROUPS_ENABLED ? "from group '" + group + "'" : "") + " !");
        } else {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Unable to remove warp, please report the error in the console to Buck - It!");
        }
        
        return true;
    }

}
