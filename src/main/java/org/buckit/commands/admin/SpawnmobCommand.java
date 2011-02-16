package org.buckit.commands.admin;

import net.minecraft.server.EntityChicken;
import net.minecraft.server.EntityCow;
import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntityGhast;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPig;
import net.minecraft.server.EntityPigZombie;
import net.minecraft.server.EntitySheep;
import net.minecraft.server.EntitySkeleton;
import net.minecraft.server.EntitySpider;
import net.minecraft.server.EntitySquid;
import net.minecraft.server.EntityZombie;

import org.buckit.Config;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Player;

public class SpawnMobCommand extends Command {

    public SpawnMobCommand(String name, Server server) {
        super(name);
        this.tooltip = "Spawns a mob on your location.";
        this.usageMessage = "Usage: /spawnmob [mob name] <count>";
        this.accessname = "buckit.admin.spawnmob";
    }
    
    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        if (args.length == 0) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Insufficient arguments specified.");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
            return true;
        }
        
        Player player = (Player)sender;
        CraftWorld world = (CraftWorld)player.getWorld();
        String mobname = args[0].toLowerCase();
        
        if(getEntity(mobname,world) == null) {
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid mob name.");
            sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
            return true;
        }
        int count = 1;
        if(args.length > 1) {
            try {
                count = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + "Invalid mob count.");
                sender.sendMessage(Config.DEFAULT_ERROR_COLOR + this.getUsage());
                return true;
            }
        }
        for(int i = 0; i < count;i++){
            CraftLivingEntity c = new CraftLivingEntity(world.getHandle().getServer(),getEntity(mobname,world));
            c.teleportTo(player.getLocation());
        }
        
        return true;
    }
    private static EntityLiving getEntity(String name,CraftWorld world) {
        EntityLiving en = null;
        
        if(name.equalsIgnoreCase("Pig"))             en = new EntityPig(world.getHandle());
        else if(name.equalsIgnoreCase("Cow"))        en = new EntityCow(world.getHandle());
        else if(name.equalsIgnoreCase("Chicken"))    en = new EntityChicken(world.getHandle());
        else if(name.equalsIgnoreCase("Sheep"))      en = new EntitySheep(world.getHandle());
        else if(name.equalsIgnoreCase("Skeleton"))   en = new EntitySkeleton(world.getHandle());
        else if(name.equalsIgnoreCase("Spider"))     en = new EntitySpider(world.getHandle());
        else if(name.equalsIgnoreCase("Zombie"))     en = new EntityZombie(world.getHandle());
        else if(name.equalsIgnoreCase("Squid"))      en = new EntitySquid(world.getHandle());
        else if(name.equalsIgnoreCase("PigZombie"))  en = new EntityPigZombie(world.getHandle());
        else if(name.equalsIgnoreCase("Ghast"))      en = new EntityGhast(world.getHandle());
        else if(name.equalsIgnoreCase("Creeper"))    en = new EntityCreeper(world.getHandle());
        
        return en;
    }

}
