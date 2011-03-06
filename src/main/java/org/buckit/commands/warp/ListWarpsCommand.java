package org.buckit.commands.warp;

import java.util.Arrays;
import java.util.Collection;

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
        if(!(sender instanceof Player))
            return false;
        
        String group = Config.WARPS_DEFAULT_GROUP_NAME;
        if(args.length > 0 && Config.WARPS_GROUPS_ENABLED)
            group = args[0].toLowerCase();
        
        int accesslevel = ((Player)sender).getAccessLevel().getId();
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
            
        // TODO: implement this into the interface.
        //}else if(group.equals("groups") && !Config.WARPS_DEFAULT_GROUP_NAME.equals("groups")){
        
        }else{
            Collection<Warp> warps = datasource.getWarps(group);
            String str = "";
            for(Warp w : warps)
                if(accesslevel >= w.getMinAccessLevel())
                    str += w.getName() + ", ";
            
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
