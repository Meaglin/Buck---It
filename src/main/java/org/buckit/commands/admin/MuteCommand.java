package org.buckit.commands.admin;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.buckit.Config;
import org.buckit.model.UserDataHolder;
import org.buckit.util.TimeFormat;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand extends Command {

    private final Server server;
    private static Logger     log              = Logger.getLogger(MuteCommand.class.getName());
    
    public MuteCommand(String name, Server server) {
        super(name);
        this.tooltip = "Mutes a player.";
        this.usageMessage = "Usage: /mute [player] <weeks|days|hours|minutes|seconds> <time> <reason>";
        this.accessname = "buckit.admin.mute";
        
        this.server = server;
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        if (args.length < 1) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Insufficient arguments specified.");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
            return true;
        }
        String playername = args[0].toLowerCase();
        UserDataHolder data = server.getDataSource().getUserDataSource().getUserData(playername);
        
        if(data == null){
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No player with name '" + playername + "' ever logged into this server.");
            return true;
        }
        
        int time = 0;
        int multiplier = 0;
        if(args.length > 1){
            if(args[1].equalsIgnoreCase("weeks")) multiplier = TimeFormat.WEEK;
            else if(args[1].equalsIgnoreCase("days")) multiplier = TimeFormat.DAY;
            else if(args[1].equalsIgnoreCase("hours")) multiplier = TimeFormat.HOUR;
            else if(args[1].equalsIgnoreCase("minutes")) multiplier = TimeFormat.MINUTE;
            else multiplier = 1;
        }
        if(args.length > 2){
            try{
                time = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid time amount.");
                return true;
            }
        }
        time *= multiplier;
        String reason = "";
        if(args.length > 3){
            for(String part : Arrays.copyOfRange(args, 3, args.length))
                reason += part + " ";
        }
        List<Player> list = server.matchPlayer(playername);
        Player player = null;
        for(Player p : list)
            if(p.getName().equalsIgnoreCase(playername))
                player = p;
        
        if(player != null)data = player.getUserDataHolder();
        
        if(time == 0){
            data.setMuted(true);
            server.getDataSource().getUserDataSource().updateUser(data);
            if(player != null)player.kickPlayer("You have been permanently muted.");
            log.info("Player '" + playername + "' has been permanently muted by " + ((Player)sender).getName() + (!reason.equals("") ? " with reason " + reason : "") +".");
        } else {
            data.setMutetime(time + (int)(System.currentTimeMillis()/1000));
            server.getDataSource().getUserDataSource().updateUserMuteTime(data);
            if(player != null)player.sendMessage(Config.DEFAULT_ERROR_COLOR + "You have been muted for " + TimeFormat.formatRemaining(time) + ".");
            log.info("Player '" + playername + "' has been muted for "  + TimeFormat.formatRemaining(time) + " by " + ((Player)sender).getName() + (!reason.equals("") ? " with reason " + reason : "") +".");
        }
        
        sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Player '" + playername + "' is now muted " + (time != 0 ? "for " + TimeFormat.formatRemaining(time) : "permanently") + ".");
        
        return true;
    }

}
