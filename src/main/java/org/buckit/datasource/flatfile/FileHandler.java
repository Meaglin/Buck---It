package org.buckit.datasource.flatfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.buckit.Config;

public class FileHandler {
    
    private static Map<String, String> filenames = new HashMap<String, String>();
    private static boolean loaded = false;
    
    public final static String sep1 = ":";
    public final static String sep2 = ";";
    public final static String sep3 = ",";
    
    public static List<String> getLines(String file) {
        if (!loaded)
            load();
        
        return readFile(new File(filenames.get(file.toLowerCase())));
    }

    public static boolean addLine(String file, String line) {
        if (!loaded)
            load();
        
        File f = new File(filenames.get(file.toLowerCase()));
        List<String> lines = readFile(f);
        lines.add(line);
        return writeFile(f, lines);
    }
    
    public static boolean writeFile(String file, List<String> lines) {
        if (!loaded)
            load();
        
        List<String> newLines = defaultText(file);
        newLines.addAll(lines);
        
        File f = new File(filenames.get(file.toLowerCase()));
        return writeFile(f, newLines);
    }
    
    
    //read function
    //@return error = null;
    public static List<String> readFile(File file) {
        if (!loaded)
            load();
        
        List<String> lines = null;
        
        try {
            lines = new ArrayList<String>();
            
            FileInputStream fstream = new FileInputStream(file.getPath());
            
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            String strLine;
            while ((strLine = br.readLine()) != null)   {
                if (!strLine.startsWith("#") && !strLine.equals(""))
                    lines.add(strLine);
            }
            
            in.close();
        } catch(Exception e) {
            
        }
        
        return lines;
    }
    
    //write function
    //@return error = false;
    public static boolean writeFile(File file, List<String> lines) {        
        boolean worked;
        
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file.getPath()));
            for (String line : lines) {
                out.write(line);
                out.newLine();
            }
            out.close();
            worked = true;
        } catch (IOException e) {
            worked = false;
        }
        return worked;
    }

    private static void load() {
        //TODO insert filenames
        filenames.put("groups", Config.FLATFILE_ACCESSGROUPS_FILE);
        filenames.put("accesslevels", Config.FLATFILE_ACCESS_FILE);
        filenames.put("kits", Config.FLATFILE_KITS_FILE);
        filenames.put("kitslast", Config.FLATFILE_KITS_DELAY_FILE);
        filenames.put("reservelist", Config.FLATFILE_RESERVELIST_FILE);
        filenames.put("user", Config.FLATFILE_USERS_FILE);
        filenames.put("warps", Config.FLATFILE_WARPS_FILE);
        filenames.put("whitelist", Config.FLATFILE_WHITELIST_FILE);
        
        Collection<String> files = filenames.values();

        for (String f : files) {
            File temp = new File(f);
            if (!temp.exists()) {
                createFile(temp);
            }
        }
        
        loaded = true;
    }
    
    private static void createFile(File file) {
        try {
            new File(file.getParent()).mkdirs();
            file.createNewFile();
            Logger.getLogger("Minecraft").info("Created new file: "+file.getPath());
            writeFile(file, defaultText(file.getName()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static List<String> defaultText(String name) {
        List<String> l = new ArrayList<String>();
        
        //TODO make default filetext
        l.add("#File: " + name);
        l.add("");
        
        return l;
    }
}
