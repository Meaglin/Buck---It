package org.buckit.commands.rights;

import java.util.List;

import org.buckit.Config;
import org.buckit.datasource.type.UserDataSource;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends Command {
	
	private final Server server;

	public KickCommand(String name, Server server) {
		super(name);
        this.tooltip = "Modifies a player's properties.";
        this.usageMessage = "Usage: /kick <player> <reason";
        this.accessname = "buckit.admin.kick";
        
		this.server = server;
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		if (args.length < 2) {
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Insufficient arguments specified");
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
			return true;
		}
		
		List<Player> receivers = server.matchPlayer(args[0]);
	
		if (receivers.size() == 0) {
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No players found to kick");
			return true;
		}
		else if (receivers.size() == 1) {
			
			receivers.get(0).kickPlayer(args[1]);
			
			return true;
		}
		else {
			String names = "";
			for (int i=0; i<receivers.size(); i++)
				names += ", " + receivers.get(i).getDisplayName() + Config.DEFAULT_ERROR_COLOR;
			
			names = names.substring(2);
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Multiple players found to kick: "+names);
			return true;
		}
	}

}
