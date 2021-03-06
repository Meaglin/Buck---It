package net.minecraft.server;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

// CraftBukkit start
import java.io.PrintStream;
import java.net.UnknownHostException;
import jline.ConsoleReader;
import joptsimple.OptionSet;

import org.buckit.Config;
import org.bukkit.World.Environment;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.LoggerOutputStream;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;
import org.bukkit.craftbukkit.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.util.ServerShutdownThread;
import org.bukkit.event.world.WorldSaveEvent;


public class MinecraftServer implements Runnable, ICommandListener {

    public static Logger a = Logger.getLogger("Minecraft");
    public static HashMap b = new HashMap();
    public NetworkListenThread c;
    public PropertyManager d;
    // public WorldServer e; // CraftBukkit - removed
    public ServerConfigurationManager f;
    public ConsoleCommandHandler o; // CraftBukkit - made public
    private boolean p = true;
    public boolean g = false;
    int h = 0;
    public String i;
    public int j;
    private List q = new ArrayList();
    private List r = Collections.synchronizedList(new ArrayList());
    public EntityTracker k;
    public boolean l;
    public boolean m;
    public boolean n;

    // CraftBukkit start
    public int spawnProtection;
    public List<WorldServer> worlds = new ArrayList<WorldServer>();
    public CraftServer server;
    public OptionSet options;
    public ColouredConsoleSender console;
    public ConsoleReader reader;
    // Craftbukkit end

