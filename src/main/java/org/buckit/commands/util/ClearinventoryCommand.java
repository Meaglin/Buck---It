package org.buckit.commands.util;

import java.util.List;

import org.buckit.Config;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearInventoryCommand extends Command {

    private final Server server;
    public ClearInventoryCommand(String name, Server server) {
        super(name);
        this.tooltip = "Clears your inventory.";
        this.usageMessage = "Usage: /clearinventory";
        this.accessname = "buckit.util.clearinventory";
        this.server = server;
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        Player player = (Player)sender;
        if(player.isAdmin() && args.length == 1){
            String name = args[0].toLowerCase();
            List<Player> list = server.matchPlayer(name);
            if(list.size() == 0) {
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No player found.");
            } else if(list.size() == 1) {
                list.get(0).getInventory().clear();
                sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Cleared inventory of player "+list.get(0).getName()+".");
                list.get(0).sendMessage(Config.DEFAULT_INFO_COLOR + "Your inventory has been cleared by an admin.");
            } else {
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Too much players found:");
                String str = "";
                for(Player p : list) str += p.getName() +", ";
                if(str.length() > 2) str = str.substring(0,str.length()-2);
                sender.sendMessage(str);
            }
        } else {
            player.getInventory().clear();
        }
        
        return true;
    }

}
