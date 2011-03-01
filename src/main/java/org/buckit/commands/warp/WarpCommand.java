package org.buckit.commands.warp;


import org.buckit.Config;
import org.buckit.datasource.type.WarpsDataSource;
import org.buckit.model.Warp;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand extends Command{

    private final WarpsDataSource datasource;
    public WarpCommand(String name, Server server){
        super(name);
        this.datasource = server.getDataSource().getWarpsDataSource();
        this.description = "Warps you to the location of the specified warp.";
        
        if(Config.WARPS_GROUPS_ENABLED)
            this.usageMessage = "Usage: /warp [warp name] <group name>";
        else
            this.usageMessage = "Usage: /warp [warp name]";
        
        this.accessname = "buckit.warps.warp";
        
    }
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if(!(sender instanceof Player))
            return false;
        if(args.length < 1){
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No warp name specified.");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
        } else if(args.length == 1) {
            Warp warp = datasource.getWarp(Config.WARPS_DEFAULT_GROUP_NAME, args[0].toLowerCase());
            if(warp == null)
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No warp found with name " + args[0].toLowerCase() + ".");
            else {
                sender.sendMessage(ChatColor.AQUA + "Woosh!");
                ((Player)sender).teleportTo(warp.getLocation());
            }
        } else if(args.length == 2) {
            if(Config.WARPS_GROUPS_ENABLED){
                Warp warp = datasource.getWarp(args[1].toLowerCase(), args[0].toLowerCase());
                if(warp == null)
                    sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No warp found with name '" + args[0].toLowerCase() + "' in group '" + args[1].toLowerCase() + "'.");
                else {
                    sender.sendMessage(ChatColor.AQUA + "Woosh!");
                    ((Player)sender).teleportTo(warp.getLocation());
                    
                }
            } else {
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
            }
        } else {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
        }
        
        return true;
    }

}