    public MinecraftServer(OptionSet options) { // CraftBukkit - adds argument OptionSet
        new ThreadSleepForever(this);

         // CraftBukkit start
        this.options = options;
        try {
            this.reader = new ConsoleReader();
        } catch (IOException ex) {
            Logger.getLogger(MinecraftServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        Runtime.getRuntime().addShutdownHook(new ServerShutdownThread(this));
        // CraftBukkit end
    }

    private boolean d() throws UnknownHostException { // CraftBukkit - added throws UnknownHostException
        this.o = new ConsoleCommandHandler(this);
        ThreadCommandReader threadcommandreader = new ThreadCommandReader(this);

        threadcommandreader.setDaemon(true);
        threadcommandreader.start();
        ConsoleLogManager.a(this); // Craftbukkit

        // CraftBukkit start
        System.setOut(new PrintStream(new LoggerOutputStream(a, Level.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(a, Level.SEVERE), true));
        // CraftBukkit end

        a.info("Starting minecraft server version Beta 1.4");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            a.warning("**** NOT ENOUGH RAM!");
            a.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        a.info("Loading properties");
        this.d = new PropertyManager(options); // CraftBukkit
        String s = Config.SERVER_IP;

        this.l = Config.ONLINE_MODE_ENABLED;
        this.m = Config.ANIMALS_ENABLED;
        this.n = Config.PVP_ENABLED;
        //this.spawnProtection = this.d.a("spawn-protection", 16); // CraftBukkit Configurable spawn protection start
        InetAddress inetaddress = null;

        if (s.length() > 0) {
            inetaddress = InetAddress.getByName(s);
        }

        int i = Config.SERVER_PORT;

        a.info("Starting Minecraft server on " + (s.length() == 0 ? "*" : s) + ":" + i);

        try {
            this.c = new NetworkListenThread(this, inetaddress, i);
        } catch (Throwable ioexception) { // CraftBukkit - IOException -> Throwable
            a.warning("**** FAILED TO BIND TO PORT!");
            a.log(Level.WARNING, "The exception was: " + ioexception.toString());
            a.warning("Perhaps a server is already running on that port?");
            return false;
        }

        if (!this.l) {
            a.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            a.warning("The server will make no attempt to authenticate usernames. Beware.");
            a.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            a.warning("To change this, set \"online-mode\" to \"true\" in the server.settings file.");
        }

        this.f = new ServerConfigurationManager(this);
        this.k = new EntityTracker(this);
        long j = System.nanoTime();
        String s1 = this.d.a("level-name", "world");
        String s2 = this.d.a("level-seed", "");
        long k = (new Random()).nextLong();

        if (s2.length() > 0) {
            try {
                k = Long.parseLong(s2);
            } catch (NumberFormatException numberformatexception) {
                k = (long) s2.hashCode();
            }
        }

        a.info("Preparing level \"" + s1 + "\"");
        this.a(new WorldLoaderServer(new File(".")), s1, k);

        // CraftBukkit start
        long elapsed = System.nanoTime() - j;
        String time = String.format( "%.3fs", elapsed / 10000000000.0D );
        a.info("Done (" + time + ")! For help, type \"help\" or \"?\"");
        // CraftBukkit end

        return true;
    }

    private void a(Convertable convertable, String s, long i) {
        if (convertable.a(s)) {
            a.info("Converting map!");
            convertable.a(s, new ConvertProgressUpdater(this));
        }

        a.info("Preparing start region");

        // CraftBukkit start
        WorldServer world = new WorldServer(this, new ServerNBTManager(new File("."), s, true), s, this.d.a("hellworld", false) ? -1 : 0, i);
        world.a(new WorldManager(this, world));
        world.j =   Config.MONSTERS_ENABLED ? 1 : 0;
        world.a(Config.MONSTERS_ENABLED, this.m);
        this.f.a(world);
        worlds.add(world);
        // CraftBukkit end

        short short1 = 196;
        long j = System.currentTimeMillis();
        ChunkCoordinates chunkcoordinates = worlds.get(0).m(); // CraftBukkit

        for (int k = -short1; k <= short1 && this.p; k += 16) {
            for (int l = -short1; l <= short1 && this.p; l += 16) {
                long i1 = System.currentTimeMillis();

                if (i1 < j) {
                    j = i1;
                }

                if (i1 > j + 1000L) {
                    int j1 = (short1 * 2 + 1) * (short1 * 2 + 1);
                    int k1 = (k + short1) * (short1 * 2 + 1) + l + 1;

                    this.a("Preparing spawn area", k1 * 100 / j1);
                    j = i1;
                }

                // CraftBukkit start
                for (WorldServer worldserver: worlds) {
                    world.u.c(chunkcoordinates.a + k >> 4, chunkcoordinates.c + l >> 4);

                    while (world.f() && this.p) {
                        ;
                    }
                }
                // CraftBukkit end
            }
        }
        
        this.e();
    }

    private void a(String s, int i) {
        this.i = s;
        this.j = i;
        a.info(s + ": " + i + "%");
    }

    private void e() {
        this.i = null;
        this.j = 0;

        
        server.loadPlugins(); // CraftBukkit
        
        // Buck - It start
        if(Config.HELL_ENABLED) {
            server.createWorld(Config.HELL_DIRECTORY, Environment.NETHER);
        }
        // Buck - It end
    }

    void f() { // CraftBukkit - private -> default
        a.info("Saving chunks");

        // CraftBukkit start
        for (WorldServer world: worlds) {
            world.a(true, (IProgressUpdate) null);
            world.t();

            WorldSaveEvent event = new WorldSaveEvent( world.getWorld() );
            server.getPluginManager().callEvent( event );
        }

        this.f.d(); // CraftBukkit - player data should be saved whenever a save happens.
        // CraftBukkit end
    }

    public void g() { // Craftbukkit: private -> public
        a.info("Stopping server");
        // CraftBukkit start
        if (server != null) {
            server.disablePlugins();
        }
        // CraftBukkit end

        if (this.f != null) {
            this.f.d();
        }

        if (this.worlds.size() > 0) { // CraftBukkit
            this.f();
        }
    }

    public void a() {
        this.p = false;
    }

    public void run() {
        try {
            if (this.d()) {
                long i = System.currentTimeMillis();

                for (long j = 0L; this.p; Thread.sleep(1L)) {
                    long k = System.currentTimeMillis();
                    long l = k - i;

                    if (l > 2000L) {
                        a.warning("Can\'t keep up! Did the system time change, or is the server overloaded?");
                        l = 2000L;
                    }

                    if (l < 0L) {
                        a.warning("Time ran backwards! Did the system time change?");
                        l = 0L;
                    }

                    j += l;
                    i = k;
                    // CraftBukkit - TODO - Replace with loop?
                    if (this.worlds.size() > 0 && this.worlds.get(0).s()) {
                        this.h();
                        j = 0L;
                    } else {
                        while (j > 50L) {
                            j -= 50L;
                            this.h();
                        }
                    }
                }
            } else {
                while (this.p) {
                    this.b();

                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException interruptedexception) {
                        interruptedexception.printStackTrace();
                    }
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            a.log(Level.SEVERE, "Unexpected exception", throwable);

            while (this.p) {
                this.b();

                try {
                    Thread.sleep(10L);
                } catch (InterruptedException interruptedexception1) {
                    interruptedexception1.printStackTrace();
                }
            }
        } finally {
            try {
                this.g();
                this.g = true;
            } catch (Throwable throwable1) {
                throwable1.printStackTrace();
            } finally {
                System.exit(0);
            }
        }
    }

    private void h() {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = b.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            int i = ((Integer) b.get(s)).intValue();

            if (i > 0) {
                b.put(s, Integer.valueOf(i - 1));
            } else {
                arraylist.add(s);
            }
        }

        int j;

        for (j = 0; j < arraylist.size(); ++j) {
            b.remove(arraylist.get(j));
        }

        AxisAlignedBB.a();
        Vec3D.a();
        ++this.h;

        // CraftBukkit start
        if (this.h % 20 == 0) {
            for (int i = 0; i < this.f.b.size(); ++i) {
                EntityPlayer entityplayer = (EntityPlayer) this.f.b.get(i);
                entityplayer.a.b((Packet) (new Packet4UpdateTime(entityplayer.world.l())));
            }
        }

        ((CraftScheduler) server.getScheduler()).mainThreadHeartbeat(this.h);

        for (WorldServer world: worlds) {
            world.h();

            while (world.f()) {
                ;
            }

            world.e();
        }
        // CraftBukkit end
        this.c.a();
        this.f.b();
        this.k.a();

        for (j = 0; j < this.q.size(); ++j) {
            ((IUpdatePlayerListBox) this.q.get(j)).a();
        }

        try {
            this.b();
        } catch (Exception exception) {
            a.log(Level.WARNING, "Unexpected exception while parsing console command", exception);
        }
    }

    public void a(String s, ICommandListener icommandlistener) {
        this.r.add(new ServerCommand(s, icommandlistener));
    }

    public void b() {
        while (this.r.size() > 0) {
            ServerCommand servercommand = (ServerCommand) this.r.remove(0);

            server.dispatchCommand(console, servercommand); // CraftBukkit
            // this.o.a(servercommand); // CraftBukkit - Removed its now called in server.displatchCommand
        }
    }

    public void a(IUpdatePlayerListBox iupdateplayerlistbox) {
        this.q.add(iupdateplayerlistbox);
    }

    public static void main(final OptionSet options) { // CraftBukkit - replaces main(String args[])
        try {
            Config.load();//Buck - It
            MinecraftServer minecraftserver = new MinecraftServer(options);

            // CraftBukkit - remove gui

            (new ThreadServerApplication("Server thread", minecraftserver)).start();
        } catch (Exception exception) {
            a.log(Level.SEVERE, "Failed to start the minecraft server", exception);
        }
    }

    public File a(String s) {
        return new File(s);
    }

    public void b(String s) {
        a.info(s);
    }

    public void c(String s) {
        a.warning(s);
    }

    public String c() {
        return "CONSOLE";
    }

    public static boolean a(MinecraftServer minecraftserver) {
        return minecraftserver.p;
    }
}
