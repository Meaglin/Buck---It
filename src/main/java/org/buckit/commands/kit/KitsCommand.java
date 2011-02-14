package org.buckit.commands.kit;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.buckit.datasource.type.KitsDataSource;
import org.buckit.model.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitsCommand extends Command {

    private final KitsDataSource datasource;
    public KitsCommand(String name, Server server){
        super(name);
        this.datasource = server.getDataSource().getKitsDataSource();
        this.tooltip = "A list of all availeble kits.";

        this.usageMessage = "Usage: /kits";
        
        this.accessname = "buckit.kits.kits";
        this.setAliases(Arrays.asList("listkits"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if(!(sender instanceof Player))
            return false;
        
        Player player = (Player)sender;
        
        Collection<Kit> kits = datasource.getKits();
        Collection<String> names = new LinkedHashSet<String>();
        for(Kit k : kits)
            if(player.getAccessLevel().getId() >= k.getMinaccesslevel())
                names.add(k.getName());
        
        String str = "";
        for(String name : names)
            str += name + ", ";
        if(str.length() == 0){
            sender.sendMessage("No kits availeble.");
            return true;
        }else
            str = str.substring(0,str.length()-2);
        
        sender.sendMessage(ChatColor.GREEN + "List of availeble kits:");
        sender.sendMessage(str);
        
        return true;
    }

}
