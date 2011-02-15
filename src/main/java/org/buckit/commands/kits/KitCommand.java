package org.buckit.commands.kits;

import java.util.Arrays;

import org.buckit.Config;
import org.buckit.datasource.type.KitsDataSource;
import org.buckit.model.Kit;
import org.buckit.util.TimeFormat;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitCommand extends Command {

    private final KitsDataSource datasource;
    public KitCommand(String name, Server server){
        super(name);
        this.datasource = server.getDataSource().getKitsDataSource();
        this.tooltip = "Gives the specified kit to yourself.";

        this.usageMessage = "Usage: /kit [kit name]";
        
        this.accessname = "buckit.kits.kit";
        this.setAliases(Arrays.asList("getkit"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if(!(sender instanceof Player))
            return false;
        
        if(args.length < 1) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Please specify a kit name.");
            return true;
        }
        String name = args[0].toLowerCase();
        Kit kit = datasource.getKit(name);
        Player player = ((Player)sender);
        if(kit == null || kit.getMinaccesslevel() > player.getAccessLevel().getId()) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No kit found with name '" + name + "' .");
            return true;
        } 
        
        int lastused = (kit.getDelay() != 0 ? datasource.lastUsed(player.getPlayerId(), name) + kit.getDelay() : 0);
        if(lastused > (System.currentTimeMillis()/1000)) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "You cannot use this kit yet, please wait " + TimeFormat.formatRemaining((int) (lastused - (System.currentTimeMillis()/1000))) + " before using this kit again.");
        } else {
            ItemStack[] items = kit.getItems();
            player.getInventory().addItem(items);
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "There you go " + player.getName() + ".");
        }
        return true;
    }
    
    
    
}
