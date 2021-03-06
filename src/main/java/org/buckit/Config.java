package org.buckit;

import java.io.File;
import java.util.logging.Logger;

import org.buckit.datasource.DataType;
import org.buckit.util.Properties;

public class Config {

    // --------------------------------------------------
    // Property File Definitions
    // --------------------------------------------------
    public static final String GENERAL_CONFIG_FILE  = "./config/General.properties";
    public static final String DATABASE_CONFIG_FILE = "./config/Database.properties";
    public static final String FLATFILE_CONFIG_FILE = "./config/Flatfile.properties";
    public static final String WORLD_CONFIG_FILE    = "./config/World.properties";
    public static final String EXPERT_CONFIG_FILE   = "./config/Expert.properties";
    public static final String HELL_CONFIG_FILE     = "./config/Hell.properties";
    
    // --------------------------------------------------
    // General Properties
    // --------------------------------------------------
    
    public static int          PLAYER_LIMIT;
    public static boolean      ONLINE_MODE_ENABLED;
    public static boolean      ANIMALS_ENABLED;
    public static boolean      MONSTERS_ENABLED;
    public static boolean      HEALTH_ENABLED;
    public static boolean      PVP_ENABLED;
    public static String       SERVER_IP;
    public static int          SERVER_PORT;
    
    public static int          SPAWN_RADIUS;
    public static int          SPAWN_RESPAWN_AREA_RADIUS;

    public static DataType     DATA_SOURCE_TYPE;

    public static boolean      DURABILITY_ENABLED;
    public static boolean      DURABILITY_FORCE_UPDATE;
    
    public static boolean      TP_REQUEST_COMMANDS_ENABLED;
    public static boolean      KITS_ENABLED;
    public static boolean      HOMES_ENABLED;
    public static boolean      HOMES_MULTI_ENABLED;
    public static boolean      WARPS_ENABLED;
    public static boolean      WARPS_GROUPS_ENABLED;
    public static String       WARPS_DEFAULT_GROUP_NAME;
    public static boolean      WHITELIST_ENABLED;
    public static boolean      RESERVELIST_ENABLED;
    
    public static String       WHITELIST_MESSAGE;
    public static String       RESERVELIST_MESSAGE;
    
    public static String       DEFAULT_USER_FORMAT;
    public static String       DEFAULT_CHAT_FORMAT;
    
    public static int          DEFAULT_ACCESS_LEVEL;
    
	public static String	   DEFAULT_ERROR_COLOR;
	public static String       DEFAULT_INFO_COLOR;
	
    public static boolean      TRACK_USER_ONLINE_TIME;
    public static String       NOT_ENOUGH_ACCESS_MESSAGE;
    public static boolean      LIMIT_BUILD_BY_BUILD_FLAG;
    public static boolean      SEND_MOTD_ON_LOGIN;
    
    public static boolean      PERMISSIONS_COMPATIBILITY_ENABLED;
    // --------------------------------------------------
    // Expert Properties
    // --------------------------------------------------
    public static String       DATABASE_DELIMITER;
    public static String       DATABASE_SEPERATOR;
    public static String       FULL_ACCESS_STRING;

    // --------------------------------------------------
    // Hell Properties
    // --------------------------------------------------
    public static boolean       HELL_ENABLED;
    public static String        HELL_DIRECTORY;
    public static boolean       HELL_EMULATE_GATES;
    
    public static int           HELL_LIGHTSTONE_OCCURANCE;
    public static int           HELL_RED_MUSHROOM_OCCURANCE;
    public static int           HELL_BROWN_MUSHROOM_OCCURANCE;
    public static int           HELL_FIRE_OCCURANCE;
    public static int           HELL_RANDOM_LAVA_OCCURANCE;
    
    // --------------------------------------------------
    // Database Properties
    // --------------------------------------------------
    public static int          DATABASE_MAX_CONNECTIONS;
    public static int          DATABASE_MAX_IDLE_TIME;
    public static String       DATABASE_DRIVER;
    public static String       DATABASE_URL;
    public static String       DATABASE_LOGIN;
    public static String       DATABASE_PASSWORD;

