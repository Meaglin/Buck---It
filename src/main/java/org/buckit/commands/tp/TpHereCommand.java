package org.buckit.commands.tp;

import java.util.ArrayList;
import java.util.List;

import org.buckit.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpHereCommand extends Command {

	Server server;
	
	public TpHereCommand(String name, Server server) {
		super(name);
        this.tooltip = "Teleports another online player to you.";
        this.usageMessage = "Usage: /tphere <playername>";
        this.accessname = "buckit.tp.tphere";
        
        this.server = server;
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        List<Player> receivers;
		
		if (!(sender instanceof Player))
            return false;
		
		if (args.length < 1) {
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Insufficient arguments specified");
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
			return true;
		} else {
			receivers = server.matchPlayer(args[0]);
		}
		
		if (receivers.size() == 0) {
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No players found to teleport");
			return true;
		}
		else if (receivers.size() == 1) {
			Player receiver = receivers.get(0);
			Location target = this.getLandingLocation(((Player) sender).getLocation());
			
			if (((Player) sender).equals(receiver.getPlayerId())) {
				sender.sendMessage(Config.DEFAULT_INFO_COLOR + "You're already here!");
			}
			else {
				((Player) receiver).teleportTo(target);
				
				receiver.sendMessage(Config.DEFAULT_INFO_COLOR + "Whoosh, you teleported to " + ((Player) sender).getDisplayName() + Config.DEFAULT_INFO_COLOR + ", because he wanted to.");
				sender.sendMessage(receiver.getDisplayName() + Config.DEFAULT_INFO_COLOR + " teleported to you.");
			}
			return true;
		}
		else {
			String names = "";
			for (int i=0; i<receivers.size(); i++)
				names += ", "+receivers.get(i).getDisplayName();
			
			names = names.substring(2);
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Multiple players found: "+names);
			return true;
		}
	}
	
	private Location getLandingLocation(Location target) {
		Location landing = target;
		for (int dX=-1; dX<=1; dX++) {
			for (int dZ=-1; dZ<=1; dZ++) {
				if (!(dZ==0 && dX==0)) {
					Location check = new Location(target.getWorld(), target.getX()+dX, target.getY(), target.getZ()+dZ, target.getYaw(), target.getPitch());
					
					int checkBlok1 = target.getWorld().getBlockTypeIdAt(target.getBlockX()+dX, target.getBlockY(), target.getBlockZ()+dZ);
					int checkBlok2 = target.getWorld().getBlockTypeIdAt(target.getBlockX()+dX, target.getBlockY()+1, target.getBlockZ()+dZ);
					
					List<Integer> validBlocks = new ArrayList<Integer>();
					validBlocks.add(Material.AIR.getId());
					validBlocks.add(Material.SAPLING.getId());
					validBlocks.add(Material.WATER.getId());
					validBlocks.add(Material.STATIONARY_WATER.getId());
					validBlocks.add(Material.YELLOW_FLOWER.getId());
					validBlocks.add(Material.RED_ROSE.getId());
					validBlocks.add(Material.BROWN_MUSHROOM.getId());
					validBlocks.add(Material.RED_MUSHROOM.getId());
					validBlocks.add(Material.TORCH.getId());
					validBlocks.add(Material.REDSTONE_WIRE.getId());
					validBlocks.add(Material.CROPS.getId());
					validBlocks.add(Material.SIGN_POST.getId());
					validBlocks.add(Material.WOODEN_DOOR.getId());
					validBlocks.add(Material.LADDER.getId());
					validBlocks.add(Material.RAILS.getId());
					validBlocks.add(Material.WALL_SIGN.getId());
					validBlocks.add(Material.LEVER.getId());
					validBlocks.add(Material.STONE_PLATE.getId());
					validBlocks.add(Material.IRON_DOOR.getId());
					validBlocks.add(Material.WOOD_PLATE.getId());
					validBlocks.add(Material.REDSTONE_TORCH_OFF.getId());
					validBlocks.add(Material.REDSTONE_TORCH_ON.getId());
					validBlocks.add(Material.STONE_BUTTON.getId());
					validBlocks.add(Material.SUGAR_CANE.getId());
					validBlocks.add(Material.PORTAL.getId());
					validBlocks.add(Material.CAKE_BLOCK.getId());
					
					if (validBlocks.contains(checkBlok1) && validBlocks.contains(checkBlok2)) {
						landing = check;
					}
					
				}
			}
		}
		
		return landing;
	}
	
}





