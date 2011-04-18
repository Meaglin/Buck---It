package net.minecraft.server;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.buckit.Config;

// CraftBukkit start
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityDeathEvent;
// CraftBukkit end

public class EntityPlayer extends EntityHuman implements ICrafting {

    public NetServerHandler a;
    public MinecraftServer b;
    public ItemInWorldManager c;
    public double d;
    public double e;
    public List f = new LinkedList();
    public Set g = new HashSet();
    private int bE = -99999999;
    private int bF = 60;
    private ItemStack[] bG = new ItemStack[] { null, null, null, null, null};
    private int bH = 0;
    public boolean h;

    public EntityPlayer(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager) {
        super(world);
        ChunkCoordinates chunkcoordinates = world.m();
        int i = chunkcoordinates.a;
        int j = chunkcoordinates.c;
        int k = chunkcoordinates.b;

        if (!world.m.e) {
            i += this.random.nextInt(Config.SPAWN_RESPAWN_AREA_RADIUS*2 + 1) - Config.SPAWN_RESPAWN_AREA_RADIUS;
            k = world.e(i, j);
            j += this.random.nextInt(Config.SPAWN_RESPAWN_AREA_RADIUS*2 + 1) - Config.SPAWN_RESPAWN_AREA_RADIUS;
        }

        this.c((double) i + 0.5D, (double) k, (double) j + 0.5D, 0.0F, 0.0F);
        this.b = minecraftserver;
        this.bm = 0.0F;
        iteminworldmanager.a = this;
        this.name = s;
        this.c = iteminworldmanager;
        this.height = 0.0F;

        // CraftBukkit start
        this.displayName = this.name;
    }

    public String displayName;
    public Location compassTarget;
    // CraftBukkit end

