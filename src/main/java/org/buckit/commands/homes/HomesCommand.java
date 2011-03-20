package org.buckit.commands.homes;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.buckit.Config;
import org.buckit.datasource.type.HomesDataSource;
import org.buckit.model.Home;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomesCommand extends Command {

    private final HomesDataSource datasource;
    public HomesCommand(String name, Server server){
        super(name);
        this.datasource = server.getDataSourceManager().getHomesDataSource();
        this.description = "Gives a list of your homes.";

        this.usageMessage = "Usage: /homes";
        
        this.accessname = "buckit.homes.homes";
        this.setAliases(Arrays.asList("listhomes"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if(!(sender instanceof Player))
            return false;
        
        if(!Config.HOMES_MULTI_ENABLED)
            return false;
        
        Collection<Home> homes = datasource.getHomes(((Player)sender).getPlayerId());
        
        if(homes.size() == 0) {
            sender.sendMessage("No homes made yet.");
            return true;
        }

        Collection<String> names = new LinkedHashSet<String>();
        for(Home h : homes)
            names.add(h.getName());
        
        String str = "";
        for(String name : names)
            str += name + ", ";
        str = str.substring(0,str.length()-2);
        
        sender.sendMessage(ChatColor.GREEN + "List of available homes:");
        sender.sendMessage(str);
        
        return true;
    }

}
