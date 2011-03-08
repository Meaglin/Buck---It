package org.buckit.commands.homes;

import org.buckit.Config;
import org.buckit.datasource.type.HomesDataSource;
import org.buckit.model.Home;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand extends Command {

    private final HomesDataSource datasource;
    public HomeCommand(String name, Server server){
        super(name);
        this.datasource = server.getDataSourceManager().getHomesDataSource();
        this.description = "Teleports you to your home.";
        
        if(Config.HOMES_MULTI_ENABLED)
            this.usageMessage = "Usage: /home <home name>";
        else
            this.usageMessage = "Usage: /home";
        
        this.accessname = "buckit.homes.home";
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if(!(sender instanceof Player))
            return false;
        
        String name = "default";
        
        if(Config.HOMES_MULTI_ENABLED && args.length > 0)
            name = args[0].toLowerCase();
        else if (Config.HOMES_MULTI_ENABLED && args.length==0) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Insufficient arguments specified");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
            return true;
        }
        
        Home home = datasource.getHome(((Player)sender).getPlayerId(), name);
        if(home == null)
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No home found"+(!name.equals("default") ? " with name '" + name + "' " : "" )+".");
        else
            ((Player)sender).teleportTo(home.getLocation());
        
        return false;
    }

}
