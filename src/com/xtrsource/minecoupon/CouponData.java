/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xtrsource.minecoupon;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Thomas Raith
 */
public class CouponData {
    
      protected String user;
      protected String code;
      protected int usings;
      protected long expire;
      protected String command;
      protected int step;

    CouponData(){
        String user = new String();
    }
      
    CouponData(CommandSender sender, String code){
        this.user = sender.getName();
        this.code = code;
        this.step = 2;
    }
    
    public void setusings(int usings)
    {
        this.usings = usings;
    }
    
    public void setexpire(String expire)
    {
        long time_expire = System.currentTimeMillis() / 1000;
        String[ ] zahl;
        if (expire.endsWith("seconds") || expire.endsWith("second") || expire.endsWith("sec")){
               zahl = expire.split("sec");
               time_expire = time_expire + Integer.parseInt(zahl[0]); 
        }
                         
        if (expire.endsWith("minutes") || expire.endsWith("minute") || expire.endsWith("min")){
               zahl = expire.split("min");
               time_expire = time_expire + Integer.parseInt(zahl[0])*60; 
        }
                         
        if (expire.endsWith("hours") || expire.endsWith("hour") || expire.endsWith("h")){
                zahl = expire.split("h");
                time_expire = time_expire + Integer.parseInt(zahl[0])*3600; 
        }
                         
        if (expire.endsWith("days") || expire.endsWith("day") || expire.endsWith("d")){
                zahl = expire.split("d");
                time_expire = time_expire + Integer.parseInt(zahl[0])*86400; 
        }
                         
        if (expire.endsWith("weeks") || expire.endsWith("week") || expire.endsWith("w")){
                 zahl = expire.split("w");
                 time_expire = time_expire + Integer.parseInt(zahl[0])*604800; 
        }
                         
         if (expire.endsWith("months") || expire.endsWith("month") || expire.endsWith("mon")){
             zahl = expire.split("mon");
             time_expire = time_expire + Integer.parseInt(zahl[0])*18144000; 
         }
        
         this.expire = time_expire;
    }
    
    public void setcommand(String command)
    {
        this.command = command;
    }
    
}
