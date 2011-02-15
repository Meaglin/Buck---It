package org.buckit.commands.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.buckit.Config;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClothCommand extends Command{

    private static Logger log = Logger.getLogger(ClothCommand.class.getName());
    
    public ClothCommand(String name, Server server) {
        super(name);
        this.tooltip = "Gives you wool in a sertain color.";
        this.usageMessage = "Usage: /cloth <color> <amount>";
        this.accessname = "buckit.admin.cloth";
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        Map<String, Integer> toId = new HashMap<String, Integer>();
        toId.put("white",       0);
        toId.put("orange",      1);
        toId.put("magenta",     2);
        toId.put("lightblue",   3);
        toId.put("yellow",      4);
        toId.put("lightgreen",  5);
        toId.put("pink",        6);
        toId.put("gray",        7);
        toId.put("lightgray",   8);
        toId.put("cyan",        9);
        toId.put("purple",      10);
        toId.put("blue",        11);
        toId.put("brown",       12);
        toId.put("darkgreen",   13);
        toId.put("red",         14);
        toId.put("black",       15);

        int color = 0;
        
        if (args.length < 1) {
            color = 0;
        } else {
            try {
                if (toId.containsValue(Integer.parseInt(args[0])))
                    color = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                if (toId.containsKey(args[0]))
                    color = toId.get(args[0]); 
                else {
                    sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Unknown color: " + ChatColor.WHITE + "'" + args[0] + "'");
                    sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
                    return true;
                }
            }
        }
        
        int amount = 1;
        if(args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid item amount '" + args[1] + "'!");
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
                return true;
            }
        }
        
        Player player = ((Player) sender);
        
        ItemStack stack = new ItemStack(35,amount,(short)color);
        player.getInventory().addItem(stack);

        player.sendMessage(Config.DEFAULT_INFO_COLOR + "There you go " + player.getName() + ".");
        log.info(((Player)sender).getName() + " has got himself " + stack.toString() + ".");
        
        return true;
        
    }
}
