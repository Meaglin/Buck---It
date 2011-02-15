package org.buckit.commands.admin;

import org.buckit.Config;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends Command {

	public SetSpawnCommand(String name, Server server) {
		super(name);
        this.tooltip = "Sets the spawn location.";
        this.usageMessage = "Usage: /setspawn";
        this.accessname = "buckit.admin.setspawn";
	}

	//TODO implement!
	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player)sender;
		CraftWorld world = (CraftWorld)player.getWorld();
		world.getHandle().spawnX = player.getLocation().getBlockX();
		world.getHandle().spawnY = player.getLocation().getBlockY();
		world.getHandle().spawnZ = player.getLocation().getBlockZ();
		//sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "This command isn't supported yet!");
		sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Warning: spawn location gets reverted after a server restart.");
		sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Spawn changed to your location.");
		return true;
	}
}