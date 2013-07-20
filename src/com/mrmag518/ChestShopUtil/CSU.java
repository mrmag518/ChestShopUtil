package com.mrmag518.ChestShopUtil;

import com.mrmag518.ChestShopUtil.Files.Local;
import com.mrmag518.ChestShopUtil.Files.Config;
import com.mrmag518.ChestShopUtil.Files.ShopDB;
import com.mrmag518.ChestShopUtil.Util.Cooldown;
import com.mrmag518.ChestShopUtil.Util.Log;
import com.mrmag518.ChestShopUtil.Util.UpdateThread;
import com.mrmag518.ChestShopUtil.Util.Updater;

import java.io.IOException;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CSU extends JavaPlugin {
    public boolean updateFound = false;
    public String verionFound = "";
    private Plugin chestshop = null;
    
    @Override
    public void onDisable() {
        Cooldown.clear();
        Log.info("Version " + getVersion() + " disabled.");
    }
    
    @Override
    public void onEnable() {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        verifyChestShop();
        Config.load();
        Local.load();
        ShopDB.properLoad();
        
        if(Config.checkUpdates) {
            if(Config.useMultiThread) {
                Thread t = new Thread(new UpdateThread(this, null));
                t.start();
            } else {
                updateCheck(null);
            }
        }
        
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
        }
        
        getCommand("chestshoputil").setExecutor(new Commands(this));
        Log.info("Version " + getVersion() + " enabled.");
    }
    
    public String getVersion() {
        PluginDescriptionFile pdffile = getDescription();
        return pdffile.getVersion().replace("v", "");
    }
    
    public void updateCheck(@Nullable CommandSender s) {    
        Log.info("Checking for updates ..");
        
        try {
            Updater updater = new Updater(this, "chestshoputil", this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);

            Updater.UpdateResult result = updater.getResult();
            switch(result) {
                case NO_UPDATE:
                    Log.info("No update was found.");
                    if(s != null) {
                        s.sendMessage(ChatColor.BLUE + "No update was found.");
                    }
                    break;
                case FAIL_DBO:
                    Log.warning("Failed to contact dev.bukkkit.org!");
                    if(s != null) {
                        s.sendMessage(ChatColor.RED + "Failed to contact deve.bukkit.org!");
                    }
                    break;
                case UPDATE_AVAILABLE:
                    updateFound = true;
                    verionFound = updater.getLatestVersionString().replace("v", "").replace("ChestShopUtil", "");
                    Log.info("A new version of ChestShopUtil was found!");
                    Log.info("Version found: " + updater.getLatestVersionString());
                    Log.info("Version running: ChestShopUtil v" + getVersion());
                    if(s != null) {
                        s.sendMessage(ChatColor.BLUE + "An update was found!");
                        s.sendMessage(ChatColor.BLUE + "Version found: " + updater.getLatestVersionString());
                        s.sendMessage(ChatColor.BLUE + "Version running: " + getVersion());
                    }
                    break;
            }
        } catch(RuntimeException re) {
            Log.warning("Failed to establish a connection to dev.bukkit.org!");
            if(s != null) {
                s.sendMessage(ChatColor.RED + "Failed to establish a connection to dev.bukkit.org!");
            }
        }
    }
    
    private void verifyChestShop() {
        PluginManager pm = getServer().getPluginManager();
        chestshop = pm.getPlugin("ChestShop");
        
        if(chestshop != null && pm.isPluginEnabled(chestshop)) {
            String version = chestshop.getDescription().getVersion().replace("v", "");
            Log.info("ChestShop version " + version + " found!");
            EventListener listener = new EventListener(this);
        } else {
            Log.severe("ChestShop was not found!");
            Log.severe("Please make sure ChestShop is installed, and is being enabled without errors!");
            Log.severe("Disabling ..");
            pm.disablePlugin(this);
        }
    }
}
