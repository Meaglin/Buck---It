package org.buckit.commands;

import org.buckit.Config;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends Command {

	public SpawnCommand(String name, Server server) {
		super(name);
        this.description = "Warps you to spawn.";
        this.usageMessage = "Usage: /spawn";
        this.accessname = "buckit.spawn";
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Location spawn = ((Player) sender).getLocation().getWorld().getSpawnLocation();
		((Player) sender).teleportTo(spawn);
		
		sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Welcome at spawn!");
		
		return true;
	}
}
