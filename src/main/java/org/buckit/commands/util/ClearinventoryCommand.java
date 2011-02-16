package org.buckit.commands.util;

import org.buckit.Config;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearInventoryCommand extends Command {

    public ClearInventoryCommand(String name, Server server) {
        super(name);
        this.tooltip = "Clears your inventory.";
        this.usageMessage = "Usage: /clearinventory";
        this.accessname = "buckit.util.clearinventory";
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        Player player = (Player)sender;
        player.getInventory().clear();
        sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Your inventory has been cleared, enjoy :).");
        
        return true;
    }

}