    public static String       DATABASE_USERS_TABLE;
    public static String       DATABASE_WARPS_TABLE;
    public static String       DATABASE_HOMES_TABLE;
    public static String       DATABASE_KITS_TABLE;
    public static String       DATABASE_KITS_DELAY_TABLE;
    public static String       DATABASE_ACCESS_TABLE;
    public static String       DATABASE_ACCESSGROUPS_TABLE;
    public static String       DATABASE_WHITELIST_TABLE;
    public static String       DATABASE_RESERVELIST_TABLE;

    

    // --------------------------------------------------
    // FlatFile Properties
    // --------------------------------------------------
    
    public static String FLATFILE_USERS_FILE;
    public static String FLATFILE_WARPS_FILE;
    public static String FLATFILE_HOMES_DIRECTORY;
    public static String FLATFILE_KITS_FILE;
    public static String FLATFILE_KITS_DELAY_FILE;
    public static String FLATFILE_ACCESS_FILE;
    public static String FLATFILE_ACCESSGROUPS_FILE;
    public static String FLATFILE_WHITELIST_FILE;
    public static String FLATFILE_RESERVELIST_FILE;
    
    
    // --------------------------------------------------
    // World Properties
    // --------------------------------------------------
    public static int          WORLD_GRAVEL_OCCURENCE;
    public static int          WORLD_DIRT_OCCURENCE;

    public static int          WORLD_ORE_COAL_OCCURENCE;
    public static int          WORLD_ORE_IRON_OCCURENCE;
    public static int          WORLD_ORE_GOLD_OCCURENCE;
    public static int          WORLD_ORE_DIAMOND_OCCURENCE;
    public static int          WORLD_ORE_REDSTONE_OCCURENCE;
    public static int          WORLD_ORE_LAPISLAZULI_OCCURENCE;

    public static int          WORLD_GRAVEL_SIZE;
    public static int          WORLD_DIRT_SIZE;

    public static int          WORLD_ORE_COAL_SIZE;
    public static int          WORLD_ORE_IRON_SIZE;
    public static int          WORLD_ORE_GOLD_SIZE;
    public static int          WORLD_ORE_DIAMOND_SIZE;
    public static int          WORLD_ORE_REDSTONE_SIZE;
    public static int          WORLD_ORE_LAPISLAZULI_SIZE;

    public static int          WORLD_DUNGEONS_OCCURENCE;

    public static int          WORLD_CLAY_OCCURENCE;
    public static int          WORLD_CLAY_SIZE;

    public static int          WORLD_CACTUS_OCCURENCE;

    public static int          WORLD_PUMPKIN_OCCURENCE;

    public static int          WORLD_REED_OCCURENCE;

    public static int          WORLD_YELLOW_FLOWER_OCCURENCE;
    public static int          WORLD_RED_FLOWER_OCCURENCE;

    public static int          WORLD_BROWN_MUSHROOM_OCCURENCE;
    public static int          WORLD_RED_MUSHROOM_OCCURENCE;

    public static int          WORLD_NOISE_1;
    public static int          WORLD_NOISE_2;
    public static int          WORLD_NOISE_3;
    public static int          WORLD_NOISE_4;
    public static int          WORLD_NOISE_5;
    public static int          WORLD_NOISE_6;
    public static int          WORLD_NOISE_7;
    public static int          WORLD_NOISE_8;
    
    
    private static Logger      log                  = Logger.getLogger(Properties.class.getName());

    public static void load() {
        loadGeneralProperties();
        loadWorldProperties();
        switch(DATA_SOURCE_TYPE){
            case DATABASE:
                loadDatabaseProperties();
                break;
            case FLATFILE:
                loadFlatFileProperties();
                break;
            case YAML:
                
                break;
        }
        loadExpertProperties();
        loadHellProperties();
    }

