package org.buckit.util;

public class TimeFormat {

    public static final int WEEK = 60 * 60 * 24 * 7;
    public static final int DAY = 60 * 60 * 24;
    public static final int HOUR = 60 * 60;
    public static final int MINUTE = 60;
    
    public static String formatRemaining(int remainingtime) {
        int weeks = 0,days = 0,hours = 0,minutes = 0,seconds = 0;
        if(remainingtime >= WEEK){
            weeks = (int) Math.floor(remainingtime / WEEK);
            remainingtime -= weeks * WEEK;
        }
        if(remainingtime >= DAY){
            days = (int) Math.floor(remainingtime / DAY);
            remainingtime -= days * DAY;
        }
        if(remainingtime >= HOUR){
            hours = (int) Math.floor(remainingtime / HOUR);
            remainingtime -= hours * HOUR;
        }
        if(remainingtime >= MINUTE){
            minutes = (int) Math.floor(remainingtime / MINUTE);
            remainingtime -= minutes * MINUTE;
        }
        seconds = remainingtime;
        String rt = "";
        
        if(weeks != 0) rt += (weeks == 1 ? "1 day, " : weeks + " days, ");
        if(hours != 0) rt += (hours == 1 ? "1 hour, " : hours + " hours, ");
        if(minutes != 0) rt += (minutes == 1 ? "1 minute, " : minutes + " minutes, ");
        if(seconds != 0){
            if(rt.length() > 0){
                rt = rt.substring(0, rt.length() - 2 );
                rt += " and ";
            }
            rt += (seconds == 1 ? "1 second" : seconds + " seconds");
        } else if(rt.length() > 0){
            rt = rt.substring(0, rt.length() - 2);
        }
        
        return rt;
    }
}
