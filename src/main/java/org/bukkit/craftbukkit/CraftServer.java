package org.bukkit.craftbukkit;

import org.buckit.Config;
import org.buckit.datasource.DataSourceManager;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.SQLitePlatform;
import com.avaje.ebeaninternal.server.lib.sql.TransactionIsolation;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.world.WorldLoadEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.ConsoleReader;
import net.minecraft.server.ChunkCoordinates;
import net.minecraft.server.ConvertProgressUpdater;
import net.minecraft.server.Convertable;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PropertyManager;
import net.minecraft.server.ServerConfigurationManager;
import net.minecraft.server.ServerNBTManager;
import net.minecraft.server.WorldLoaderServer;
import net.minecraft.server.WorldManager;
import net.minecraft.server.WorldServer;
import net.minecraft.server.ServerCommand;
import net.minecraft.server.ICommandListener;
import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.scheduler.CraftScheduler;
import org.bukkit.util.config.Configuration;

public final class CraftServer implements Server {
    private final String serverName = "Craftbukkit";
    private final String serverVersion;
    private final String protocolVersion = "1.4";
    private final DataSourceManager datasourcemanager = new DataSourceManager(this);
    private final PluginManager pluginManager = new SimplePluginManager(this);
    private final BukkitScheduler scheduler =  new CraftScheduler(this);
    private final CommandMap commandMap = new SimpleCommandMap(this);
    protected final MinecraftServer console;
    protected final ServerConfigurationManager server;
    private final Map<String, World> worlds = new LinkedHashMap<String, World>();
    private final Configuration configuration;

    public CraftServer(MinecraftServer console, ServerConfigurationManager server) {
        this.console = console;
        this.server = server;
        this.serverVersion = CraftServer.class.getPackage().getImplementationVersion();

        Bukkit.setServer(this);

        Logger.getLogger("Minecraft").log(Level.INFO, "This server is running " + getName() + " version " + getVersion());

        configuration = new Configuration((File)console.options.valueOf("bukkit-settings"));
        configuration.load();
        loadConfigDefaults();
        configuration.save();
    }

    private void loadConfigDefaults() {
        configuration.getString("database.url", "jdbc:sqlite:{DIR}{NAME}.db");
        configuration.getString("database.username", "bukkit");
        configuration.getString("database.password", "walrus");
        configuration.getString("database.driver", "org.sqlite.JDBC");
        configuration.getString("database.isolation", "SERIALIZABLE");
    }

