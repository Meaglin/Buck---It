package org.buckit.commands.admin;

import java.util.List;
import java.util.logging.Logger;

import org.buckit.Config;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends Command {
	
	private final Server server;

	public KickCommand(String name, Server server) {
		super(name);
        this.description = "Kick a player.";
        this.usageMessage = "Usage: /kick <player> <reason>";
        this.accessname = "buckit.admin.kick";
        
		this.server = server;
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		if (args.length < 2) {
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Insufficient arguments specified:");
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
			return true;
		}
		
		List<Player> receivers = server.matchPlayer(args[0]);
	
		if (receivers.size() == 0) {
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No players found to kick.");
			return true;
		}
		else if (receivers.size() == 1) {
			
			String out = "";
			for (int i=1; i<args.length; i++) {
				out += args[i]+" ";
			}
			
			receivers.get(0).kickPlayer(out);
			
			Logger log = Logger.getLogger(KickCommand.class.getName());
			log.info("Player " + receivers.get(0).getDisplayName() + " has been kicked by " + ((Player) sender).getDisplayName() + " with reason: " + out);	
			
			return true;
		}
		else {
			String names = "";
			for (int i=0; i<receivers.size(); i++)
				names += ", " + receivers.get(i).getDisplayName() + Config.DEFAULT_ERROR_COLOR;
			
			names = names.substring(2);
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Multiple players found to kick: "+names+".");
			return true;
		}
	}

}