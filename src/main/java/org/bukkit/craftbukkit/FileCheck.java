package org.bukkit.craftbukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import c.m;

public class FileCheck {

    private static String minecraft_server = "./minecraft_server.jar";
    private static String minecraft_servero = "./minecraft_servero.jar";
    private static String temp_file = "deleteme.jar";
    private static String temp2_file = "deleteme2.jar";
    private static String temp3_file = "deleteme.log";
    
    private static String jarjar = "./libs/jarjar.jar";
    private static String retroguard = "./libs/retroguard.jar";
    
    private static String jarjar_rules = "./libs/rules/1.2.rules";
    private static String retroguard_rules = "./libs/rules/1.2_01.rgs";
    private static String jarjar_namespace_rules = "./libs/rules/namespace.rules";
    
    private static long crc_minecraft_server = 280458825L;
    private static long crc_minecraft_servero = 2186524365L;
    
    private static long crc_jarjar = 519567578L;
    private static long crc_retroguard = 2728595211L;
    
    private static long crc_jarjar_rules = 149905660L;
    private static long crc_retroguard_rules = 3002032626L;
    private static long crc_jarjar_namespace_rules = 4187131396L;
    
    public static void checkAllFiles() throws IOException {
        if(!fileExists(minecraft_servero)) {
            if (!fileExists(jarjar) || !fileExists(retroguard) || !fileExists(jarjar_rules) || !fileExists(retroguard_rules) || !fileExists(jarjar_namespace_rules)) {
                log("-----------------------------");
                log("Some of the tools to make a normalized minecraft jar (aka minecraft_servero.jar) are missing!");
                log("Without these files or a proper minecraft_servero.jar the server cannot run!");
                log("-----------------------------");
                System.exit(0);
            }
            checkCRC32(jarjar, crc_jarjar);
            checkCRC32(retroguard, crc_retroguard);
            checkCRC32(jarjar_rules, crc_jarjar_rules);
            checkCRC32(retroguard_rules, crc_retroguard_rules);
            checkCRC32(jarjar_namespace_rules, crc_jarjar_namespace_rules);
            
            if (!fileExists(minecraft_server)) {
                log("Missing minecraft_servero.jar, Downloading minecraft_server.jar...");
                downloadFile("http://minecraft.net/download/minecraft_server.jar", minecraft_server);
                checkCRC32(minecraft_server, crc_minecraft_server);

                log("Finished downloading minecraft_server.jar, start converting minecraft_server.jar to minecraft_servero.jar...");
            } else
                log("Missing minecraft_servero.jar, start converting minecraft_server.jar to minecraft_servero.jar...");
            
            try {
                com.tonicsystems.jarjar.Main.main(new String[] { "process", jarjar_rules, minecraft_server, temp_file });
            } catch (Throwable t) {
                t.printStackTrace();
            }
            log("Class files renamed, 30% done ..." );
            try {
                com.tonicsystems.jarjar.Main.main(new String[] { "process", jarjar_namespace_rules, temp_file, temp2_file });
            } catch (Throwable t) {
                t.printStackTrace();
            }
            log("Class files repacked, 70% done ..." );
            try {
                m.a(temp2_file, minecraft_servero, retroguard_rules, temp3_file);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            log("Field names renamed, 99% done...");
            
            checkCRC32(minecraft_servero, crc_minecraft_servero);

            
            log("Finished converting minecraft_server.jar, Starting minecraft server...");
            dynamicLoadJar(minecraft_servero);
            
        } else {
            checkCRC32(minecraft_servero,crc_minecraft_servero);
        }
        createMissingConfigFiles();
    }
    
    private static String jar_config_dir = "/org/buckit/config/";
    private static String config_dir = "./config/";
    private static String[] config_files = new String[]{ 
        "Database.properties" , 
        "Expert.properties" , 
        "FlatFile.properties" , 
        "Database.properties" ,
        "motd.txt" ,
        "World.properties"
        };
    private static void createMissingConfigFiles() throws IOException {
        for(String file : config_files) {
            if(!fileExists(config_dir + file)) {
                
                    InputStream input = null;
                    try {
                        input = FileCheck.class.getResourceAsStream(jar_config_dir + file);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(input == null) log("null :< " + jar_config_dir + file );
                    
                 

                    //For Overwrite the file.
                    OutputStream output = new FileOutputStream(new File(config_dir + file));

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = input.read(buf)) > 0){
                      output.write(buf, 0, len);
                    }
                    input.close();
                    output.close();
                    
                    
                    log("Missing " + file + " restored.");
                
            }
        }
    }
    
    
    public static boolean fileExists(String filename) {
        return new File(filename).exists();
    }

    public static void checkCRC32(String fileName, long[] crcs) throws IOException {

        long checksum = getCRC32(fileName);
        for (long i : crcs)
            if (i == checksum)
                return;
        log("-----------------------------");
        log(fileName + " does not match checksum!");
        log("This means some of your files are either corrupted,outdated or to new(minecraft got updated?).");
        log("Running the server with these files is NOT recommended and you will NEVER receive support for them!!");
        log("-----------------------------");
        //System.exit(0);

    }

    public static void checkCRC32(String fileName, long crc) throws IOException {

        long checksum = getCRC32(fileName);
        if (checksum != crc) {
            log("-----------------------------");
            log(fileName + " does not match checksum! Checksum found: " + checksum + ", required checksum: " + crc + ".");
            log("This means some of your files are either corrupted,outdated or to new(minecraft got updated?).");
            log("Running the server with these files is NOT recommended and you will NEVER receive support for them!!");
            log("-----------------------------");
            //System.exit(0);
        }
    }

    public static long getCRC32(String fileName) throws IOException {

        FileInputStream stream = new FileInputStream(fileName);
        CheckedInputStream cis = new CheckedInputStream(stream, new CRC32());
        byte[] buf = new byte[128];
        while (cis.read(buf) >= 0) {
        }

        long rt = cis.getChecksum().getValue();
        stream.close();
        cis.close();

        return rt;
    }

    public static void downloadFile(String website, String fileLocation) throws IOException {
        URL url = new URL(website);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(fileLocation);
        fos.getChannel().transferFrom(rbc, 0, 1 << 24);
    }

    public static void log(String str) {
        System.out.println(str);
    }
    
    public static void copyFile(File in, File out) throws IOException 
    {
        FileChannel inChannel = new FileInputStream(in).getChannel();
        FileChannel outChannel = new FileOutputStream(out).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
        }
    }

    public static void dynamicLoadJar(String fileName) throws IOException {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<?> sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { (new File(fileName)).toURI().toURL() });
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }
}
