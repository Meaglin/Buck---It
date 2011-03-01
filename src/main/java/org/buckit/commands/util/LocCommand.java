package org.buckit.commands.util;

import java.util.Arrays;

import org.buckit.Config;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LocCommand extends Command {

    
    public LocCommand(String name, Server server){
        super(name);
        this.description = "Sends you your current location.";
        this.usageMessage = "Usage: /loc";
        
        this.accessname = "buckit.util.loc";
        this.setAliases(Arrays.asList("getpos"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if(!(sender instanceof Player))
            return false;
        
        Player player = (Player)sender;
        player.sendMessage(Config.DEFAULT_INFO_COLOR + "Pos X: " + player.getLocation().getX() + " Y: " + player.getLocation().getY() + " Z: " + player.getLocation().getZ());
        player.sendMessage(Config.DEFAULT_INFO_COLOR + "Rotation: " + player.getLocation().getYaw() + " Pitch: " + player.getLocation().getPitch());
        
        return true;
    }

}