    // Buck - It start
    // Called when a player is standing in/near a gate.
    private int start = 0;
    private boolean nearGate = false;
    private long lastCall = 0;
    public int lastwarp = 0;
    private int peak = 0;
    @Override
    public void T() {
        if(lastwarp > floor(System.currentTimeMillis()/1000)-20)
            return;
        
        if(!nearGate) {
            start = floor(System.currentTimeMillis()/1000);
            nearGate = true;
            lastCall = System.currentTimeMillis();
            a.b("Warping in ...");
        } else {
            long currentCall = System.currentTimeMillis();
            if(lastCall < currentCall-1000) {
                nearGate = false;
                return;
            }
            if(floor(lastCall/1000) != floor(currentCall/1000)) {
                int t = start + 4 - floor(currentCall/1000);
                if(t != 0)
                    a.b(t + "...");
            }
            if(currentCall-lastCall > peak) peak = (int) (currentCall-lastCall);
            lastCall = currentCall;
            if(start < floor(lastCall/1000) - 3) {
                this.a.b("Warp! " + peak);
                org.bukkit.entity.Entity entity = getBukkitEntity(); 
                org.bukkit.World from = entity.getWorld();
                org.bukkit.World to = null;
                int tox = 0,toz= 0;
                if(from.getEnvironment() == org.bukkit.World.Environment.NETHER){
                    tox = entity.getLocation().getBlockX()*8;
                    toz = entity.getLocation().getBlockZ()*8;
                    to = entity.getServer().getWorld("world");
                } else {
                    tox = entity.getLocation().getBlockX()/8;
                    toz = entity.getLocation().getBlockZ()/8;
                    to = entity.getServer().getWorld(Config.HELL_DIRECTORY);
                }
                int range = 20;
                Location gate = null;
                for(int x = (tox >> 4)-1;x <= (tox >> 4)+1;x++) {
                    for(int z = (toz >> 4)-1;z <= (toz >>4)+1;z++) {                        
                        to.loadChunk(x, z);
                    }
                }
                
                for(int x = tox-range;x <= tox+range;x++){
                    for(int z = toz-range;z <= toz+range;z++){
                        for(int y = 10;y < 124;y++){ 
                            if(to.getBlockTypeIdAt(x, y, z) == 90) {
                                gate = new Location(to,x,y,z);
                            }
                        }
                    }
                }
                if(gate != null){
                    if(to.getBlockTypeIdAt(gate.getBlockX(), gate.getBlockY()-1, gate.getBlockZ()) == org.bukkit.Material.PORTAL.getId()){
                        gate = new Location(to,gate.getBlockX(), gate.getBlockY()-1, gate.getBlockZ());
                    }
                    entity.teleportTo(gate);
                } else {
                    range:for(int r = 0;r < range;r++) {
                        for(int z = toz-r;z <= toz+r;z++){
                            int x = tox+r;
                            gate = makeGate(to,x,z);
                            if(gate != null)
                                break range;
                        }
                        for(int x = tox+r-1;x >= tox-r+1;x--){
                            int z = toz+r;
                            gate = makeGate(to,x,z);
                            if(gate != null)
                                break range;
                        }
                        for(int z = toz+r;z >= toz-r;z--){
                            int x = tox-r;
                            gate = makeGate(to,x,z);
                            if(gate != null)
                                break range;
                        }
                        for(int x = tox-r+1;x <= tox+r-1;x++){
                            int z = toz+r;
                            gate = makeGate(to,x,z);
                            if(gate != null)
                                break range;
                        }
                    }
                    if(gate == null) {
                        // oh seriously FUCK.
                        ((org.bukkit.entity.Player)entity).sendMessage("No viable gate location found, go screw yourself.");
                    
                    } else {
                        lastwarp = floor(System.currentTimeMillis()/1000);
                        //CraftChunk chunk = (CraftChunk) gate.getBlock().getChunk();
                        //this.a.b((Packet) (new Packet51MapChunk(chunk.getX() * 16, 0, chunk.getZ() * 16, 16, 128, 16, ((CraftWorld)to).getHandle())));
                        entity.teleportTo(gate);
                        //entity.teleportTo(gate);
                        //a.b(new Packet1Login("", "", this.id, this.world.j(), (byte) this.world.m.g));
                    
                    }
                    
                }
                start = 0;
                lastCall = 0;
                nearGate = false;
            }
        }
    }
    private Location makeGate(org.bukkit.World world,int x,int z) {
        int y = 10;
        int lastType = -1;
        int curType = -1;
        height:for(;y < 120;y++){
            // 87 = netherrack 88 = soul sand 89 = lightstone.
            curType = world.getBlockTypeIdAt(x, y, z);
            if(curType == 0 && isSolid(lastType)){
                lastType = curType;
                if(world.getBlockTypeIdAt(x, y+1, z) != 0)
                    continue;
                
                int type = -1,typeunder = -1;
                for(int i = -1;i <= 1;i++)
                    for(int o = -1;o <=0;o++) {
                        type = world.getBlockTypeIdAt(x+o, y, z+i);
                        typeunder = world.getBlockTypeIdAt(x+o, y-1, z+i);
                        if(!isSolid(type) && !isSolid(typeunder))
                            continue height;
                    }
                
                for(int i = -1;i <= 1;i++)
                    for(int o = -1;o <=0;o++)
                        for(int p = 1;p <= 3;p++)
                            if(world.getBlockTypeIdAt(x+o, y+p, z+i) != 0)
                                continue height;
                
                for(int i = -1;i <= 2;i++){
                    ((CraftWorld)world).getHandle().setTypeId(x+i, y, z, 49);
                    ((CraftWorld)world).getHandle().setTypeId(x+i, y+4, z, 49);
                }
                for(int i = 1;i <= 3;i++){
                    ((CraftWorld)world).getHandle().setTypeId(x-1, y+i, z, 49);
                    ((CraftWorld)world).getHandle().setTypeId(x+2, y+i, z, 49);
                }
                for(int i = 1;i <= 3;i++){
                    ((CraftWorld)world).getHandle().setTypeId(x, y+i, z, 90);
                    ((CraftWorld)world).getHandle().setTypeId(x+1, y+i, z, 90);
                }

                
                return new Location(world,x,y+1,z);
            } else {
                lastType = curType;
            }
        }
        
        
        return null;
    }
    private boolean isSolid(int type){
        return (type != 10 && type != 11 && type != 0);
    }
    // Buck - It end
    private int floor(double l ) {int k = (int)l; return (k > l ? k-1 : k); }
    
    public void m() {
        this.activeContainer.a((ICrafting) this);
    }

    public ItemStack[] k_() {
        return this.bG;
    }

