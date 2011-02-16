package org.buckit.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MotdReader {
    
    private static List<String> lines = new ArrayList<String>();
    private static boolean loaded = false;
    public static void load() {
        try {
            FileInputStream fstream = new FileInputStream("./config/motd.txt");
            
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            String strLine;
            while ((strLine = br.readLine()) != null)   {
                lines.add(strLine);
            }
            
            in.close();
            loaded = true;
        } catch (Exception e) {
        }
    }
    
    public static List<String> getMotd() {
        if(!loaded) load();
        return lines;
    }
}
