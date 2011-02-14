package org.buckit.commands.warp;

import java.util.Arrays;

import org.buckit.Config;
import org.buckit.datasource.type.WarpsDataSource;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand extends Command {

    private final WarpsDataSource datasource;
    public SetWarpCommand(String name, Server server){
        super(name);
        this.datasource = server.getDataSource().getWarpsDataSource();
        this.tooltip = "Specifies a new warp or replaces a existing one(if name was already used).";
        
        if(Config.WARPS_GROUPS_ENABLED)
            this.usageMessage = "Usage: /setwarp [warp name] <group name> <min accesslevel>";
        else
            this.usageMessage = "Usage: /setwarp [warp name] <min accesslevel>";
            
        this.accessname = "buckit.admin.setwarp";
        this.setAliases(Arrays.asList("addwarp"));
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
        String group = Config.WARPS_DEFAULT_GROUP_NAME;
        String name = args[0].toLowerCase();
        int accesslevel = Config.DEFAULT_ACCESS_LEVEL;
        if(args.length > 1){
            if(Config.WARPS_GROUPS_ENABLED){
                group = args[1].toLowerCase();
            }else{
                try{
                    accesslevel = Integer.parseInt(args[1]);
                }catch(NumberFormatException e){
                    sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid accesslevel.");
                    sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
                    return true;
                }
            }
        }
        if(args.length > 2 && Config.WARPS_GROUPS_ENABLED){
            try{
                accesslevel = Integer.parseInt(args[2]);
            }catch(NumberFormatException e){
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid accesslevel.");
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
                return true;
            }
        }
        
        if(datasource.addWarp(group, name, ((Player)sender).getLocation(), accesslevel)){
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Succesfully added warp '" + name + "' "+(Config.WARPS_GROUPS_ENABLED ?  "in group '" + group + "'" : "" )+"  !");
        } else {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Error while setting warp, please report this to Buck - It(with the error message in the console).");
        }
        
        return true;
    }

}