    protected void l_() {
        this.height = 0.0F;
    }

    public float q() {
        return 1.62F;
    }

    public void f_() {
        this.c.a();
        --this.bF;
        this.activeContainer.a();

        for (int i = 0; i < 5; ++i) {
            ItemStack itemstack = this.b_(i);

            if (itemstack != this.bG[i]) {
                this.b.k.a(this, new Packet5EntityEquipment(this.id, i, itemstack));
                this.bG[i] = itemstack;
            }
        }
    }

    public ItemStack b_(int i) {
        return i == 0 ? this.inventory.b() : this.inventory.b[i - 1];
    }

    public void a(Entity entity) {
        // CraftBukkit start
        List<org.bukkit.inventory.ItemStack> loot = new ArrayList<org.bukkit.inventory.ItemStack>();

        for (int i = 0; i < inventory.a.length; ++i) {
            if (inventory.a[i] != null) {
                loot.add(new CraftItemStack(inventory.a[i]));
                inventory.a[i] = null;
            }
        }

        for (int i = 0; i < inventory.b.length; ++i) {
            if (inventory.b[i] != null) {
                loot.add(new CraftItemStack(inventory.b[i]));
                inventory.b[i] = null;
            }
        }

        CraftEntity craftEntity = (CraftEntity) getBukkitEntity();
        CraftWorld cworld = ((WorldServer) world).getWorld();
        Server server = ((WorldServer) world).getServer();

        EntityDeathEvent event = new EntityDeathEvent(craftEntity, loot);
        server.getPluginManager().callEvent(event);

        for (org.bukkit.inventory.ItemStack stack: event.getDrops()) {
            cworld.dropItemNaturally(craftEntity.getLocation(), stack);
        }
        // CraftBukkit end
    }

    public boolean a(Entity entity, int i) {
        if (this.bF > 0) {
            return false;
        } else {
            if (!this.b.n) {
                if (entity instanceof EntityHuman) {
                    return false;
                }

                if (entity instanceof EntityArrow) {
                    EntityArrow entityarrow = (EntityArrow) entity;

                    if (entityarrow.b instanceof EntityHuman) {
                        return false;
                    }
                }
            }

            return super.a(entity, i);
        }
    }

    public void b(int i) {
        super.b(i);
    }

    public void a(boolean flag) {
        super.f_();
        if (flag && !this.f.isEmpty()) {
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) this.f.get(0);

            if (chunkcoordintpair != null) {
                boolean flag1 = false;

                if (this.a.b() < 2) {
                    flag1 = true;
                }

                if (flag1) {
                    this.f.remove(chunkcoordintpair);

                    // CraftBukkit start
                    this.a.b((Packet) (new Packet51MapChunk(chunkcoordintpair.a * 16, 0, chunkcoordintpair.b * 16, 16, 128, 16, this.world)));
                    List list = ((WorldServer) world).d(chunkcoordintpair.a * 16, 0, chunkcoordintpair.b * 16, chunkcoordintpair.a * 16 + 16, 128, chunkcoordintpair.b * 16 + 16);
                    // CraftBukkit end

                    for (int i = 0; i < list.size(); ++i) {
                        this.a((TileEntity) list.get(i));
                    }
                }
            }
        }

