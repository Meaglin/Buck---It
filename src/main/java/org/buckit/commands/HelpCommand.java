package org.buckit.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.buckit.Config;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends Command {
    
    private final Server server;

    public HelpCommand(String name, Server server) {
        super(name);
        this.description = "Gives information about the available commands.";
        this.usageMessage = "Usage: /help <page> or /help <command>";
        this.accessname = "buckit.help";
        
        this.setAliases(Arrays.asList("h"));
        
        this.server = server;
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        int page = 0;
        String commandIn = "";
        Map<String, Command> commandMapTemp = server.getCommands();
        String[] keysTemp = (String[]) commandMapTemp.keySet().toArray();
        
        for (String k : keysTemp) {
            if (commandMapTemp.get(k).getAccessName()==null || !((Player) sender).canUseCommand(commandMapTemp.get(k).getAccessName()))
                commandMapTemp.remove(k);
        }
        
        Map<String, Command> commandMap = commandMapTemp;
        String[] keys = (String[]) commandMap.keySet().toArray();
        
        if (args.length > 0)
            page = 1;
        else {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                commandIn = (args[0].startsWith("/")) ? args[0].substring(1) : args[0];
            }
        }
                
        if (page == 0) {
            Command commandOut = null;
            
            if (commandMap.containsKey(commandIn)) {
                commandOut = commandMap.get(commandIn);
            } else {
                Collection<Command> commandColl = commandMap.values();
                for (Command c : commandColl) {
                    List<String> aliases = c.getAliases();
                    if (aliases.contains(commandIn)) {
                        commandOut = c;
                        break;
                    }
                }
                
                if (commandOut == null) {
                    sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Unknown command: " + ChatColor.WHITE + "'" + commandIn + "'");
                    return true;
                }
            }
            
            String aliasesString = "";
            List<String> aliases = commandOut.getAliases();
            if (aliases.size() != 0) {
                for (String a : aliases) {
                    aliasesString += ", " + a;
                }
                aliasesString = aliasesString.substring(2);
            } else {
                aliasesString = "none";
            }
            
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Info about command /" + commandOut.getName());
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "- Aliases: " + aliasesString);
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "- Tooltip: " + commandOut.getTooltip());
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "- Usage:   " + commandOut.getUsage());
            return true;
            
        } else {
            int commandTotal = commandMap.size();
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Page " + page + " of " + (Math.ceil(commandTotal/7)) + ":");
            int begin   = 7*(page-1);
            int end     = (commandTotal < 7*page) ? commandTotal : 7*page;
            for (int i=begin; i<end; i++) {
                Command commandOutTemp = commandMap.get(keys[i]);
                sender.sendMessage(Config.DEFAULT_INFO_COLOR + "- /" + commandIn + ": " + commandOutTemp.getTooltip());
            }
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "For more info about commands, type /help <command>");
            return true;
        }
    }
}