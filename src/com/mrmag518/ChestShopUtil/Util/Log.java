
package com.mrmag518.ChestShopUtil.Util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
    private static final Logger log = Logger.getLogger("Minecraft");
    private static final String PREFIX = "[ChestShopUtil] ";
    
    public static void info(String output) {
        log.log(Level.INFO,PREFIX + "{0}", output);
    }
    
    public static void severe(String output) {
        log.log(Level.SEVERE,PREFIX + "{0}", output);
    }
    
    public static void warning(String output) {
        log.log(Level.WARNING,PREFIX + "{0}", output);
    }
}
