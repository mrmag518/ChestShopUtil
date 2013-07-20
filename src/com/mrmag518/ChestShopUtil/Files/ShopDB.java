package com.mrmag518.ChestShopUtil.Files;

import com.mrmag518.ChestShopUtil.Util.Log;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ShopDB {
    private static FileConfiguration database = null;
    private static File databaseFile = null;
    
    public static void properLoad() {
        if(Config.maxShops > 0) {
            reload();
            load();
            reload();
            Log.info("Shop database loaded.");
        }
    }
    
    private static void load() {
        database = getDB();
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
    
    public static void incrementShopsMade(String player) {
        player = player.toLowerCase();
        int i = getShopsMade(player) + 1;
        getDB().set(player + ".ShopsMade", i);
        save();
    }
    
    public static void decrementShopsMade(String player) {
        player = player.toLowerCase();
        int i = getShopsMade(player) - 1;
        
        if(i < 1) {
            // Note to self: Can't remove whole player tree if additional entries is added in the future.
            getDB().set(player, null);
        } else {
            getDB().set(player + ".ShopsMade", i);
        }
        save();
    }
    
    public static int getShopsMade(String player) {
        player = player.toLowerCase();
        return getDB().getInt(player + ".ShopsMade");
    }
}
