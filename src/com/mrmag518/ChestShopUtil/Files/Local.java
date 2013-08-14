package com.mrmag518.ChestShopUtil.Files;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Local {
    private static FileConfiguration local = null;
    private static File localFile = null;
    
    public static void properLoad() {
        reload();
        load();
        reload();
    }
    
    private static void load() {
        local = getLocal();
        
        local.addDefault("SHOP_PREFIX", "&a[Shop]");
        local.addDefault("LINE", "-----------------------------------------------------");
        local.addDefault("BUY_PRICE_EXCEEDS_MAX", "%prefix% &fBuy price is too high! Max: %max%");
        local.addDefault("SELL_PRICE_EXCEEDS_MAX", "%prefix% &fSell price is too high! Max: %max%");
        local.addDefault("CANNOT_CREATE_SHOP_IN_THIS_WORLD", "%prefix% &fCannot create a shop in this world.");
        local.addDefault("BUY_PRICE_EXCEEDS_MAX_FOR_ITEM", "%prefix% &fBuy price for this item is too high! Max: %max%");
        local.addDefault("SELL_PRICE_EXCEEDS_MAX_FOR_ITEM", "%prefix% &fSell price for this item is too high! Max: %max%");
        //local.addDefault("ITEM_CURRENCY_PRICE_EXCEEDS_MAX", "&cThe owner of this shop has put a price too high! Max: %max%");
        //local.addDefault("NOT_ENOUGH_ITEMS_IN_INV", "&cYou do not have enough %item% in your inventory!");
        //local.addDefault("ITEM_VALUE_ABOVE_SHOP_PRICE", "&cThe price of this shop is too cheap!");
        //local.addDefault("ITEM_CURRENCY_BUY_SUCCESS", "&a[Shop] &fYou bought %bought% %shop_item% from %owner% for %amount% %curr_item%");
        local.addDefault("SHOP_CREATION_LIMIT_REACHED", "%prefix% &fYou have reached your shop creation limit! (%limit%)");
        local.addDefault("ITEM_IS_DISALLOWED", "%prefix% &fYou are not allowed to create a shop using %item%");
        local.addDefault("CANNOT_TRADE_IN_THIS_WORLD", "%prefix% &fYou are not allowed to use shops in this world!");
        local.addDefault("CANNOT_TRADE_AT_THIS_MOMENT", "%prefix% &fA disallowed trade period is currently active.");
        local.addDefault("CANNOT_ACCESS_THIS_COMMAND", "%prefix% &fYou don't have permissions to do that!");
        local.addDefault("COOLDOWN_ACTIVE", "%prefix% &fYou need to wait %cooldown%ms in order to create another shop.");
        local.addDefault("CANT_BUY_MORE_ADMINSHOP", "%prefix% &fCan't buy more from admin shops today. (Limit: %limit%)");
        local.addDefault("CANT_SELL_MORE_ADMINSHOP", "%prefix% &fCan't sell more to admin shops today. (Limit: %limit%)");
        local.addDefault("CANT_BUY_MORE_SHOP", "%prefix% &fCan't buy more from shops today. (Limit: %limit%)");
        local.addDefault("CANT_SELL_MORE_SHOP", "%prefix% &fCan't sell more to shops today. (Limit: %limit%)");
        local.addDefault("BUY_OVERFLOW_LIMIT", "%prefix% &fQuantity would exceed the daily buy limit to an unacceptable extent.");
        local.addDefault("SELL_OVERFLOW_LIMIT", "%prefix% &fQuantity would exceed the daily sell limit to an unacceptable extent.");
        local.addDefault("SHOPEDIT_USAGE_L1", "%prefix% &fAllows you to modify a shop sign.");
        local.addDefault("SHOPEDIT_USAGE_L2", "%prefix% &fCommand syntax: /shopedit <owner|amount|price|item> <text/number>");
        local.addDefault("SHOPEDIT_CANT_MODIFY_OTHERS", "%prefix% &fYou may only edit your own shops!");
        local.addDefault("NOT_SIGN", "%prefix% &fThat block is not a sign!");
        local.addDefault("INVALID_SIGN", "%prefix% &fThat is not a valid chestshop sign!");
        local.addDefault("SHOPEDIT_OWNER_EDIT_SUCCESS", "%prefix% &fShop owner changed to '%owner%'!");
        local.addDefault("INVALID_AMOUNT", "%prefix% &f'%amount%' is not a valid amount!");
        local.addDefault("SHOPEDIT_AMOUNT_EDIT_SUCCESS", "%prefix% &fShop amount changed to '%amount%'!");
        local.addDefault("SHOPEDIT_PRICE_EDIT_SUCCESS", "%prefix% &fShop price changed to '%price%'!");
        local.addDefault("SHOPEDIT_CANT_MODIFY_OWNER", "%prefix% &fYou are not allowed to edit the shop owner.");
        local.addDefault("SHOPEDIT_ITEM_EDIT_SUCCESS", "%prefix% &fShop item changed to '%item%'!");
        local.addDefault("SHOPEDIT_INVALID_ARG", "%prefix% &fInvalid usage! Run '/shopedit' for the correct usage.");
        local.addDefault("INVALID_NAME", "%prefix% &f%name% is not a valid username!");
        local.addDefault("INVALID_PRICE", "%prefix% &f%price% is not a valid price!");
        local.addDefault("INVALID_ITEM", "%prefix% &f%item% is not a valid item!");
        local.addDefault("TOO_POOR", "%prefix% &fYou need at least $%money% for that!");
        
        getLocal().options().copyDefaults(true);
        save();
    }
    
    private static void reload() {
        if(localFile == null) {
            localFile = new File("plugins/ChestShopUtil/local.yml");
        }
        local = YamlConfiguration.loadConfiguration(localFile);
    }
    
    public static FileConfiguration getLocal() {
        if(local == null) {
            reload();
        }
        return local;
    }
    
    public static void save() {
        if(local == null || localFile == null) {
            return;
        }
        
        try {
            local.save(localFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save local.yml to " + localFile, ex);
        }
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
