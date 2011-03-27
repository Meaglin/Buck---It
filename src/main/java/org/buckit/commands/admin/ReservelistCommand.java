package org.buckit.commands.admin;

import java.util.List;

import org.buckit.Config;
import org.buckit.datasource.type.ReserveListDataSource;
import org.buckit.model.UserDataHolder;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReservelistCommand extends Command {
    
    private final Server server;

    public ReservelistCommand(String name, Server server) {
        super(name);
        this.description = "Add or remove a player from the reservelist.";
        this.usageMessage = "Usage: /reservelist [add/a|remove/r|status/s] [player] ";
        this.accessname = "buckit.admin.reservelist";
        
        this.server = server;
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {

        String playername = args[0].toLowerCase();
        UserDataHolder data = server.getDataSourceManager().getUserDataSource().getUserData(playername);
        
        if (args.length < 2) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Insufficient arguments specified.");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
            return true;
        }
        
        if(data == null) {
            List<Player> matches = server.matchPlayer(args[1]);
            
            if (matches.size() == 0) {
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No players found.");
                return true;
            }
            else if (matches.size() > 1) {
                String names = "";
                for (int i=0; i<matches.size(); i++)
                    names += ", " + matches.get(i).getDisplayName() + Config.DEFAULT_ERROR_COLOR;
                
                names = names.substring(2);
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Multiple players found: "+names);
                return true;
            }
    
            Player player = matches.get(0);
            data = player.getUserDataHolder();
        }
        
        if(data == null) {
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "No player found with name '" + playername + "'.");
            return true;
        }
        
        ReserveListDataSource reservelist = server.getDataSourceManager().getReserveListDataSource();        

        boolean before = reservelist.isReserveListed(data.getId(), data.getUsername());
        
        if (args[0].equals("add") || args[0].equals("a")) {
            reservelist.setReserveListed(data.getId(), data.getUsername(), true);
            sender.sendMessage(data.getUsername() + Config.DEFAULT_INFO_COLOR + " has been added to the reservelist!");
            return true;
        }
        else if (args[0].equals("remove") || args[0].equals("r")) {
            reservelist.setReserveListed(data.getId(), data.getUsername(), false);
            sender.sendMessage(data.getUsername() + Config.DEFAULT_INFO_COLOR + " has been removed to the reservelist!");
            return true;
        }
        else if (args[0].equals("status") || args[0].equals("s")) {
            String status = (before) ? " is on the whitelist." : "is not on the reservelist.";
            sender.sendMessage(data.getUsername() + Config.DEFAULT_INFO_COLOR + status);
            return true;
        }
        else {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Unknown key: '" + ChatColor.WHITE + args[0] + "'");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
            return true;
        }
    }
}
