package com.mrmag518.ChestShopUtil.Files;

import com.mrmag518.ChestShopUtil.Util.Log;
import com.mrmag518.ChestShopUtil.Util.Time;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ShopDB {
    private static FileConfiguration database = null;
    private static File databaseFile = null;
    
    // ----
    public static int maxShopPermValueCap;
    // ----
    
    public static void properLoad() {
        if(use()) {
            reload();
            load();
            reload();
            Log.info("Shop database loaded.");
        }
    }
    
    private static void load() {
        database = getDB();
        Time time = new Time();
        
        if(database.get("Util.SystemMS") != null) {
            database.set("Util.SystemMS", null);
        }
        database.addDefault("Util.Today", time.formatDate(time.getToday()));
        database.addDefault("Util.Tomorrow", time.formatDate(time.getTomorrow()));
        
        Date recTomorrow = time.parseDate(database.getString("Util.Tomorrow"));
        Date recToday = time.parseDate(database.getString("Util.Today"));
        
        if(recToday.equals(recTomorrow)) {
            database.set("Util.Today", time.formatDate(time.getToday()));
            database.set("Util.Tomorrow", time.formatDate(time.getTomorrow()));
        } else if(time.getToday().equals(recTomorrow) || time.getToday().after(recTomorrow)) {
            database.set("Util.Today", time.formatDate(time.getToday()));
            database.set("Util.Tomorrow", time.formatDate(time.getTomorrow()));
            
            ConfigurationSection cs = database.getConfigurationSection("Players");
            
            if(cs != null) {
                for(String s : cs.getKeys(false)) {
                    if(cs.getInt(s + ".AdminSoldToday") > 0) {
                        cs.set(s + ".AdminSoldToday", 0);
                    }
                    if(cs.getInt(s + ".SoldToday") > 0) {
                        cs.set(s + ".SoldToday", 0);
                    }
                    if(cs.getInt(s + ".AdminBoughtToday") > 0) {
                        cs.set(s + ".AdminBoughtToday", 0);
                    }
                    if(cs.getInt(s + ".BoughtToday") > 0) {
                        cs.set(s + ".BoughtToday", 0);
                    }
                }
                save();
            }
        }
        database.addDefault("Util.ShopPermSearchCap", 508);
        
        if(database.getConfigurationSection("Players") == null) {
            database.createSection("Players");
        }
        
        cacheVariables();
        getDB().options().copyDefaults(true);
        save();
    }
    
    private static void reload() {
        if (databaseFile == null) {
            databaseFile = new File("plugins/ChestShopUtil/shops.yml");
        }
        database = YamlConfiguration.loadConfiguration(databaseFile);
    }
    
    public static FileConfiguration getDB() {
        if (database == null) {
            reload();
        }
        return database;
    }
    
    private static void save() {
        if (database == null || databaseFile == null) {
            return;
        }
        try {
            database.save(databaseFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save shops.yml to " + databaseFile, ex);
        }
    }
    
    public static void cacheVariables() {
        maxShopPermValueCap = getDB().getInt("Util.ShopPermSearchCap");
    }
    
    public static boolean use() {
        return Config.maxShops > 0 || Config.maxDailyAdminShopBuy > 0 || Config.maxDailyAdminShopSell > 0 || Config.maxDailyShopBuy > 0 || Config.maxDailyShopSell > 0;
    }
    
    public static void incrementDailySold(String player, boolean adminShop, int amount) {
        player = player.toLowerCase();
        int i = getDailySoldAmount(player, adminShop) + amount;
        
        if(adminShop) {
            getDB().set("Players." + player + ".AdminSoldToday", i);
        } else {
            getDB().set("Players." + player + ".SoldToday", i);
        }
        save();
    }
    
    public static int getDailySoldAmount(String player, boolean adminShop) {
        player = player.toLowerCase();
        int i = (adminShop)? getDB().getInt("Players." + player + ".AdminSoldToday") : getDB().getInt("Players." + player + ".SoldToday");
        return i;
    }
    
    public static void incrementDailyBought(String player, boolean adminShop, int amount) {
        player = player.toLowerCase();
        int i = getDailyBoughtAmount(player, adminShop) + amount;
        
        if(adminShop) {
            getDB().set("Players." + player + ".AdminBoughtToday", i);
        } else {
            getDB().set("Players." + player + ".BoughtToday", i);
        }
        save();
    }
    
    public static int getDailyBoughtAmount(String player, boolean adminShop) {
        player = player.toLowerCase();
        int i = (adminShop)? getDB().getInt("Players." + player + ".AdminBoughtToday") : getDB().getInt("Players." + player + ".BoughtToday");
        return i;
    }
    
    public static void incrementShopsMade(String player) {
        player = player.toLowerCase();
        int i = getShopsMade(player) + 1;
        getDB().set("Players." + player + ".ShopsMade", i);
        save();
    }
    
    public static void decrementShopsMade(String player) {
        player = player.toLowerCase();
        int i = getShopsMade(player) - 1;
        
        if(i < 1) {
            getDB().set("Players." + player + ".ShopsMade", null);
        } else {
            getDB().set("Players." + player + ".ShopsMade", i);
        }
        save();
    }
    
    public static int getShopsMade(String player) {
        player = player.toLowerCase();
        return getDB().getInt("Players." + player + ".ShopsMade");
    }
}
