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
        this.tooltip = "Warps you to the location of the specified warp.";
        
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
        if(args.length < 2){
            sender.sendMessage(ChatColor.RED + "No warp name specified.");
            sender.sendMessage(ChatColor.RED + getUsage());
        } else if(args.length == 2) {
            Warp warp = datasource.getWarp(Config.WARPS_DEFAULT_GROUP_NAME, args[1].toLowerCase());
            if(warp == null)
                sender.sendMessage(ChatColor.RED + "No warp found with name " + args[1].toLowerCase() + ".");
            else {
                sender.sendMessage(ChatColor.AQUA + "Woosh!");
                ((Player)sender).teleportTo(warp.getLocation());
            }
        } else if(args.length == 3) {
            if(Config.WARPS_GROUPS_ENABLED){
                Warp warp = datasource.getWarp(args[2].toLowerCase(), args[1].toLowerCase());
                if(warp == null)
                    sender.sendMessage(ChatColor.RED + "No warp found with name '" + args[1].toLowerCase() + "' in group '" + args[2].toLowerCase() + "'.");
                else {
                    sender.sendMessage(ChatColor.AQUA + "Woosh!");
                    ((Player)sender).teleportTo(warp.getLocation());
                }
            } else {
                sender.sendMessage(ChatColor.RED + getUsage());
            }
        } else {
            sender.sendMessage(ChatColor.RED + getUsage());
        }
        
        return true;
    }

}
