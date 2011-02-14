package org.buckit.commands.rights;

import java.util.logging.Logger;

import org.buckit.Config;
import org.buckit.model.UserDataHolder;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnbanCommand extends Command {

    private final Server server;
    private static Logger     log              = Logger.getLogger(BanCommand.class.getName());
    
    public UnbanCommand(String name, Server server) {
        super(name);
        this.tooltip = "Unban a player.";
        this.usageMessage = "Usage: /unban [player]";
        this.accessname = "buckit.admin.unban";
        
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
        UserDataHolder data = server.getDataSource().getUserDataSource().getUserData(playername);
        
        if(data == null){
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No player with name '" + playername + "' ever logged into this server.");
            return true;
        }
        
        data.setBantime(0);
        data.setAccesslevel(server.getDataSource().getAccessDataSource().getAccessLevel(Config.DEFAULT_ACCESS_LEVEL));
        server.getDataSource().getUserDataSource().updateUser(data);
        
        sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Player '" + playername + "' is now unbanned.");
        log.info("Player '" + playername + "' has been unbanned by " + ((Player)sender).getName() + ".");
        
        return true;
    }

}