    private static void loadGeneralProperties() {
        try {
            Properties gp = new Properties(new File(GENERAL_CONFIG_FILE));
            
            PLAYER_LIMIT = gp.getInt("PlayerLimit", 20, 0);
            ONLINE_MODE_ENABLED = gp.getBool("OnlineMode", true);
            ANIMALS_ENABLED = gp.getBool("SpawnAnimals", true);
            MONSTERS_ENABLED = gp.getBool("SpawnMonsters", true);
            HEALTH_ENABLED = gp.getBool("HealthEnabled", true);
            PVP_ENABLED = gp.getBool("PvpEnabled", true);
            SERVER_IP = gp.getProperty("ServerIP", "");
            SERVER_PORT = gp.getInt("ServerPort", 25565,1,65535);
            
            SPAWN_RADIUS = gp.getInt("SpawnRadius", 16);
            SPAWN_RESPAWN_AREA_RADIUS = gp.getInt("RespawnAreaRadius", "10");

            DURABILITY_ENABLED = gp.getBool("DurabilityEnabled", true);
            DURABILITY_FORCE_UPDATE = gp.getBool("DurabilityForceUpdate", false);
            
            String datasource = gp.getProperty("DataSource", "database");
            if(datasource.equalsIgnoreCase("flatfile"))DATA_SOURCE_TYPE = DataType.FLATFILE;
            else if(datasource.equalsIgnoreCase("database"))DATA_SOURCE_TYPE = DataType.DATABASE;
            else if(datasource.equalsIgnoreCase("yaml"))DATA_SOURCE_TYPE = DataType.YAML;
            
            KITS_ENABLED = gp.getBool("KitsEnabled", "false");

            HOMES_ENABLED = gp.getBool("HomesEnabled", "true");
            HOMES_MULTI_ENABLED = gp.getBool("MultiHomesEnabled", "false");

            WARPS_ENABLED = gp.getBool("WarpsEnabled", "true");
            WARPS_GROUPS_ENABLED = gp.getBool("WarpsGroupsEnabled", "false");
            WARPS_DEFAULT_GROUP_NAME = gp.getProperty("WarpsDefaultGroup","default");
            
            WHITELIST_ENABLED = gp.getBool("WhiteListEnabled", false);
            RESERVELIST_ENABLED = gp.getBool("ReserveListEnabled", false);
            
            WHITELIST_MESSAGE = gp.getProperty("WhiteListMessage","Not on whitelist.");
            RESERVELIST_MESSAGE = gp.getProperty("ReserveListMessage","The server is full!");
            
            TP_REQUEST_COMMANDS_ENABLED = gp.getBool("TpRequestEnabled", false);
            
            DEFAULT_USER_FORMAT = gp.getProperty("DefaultUserFormat","{$username}");
            DEFAULT_CHAT_FORMAT = gp.getProperty("DefaultChatFormat","<{$usernameformat}> {$message}");
            DEFAULT_ACCESS_LEVEL = gp.getInt("DefaultAccessLevel", 0);
            
            DEFAULT_ERROR_COLOR = gp.getProperty("DefaultErrorColor","^C").replace("^", "\u00A7");
            DEFAULT_INFO_COLOR = gp.getProperty("DefaultInfoColor","^A").replace("^", "\u00A7");
            
            TRACK_USER_ONLINE_TIME = gp.getBool("TrackUserOnlineTime", "true");
            NOT_ENOUGH_ACCESS_MESSAGE = gp.getProperty("AccessMessage", "Your are not allowed to use this command.");
            
            LIMIT_BUILD_BY_BUILD_FLAG = gp.getBool("LimitBuildByFlag", true);
            SEND_MOTD_ON_LOGIN = gp.getBool("MotdOnLogin", true);
            
            PERMISSIONS_COMPATIBILITY_ENABLED = gp.getBool("PermissionsCompatibilityEnabled", false);
            
        } catch (Exception e) {
            e.printStackTrace();
            log.warning("Config: Error loading General properties file.");
        }
    }

