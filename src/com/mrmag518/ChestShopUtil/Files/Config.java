package com.mrmag518.ChestShopUtil.Files;

import com.mrmag518.ChestShopUtil.Util.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
    private static FileConfiguration config = null;
    private static File configFile = null;
    
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
    public static double shopeditOwnerFee;
    public static double shopeditAmountFee;
    public static double shopeditPriceFee;
    public static double shopeditItemFee;
    public static List<String> shopeditFeePerms_L1;
    public static List<String> shopeditFeePerms_L2;
    public static List<String> shopeditFeePerms_L3;
    public static List<String> shopeditFeePerms_L4;
    // ----
    
    public static void properLoad() {
        reload();
        load();
        reload();
        Log.info("Config loaded.");
    }
    
    private static void load() {
        config = getConfig();
        
        String[] default_create_worlds = {"example_world1", "example2"};
        String[] default_trade_worlds = {"example"};
        String[] default_buy_prices = {"0;0", "0:20;200"};
        String[] default_sell_prices = {"0;0", "0:20;200"};
        String[] default_items = {"0", "1337:20"};
        String[] default_times = {"0-0"};
        String[] shopedit_perms1 = {"my.custom.permission;0"}; // YAML refuses to cooperate unless I separate the lists for some reason..
        String[] shopedit_perms2 = {"my.custom.permission;0"};
        String[] shopedit_perms3 = {"my.custom.permission;0"};
        String[] shopedit_perms4 = {"my.custom.permission;0", "example.donator.package.vip;10"};
        
        config.options().header("For help with these configuration options, \n"
                + "please see the 'plugins/ChestShopUtil/config_explained.yml' file."
                + "\n");
        
        config.addDefault("UpdateChecker.Enabled", true);
        config.addDefault("UpdateChecker.UseMultiThreading", true);
        
        config.addDefault("Global.MaxBuyPrice", 0);
        config.addDefault("Global.MaxSellPrice", 0);
        config.addDefault("Global.MaxShopsPerPlayer", 0);
        
        config.addDefault("DisallowedCreateWorlds", default_create_worlds);
        config.addDefault("DisallowedTradeWorlds", default_trade_worlds);
        
        config.addDefault("BlacklistedItems", default_items);
        
        config.addDefault("ItemPrices.Max.Buy", default_buy_prices);
        config.addDefault("ItemPrices.Max.Sell", default_sell_prices);
        
        config.addDefault("TimePeriods.Periods", default_times);
        
        config.addDefault("CreationCooldown", 0);
        
        config.addDefault("Daily.Max.Buy.AdminShop", 0);
        config.addDefault("Daily.Max.Buy.PlayerShop", 0);
        config.addDefault("Daily.Max.Buy.OverflowLimit", 10);
        
        config.addDefault("Daily.Max.Sell.AdminShop", 0);
        config.addDefault("Daily.Max.Sell.PlayerShop", 0);
        config.addDefault("Daily.Max.Sell.OverflowLimit", 10);
        
        config.addDefault("ShopEdit.Name.Fee", 0);
        config.addDefault("ShopEdit.Name.FeePerms", shopedit_perms1);
        config.addDefault("ShopEdit.Quantity.Fee", 0);
        config.addDefault("ShopEdit.Quantity.FeePerms", shopedit_perms2);
        config.addDefault("ShopEdit.Price.Fee", 0);
        config.addDefault("ShopEdit.Price.FeePerms", shopedit_perms3);
        config.addDefault("ShopEdit.Item.Fee", 0);
        config.addDefault("ShopEdit.Item.FeePerms", shopedit_perms4);
        
        getConfig().options().copyDefaults(true);
        save();
        runTransitioner(); // 1.0,1.1 -> 1.2+ config transition.
        cacheVariables();
        handleExplanation();
    }
    
    private static void reload() {
        if(configFile == null) {
            configFile = new File("plugins/ChestShopUtil/config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        cacheVariables();
    }
    
    public static FileConfiguration getConfig() {
        if(config == null) {
            reload();
        }
        return config;
    }
    
    public static void save() {
        if(config == null || configFile == null) {
            return;
        }
        
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save config.yml to " + configFile, ex);
        }
    }
    
    private static void cacheVariables() {
        cooldown = getConfig().getLong("CreationCooldown");
        maxShops = getConfig().getInt("Global.MaxShopsPerPlayer");
        disallowedCreateWorlds = getConfig().getStringList("DisallowedCreateWorlds");
        disallowedItems = getConfig().getStringList("BlacklistedItems");
        maxBuyPrice = getConfig().getDouble("Global.MaxBuyPrice");
        maxSellPrice = getConfig().getDouble("Global.MaxSellPrice");
        maxBuyPrices = getConfig().getStringList("ItemPrices.Max.Buy");
        maxSellPrices = getConfig().getStringList("ItemPrices.Max.Sell");
        disallowedTradeWorlds = getConfig().getStringList("DisallowedTradeWorlds");
        tradePeriods = getConfig().getStringList("TimePeriods.Periods");
        checkUpdates = getConfig().getBoolean("UpdateChecker.Enabled");
        useMultiThread = getConfig().getBoolean("UpdateChecker.UseMultiThreading");
        maxDailyAdminShopBuy = getConfig().getInt("Daily.Max.Buy.AdminShop");
        maxDailyAdminShopSell = getConfig().getInt("Daily.Max.Sell.AdminShop");
        maxDailyShopBuy = getConfig().getInt("Daily.Max.Buy.PlayerShop");
        maxDailyShopSell = getConfig().getInt("Daily.Max.Sell.PlayerShop");
        maxBuyOverflow = getConfig().getInt("Daily.Max.Buy.OverflowLimit");
        maxSellOverflow = getConfig().getInt("Daily.Max.Sell.OverflowLimit");
        shopeditOwnerFee = getConfig().getDouble("ShopEdit.Name.Fee");
        shopeditAmountFee = getConfig().getDouble("ShopEdit.Quantity.Fee");
        shopeditPriceFee = getConfig().getDouble("ShopEdit.Price.Fee");
        shopeditItemFee = getConfig().getDouble("ShopEdit.Item.Fee");
        shopeditFeePerms_L1 = getConfig().getStringList("ShopEdit.Name.FeePerms");
        shopeditFeePerms_L2 = getConfig().getStringList("ShopEdit.Quantity.FeePerms");
        shopeditFeePerms_L3 = getConfig().getStringList("ShopEdit.Price.FeePerms");
        shopeditFeePerms_L4 = getConfig().getStringList("ShopEdit.Item.FeePerms");
    }
    
    private static void runTransitioner() {
        boolean oldBoolean;
        int oldInt;
        double oldDouble;
        long oldLong;
        List<String> oldList;
        
        if(config.get("UpdateChecker.Use-Multi-Threading") != null) {
            oldBoolean = config.getBoolean("UpdateChecker.Use-Multi-Threading");
            config.set("UpdateChecker.Use-Multi-Threading", null);
            config.set("UpdateChecker.UseMultiThreading", oldBoolean);
        }
        
        if(config.get("Global-Max-Buy-Price") != null) {
            oldInt = config.getInt("Global-Max-Buy-Price");
            config.set("Global-Max-Buy-Price", null);
            config.set("Global.MaxBuyPrice", oldInt);
        }
        
        if(config.get("Global-Max-Sell-Price") != null) {
            oldInt = config.getInt("Global-Max-Sell-Price"); 
            config.set("Global-Max-Sell-Price", null); 
            config.set("Global.MaxSellPrice", oldInt);
        }
        
        if(config.get("Disallowed-Create-Worlds") != null) {
            oldList = config.getStringList("Disallowed-Create-Worlds");
            config.set("Disallowed-Create-Worlds", null);
            config.set("DisallowedCreateWorlds", oldList);
        }
        
        if(config.get("Disallowed-Trade-Worlds") != null) {
            oldList = config.getStringList("Disallowed-Trade-Worlds");
            config.set("Disallowed-Trade-Worlds", null);
            config.set("DisallowedTradeWorlds", oldList);
        }
        
        if(config.get("Disallowed-Items") != null) {
            oldList = config.getStringList("Disallowed-Items");
            config.set("Disallowed-Items", null);
            config.set("BlacklistedItems", oldList);
        }
        
        if(config.get("Max-Buy-Prices") != null) {
            oldList = config.getStringList("Max-Buy-Prices");
            config.set("Max-Buy-Prices", null);
            config.set("ItemPrices.Max.Buy", oldList);
        }
        
        if(config.get("Max-Sell-Prices") != null) {
            oldList = config.getStringList("Max-Sell-Prices");
            config.set("Max-Sell-Prices", null);
            config.set("ItemPrices.Max.Sell", oldList);
        }
        
        if(config.get("Max-Shops-Per-Player") != null) {
            oldInt = config.getInt("Max-Shops-Per-Player");
            config.set("Max-Shops-Per-Player", null);
            config.set("Global.MaxShopsPerPlayer", oldInt);
        }
        
        if(config.get("Disallowed-Time-Periods") != null) {
            oldList = config.getStringList("Disallowed-Time-Periods");
            config.set("Disallowed-Time-Periods", null);
            config.set("TimePeriods.Periods", oldList);
        }
        
        if(config.get("Creation-Cooldown") != null) {
            oldLong = config.getLong("Creation-Cooldown");
            config.set("Creation-Cooldown", null);
            config.set("CreationCooldown", oldLong);
        }
        
        if(config.get("ShopEdit.Name-Line.Fee") != null) {
            oldDouble = config.getDouble("ShopEdit.Name-Line.Fee");
            config.set("ShopEdit.Name-Line.Fee", null);
            config.set("ShopEdit.Name.Fee", oldDouble);
        }
        
        if(config.get("ShopEdit.Name-Line.Fee-Perms") != null) {
            oldList = config.getStringList("ShopEdit.Name-Line.Fee-Perms");
            config.set("ShopEdit.Name-Line", null);
            config.set("ShopEdit.Name.FeePerms", oldList);
        }
        
        if(config.get("ShopEdit.Quantity-Line.Fee") != null) {
            oldDouble = config.getDouble("ShopEdit.Quantity-Line.Fee");
            config.set("ShopEdit.Quantity-Line.Fee", null);
            config.set("ShopEdit.Quantity.Fee", oldDouble);
        }
        
        if(config.get("ShopEdit.Quantity-Line.Fee-Perms") != null) {
            oldList = config.getStringList("ShopEdit.Quantity-Line.Fee-Perms");
            config.set("ShopEdit.Quantity-Line", null);
            config.set("ShopEdit.Quantity.FeePerms", oldList);
        }
        
        if(config.get("ShopEdit.Price-Line.Fee") != null) {
            oldDouble = config.getDouble("ShopEdit.Price-Line.Fee");
            config.set("ShopEdit.Price-Line.Fee", null);
            config.set("ShopEdit.Price.Fee", oldDouble);
        }
        
        if(config.get("ShopEdit.Price-Line.Fee-Perms") != null) {
            oldList = config.getStringList("ShopEdit.Price-Line.Fee-Perms");
            config.set("ShopEdit.Price-Line", null);
            config.set("ShopEdit.Price.FeePerms", oldList);
        }
        
        if(config.get("ShopEdit.Item-Line.Fee") != null) {
            oldDouble = config.getDouble("ShopEdit.Item-Line.Fee");
            config.set("ShopEdit.Item-Line.Fee", null);
            config.set("ShopEdit.Item.Fee", oldDouble);
        }
        
        if(config.get("ShopEdit.Item-Line.Fee-Perms") != null) {
            oldList = config.getStringList("ShopEdit.Item-Line.Fee-Perms");
            config.set("ShopEdit.Item-Line", null);
            config.set("ShopEdit.Item.FeePerms", oldList);
        }
        save();
    }
    
    private static void handleExplanation() {
        File f = new File("plugins/ChestShopUtil/config_explained.yml");
        Plugin plugin = Bukkit.getPluginManager().getPlugin("ChestShopUtil");
        
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Log.severe("An error occured while creating the 'config_explained.yml' file!");
            }
        }
        plugin.saveResource(f.getName(), true);
    }
}
