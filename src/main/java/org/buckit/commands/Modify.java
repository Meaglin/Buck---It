package org.buckit.commands;

import java.util.List;

import org.buckit.Config;
import org.buckit.access.AccessLevel;
import org.buckit.datasource.type.AccessDataSource;
import org.buckit.datasource.type.UserDataSource;
import org.buckit.model.UserDataHolder;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Modify extends Command {
	
	private final Server server;
	private final UserDataSource userData;
	private final AccessDataSource accessData;

	public Modify(String name, Server server) {
		super(name);
        this.tooltip = "Modifies.";
        this.usageMessage = "Usage: /modify <player> <key1>:<value1> <key2>:<value2> <keyN>:<valueN>";
        this.accessname = "buckit.modify";
        
        this.server = server;
        this.userData = server.getDataSource().getUserDataSource();
        this.accessData = server.getDataSource().getAccessDataSource();
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
		
		List<Player> players = server.matchPlayer(args[0].toLowerCase());
		
		if (players.size() == 0) {
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No players found to modify");
			return true;
		}
		else if (players.size() == 1) {
			Player player = players.get(0);
			UserDataHolder playerData = player.getUserDataHolder();
			
			sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Modifying player: " + player.getDisplayName());
			
			for (int i=1; i<args.length; i++) {
				String[] key_value = args[i].split(":");
				String key 		= key_value[0].toLowerCase();
				String value 	= key_value[1].toLowerCase();
				
				if (key.equals("accesslevel") || key.equals("al")) {
					
					AccessLevel newAccessLevel;
					try {
						newAccessLevel = accessData.getAccessLevel(Integer.parseInt(value));
		            } catch (NumberFormatException e) {
						newAccessLevel = accessData.getAccessLevel(value);
		            }
		            
		            AccessLevel playerAccessLevel = player.getAccessLevel();
		            AccessLevel senderAccessLevel = ((Player)sender).getAccessLevel();
		            
		            if (newAccessLevel == null) {
		            	sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "[accesslevel] : unknown value");
		            }
		            else if (senderAccessLevel.getId() < newAccessLevel.getId()) {
		            	sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "[accesslevel] : you are not able to modify this in this way");
		            }
		            else {
		            	playerData.setAccesslevel(newAccessLevel);
		            	sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "[accesslevel] : " + playerAccessLevel.getName() + " -> " + newAccessLevel.getName());
		            }
				}
				else if (key.equals("format") || key.equals("f")) {
					playerData.setUsernameformat(value);	
					sender.sendMessage(Config.DEFAULT_INFO_COLOR + "[format] : modified");
				}
				else if (key.equals("commands") || key.equals("c")) {
					playerData.setCommands(value);
					sender.sendMessage(Config.DEFAULT_INFO_COLOR + "[commands] : modified");
				}
				else if (key.equals("admin") || key.equals("a")) {
					boolean before = playerData.isAdmin();
					boolean after = before;
					
					if (value.equals("true") || value.equals("1"))
						after = true;
					else if (value.equals("false") || value.equals("0"))
						after = false;
					
					playerData.setAdmin(after);
					sender.sendMessage(Config.DEFAULT_INFO_COLOR + "[admin] : " + before + " -> " + after);
				}
				else if (key.equals("canbuild") || key.equals("cb")) {
					boolean before = playerData.canbuild();
					boolean after = before;
					
					if (value.equals("true") || value.equals("1")) {
						after = true;
					}
					else if (value.equals("false") || value.equals("0")) {
						after = false;
					}
					
					playerData.setCanbuild(after);
					sender.sendMessage(Config.DEFAULT_INFO_COLOR + "[canbuild] : " + before + " -> " + after);
				}
				else {
					sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Unknown modify-key: " + key);
				}
			}
			
			userData.updateUser(playerData);
			return true;
		}
		else {
			String names = "";
			for (int i=0; i<players.size(); i++)
				names += ", " + players.get(i).getDisplayName() + Config.DEFAULT_ERROR_COLOR;
			
			names = names.substring(2);
			sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Multiple players found: "+names);
			return true;
		}		
		
		return false;
	}

}
