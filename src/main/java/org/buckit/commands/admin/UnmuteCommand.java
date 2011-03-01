package org.buckit.commands.admin;

import java.util.List;
import java.util.logging.Logger;

import org.buckit.Config;
import org.buckit.model.UserDataHolder;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand extends Command {

    private final Server server;
    private static Logger     log              = Logger.getLogger(BanCommand.class.getName());
    
    public UnmuteCommand(String name, Server server) {
        super(name);
        this.description = "Unmute a player.";
        this.usageMessage = "Usage: /unmute [player]";
        this.accessname = "buckit.admin.unmute";
        
        this.server = server;
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        if (args.length < 1) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Insufficient arguments specified.");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
            return true;
        }
        
        String playername = args[0];
        List<Player> list = server.matchPlayer(playername);
        Player player = null;
        for(Player p : list)
            if(p.getName().equalsIgnoreCase(playername))
                player = p;
        UserDataHolder data;
        if(player != null)
            data = player.getUserDataHolder();
        else
            data = server.getDataSource().getUserDataSource().getUserData(playername);
        
        if(data == null){
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No player with name '" + playername + "' ever logged into this server.");
            return true;
        }
        
        data.setMutetime(0);
        server.getDataSource().getUserDataSource().updateUserMuteTime(data);
        
        sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Player '" + playername + "' is now unmuted.");
        if(player != null)player.sendMessage(Config.DEFAULT_INFO_COLOR + "You have been unmuted.");
        log.info("Player '" + playername + "' has been unmuted by " + ((Player)sender).getName() + ".");
        
        return true;
    }

}
