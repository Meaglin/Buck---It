package org.buckit.commands.kits;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.buckit.Config;
import org.buckit.datasource.type.KitsDataSource;
import org.buckit.model.Kit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitsCommand extends Command {

    private final KitsDataSource datasource;
    public KitsCommand(String name, Server server){
        super(name);
        this.datasource = server.getDataSourceManager().getKitsDataSource();
        this.description = "A list of all available kits.";

        this.usageMessage = "Usage: /kits";
        
        this.accessname = "buckit.kits.kits";
        this.setAliases(Arrays.asList("listkits"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        
        int accesslevel = Config.DEFAULT_ACCESS_LEVEL;
        if(sender instanceof Player)
            accesslevel = ((Player)sender).getAccessLevel().getLevel();
        else
            accesslevel = Integer.MAX_VALUE;
        
        Collection<Kit> kits = datasource.getKits();
        Collection<String> names = new LinkedHashSet<String>();
        for(Kit k : kits)
            if(accesslevel >= k.getMinaccesslevel())
                names.add(k.getName());
        
        String str = "";
        for(String name : names)
            str += name + ", ";
        if(str.length() == 0){
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "No kits available.");
            return true;
        }else
            str = str.substring(0,str.length()-2);
        
        sender.sendMessage(Config.DEFAULT_INFO_COLOR + "List of available kits:");
        sender.sendMessage(str);
        
        return true;
    }

}
