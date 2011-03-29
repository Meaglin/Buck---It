package org.buckit.commands.admin;

import java.net.InetAddress;
import java.util.List;

import org.buckit.Config;
import org.buckit.model.UserDataHolder;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhoisCommand extends Command {

    private Server server;
    public WhoisCommand(String name, Server server) {
        super(name);
        this.description = "Gives regular information about [player].";
        this.usageMessage = "Usage: /whois [player]";
        this.accessname = "buckit.admin.whois";
        
        this.server = server;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        
        if(args.length == 0) {
            sender.sendMessage(getUsage());
        }
        String playername = args[0].toLowerCase();
        List<Player> matches = server.matchPlayer(playername);
        UserDataHolder data = null;
        if(matches.size() == 0) {
            data = server.getDataSourceManager().getUserDataSource().getUserData(playername);
        } else if(matches.size() == 1) {
            data = matches.get(0).getUserDataHolder();
        } else {
            for(Player p : matches) {
                if(p.getName().equalsIgnoreCase(playername)) {
                    data = p.getUserDataHolder();
                    break;
                }
            }
        }
        
        if(data == null){
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No player found with name: '" + playername + "'.");
            return true;
        }
        byte[] addr = new byte[4];
        String[] split =data.getIp().split(".");
        for(int i = 0;i < split.length;i++){
            addr[i] = Byte.parseByte(split[i]);
        }
        String hostname = "None found.";
        try {
            hostname = InetAddress.getByAddress(addr).getCanonicalHostName();
        } catch (Exception e) {
            
        }
        sender.sendMessage("Player: "+data.getUsername());
        sender.sendMessage("Ip: " + data.getIp());
        sender.sendMessage("Hostname: " + hostname);
        sender.sendMessage("Online: " + data.isOnline());
        
        return true;
    }

}
