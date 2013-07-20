package com.mrmag518.ChestShopUtil.Util;

import com.mrmag518.ChestShopUtil.CSU;

import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;

public class UpdateThread implements Runnable {
    private CSU plugin;
    private CommandSender s = null;
    public UpdateThread(CSU instance, @Nullable CommandSender sender) {
        plugin = instance;
        s = sender;
    }
    
    @Override
    public void run() {
        if(plugin != null) {
            plugin.updateCheck(s);
        }
    }
}