        if (this.health != this.bE) {
            this.a.b((Packet) (new Packet8UpdateHealth(this.health)));
            this.bE = this.health;
        }
    }

    private void a(TileEntity tileentity) {
        if (tileentity != null) {
            Packet packet = tileentity.e();

            if (packet != null) {
                this.a.b(packet);
            }
        }
    }

    public void r() {
        super.r();
    }

    public void b(Entity entity, int i) {
        if (!entity.dead) {
            if (entity instanceof EntityItem) {
                this.b.k.a(entity, new Packet22Collect(entity.id, this.id));
            }

            if (entity instanceof EntityArrow) {
                this.b.k.a(entity, new Packet22Collect(entity.id, this.id));
            }
        }

        super.b(entity, i);
        this.activeContainer.a();
    }

    public void m_() {
        if (!this.p) {
            this.q = -1;
            this.p = true;
            this.b.k.a(this, new Packet18ArmAnimation(this, 1));
        }
    }

    public void t() {}

    public EnumBedError a(int i, int j, int k) {
        EnumBedError enumbederror = super.a(i, j, k);

        if (enumbederror == EnumBedError.OK) {
            this.b.k.a(this, new Packet17(this, 0, i, j, k));
        }

        return enumbederror;
    }

    public void a(boolean flag, boolean flag1, boolean flag2) {
        if (this.F()) {
            this.b.k.b(this, new Packet18ArmAnimation(this, 3));
        }

        super.a(flag, flag1, flag2);
        this.a.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
    }

    public void b(Entity entity) {
        // CraftBukkit start
        setPassengerOf(entity);
    }

    public void setPassengerOf(Entity entity) {
        // b(null) doesn't really fly for overloaded methods,
        // so this method is needed

        super.setPassengerOf(entity);
        // CraftBukkit end
        this.a.b((Packet) (new Packet39AttachEntity(this, this.vehicle)));
        this.a.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
    }

    protected void a(double d0, boolean flag) {}

    public void b(double d0, boolean flag) {
        super.a(d0, flag);
    }

    private void aa() {
        this.bH = this.bH % 100 + 1;
    }

    public void b(int i, int j, int k) {
        this.aa();
        this.a.b((Packet) (new Packet100OpenWindow(this.bH, 1, "Crafting", 9)));
        this.activeContainer = new ContainerWorkbench(this.inventory, this.world, i, j, k);
        this.activeContainer.f = this.bH;
        this.activeContainer.a((ICrafting) this);
    }

    public void a(IInventory iinventory) {
        this.aa();
        this.a.b((Packet) (new Packet100OpenWindow(this.bH, 0, iinventory.c(), iinventory.q_())));
        this.activeContainer = new ContainerChest(this.inventory, iinventory);
        this.activeContainer.f = this.bH;
        this.activeContainer.a((ICrafting) this);
    }

    public void a(TileEntityFurnace tileentityfurnace) {
        this.aa();
        this.a.b((Packet) (new Packet100OpenWindow(this.bH, 2, tileentityfurnace.c(), tileentityfurnace.q_())));
        this.activeContainer = new ContainerFurnace(this.inventory, tileentityfurnace);
        this.activeContainer.f = this.bH;
        this.activeContainer.a((ICrafting) this);
    }

    public void a(TileEntityDispenser tileentitydispenser) {
        this.aa();
        this.a.b((Packet) (new Packet100OpenWindow(this.bH, 3, tileentitydispenser.c(), tileentitydispenser.q_())));
        this.activeContainer = new ContainerDispenser(this.inventory, tileentitydispenser);
        this.activeContainer.f = this.bH;
        this.activeContainer.a((ICrafting) this);
    }

    public void a(Container container, int i, ItemStack itemstack) {
        if (!(container.a(i) instanceof SlotResult)) {
            if (!this.h) {
                this.a.b((Packet) (new Packet103SetSlot(container.f, i, itemstack)));
            }
        }
    }

    public void a(Container container, List list) {
        this.a.b((Packet) (new Packet104WindowItems(container.f, list)));
        this.a.b((Packet) (new Packet103SetSlot(-1, -1, this.inventory.j())));
    }

    public void a(Container container, int i, int j) {
        this.a.b((Packet) (new Packet105CraftProgressBar(container.f, i, j)));
    }

    public void a(ItemStack itemstack) {}

    public void u() {
        this.a.b((Packet) (new Packet101CloseWindow(this.activeContainer.f)));
        this.w();
    }

    public void v() {
        if (!this.h) {
            this.a.b((Packet) (new Packet103SetSlot(-1, -1, this.inventory.j())));
        }
    }

    public void w() {
        this.activeContainer.a((EntityHuman) this);
        this.activeContainer = this.defaultContainer;
    }

    public void a(float f, float f1, boolean flag, boolean flag1, float f2, float f3) {
        this.au = f;
        this.av = f1;
        this.ax = flag;
        this.e(flag1);
        this.pitch = f2;
        this.yaw = f3;
    }

    // Craftbukkit start
    @Override
    public String toString() {
        return super.toString() + "(" + name + " at " + locX + "," + locY + "," + locZ + ")";
    }
    // Craftbukkit end
}
