package com.mrmag518.ChestShopUtil.Util;

import java.util.HashMap;
import java.util.Map;

public class Cooldown {
    private static Map<String, Long> database = new HashMap<String, Long>();

    public static void clear() {
        database.clear();
    }
    
    public static void addPlayer(String player) {
        database.put(player, System.currentTimeMillis());
    }
    
    public static void removePlayer(String player) {
        database.remove(player);
    }
    
    public static boolean hasCompleted(String player, long cooldown) {
        long currTime = System.currentTimeMillis();
        long databaseTime;
        
        if(database.containsKey(player)) {
            databaseTime = database.get(player).longValue();
        } else {
            return true;
        }
        long diff = currTime - databaseTime;
        
        if(diff > cooldown) {
            removePlayer(player);
            return true;
        }
        return false;
    }
}
