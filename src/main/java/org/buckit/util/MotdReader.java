package org.buckit.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MotdReader {
    
    static List<String> lines = new ArrayList<String>();
    
    public static List<String> getMotd() {
        
        try {
            FileInputStream fstream = new FileInputStream("config/motd.txt");
            
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            String strLine;
            while ((strLine = br.readLine()) != null)   {
                lines.add(strLine);
            }
            
            in.close();
            
            return lines;
        } catch (Exception e) {
            return null;
        }
    }
}
