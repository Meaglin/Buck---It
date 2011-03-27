package org.bukkit.craftbukkit.entity;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet3Chat;
import net.minecraft.server.Packet6SpawnPosition;
import net.minecraft.server.Packet9Respawn;
import net.minecraft.server.ServerConfigurationManager;
import net.minecraft.server.WorldServer;

import org.buckit.Config;
import org.buckit.access.AccessLevel;
import org.buckit.model.UserDataHolder;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.TextWrapper;
import org.bukkit.entity.Player;

public class CraftPlayer extends CraftHumanEntity implements Player {
    private EntityPlayer entity;
    private String name;

    public CraftPlayer(CraftServer server, EntityPlayer entity) {
        super(server, entity);
        this.name = getName();
        this.entity = entity;
        
        //Buck - It
        loadBuckItData();
    }

    public boolean isOp() {
        return server.getHandle().h(getName());
    }

    public boolean isPlayer() {
        return true;
    }

    public boolean isOnline() {
        for (Object obj: server.getHandle().b) {
            EntityPlayer player = (EntityPlayer) obj;
            if (player.name.equalsIgnoreCase(getName())) {
                return true;
            }
        }
        return false;
    }

    public InetSocketAddress getAddress() {
        SocketAddress addr = entity.a.b.b();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    @Override
    public EntityPlayer getHandle() {
        return entity;
    }

    public double getEyeHeight() {
        return getEyeHeight(false);
    }

    public double getEyeHeight(boolean ignoreSneaking) {
        if(ignoreSneaking) {
            return 1.62D;
        } else {
            if (isSneaking()) {
                return 1.42D;
            } else {
                return 1.62D;
            }
        }
    }

    public void setHandle(final EntityPlayer entity) {
        super.setHandle((EntityHuman) entity);
        this.entity = entity;
    }

    public void sendMessage(String message) {
        for (final String line: TextWrapper.wrapText(message)) {
            entity.a.b(new Packet3Chat(line));
        }
    }

    public String getDisplayName() {
        return name;
    }

    public void setDisplayName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + getName() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftPlayer other = (CraftPlayer) obj;
        if ((this.getName() == null) ? (other.getName() != null) : !this.getName().equals(other.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.getName() != null ? this.getName().hashCode() : 0);
        return hash;
    }

    public void kickPlayer(String message) {
        entity.a.a(message == null ? "" : message);
    }

    public void setCompassTarget(Location loc) {
        entity.a.b(((Packet) (new Packet6SpawnPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))));
    }

    public void chat(String msg) {
        entity.a.chat(msg);
    }

    public boolean performCommand(String command) {
        return server.dispatchCommand(this, command);
    }

    @Override
    public void teleportTo(Location location) {
        WorldServer oldWorld = ((CraftWorld)getWorld()).getHandle();
        WorldServer newWorld = ((CraftWorld)location.getWorld()).getHandle();
        ServerConfigurationManager manager = server.getHandle();

        if (oldWorld != newWorld) {
            manager.c.k.a(entity);
            manager.c.k.b(entity);
            oldWorld.manager.b(entity);
            manager.b.remove(entity);
            oldWorld.e(entity);

            EntityPlayer newEntity = new EntityPlayer(manager.c, newWorld, entity.name, new ItemInWorldManager(newWorld));

            newEntity.id = entity.id;
            newEntity.a = entity.a;
            newEntity.health = entity.health;
            newEntity.fireTicks = entity.fireTicks;
            newEntity.inventory = entity.inventory;
            newEntity.inventory.e = newEntity;
            newEntity.activeContainer = entity.activeContainer;
            newEntity.defaultContainer = entity.defaultContainer;
            newEntity.lastwarp = entity.lastwarp;// Buck - It
            newWorld.u.d((int) location.getBlockX() >> 4, (int) location.getBlockZ() >> 4);

            newEntity.a.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            newWorld.manager.a(newEntity);
            newWorld.a(newEntity);
            manager.b.add(newEntity);

            
            entity.a.e = newEntity;
            entity = newEntity;
        } else {
            entity.a.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }
    }

    public void setSneaking(boolean sneak) {
        entity.b(sneak);
    }

    public boolean isSneaking() {
        return entity.U();
    }
    
    public void updateInventory() {
        entity.l();
    }

    /*
     * Buck - It functions
     * 
     */
    private UserDataHolder dataholder;

    private void loadBuckItData(){
        dataholder = server.getDataSourceManager().getUserDataSource().getUserData(getName().toLowerCase());
        
        String format = Config.DEFAULT_USER_FORMAT;
        
        if(dataholder.getUsernameformat() != null && !dataholder.getUsernameformat().equals(""))
            format = dataholder.getUsernameformat();
        else if(getAccessLevel().getUsernameformat() != null && !getAccessLevel().getUsernameformat().equals(""))
            format = getAccessLevel().getUsernameformat();
        
        setDisplayName(format.replace("{$username}", getName()).replace("{$group}", getAccessLevel().getName()).replace("^", "\u00A7"));
        
    }
    
    public void reloadBuckItData() {
        try {
            loadBuckItData();
        } catch(Throwable t) {
            server.getLogger().warning("Error reloading Buck - It data of player " + getName() + ", error:");
            t.printStackTrace();
        }
    }
    
    @Override
    public boolean canBuild() {
        return getAccessLevel().canBuild() || dataholder.canbuild();
    }

    @Override
    // TODO: optimize
    public boolean canUseCommand(String command) {
        if(dataholder.canUseCommand(command, getWorld().getName()))
            return true;
        
        String[] split = command.split(".");
        if(split.length > 1){
            for(int i = 0;i < split.length;i++){
                String t = split[0];
                for(int x = 1;x <= i;x++)
                    t += "." + split[x];
                t += "."+Config.FULL_ACCESS_STRING;
                if(dataholder.canUseCommand(t, getWorld().getName()))
                    return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean isAdmin() {
        return getAccessLevel().isAdmin() || dataholder.isAdmin();
    }
    
    @Override
    public AccessLevel getAccessLevel() {
        return dataholder.getAccessLevel();
    }
    
    @Override
    public int getPlayerId() {
        return dataholder.getId();
    }
    
    @Override
    public UserDataHolder getUserDataHolder() {
        return dataholder;
    }
}
