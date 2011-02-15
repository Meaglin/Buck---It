package org.buckit.commands.admin;

import java.util.Arrays;

import org.buckit.Config;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand extends Command {

    public TimeCommand(String name, Server server) {
        super(name);
        this.tooltip = "Changes the the time.";
        this.usageMessage = "Usage: /time [day|night|check|raw|time to set] <raw time>";
        this.accessname = "buckit.admin.time";
        this.setAliases(Arrays.asList("settime"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        if(args.length == 0) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Insufficient arguments specified.");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
            return true;
        }
        
        World world = ((Player)sender).getWorld();
        
        if(args.length == 1){
            String param = args[0].toLowerCase();
            if(param.equals("day")){
                world.setTime(0);
                sender.sendMessage(Config.DEFAULT_INFO_COLOR + "It is now day!");
            } else if(param.equals("night")) {
                world.setTime(13000);
                sender.sendMessage(Config.DEFAULT_INFO_COLOR + "It is now night!");                
            } else if(param.equals("check")) {
                sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Current time " + world.getTime() + " [RAW:" + world.getFullTime() + "]");
            } else if(param.equals("raw")) {
                sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Raw time:" + world.getFullTime());
            } else {
                long time = 0L;
                try {
                    time = Long.parseLong(param);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid time amount, you don't put any letters did you?");
                    return true;
                }
                world.setTime(time);
                sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Time has been changed to " + world.getTime());
            }
        } else if(args.length == 2 && args[0].equalsIgnoreCase("raw")) {
            long time = 0L;
            try {
                time = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid time amount, you don't put any letters did you?");
                return true;
            }
            world.setFullTime(time);
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Raw time changed to " + world.getFullTime());
        } else {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Too much arguments specified.");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
            return true;
        }
        
        return true;
    }

}
