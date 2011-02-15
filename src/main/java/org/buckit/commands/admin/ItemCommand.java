package org.buckit.commands.admin;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.buckit.Config;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand extends Command {

    private final Server server;
    private static Logger     log              = Logger.getLogger(ItemCommand.class.getName());
    
    public ItemCommand(String name, Server server){
        super(name);
        this.tooltip = "Gives you a certain item.";
        this.server = server;
        this.usageMessage = "Usage: /item [item id] <amount> <damage> <player to give to>";
        
        this.accessname = "buckit.admin.item";
        this.setAliases(Arrays.asList("i", "give"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if(!(sender instanceof Player))
            return false;
        
        if(args.length == 0) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Insufficient arguments specified.");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
            return true;
        }
        
        String item = args[0].toLowerCase();
        Material m = Material.matchMaterial(item);
        
        if(m == null) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid item id or name given.");
            return true;
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
        
        int damage = 0;
        if(args.length > 2) {
            try {
                damage = Integer.parseInt(args[2]);
            } catch (NumberFormatException e){
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid damage amount '" + args[2] + "' !");
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
                return true;
            }
        }
        
        Player player = (Player)sender;
        if(args.length > 3) {
            List<Player> list = server.matchPlayer(args[3]);
            if(list.size() == 1)
                player = list.get(0);
        }
        
        ItemStack stack = new ItemStack(m,amount,(short)damage);
        player.getInventory().addItem(stack);
        if(!player.equals(((Player)sender))){
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Given " + player.getName() + " " + amount + " " + m.name() + ".");
        }
        player.sendMessage(Config.DEFAULT_INFO_COLOR + "There you go " + player.getName() + ".");
        log.info(((Player)sender).getName() + " has given " + player.getName() + " " + stack.toString() + ".");
        
        return true;
    }

}
