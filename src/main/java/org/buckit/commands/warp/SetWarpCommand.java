package org.buckit.commands.warp;

import java.util.Arrays;

import org.buckit.Config;
import org.buckit.datasource.type.WarpsDataSource;
import org.bukkit.ChatColor;
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
        if(args.length < 2){
            sender.sendMessage(ChatColor.RED + "No warp name specified.");
            sender.sendMessage(ChatColor.RED + getUsage());
            return true;
        }
        String group = Config.WARPS_DEFAULT_GROUP_NAME;
        String name = args[1].toLowerCase();
        int accesslevel = Config.DEFAULT_ACCESS_LEVEL;
        if(args.length > 2){
            if(Config.WARPS_GROUPS_ENABLED){
                group = args[2].toLowerCase();
            }else{
                try{
                    accesslevel = Integer.parseInt(args[2]);
                }catch(NumberFormatException e){
                    sender.sendMessage(ChatColor.RED + "Invalid accesslevel.");
                    sender.sendMessage(ChatColor.RED + getUsage());
                    return true;
                }
            }
        }
        if(args.length > 3 && Config.WARPS_GROUPS_ENABLED){
            try{
                accesslevel = Integer.parseInt(args[2]);
            }catch(NumberFormatException e){
                sender.sendMessage(ChatColor.RED + "Invalid accesslevel.");
                sender.sendMessage(ChatColor.RED + getUsage());
                return true;
            }
        }
        
        if(datasource.addWarp(group, name, ((Player)sender).getLocation(), accesslevel)){
            sender.sendMessage(ChatColor.GREEN + "Succesfully added warp '" + args[1] + "' "+(Config.WARPS_GROUPS_ENABLED ?  "in group '" + group + "'" : "" )+"  !");
        } else {
            sender.sendMessage(ChatColor.RED + "Error while setting warp, please report this to Buck - It(with the error message in the console).");
        }
        
        return true;
    }

}
