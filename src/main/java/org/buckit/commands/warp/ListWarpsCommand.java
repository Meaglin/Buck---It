package org.buckit.commands.warp;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.buckit.Config;
import org.buckit.datasource.type.WarpsDataSource;
import org.buckit.model.Warp;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListWarpsCommand extends Command {

    private final WarpsDataSource datasource;
    public ListWarpsCommand(String name, Server server){
        super(name);
        this.datasource = server.getDataSourceManager().getWarpsDataSource();
        this.description = "Gives a list of all the warps.";
        
        if(Config.WARPS_GROUPS_ENABLED)
            this.usageMessage = "Usage: /listwarps <group name>";
        else
            this.usageMessage = "Usage: /listwarps";
        
        this.accessname = "buckit.warps.listwarps";
        this.setAliases(Arrays.asList("warplist", "warps"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        
        String group = Config.WARPS_DEFAULT_GROUP_NAME;
        if(args.length > 0 && Config.WARPS_GROUPS_ENABLED)
            group = args[0].toLowerCase();
        
        int accesslevel = Config.DEFAULT_ACCESS_LEVEL;
        if(sender instanceof Player)
            accesslevel = ((Player)sender).getAccessLevel().getId();
        else
            accesslevel = Integer.MAX_VALUE;
        
        //some1 is bound to try this out .....
        if(group.equalsIgnoreCase("all") && !Config.WARPS_DEFAULT_GROUP_NAME.equals("all")){
            Collection<Warp> warps = datasource.getAllWarps();
            String str = "";
            for(Warp w : warps)
                if(accesslevel >= w.getMinAccessLevel())
                    str += "[" + w.getGroup() + "]" + w.getName() + ", ";
            str = str.substring(0, str.length() - 2);
            sender.sendMessage("List of all warps:");
            sender.sendMessage(str);
            
        }else if(group.equals("groups") && !Config.WARPS_DEFAULT_GROUP_NAME.equals("groups")){
            Collection<String> groups = new LinkedHashSet<String>();
            for(Warp warp : datasource.getAllWarps())
                if(!groups.contains(warp.getGroup()) && !warp.getGroup().equals(Config.WARPS_DEFAULT_GROUP_NAME))
                    groups.add(warp.getGroup());
                
            String message = "";
            for(String str : groups) {
                message += str + ", ";
            }
            if(groups.size() < 1) {
                sender.sendMessage("No groups found.");
            } else {
                sender.sendMessage("List of availeble groups:");
                sender.sendMessage(message.substring(0,message.length()-2));
            }
        }else{
            Collection<Warp> warps = datasource.getWarps(group);
            String str = "";
            
            Collection<String> names = new LinkedHashSet<String>();
            for(Warp w : warps)
                if(accesslevel >= w.getMinAccessLevel())
                    names.add(w.getName());
            
            for(String name : names) 
                str += name + ", ";
                    
            if(str.length() == 0)
                str = "No warps are available.";
            else
                str = str.substring(0, str.length() - 2);
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "List of available warps " + (!Config.WARPS_DEFAULT_GROUP_NAME.equals(group) ? " in group " + group : "" ) + ":");
            sender.sendMessage(str);
        }
        return true;
    }

}
