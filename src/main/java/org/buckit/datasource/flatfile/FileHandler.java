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

import org.buckit.Config;

public class FileHandler {
    
    private static Map<String, String> filenames = new HashMap<String, String>();
    private static boolean loaded = false;
    
    public final static String sep1 = "<-<-->->";
    public final static String sep2 = ";";
    public final static String sep3 = ",";
    public final static String sep1Good = ":";
    
    public static List<String> getLines(String file) {
        if (!loaded)
            load();
        
        return readFile(new File(filenames.get(file.toLowerCase())));
    }

    public static boolean addLine(String file, String line) {
        if (!loaded)
            load();
        
        //SECURITY!!
        line = line.replace(sep1Good, "_");
        line = line.replace(sep1, sep1Good);
        
        File f = new File(filenames.get(file.toLowerCase()));
        List<String> lines = defaultText(file);
        lines.addAll(readFile(f));
        lines.add(line);
        return writeFile(f, lines);
    }
    
    public static boolean writeFile(String file, List<String> lines) {
        if (!loaded)
            load();
        
        for(int i=0; i<lines.size(); i++) {
            lines.set(i, lines.get(i).replace(sep1Good, "_").replace(sep1, sep1Good));
        }
        
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
                if (!strLine.startsWith("#") && !strLine.equals("")) {
                    lines.add(strLine.replace(sep1Good, sep1));
                }
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
        filenames.put("groups", Config.FLATFILE_ACCESSGROUPS_FILE);
        filenames.put("accesslevels", Config.FLATFILE_ACCESS_FILE);
        filenames.put("kits", Config.FLATFILE_KITS_FILE);
        filenames.put("kitslast", Config.FLATFILE_KITS_DELAY_FILE);
        filenames.put("reservelist", Config.FLATFILE_RESERVELIST_FILE);
        filenames.put("user", Config.FLATFILE_USERS_FILE);
        filenames.put("warps", Config.FLATFILE_WARPS_FILE);
        filenames.put("whitelist", Config.FLATFILE_WHITELIST_FILE);
        
        Collection<String> keys = filenames.keySet();

        for (String k : keys) {
            File temp = new File(filenames.get(k));
            if (!temp.exists()) {
                createFile(k);
            }
        }
        
        loaded = true;
    }
    
    private static void createFile(String k) {
        try {
            File file = new File(filenames.get(k));
            new File(file.getParent()).mkdirs();
            file.createNewFile();
            
            FFLog.newFile(file.getPath());
            
            List<String> lines = defaultText(k);
            lines.addAll(defaultSetting(k));
            
            writeFile(file, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> defaultText(String k) {
        List<String> l = new ArrayList<String>();
        
        l.add("#File: " + (new File(filenames.get(k)).getName()));
        l.add("");
        
        if (k.equals("groups")) {
            l.add("#USAGE: ID:NAME:COMMANDS");
        }
        else if (k.equals("accesslevels")) {
            l.add("#USAGE: ID:NAME:USERNAMEFORMAT:ACCESSGROUPS:ADMINGROUP:CANBUILD");
        }
        else if (k.equals("kits")) {
            l.add("#USAGE: NAME:IDs:DELAY:ACCESSLEVEL");
        }
        else if (k.equals("kitslast")) {
            l.add("#USAGE: USERID:KITNAME:TIME");
        }
        else if (k.equals("reservelist")) {
            l.add("#USAGE: USERID:USERNAME");
        }
        else if (k.equals("user")) {
            l.add("#USAGE: ID:USERNAME:USERNAMEFORMAT:FIRSTLOGIN:LASTLOGIN:ONLINETIME:BANTIME:MUTETIME:COMMANDS:CANBUILD:ISADMIN:ACCESSLEVELID");
        }
        else if (k.equals("warps")) {
            l.add("#USAGE: ID:NAME:GROUPNAME:WORLD:X:Y:Z:ROTX:ROTY:MINACCESSLEVEL");
        }
        else if (k.equals("whitelist")) {
            l.add("#USAGE: USERID:USERNAME");
        }
        
        return l;
    }
    
    private static List<String> defaultSetting(String k) {
        List<String> l = new ArrayList<String>();
        
        if (k.equals("groups")) {
            l.add("0:none_group:/spawn");
            l.add("1:default_group:/help,/sethome,/home,/spawn,/me,/msg,/kit,/playerlist,/warp,/motd,/compass");
            l.add("2:vip_group:/tp,/tpchere");
            l.add("3:mod_group:/ban,/kick,/item,/tp,/tphere,/s,/i,/give");
            l.add("4:admin_group:*");
        }
        else if (k.equals("accesslevels")) {
            l.add("0:nieuw:^f{$username}:none_group:false:false");
            l.add("1:beunhaas:^8{$username}^f:default_group:false:false");
            l.add("2:default:^a{$username}^f:default_group:false:true");
            l.add("3:vip:^6{$username}^f:default_group,vip_group:false:true");
            l.add("4:mod:^9{$username}^f:default_group,vip_group,mod_group:true:true");
            l.add("5:admin:^4{$username}^f:admin_group:true:true");
        }
        
        return l;       
    }
}
