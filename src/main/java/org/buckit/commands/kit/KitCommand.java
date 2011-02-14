package org.buckit.commands.kit;

import java.util.Arrays;

import org.buckit.datasource.type.KitsDataSource;
import org.buckit.model.Kit;
import org.bukkit.ChatColor;
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
        
        if(args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Please specify a kit name.");
            return true;
        }
        String name = args[1].toLowerCase();
        Kit kit = datasource.getKit(name);
        Player player = ((Player)sender);
        if(kit == null || kit.getMinaccesslevel() > player.getAccessLevel().getId()) {
            sender.sendMessage(ChatColor.RED + "No kit found with name '" + name + "' .");
            return true;
        } 
        
        int lastused = (kit.getDelay() != 0 ? datasource.lastUsed(player.getPlayerId(), name) + kit.getDelay() : 0);
        if(lastused > (System.currentTimeMillis()/1000)) {
            sender.sendMessage(ChatColor.RED + "You cannot use this kit yet, please wait " + formatTime((int) (lastused - (System.currentTimeMillis()/1000))) + " before using this kit again.");
        } else {
            ItemStack[] items = kit.getItems();
            player.getInventory().addItem(items);
            sender.sendMessage(ChatColor.GREEN + "There you go " + player.getName() + ".");
        }
        return true;
    }
    
    private static final int week = 60 * 60 * 24 * 7;
    private static final int day = 60 * 60 * 24;
    private static final int hour = 60 * 60;
    private static final int minute = 60;
    private static String formatTime(int duration) {
        int weeks = 0,days = 0,hours = 0,minutes = 0,seconds = 0;
        if(duration >= week){
            weeks = (int) Math.floor(duration / week);
            duration -= weeks * week;
        }
        if(duration >= day){
            days = (int) Math.floor(duration / day);
            duration -= days * day;
        }
        if(duration >= hour){
            hours = (int) Math.floor(duration / hour);
            duration -= hours * hour;
        }
        if(duration >= minute){
            minutes = (int) Math.floor(duration / minute);
            duration -= minutes * minute;
        }
        seconds = duration;
        String rt = "";
        
        if(weeks != 0) rt += (weeks == 1 ? "1 day, " : weeks + " days, ");
        if(hours != 0) rt += (hours == 1 ? "1 hour, " : hours + " hours, ");
        if(minutes != 0) rt += (minutes == 1 ? "1 minute, " : minutes + " minutes, ");
        if(seconds != 0){
            if(rt.length() > 0){
                rt = rt.substring(0, rt.length() - 2 );
                rt += " and ";
            }
            rt += (seconds == 1 ? "1 second" : seconds + " seconds");
        } else if(rt.length() > 0){
            rt = rt.substring(0, rt.length() - 2);
        }
        
        return rt;
    }
}
