package org.buckit.commands;

import org.buckit.Config;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends Command {

	public SetSpawnCommand(String name, Server server) {
		super(name);
        this.tooltip = "Sets the spawn location.";
        this.usageMessage = "Usage: /setspawn";
        this.accessname = "buckit.setspawn";
	}

	//TODO implement!
	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "This command isn't supported yet!");
		return true;
	}
}