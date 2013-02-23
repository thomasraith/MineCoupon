/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xtrsource.minecoupon;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 *
 * @author Thomas Raith
 */
public class MineCouponListener implements Listener {

    Plugin plugin;
    
    public MineCouponListener(MineCoupon p) {
        plugin = p;
    }
    
    @EventHandler
    public void checkUpdate(PlayerJoinEvent event){
        if(permCheck(event.getPlayer(), "minecoupon.checkupdate")){   
                checkVersion(event.getPlayer());
        }
    }

    private void checkVersion(Player player){
        
        player.sendMessage(ChatColor.GREEN+"[MineCoupon] " + plugin.getConfig().getString("config.update.message.check"));
        System.out.println("[MineCoupon] Check for updates ...");   
        
        PluginDescriptionFile descFile = plugin.getDescription();
        try {
            String[] version_message;
            version_message = read("http://rcraft.at/plugins/minecoupon/VERSION_MESSAGES/"+descFile.getVersion());
            
            for (int i = 0; i < 25; i++) {
                if (version_message[i] != null) {
                    System.out.println(version_message[i].split(";;;;")[1]);
                    
                    if (version_message[i].split(";;;;")[0].equals("RED")){
                        player.sendMessage(ChatColor.RED + version_message[i].split(";;;;")[1]);
                    }
                    
                    if (version_message[i].split(";;;;")[0].equals("GREEN")){
                        player.sendMessage(ChatColor.GREEN + version_message[i].split(";;;;")[1]);
                    }
                    
                    if (version_message[i].split(";;;;")[0].equals("BLUE")){
                        player.sendMessage(ChatColor.BLUE + version_message[i].split(";;;;")[1]);
                    }
                    
                    if (version_message[i].split(";;;;")[0].equals("YELLOW")){
                        player.sendMessage(ChatColor.YELLOW + version_message[i].split(";;;;")[1]);
                    }
                    
                    if (version_message[i].split(";;;;")[0].equals("WHITE")){
                        player.sendMessage(ChatColor.WHITE + version_message[i].split(";;;;")[1]);
                    }
                    
                    if (version_message[i].split(";;;;")[0].equals("GOLD")){
                        player.sendMessage(ChatColor.GOLD + version_message[i].split(";;;;")[1]);
                    }
                    
                    if (version_message[i].split(";;;;")[0].equals("GRAY")){
                        player.sendMessage(ChatColor.GRAY + version_message[i].split(";;;;")[1]);
                    }
                    
                    if (version_message[i].split(";;;;")[0].equals("DARK_RED")){
                        player.sendMessage(ChatColor.DARK_RED + version_message[i].split(";;;;")[1]);
                    }
                    
                    if (version_message[i].split(";;;;")[0].equals("DARK_BLUE")){
                        player.sendMessage(ChatColor.DARK_BLUE + version_message[i].split(";;;;")[1]);
                    }
                    
                    if (version_message[i].split(";;;;")[0].equals("DARK_PURPLE")){
                        player.sendMessage(ChatColor.DARK_PURPLE + version_message[i].split(";;;;")[1]);
                    }
                }
            }
            
        }
        catch(Exception e)
        {
           System.out.println("[MineCoupon] Check for updates failed.");
           player.sendMessage(ChatColor.RED+"[MineCoupon] " + plugin.getConfig().getString("config.update.message.error"));
        }
    }
    
    
    private boolean permCheck(Player player, String permission){
        if(player.isOp() || player.hasPermission(permission)){
            return true;
        }
        return false;
    }
    
    public static String[] read(String url) throws Exception
    {
        URL url1 = new URL(url);
        BufferedReader br1 = new BufferedReader(new InputStreamReader(url1.openStream()));
        String[] version_message = new String[25];
        String temp_string = "";
        int i = 0;
        while ((temp_string = br1.readLine()) != null) {
            version_message[i] = temp_string;
            i++;
        }
        return version_message;
    }
    
}
