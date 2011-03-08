package org.buckit.commands.kits;

import java.util.Arrays;

import org.buckit.Config;
import org.buckit.datasource.type.KitsDataSource;
import org.buckit.model.Kit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateKitCommand extends Command {

    private final KitsDataSource datasource;
    public CreateKitCommand(String name, Server server){
        super(name);
        this.datasource = server.getDataSourceManager().getKitsDataSource();
        this.description = "Used to create kits on a live server, items format: itemid,amount,damage and ";
        
        this.usageMessage = "Usage: /createkit [kit name] [min access level] [delay] [items...]";
        
        this.accessname = "buckit.kits.createkit";
        this.setAliases(Arrays.asList("setkit"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if(!(sender instanceof Player))
            return false;
        
        if(args.length < 3) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Insufficient arguments specified");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + getUsage());
            return true;
        }
        String name = args[0].toLowerCase();
        int minaccesslevel,delay;
        try {
            minaccesslevel = Integer.parseInt(args[1]);
        } catch (NumberFormatException e ) {sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid minimum access level '" + args[1] +  "' !"); return true; }
        try {
            delay = Integer.parseInt(args[2]);
        } catch (NumberFormatException e ) {sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid delay '" + args[2] +  "' !"); return true; }
        
        String[] items = Arrays.copyOfRange(args, 3, args.length);
        Kit kit;
        try {
            int[][] itemarray = getItemArray(items);
            if (itemarray==null) {
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid items.");
                return true;
            }
            kit = new Kit(-1, name, itemarray, minaccesslevel, delay);
        } catch (NumberFormatException e ) {sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid items."); return true; }
        
        if(kit != null && datasource.setKit(kit)) {
            sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Succesfully added kit '" + name + "' with " + kit.getItems().length + " items.");
        } else {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Error while making kit, please report any errors from the console to Buck - It!");
        }
        return true;
    }

    private static int[][] getItemArray(String[] items) throws NumberFormatException{
        int[][] rt = new int[items.length][3];
        String[] parts;
        for (int i = 0; i < items.length; i++) {
            parts = items[i].split(",");
            
            if (parts.length<3){
                return null;
            }
            
            rt[i][0] = Integer.parseInt(parts[0]);
            rt[i][1] = Integer.parseInt(parts[1]);
            rt[i][2] = Integer.parseInt(parts[2]);
        }
        return rt;
    }
}
