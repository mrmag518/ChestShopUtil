package com.mrmag518.ChestShopUtil.Util;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EcoHandler {
    private static Economy econ = null;
    
    public static Economy getEconomy() {
        return econ;
    }
    
    public static boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    public static boolean hasAtleast(String victim, double amount) {
        if(econ.getBalance(victim) >= amount) {
            return true;
        }
        return false;
    }
    
    public static void resetBalance(String victim) {
        econ.withdrawPlayer(victim, econ.getBalance(victim));
    }
    
    public static void give(String player, double amount) {
        econ.depositPlayer(player, amount);
    }
    
    public static void take(String player, double amount) {
        econ.withdrawPlayer(player, amount);
    }
    
    public static double get(String player) {
        return econ.getBalance(player);
    }
}