    public void loadPlugins() {
        pluginManager.registerInterface(JavaPluginLoader.class);

        File pluginFolder = (File)console.options.valueOf("plugins");

        if (pluginFolder.exists()) {
            try {
                Plugin[] plugins = pluginManager.loadPlugins(pluginFolder);
                for (Plugin plugin : plugins) {
                    try {
                        plugin.onLoad();
                    } catch (Throwable ex) {
                        Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, ex.getMessage() + " initializing " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
                    }
                }
                for (Plugin plugin : plugins) {
                    loadPlugin(plugin);
                }
            } catch (Throwable ex) {
                Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, ex.getMessage() + " (Is it up to date?)", ex);
            }
        } else {
            pluginFolder.mkdir();
        }
    }

    public void disablePlugins() {
        pluginManager.disablePlugins();
    }

    private void loadPlugin(Plugin plugin) {
        List<Command> pluginCommands = PluginCommandYamlParser.parse(plugin);

        if (!pluginCommands.isEmpty()) {
            commandMap.registerAll(plugin.getDescription().getName(), pluginCommands);
        }

        try {
            pluginManager.enablePlugin(plugin);
        } catch (Throwable ex) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, ex.getMessage() + " loading " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
        }
    }

    public String getName() {
        return serverName;
    }

    public String getVersion() {
        return serverVersion + " (MC: " + protocolVersion + ")";
    }

    public Player[] getOnlinePlayers() {
        List<EntityPlayer> online = server.b;
        Player[] players = new Player[online.size()];

        for (int i = 0; i < players.length; i++) {
            players[i] = online.get(i).a.getPlayer();
        }

        return players;
    }

    public Player getPlayer(final String name) {
        Player[] players = getOnlinePlayers();

        Player found = null;
        String lowerName = name.toLowerCase();
        int delta = Integer.MAX_VALUE;
        for (Player player : players) {
            if (player.getName().toLowerCase().startsWith(lowerName)) {
                int curDelta = player.getName().length() - lowerName.length();
                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                }
                if (curDelta == 0) break;
            }
        }
        return found;
    }

    public int broadcastMessage(String message) {
        Player[] players = getOnlinePlayers();

        for (Player player : players) {
            player.sendMessage(message);
        }

        return players.length;
    }

    public Player getPlayer(final EntityPlayer entity) {
        return entity.a.getPlayer();
    }

    public List<Player> matchPlayer(String partialName) {
        List<Player> matchedPlayers = new ArrayList<Player>();

        for (Player iterPlayer : this.getOnlinePlayers()) {
            String iterPlayerName = iterPlayer.getName();

            if (partialName.equalsIgnoreCase(iterPlayerName)) {
                // Exact match
                matchedPlayers.clear();
                matchedPlayers.add(iterPlayer);
                break;
            }
            if (iterPlayerName.toLowerCase().indexOf(partialName.toLowerCase()) != -1) {
                // Partial match
                matchedPlayers.add(iterPlayer);
            }
        }

        return matchedPlayers;
    }

    public int getMaxPlayers() {
        return server.e;
    }

    // NOTE: These are dependent on the corrisponding call in MinecraftServer
    // so if that changes this will need to as well
    public int getPort() {
        return this.getConfigInt("server-port", 25565);
    }

    public String getIp() {
        return this.getConfigString("server-ip", "");
    }

    public String getServerName() {
        return this.getConfigString("server-name", "Unknown Server");
    }

    public String getServerId() {
        return this.getConfigString("server-id", "unnamed");
    }

    // NOTE: Temporary calls through to server.properies until its replaced
    private String getConfigString(String variable, String defaultValue) {
        return this.console.d.a(variable, defaultValue);
    }

    private int getConfigInt(String variable, int defaultValue) {
        return this.console.d.a(variable, defaultValue);
    }

    // End Temporary calls

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public BukkitScheduler getScheduler() {
        return scheduler;
    }

    public List<World> getWorlds() {
        return new ArrayList<World>(worlds.values());
    }

    public ServerConfigurationManager getHandle() {
        return server;
    }


    // NOTE: Should only be called from MinecraftServer.b()
    public boolean dispatchCommand(CommandSender sender, ServerCommand serverCommand) {
        if ( commandMap.dispatch(sender, serverCommand.a) ) {
            return true;
        }
        return console.o.a(serverCommand);
    }

    public boolean dispatchCommand(CommandSender sender, String commandLine) {
        // CraftBukkit native commands
        if (commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        if ( ! sender.isOp() ) {
            return false;
        }

        // See if the server can process this command
        return console.o.a(new ServerCommand(commandLine, new CommandListener(sender)));
    }

    public void reload() {
        // Buck - It start
        Config.load();
        getDataSourceManager().reload();
        for(Player p : getOnlinePlayers())
            if(p instanceof CraftPlayer)
                ((CraftPlayer)p).reloadBuckItData();
                
        PropertyManager config = new PropertyManager(console.options);

        console.d = config;

        boolean animals = Config.ANIMALS_ENABLED;
        boolean monsters = Config.MONSTERS_ENABLED;

        console.l = Config.ONLINE_MODE_ENABLED;
        console.m = Config.ANIMALS_ENABLED;
        console.n = Config.PVP_ENABLED;

        // Buck - It end
        
        for (WorldServer world : console.worlds) {
            world.j = monsters ? 1 : 0;
            world.a(monsters, animals);
        }

        pluginManager.clearPlugins();
        commandMap.clearCommands();
        loadPlugins();
    }

    @Override
    public String toString() {
        return "CraftServer{" + "serverName=" + serverName + ",serverVersion=" + serverVersion + ",protocolVersion=" + protocolVersion + '}';
    }

    public World createWorld(String name, World.Environment environment) {
        return createWorld(name, environment, (new Random()).nextLong());
    }

    public World createWorld(String name, World.Environment environment, long seed) {
        File folder = new File(name);
        World world = getWorld(name);

        if (world != null) {
            return world;
        }

        if ((folder.exists()) && (!folder.isDirectory())) {
            throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
        }

        Convertable converter = new WorldLoaderServer(folder);
        if (converter.a(name)) {
            getLogger().info("Converting world '" + name + "'");
            converter.a(name, new ConvertProgressUpdater(console));
        }

        WorldServer internal = new WorldServer(console, new ServerNBTManager(new File("."), name, true), name, environment == World.Environment.NETHER ? -1 : 0, seed);

        internal.a(new WorldManager(console, internal));
        internal.j = 1;
        internal.a(true, true);
        console.f.a(internal);
        console.worlds.add(internal);

        short short1 = 196;
        long i = System.currentTimeMillis();
        for (int j = -short1; j <= short1; j += 16) {
            for (int k = -short1; k <= short1; k += 16) {
                long l = System.currentTimeMillis();

                if (l < i) {
                    i = l;
                }

                if (l > i + 1000L) {
                    int i1 = (short1 * 2 + 1) * (short1 * 2 + 1);
                    int j1 = (j + short1) * (short1 * 2 + 1) + k + 1;

                    System.out.println("Preparing spawn area for " + name + ", " + (j1 * 100 / i1) + "%");
                    i = l;
                }

                ChunkCoordinates chunkcoordinates = internal.m();
                internal.u.c(chunkcoordinates.a + j >> 4, chunkcoordinates.c + k >> 4);

                while (internal.f()) {
                    ;
                }
            }
        }
        pluginManager.callEvent(new WorldLoadEvent(internal.getWorld()));
        return internal.getWorld();
    }

    public MinecraftServer getServer() {
        return console;
    }

    public World getWorld(String name) {
        return worlds.get(name.toLowerCase());
    }

    protected void addWorld(World world) {
        worlds.put(world.getName().toLowerCase(), world);
    }

    
    
    @Override
    public DataSourceManager getDataSourceManager() {
        return datasourcemanager;
    }
    
    @Override
    public Map<String, Command> getCommands() {
        return commandMap.getCommands();
    }

    public Logger getLogger() {
        return MinecraftServer.a;
    }

    public ConsoleReader getReader() {
        return console.reader;
    }

    public PluginCommand getPluginCommand(String name) {
        Command command = commandMap.getCommand(name);

        if (command instanceof PluginCommand) {
            return (PluginCommand)command;
        } else {
            return null;
        }
    }

    public void savePlayers() {
        server.d();
    }

    public void configureDbConfig(ServerConfig config) {
        DataSourceConfig ds = new DataSourceConfig();
        ds.setDriver(configuration.getString("database.driver"));
        ds.setUrl(configuration.getString("database.url"));
        ds.setUsername(configuration.getString("database.username"));
        ds.setPassword(configuration.getString("database.password"));
        ds.setIsolationLevel(TransactionIsolation.getLevel(configuration.getString("database.isolation")));

        if (ds.getDriver().contains("sqlite")) {
            config.setDatabasePlatform(new SQLitePlatform());
            config.getDatabasePlatform().getDbDdlSyntax().setIdentity("");
        }

        config.setDataSourceConfig(ds);
    }

    // Inner class to capture the output of default server commands
    class CommandListener implements ICommandListener {
        private final CommandSender commandSender;
        private final String prefix;
        CommandListener(CommandSender commandSender) {
            this.commandSender = commandSender;
            String[] parts = commandSender.getClass().getName().split( "\\." );
            this.prefix = parts[parts.length-1];
        }

        public void b(String msg) {
            this.commandSender.sendMessage(msg);
        }

        public String c() {
            return this.prefix;
        }
    }
}
