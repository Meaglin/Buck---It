package org.bukkit.craftbukkit.entity;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet3Chat;
import net.minecraft.server.Packet6SpawnPosition;
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

    public CraftPlayer(CraftServer server, EntityPlayer entity) {
        super(server, entity);
        
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
        SocketAddress addr = getHandle().a.b.b();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    @Override
    public EntityPlayer getHandle() {
        return (EntityPlayer) entity;
    }

    public double getEyeHeight() {
        return getEyeHeight(false);
    }

    public double getEyeHeight(boolean ignoreSneaking) {
        if (ignoreSneaking) {
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

    public void sendRawMessage(String message) {
        getHandle().a.b(new Packet3Chat(message));
    }

    public void sendMessage(String message) {
        for (final String line: TextWrapper.wrapText(message)) {
            getHandle().a.b(new Packet3Chat(line));
        }
    }

    public String getDisplayName() {
        return getHandle().displayName;
    }

    public void setDisplayName(final String name) {
        getHandle().displayName = name;
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
        getHandle().a.a(message == null ? "" : message);
    }

    public void setCompassTarget(Location loc) {
        // Do not directly assign here, from the packethandler we'll assign it.
        getHandle().a.b((Packet) new Packet6SpawnPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    public Location getCompassTarget() {
        return getHandle().compassTarget;
    }

    public void chat(String msg) {
        getHandle().a.chat(msg);
    }

    public boolean performCommand(String command) {
        return server.dispatchCommand(this, command);
    }

    @Override
    public boolean teleport(Location location) {
        WorldServer oldWorld = ((CraftWorld)getWorld()).getHandle();
        WorldServer newWorld = ((CraftWorld)location.getWorld()).getHandle();
        ServerConfigurationManager manager = server.getHandle();
        EntityPlayer entity = getHandle();
        boolean teleportSuccess;

        if (oldWorld != newWorld) {

            EntityPlayer newEntity = new EntityPlayer(manager.c, newWorld, entity.name, new ItemInWorldManager(newWorld));

            newEntity.id = entity.id;
            newEntity.a = entity.a;
            newEntity.health = entity.health;
            newEntity.fireTicks = entity.fireTicks;
            newEntity.inventory = entity.inventory;
            newEntity.inventory.d = newEntity;
            newEntity.activeContainer = entity.activeContainer;
            newEntity.defaultContainer = entity.defaultContainer;
            newEntity.lastwarp = entity.lastwarp;// Buck - It
            newEntity.locX = location.getX();
            newEntity.locY = location.getY();
            newEntity.locZ = location.getZ();
            newEntity.displayName = entity.displayName;
            newEntity.compassTarget = entity.compassTarget;
            newEntity.fauxSleeping = entity.fauxSleeping;
            newWorld.u.c((int) location.getBlockX() >> 4, (int) location.getBlockZ() >> 4);

            teleportSuccess = newEntity.a.teleport(location);

            if (teleportSuccess) {
                manager.c.k.a(entity);
                manager.c.k.b(entity);
                oldWorld.manager.b(entity);
                manager.b.remove(entity);
                oldWorld.e(entity);

                newWorld.manager.a(newEntity);
                newWorld.a(newEntity);
                manager.b.add(newEntity);

                entity.a.e = newEntity;
                this.entity = newEntity;

                setCompassTarget(getCompassTarget());
            }

            return teleportSuccess;
        } else {
            return entity.a.teleport(location);
        }
    }

    public void setSneaking(boolean sneak) {
        getHandle().e(sneak);
    }

    public boolean isSneaking() {
        return getHandle().Z();
    }

    public void loadData() {
        server.getHandle().n.b(getHandle());
    }

    public void saveData() {
        server.getHandle().n.a(getHandle());
    }

    public void updateInventory() {
        getHandle().m();
    }

    public void setSleepingIgnored(boolean isSleeping) {
        getHandle().fauxSleeping = isSleeping;
        ((CraftWorld)getWorld()).getHandle().checkSleepStatus();
    }

    public boolean isSleepingIgnored() {
        return getHandle().fauxSleeping;
    }

    /*
     * Buck - It functions
     * 
     */
    private UserDataHolder dataholder;

    private void loadBuckItData(){
        dataholder = server.getDataSourceManager().getUserDataSource().getUserData(getName().toLowerCase());
        dataholder.setOnline(true);
        
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
        
        if(dataholder.canUseCommand(Config.FULL_ACCESS_STRING, getWorld().getName()))
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
