package com.mrmag518.ChestShopUtil.Files;

import com.mrmag518.ChestShopUtil.CSU;
import com.mrmag518.ChestShopUtil.Util.ConfigManager;
import com.mrmag518.ChestShopUtil.Util.Configuration;
import com.mrmag518.ChestShopUtil.Util.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;

public class Config {
    private static final CSU plugin = (CSU)Bukkit.getPluginManager().getPlugin("ChestShopUtil");
    private static ConfigManager manager = new ConfigManager(plugin);
    private static Configuration config = null;
    
    // ----
    public static long cooldown;
    public static int maxShops;
    public static List<String> disallowedCreateWorlds;
    public static List<String> disallowedItems;
    public static double maxBuyPrice;
    public static double maxSellPrice;
    public static List<String> maxBuyPrices;
    public static List<String> maxSellPrices;
    public static List<String> disallowedTradeWorlds;
    public static List<String> tradePeriods;
    public static boolean checkUpdates;
    public static boolean useMultiThread;
    public static int maxDailyAdminShopBuy;
    public static int maxDailyAdminShopSell;
    public static int maxDailyShopBuy;
    public static int maxDailyShopSell;
    public static int maxBuyOverflow;
    public static int maxSellOverflow;
    // ----
    
    public static void load() {
        File f = new File("plugins" + File.separator + "ChestShopUtil" + File.separator + "config.yml");
        
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Log.severe("An error occured while creating the config.yml file!");
                ex.printStackTrace();
            }
        }
        config = manager.getNewConfig("config.yml");
        
        String[] default_create_worlds = {"example_world1", "example2"};
        String[] default_trade_worlds = {"example"};
        String[] default_buy_prices = {"0;0", "0:20;200"};
        String[] default_sell_prices = {"0;0", "0:20;200"};
        String[] default_items = {"0", "1337:20"};
        String[] default_times = {"0-0"};
        
        config.addDefault("UpdateChecker.Enabled", true, "Checks for an available update when CSU is being enabled."
                + ";This will not download anything, only notify you if"
                + ";there is an update available."
                + ";"
                + ";Use-Multi-Threading - Will run the update check on a"
                + ";separate thread.");
        config.addDefault("UpdateChecker.Use-Multi-Threading", true, null);
        
        config.addDefault("Global-Max-Buy-Price", 0.0, "Prevents players from creating shops with a buy;price above this value."
                + ";A value of 0 will be ignored.");
        
        config.addDefault("Global-Max-Sell-Price", 0.0, "Prevents players from creating shops with a sell;price above this value."
                + ";A value of 0 will be ignored.");
        
        config.addDefault("Disallowed-Create-Worlds", default_create_worlds, "Prevents shops from being created in these worlds.");
        
        config.addDefault("Disallowed-Trade-Worlds", default_trade_worlds, "Prevent players from doing transactions with ChestShop"
                + ";in these worlds.");
        
        config.addDefault("Disallowed-Items", default_items, "Prevents players from creating shops trading these items.");
        
        config.addDefault("Max-Buy-Prices", default_buy_prices, "Prevents players from creating shops with a buy"
                + ";price above the max for a specific item."
                + ";The last number after the separation symbol is the price."
                + ";A price value of 0 wil be ignored.");
        
        config.addDefault("Max-Sell-Prices", default_sell_prices, "Prevents players from creating shops with a sell"
                + ";price above the max for a specific item."
                + ";The last number after the separation symbol is the price."
                + ";A price value of 0 wil be ignored.");
        
        config.addDefault("Max-Shops-Per-Player", 0, "Setting this to a value above 0 will enable the feature."
                + ";This will create a shops.yml file if one does not exist."
                + ";Which will be used as a database."
                + ";This feature prevents players from creating too many shops."
                + ";Can be overriden by the permission csu.maxshops.x"
                + ";Where x is a number.");
        
        config.addDefault("Disallowed-Time-Periods", default_times, "Will prevent players from using shops in certain"
                + ";time periods."
                + ";For instance, 1000-4000 will prevent players from using"
                + ";shops from tick 1000 to tick 4000.");
        
        config.addDefault("Creation-Cooldown", 0, "Cooldown in milliseconds between each shop creation."
                + ";(per player)"
                + ";1000ms = 1sec"
                + ";A value of 0 will be ignored.");
        
        config.addDefault("Daily.Max.Buy.AdminShop", 0, "Will prevent players from buying/selling too many items"
                + ";from the specific shop type, per day."
                + ";This will create a shops.yml file if one does exist."
                + ";Which will be used as a database."
                + ";"
                + ";OverflowLimit - When a player trades with a shop without"
                + ";having exceeded the limit, but the amount he is about"
                + ";to buy/sell will make him exceed the limit."
                + ";Example, ADude has bought 99/100 items, but the amount"
                + ";he is about to buy is 5, he would exceed the limit with"
                + ";104/100 items. Setting the OverflowLimit to 50 would allow"
                + ";ADude to buy with a overflow of 50, which would mark him"
                + ";having bought a total of 149/100 items.");
        config.addDefault("Daily.Max.Buy.PlayerShop", 0, null);
        config.addDefault("Daily.Max.Buy.OverflowLimit", 50, null);
        
        config.addDefault("Daily.Max.Sell.AdminShop", 0, null);
        config.addDefault("Daily.Max.Sell.PlayerShop", 0, null);
        config.addDefault("Daily.Max.Sell.OverflowLimit", 50, null);
        
        /*config.addDefault("Item-Currency.Enabled", false, 
                  "Enable, Enables the item currency feature."
                + ";Item-Currency will use items instead of virtual money."
                + ";Sign format is still the same. The only difference is"
                + ";that the price for items will be handled as items."
                + ";"
                + ";Max-Price, Prevents players from creating prices above;the reasonable"
                + ";"
                + ";Item, The item id for the item to use as currency."
                + ";"
                + ";ItemValue, How much cash is 1 of your selected item worth?");
        config.addDefault("Item-Currency.Max-Price", 2304, null);
        config.addDefault("Item-Currency.Item", 266, null);
        config.addDefault("Item-Currency.ItemValue", 20, null);*/
        
        save();
        cacheVariables();
        Log.info("Config loaded.");
    }
    
    private static void cacheVariables() {
        cooldown = Config.getConfig().getLong("Creation-Cooldown");
        maxShops = Config.getConfig().getInt("Max-Shops-Per-Player");
        disallowedCreateWorlds = Config.getConfig().getStringList("Disallowed-Create-Worlds");
        disallowedItems = Config.getConfig().getStringList("Disallowed-Items");
        maxBuyPrice = Config.getConfig().getDouble("Global-Max-Buy-Price");
        maxSellPrice = Config.getConfig().getDouble("Global-Max-Sell-Price");
        maxBuyPrices = Config.getConfig().getStringList("Max-Buy-Prices");
        maxSellPrices = Config.getConfig().getStringList("Max-Sell-Prices");
        disallowedTradeWorlds = Config.getConfig().getStringList("Disallowed-Trade-Worlds");
        tradePeriods = Config.getConfig().getStringList("Disallowed-Time-Periods");
        checkUpdates = Config.getConfig().getBoolean("UpdateChecker.Enabled");
        useMultiThread = Config.getConfig().getBoolean("UpdateChecker.Use-Multi-Threading");
        maxDailyAdminShopBuy = Config.getConfig().getInt("Daily.Max.Buy.AdminShop");
        maxDailyAdminShopSell = Config.getConfig().getInt("Daily.Max.Sell.AdminShop");
        maxDailyShopBuy = Config.getConfig().getInt("Daily.Max.Buy.PlayerShop");
        maxDailyShopSell = Config.getConfig().getInt("Daily.Max.Sell.PlayerShop");
        maxBuyOverflow = Config.getConfig().getInt("Daily.Max.Buy.OverflowLimit");
        maxSellOverflow = Config.getConfig().getInt("Daily.Max.Sell.OverflowLimit");
    }
    
    private static Configuration getConfig() {
        if(config == null) {
            load();
        }
        return config;
    }
    
    public static void reload() {
        if(config == null) {
            load();
        }
        config.reloadConfig();
        cacheVariables();
    }
    
    public static void save() {
        if(config == null) {
            load();
        }
        config.saveConfig();
    }
}