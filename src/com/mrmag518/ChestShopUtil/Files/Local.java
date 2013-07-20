package com.mrmag518.ChestShopUtil.Files;

import com.mrmag518.ChestShopUtil.CSU;
import com.mrmag518.ChestShopUtil.Util.ConfigManager;
import com.mrmag518.ChestShopUtil.Util.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

public class Local {
    private static final CSU plugin = (CSU)Bukkit.getPluginManager().getPlugin("ChestShopUtil");
    private static ConfigManager manager = new ConfigManager(plugin);
    private static Configuration local = null;
    
    public static void load() {
        File f = new File("plugins" + File.separator + "ChestShopUtil" + File.separator + "local.yml");
        
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, "Error creating local.yml", ex);
            }
        }
        local = manager.getNewConfig("local.yml");

        
        local.addDefault("SHOP_PREFIX", "&a[Shop]", null);
        local.addDefault("LINE", "-----------------------------------------------------", null);
        local.addDefault("BUY_PRICE_EXCEEDS_MAX", "%prefix% &fBuy price is too high! Max: %max%", null);
        local.addDefault("SELL_PRICE_EXCEEDS_MAX", "%prefix% &fSell price is too high! Max: %max%", null);
        local.addDefault("CANNOT_CREATE_SHOP_IN_THIS_WORLD", "%prefix% &fCannot create a shop in this world.", null);
        local.addDefault("BUY_PRICE_EXCEEDS_MAX_FOR_ITEM", "%prefix% &fBuy price for this item is too high! Max: %max%", null);
        local.addDefault("SELL_PRICE_EXCEEDS_MAX_FOR_ITEM", "%prefix% &fSell price for this item is too high! Max: %max%", null);
        //local.addDefault("ITEM_CURRENCY_PRICE_EXCEEDS_MAX", "&cThe owner of this shop has put a price too high! Max: %max%", null);
        //local.addDefault("NOT_ENOUGH_ITEMS_IN_INV", "&cYou do not have enough %item% in your inventory!", null);
        //local.addDefault("ITEM_VALUE_ABOVE_SHOP_PRICE", "&cThe price of this shop is too cheap!", null);
        //local.addDefault("ITEM_CURRENCY_BUY_SUCCESS", "&a[Shop] &fYou bought %bought% %shop_item% from %owner% for %amount% %curr_item%", null);
        local.addDefault("SHOP_CREATION_LIMIT_REACHED", "%prefix% &fYou have reached your shop creation limit! (%limit%)", null);
        local.addDefault("ITEM_IS_DISALLOWED", "%prefix% &fYou are not allowed to create a shop using %item%", null);
        local.addDefault("CANNOT_TRADE_IN_THIS_WORLD", "%prefix% &fYou are not allowed to use shops in this world!", null);
        local.addDefault("CANNOT_TRADE_AT_THIS_MOMENT", "%prefix% &fA disallowed trade period is currently active.", null);
        local.addDefault("CANNOT_ACCESS_THIS_COMMAND", "%prefix% &fYou don't have permissions to do that!", null);
        local.addDefault("COOLDOWN_ACTIVE", "%prefix% &fYou need to wait %cooldown%ms in order to create another shop.", null);
        
        save();
    }
    
    public static Configuration getConfig() {
        if(local == null) {
            load();
        }
        return local;
    }
    
    public static void reload() {
        if(local == null) {
            load();
        }
        local.reloadConfig();
    }
    
    public static void save() {
        if(local == null) {
            load();
        }
        local.saveConfig();
    }
    
    public static String s(LocalOutput output) {
        String str = output.getRawOutput().replace("%prefix%", LocalOutput.SHOP_PREFIX.getRawOutput());
        return colorize(str);
    }
    
    private static String colorize(String s) {
        if (s == null) {
            return null;
        }
        return s.replaceAll("&([0-9a-f])", "\u00A7$1");
    }
}