    private static void loadWorldProperties() {
        try {
            Properties wp = new Properties(new File(WORLD_CONFIG_FILE));

            WORLD_GRAVEL_OCCURENCE = wp.getInt("GravelOccurence", 10,0,100);
            WORLD_DIRT_OCCURENCE = wp.getInt("DirtOccurence", 10,0,100);
            
            WORLD_ORE_COAL_OCCURENCE = wp.getInt("CoalOccurence", 20,0,100);
            WORLD_ORE_IRON_OCCURENCE = wp.getInt("IronOccurence", 20,0,100);
            WORLD_ORE_GOLD_OCCURENCE = wp.getInt("GoldOccurence", 2,0,100);
            WORLD_ORE_DIAMOND_OCCURENCE = wp.getInt("DiamondOccurence", 1,0,100);
            WORLD_ORE_REDSTONE_OCCURENCE = wp.getInt("RedstoneOccurence", 8,0,100);
            WORLD_ORE_LAPISLAZULI_OCCURENCE = wp.getInt("LapisLazuliOccurence", 1,0,100);
            
            WORLD_GRAVEL_SIZE = wp.getInt("GravelSize", 33,0,100)-1;
            WORLD_DIRT_SIZE = wp.getInt("DirtSize", 33,0,100)-1;
            
            WORLD_ORE_COAL_SIZE = wp.getInt("CoalSize", 17,0,100)-1;
            WORLD_ORE_IRON_SIZE = wp.getInt("IronSize", 9,0,100)-1;
            WORLD_ORE_GOLD_SIZE = wp.getInt("GoldSize", 9,0,100)-1;
            WORLD_ORE_DIAMOND_SIZE = wp.getInt("DiamondSize", 8,0,100)-1;
            WORLD_ORE_REDSTONE_SIZE = wp.getInt("RedstoneSize", 8,0,100)-1;
            WORLD_ORE_LAPISLAZULI_SIZE = wp.getInt("LapisLazuliSize", 7,0,100)-1;
            
            WORLD_DUNGEONS_OCCURENCE = wp.getInt("DungeonOccurence", 8,0,20);
            
            WORLD_CLAY_OCCURENCE = wp.getInt("ClayOccurence", 10,0,100);
            WORLD_CLAY_SIZE = wp.getInt("ClaySize", 32,0,100);
            
            WORLD_CACTUS_OCCURENCE = wp.getInt("CactusOccurence", 10,0,100);
            
            WORLD_PUMPKIN_OCCURENCE = wp.getInt("PumpkinOccurence", 1,0,32);
            
            WORLD_REED_OCCURENCE = wp.getInt("ReedOccurence", 10,0,100);
            
            WORLD_YELLOW_FLOWER_OCCURENCE = wp.getInt("YellowFlowerOccurence", 2,0,100);
            WORLD_RED_FLOWER_OCCURENCE = wp.getInt("RedFlowerOccurence", 1,0,2);
            
            WORLD_BROWN_MUSHROOM_OCCURENCE = wp.getInt("BrownMushroomOccurence", 1,0,4);
            WORLD_RED_MUSHROOM_OCCURENCE = wp.getInt("RedMushroomOccurence", 1,0,8);
            
            WORLD_NOISE_1 = wp.getInt("WorldNoise1", 16,1,50);
            WORLD_NOISE_2 = wp.getInt("WorldNoise2", 16,1,50);
            WORLD_NOISE_3 = wp.getInt("WorldNoise3", 8,1,50);
            WORLD_NOISE_4 = wp.getInt("WorldNoise4", 4,1,50);
            WORLD_NOISE_5 = wp.getInt("WorldNoise5", 4,1,50);
            WORLD_NOISE_6 = wp.getInt("WorldNoise6", 10,1,50);
            WORLD_NOISE_7 = wp.getInt("WorldNoise7", 16,1,50);
            WORLD_NOISE_8 = wp.getInt("WorldNoise8", 8,1,50);
            
        } catch (Exception e) {
            e.printStackTrace();
            log.warning("Config: Error loading World properties file.");
        }
    }
    private static void loadDatabaseProperties() {
        try {
            Properties dp = new Properties(new File(DATABASE_CONFIG_FILE));
            DATABASE_MAX_CONNECTIONS = dp.getInt("MaximumDbConnections", 100);
            DATABASE_MAX_IDLE_TIME = dp.getInt("MaximumDbIdleTime", 0);
            DATABASE_DRIVER = dp.getProperty("Driver", "com.mysql.jdbc.Driver");
            DATABASE_URL = dp.getProperty("URL", "jdbc:mysql://localhost/Minecraft");
            DATABASE_LOGIN = dp.getProperty("Login", "root");
            DATABASE_PASSWORD = dp.getProperty("Password", "");
            
            DATABASE_USERS_TABLE = dp.getProperty("UsersTable", "users");
            DATABASE_WARPS_TABLE = dp.getProperty("WarpsTable", "warps");
            DATABASE_HOMES_TABLE = dp.getProperty("HomesTable", "homes");
            DATABASE_KITS_TABLE = dp.getProperty("KitsTable", "kits");
            DATABASE_KITS_DELAY_TABLE = dp.getProperty("KitsDelayTable","kits_delay");
            DATABASE_ACCESS_TABLE = dp.getProperty("AccessTable", "accesslevels");
            DATABASE_ACCESSGROUPS_TABLE = dp.getProperty("AccessGroupsTable", "accessgroups");
            DATABASE_RESERVELIST_TABLE = dp.getProperty("ReserveListTable","reservelist");
            DATABASE_WHITELIST_TABLE = dp.getProperty("WhiteListTable","whitelist");
        } catch (Exception e) {
            e.printStackTrace();
            log.warning("Config: Error loading Database properties file.");
        }
    }
    private static void loadFlatFileProperties() {
        try{
            Properties fp = new Properties(new File(FLATFILE_CONFIG_FILE));
            FLATFILE_USERS_FILE = fp.getProperty("UsersFile", "./flatfile/users.txt");
            FLATFILE_WARPS_FILE = fp.getProperty("WarpsFile", "./flatfile/warps.txt");
            FLATFILE_HOMES_DIRECTORY = fp.getProperty("HomesDirectory", "./flatfile/homes/");
            FLATFILE_KITS_FILE = fp.getProperty("KitsFile", "./flatfile/kits.txt");
            FLATFILE_KITS_DELAY_FILE = fp.getProperty("KitsDelayFile","./flatfile/kits_delay.txt");
            FLATFILE_ACCESS_FILE = fp.getProperty("AccessFile", "./flatfile/accesslevels.txt");
            FLATFILE_ACCESSGROUPS_FILE = fp.getProperty("AccessGroupsFile", "./flatfile/accessgroups.txt");
            FLATFILE_WHITELIST_FILE = fp.getProperty("WhiteListFile","./flatfile/whitelist.txt");
            FLATFILE_RESERVELIST_FILE = fp.getProperty("ReserveListFile","./flatfile/reservelist.txt");
        } catch (Exception e) {
            e.printStackTrace();
            log.warning("Config: Error loading World properties file.");
        }
    }

