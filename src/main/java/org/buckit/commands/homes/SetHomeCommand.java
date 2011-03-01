package org.buckit.commands.homes;

import java.util.Arrays;

import org.buckit.Config;
import org.buckit.datasource.type.HomesDataSource;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand extends Command {

    private final HomesDataSource datasource;
    public SetHomeCommand(String name, Server server){
        super(name);
        this.datasource = server.getDataSource().getHomesDataSource();
        this.description = "Sets your home.";
        
        if(Config.HOMES_MULTI_ENABLED)
            this.usageMessage = "Usage: /sethome <home name>";
        else
            this.usageMessage = "Usage: /sethome";
        
        this.accessname = "buckit.homes.sethome";
        this.setAliases(Arrays.asList("addhome"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if(!(sender instanceof Player))
            return false;
        
        String name = "default";
        
        if(Config.HOMES_MULTI_ENABLED && args.length > 0)
            name = args[0].toLowerCase();
        
        Player player = (Player)sender;
        
        if(datasource.setHome(player.getPlayerId(), player.getName(), name, player.getLocation()))
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Succesfully set home"+(!name.equals("default") ? " with name '" + name + "' " : "" )+".");
        else
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Error while setting home, please report this to Buck - It!");
        
        return true;
    }

}
