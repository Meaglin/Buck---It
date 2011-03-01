package org.buckit.commands;

import java.util.List;

import org.buckit.util.MotdReader;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MotdCommand extends Command {

    public MotdCommand(String name, Server server) {
        super(name);
        this.description = "Shows the Message of the Day.";
        this.usageMessage = "Usage: /motd";
        this.accessname = "buckit.motd";
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        List<String> lines = MotdReader.getMotd();
        for (String line : lines) {
            sender.sendMessage(line);
        }
        
        return false;
    }

}
