package org.buckit.commands.admin;

import org.buckit.Config;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

public class SpawnMobCommand extends Command {

    
    public SpawnMobCommand(String name, Server server) {
        super(name);
        this.description = "Spawns a mob on your location.";
        this.usageMessage = "Usage: /spawnmob [mob name] <count>";
        this.accessname = "buckit.admin.spawnmob";
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        if (args.length == 0) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Insufficient arguments specified.");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
            return true;
        }
        
        Player player = (Player)sender;
        
        String mobname = args[0].toLowerCase();
        CreatureType type = CreatureType.fromName(mobname);
        
        if(type == null) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid mob name.");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
            return true;
        }
        
        int count = 1;
        if(args.length > 1) {
            try {
                count = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid mob count.");
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
                return true;
            }
        }
        for(int i = 0; i < count;i++){
            player.getWorld().spawnCreature(player.getLocation(), type);
        }
        
        return true;
    }
}