    private static void loadExpertProperties() {
        try {
            Properties ep = new Properties(new File(EXPERT_CONFIG_FILE));
            DATABASE_DELIMITER = ep.getProperty("DatabaseDelimiter", ",");
            DATABASE_SEPERATOR = ep.getProperty("DatabaseSeperator", ";");
            FULL_ACCESS_STRING = ep.getProperty("FullAccessString", "*");
        } catch (Exception e) {
            e.printStackTrace();
            log.warning("Config: Error loading Expert properties file.");
        }
    }
 
    private static void loadHellProperties() {
        try {
            Properties hp = new Properties(new File(HELL_CONFIG_FILE));
            HELL_ENABLED = hp.getBool("HellEnabled", true);
            HELL_DIRECTORY = hp.getProperty("HellDirectory", "Nether");
            HELL_EMULATE_GATES = hp.getBool("HellEmulateGates", false);
            HELL_LIGHTSTONE_OCCURANCE = hp.getInt("HellLightstoneOccurance", 10, 0, 100);
            HELL_RED_MUSHROOM_OCCURANCE = hp.getInt("HellRedMushroomOccurance", 1, 0, 100);
            HELL_BROWN_MUSHROOM_OCCURANCE = hp.getInt("HellBrownMushroomOccurance", 1, 0, 100);
            HELL_FIRE_OCCURANCE = hp.getInt("HellFireOccurance", 10, 0, 100);
            HELL_RANDOM_LAVA_OCCURANCE = hp.getInt("HellRandomLavaOccurance", 10, 0, 100);
            
        } catch (Exception e) {
            e.printStackTrace();
            log.warning("Config: Error loading Hell properties file.");
        }
        
    }
}
