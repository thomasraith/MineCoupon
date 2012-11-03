/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rcraft.minecoupon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Thomas
 */
public class MineUpdater {
    
    private URL bukkit_file_url;
    private URL bukkit_beforedl_url;
    private URL bukkit_download_url;
    
    private Plugin plugin;
    private CommandSender sender;
    
    public MineUpdater (Plugin plugin, CommandSender sender) throws Exception{
        System.out.println("[MineUpdater] Enabeling MineUpdater v1.0 by [Thomas Raith]");
        this.plugin = plugin;
        this.sender = sender;
        try {           
            bukkit_file_url = new URL ("http://dev.bukkit.org/server-mods/"+this.plugin.getName()+"/files.rss");
            System.out.println("[MineUpdater] MineUpdater started by " + plugin.getName());      
        }
        catch (Exception e) {
            System.out.println("[MineUpdater] ERROR: Was not able to start!");  
            throw e;
        }
    }
    
    public boolean connect_to_bukkit() {
        if (plugin.getConfig().getBoolean("config.debug")){
            System.out.println("[MineUpdater - DEBUG] Connecting to Bukkit Servers ...");
        }
        try {
             String[] web_doc = read_from_web(bukkit_file_url);
             int help = 0;
             for (int i = 0; i < web_doc.length; i++) {
                 if (web_doc[i].contains("</description>"))
                 {
                     help++;
                     if (help == 2) {
                         bukkit_beforedl_url = new URL(web_doc[i+3].split("<comments>")[1].split("</comments>")[0]);
                         i = web_doc.length;
                     }
                 }
             }
            if (plugin.getConfig().getBoolean("config.debug")){
                System.out.println("[MineUpdater - DEBUG] Connected to Bukkit Servers");
            }
            return true;
        }
        catch (Exception e) {
            if (plugin.getConfig().getBoolean("config.debug")){
                System.out.println("[MineUpdater - DEBUG] Couldn't connect to Bukkit Servers");
            }
            return false;
        }
    }
    
    public boolean get_recommended_download() {
        if (plugin.getConfig().getBoolean("config.debug")){
                System.out.println("[MineUpdater - DEBUG] Getting download URL from Bukkit Servers");
            }
        try {
            String[] temp = read_from_web(bukkit_beforedl_url);          
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].contains("user-action user-action-download") && temp[i].contains("Download</a>")) {
                    bukkit_download_url = new URL(temp[i].split("<li class=\"user-action user-action-download\"><span><a href=\\\"")[1].split("\">Download</a>")[0]);
                    if (plugin.getConfig().getBoolean("config.debug")){
                         System.out.println("[MineUpdater - DEBUG] Download URL received");
                    }
                    return true;
                }
            }
            if (plugin.getConfig().getBoolean("config.debug")){
                  System.out.println("[MineUpdater - DEBUG] Could not receive download URL");
            }
            return false;
        }
        catch (Exception e) {
            if (plugin.getConfig().getBoolean("config.debug")){
                  System.out.println("[MineUpdater - DEBUG] Failed while sending request to Bukkit Servers");
            }
            return false;
        }
    }
    
    public Plugin downloadPlugin(CommandSender sender){
        try
	     {
                 String dl_url = bukkit_download_url.toString();
                 String name[] = dl_url.split("/");
	        /*
	         * Get a connection to the URL and start up
	         * a buffered reader.
	         */
	        long startTime = System.currentTimeMillis();
	  
                sender.sendMessage(ChatColor.GREEN+plugin.getConfig().getString("config.update.message.download.connecting"));
	  
	        URL url = new URL(dl_url);
	        url.openConnection();
	        InputStream reader = url.openStream();
	  
	        /*
	         * Setup a buffered file writer to write
	         * out what we read from the website.
	         */
                System.out.println("plugins"+File.separator+name[name.length-1]);
                File plugin_file = new File("plugins"+File.separator+name[name.length-1]);
	        FileOutputStream writer = new FileOutputStream(plugin_file);
                name = null;
	        byte[] buffer = new byte[153600];
	        int totalBytesRead = 0;
	        int bytesRead = 0;
	  
                sender.sendMessage(ChatColor.GREEN+plugin.getConfig().getString("config.update.message.download.readingdetails"));
	  
	        while ((bytesRead = reader.read(buffer)) > 0)
	        { 
	           writer.write(buffer, 0, bytesRead);
	           buffer = new byte[153600];
	           totalBytesRead += bytesRead;
	        }
	  
	        long endTime = System.currentTimeMillis();
	  
                //player.sendMessage(ChatColor.GREEN+"Done. " + (new Integer(totalBytesRead).toString()) + " bytes read (" + (new Long(endTime - startTime).toString()) + " millseconds).\n");
	        String send = plugin.getConfig().getString("config.update.message.download.downloadfinished").replace("%bytes%", (new Integer(totalBytesRead).toString())).replace("%time%", (new Long(endTime - startTime).toString()));
                sender.sendMessage(ChatColor.GREEN+send);
                writer.close();
	        reader.close();
                return plugin.getServer().getPluginManager().loadPlugin(plugin_file);
	     }
	     catch (Exception e)
	     {
                sender.sendMessage(ChatColor.RED+plugin.getConfig().getString("config.errormessages.reloadfailed"));
	        System.err.println(e);
                return null;
	     }        
    }
    
    public boolean unload_mineupdater() {
        bukkit_file_url = null;
        bukkit_beforedl_url = null;
        bukkit_download_url = null;
        plugin = null;
        sender = null;
        System.out.println("[MineUpdater] MineUpdater disabled successfully");      
        return true;
    }
    
    public void run_complete_update(){
        connect_to_bukkit();
        get_recommended_download();
        downloadPlugin(sender);
        plugin.getServer().reload();
        unload_mineupdater();
    }
    
    private static String[] read_from_web(URL url) throws Exception
    {
        BufferedReader br1 = new BufferedReader(new InputStreamReader(url.openStream()));
        String[] web_file = new String[10000];
        String temp_string = "";
        int i = 0;
        while ((temp_string = br1.readLine()) != null) {
            web_file[i] = temp_string;
            i++;
        }
        return web_file;
    }
    
}
