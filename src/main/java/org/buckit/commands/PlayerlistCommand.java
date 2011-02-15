package org.buckit.commands;

import java.util.Arrays;

import org.buckit.Config;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerlistCommand extends Command {
	
	private final Server server;

	public PlayerlistCommand(String name, Server server) {
		super(name);
        this.tooltip = "Display al the online players.";
        this.usageMessage = "Usage: /playerlist";
        this.accessname = "buckit.playerlist";
        
        this.setAliases(Arrays.asList("who"));
        
		this.server = server;
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player[] players = server.getOnlinePlayers();
		
		sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Player list ("+players.length+"/"+server.getMaxPlayers()+"):");
		
		String out = " ";
		for (int i=0; i<players.length; i++) {
			int mod = i%5;

			out += players[i].getDisplayName()+", ";
			
			if (mod==4) {
				sender.sendMessage(out);
				out = " ";
			}
		}
		
		if (!out.equals(""))
			sender.sendMessage(out);
		
		return true;
	}
}
