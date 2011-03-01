package org.buckit.commands.util;

import java.util.HashMap;
import java.util.Map;

import org.buckit.Config;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StackCommand extends Command {

    public StackCommand(String name, Server server) {
        super(name);
        this.description = "Stacks up all the items in your inventory.";
        this.usageMessage = "Usage: /stack";
        this.accessname = "buckit.util.stack";
    }
    
    //TODO: make it also stack cloth.
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        Inventory inv = ((Player)sender).getInventory();
        Map<Integer,Integer> check = new HashMap<Integer, Integer>();
        for(int i = 0; i < inv.getSize(); i++){
            ItemStack item = inv.getItem(i);
            if(check.containsKey(item.getTypeId()) && item.getDurability() == inv.getItem(check.get(item.getTypeId())).getDurability()){
                ItemStack first = inv.getItem(check.get(item.getTypeId()));
                int total = first.getAmount() + item.getAmount();
                if(total > first.getMaxStackSize()){
                    first.setAmount(first.getMaxStackSize());
                    item.setAmount(total-first.getMaxStackSize());
                    if(item.getAmount() < item.getMaxStackSize()) check.put(item.getTypeId(), i);
                } else {
                    first.setAmount(total);
                    inv.clear(i);
                    if(first.getAmount() >= first.getMaxStackSize()) check.remove(first.getTypeId());
                }
            }else{
                //we don't want full stacks.
                if(item.getAmount() < item.getMaxStackSize())check.put(item.getTypeId(), i);
            }
        }
        
        sender.sendMessage(Config.DEFAULT_INFO_COLOR + "Your inventory has been cleaned, enjoy :).");
        return true;
    }

}
