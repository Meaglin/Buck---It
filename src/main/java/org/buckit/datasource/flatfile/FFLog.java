package org.buckit.datasource.flatfile;

import java.util.logging.Logger;

public class FFLog {
    
    private final static String logname = "Minecraft";
    
    public static void newFile(String f) {
        Logger.getLogger(logname).info("Flatfile - new file:    "+f);
    }
    
    public static void newInit(String ini, int total) {
        Logger.getLogger(logname).info("Flatfile - initialized: " + ini + " - total: " + total);
    }
    
    public static void newFound(String ini, int total) {
        Logger.getLogger(logname).info("Flatfile - analysed:    " + ini + " - total: " + total);  
    }
    
    public static void newEdit(String ini, String what) {
        Logger.getLogger(logname).info("Flatfile - editted: " + ini + " - " + what);
    }

}
