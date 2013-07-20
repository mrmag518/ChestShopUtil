package com.mrmag518.ChestShopUtil;

import com.mrmag518.ChestShopUtil.Files.Config;
import com.mrmag518.ChestShopUtil.Files.Local;
import com.mrmag518.ChestShopUtil.Files.LocalOutput;
import com.mrmag518.ChestShopUtil.Files.ShopDB;
import com.mrmag518.ChestShopUtil.Util.UpdateThread;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    public CSU plugin;
    public Commands(CSU instance) {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String commandLabel, String[] args) {
        if(cmd.getName().equalsIgnoreCase("chestshoputil")) {
            if(s instanceof Player) {
                if(args.length == 0) {
                    if(s.hasPermission("csu.command.main")) {
                        s.sendMessage(Local.s(LocalOutput.LINE));
                        s.sendMessage(ChatColor.BLUE + "ChestShopUtil version: " + ChatColor.GRAY + plugin.getVersion());
                        if(plugin.updateFound) {
                            s.sendMessage(ChatColor.BLUE + "Update available: " + ChatColor.GRAY + "Yes");
                        } else {
                            s.sendMessage(ChatColor.BLUE + "Update available: " + ChatColor.GRAY + "No");
                        }
                        s.sendMessage(Local.s(LocalOutput.LINE));
                    } else {
                        s.sendMessage(Local.s(LocalOutput.CANNOT_ACCESS_THIS_COMMAND));
                    }
                } else if(args.length > 0) {
                    if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("commands")) {
                        if(s.hasPermission("csu.command.help")) {
                            s.sendMessage(Local.s(LocalOutput.LINE));
                            s.sendMessage(ChatColor.BLUE + "/csu reload " + ChatColor.WHITE + "- " + ChatColor.GRAY + "Reload the plugin.");
                            s.sendMessage(ChatColor.BLUE + "/csu version " + ChatColor.WHITE + "- " + ChatColor.GRAY + "Check the version of the plugin.");
                            s.sendMessage(ChatColor.BLUE + "/csu updatecheck " + ChatColor.WHITE + "- " + ChatColor.GRAY + "Check for an available update.");
                            s.sendMessage(Local.s(LocalOutput.LINE));
                        } else {
                            s.sendMessage(Local.s(LocalOutput.CANNOT_ACCESS_THIS_COMMAND));
                        }
                    } else if(args[0].equalsIgnoreCase("reload")) {
                        if(s.hasPermission("csu.command.reload")) {
                            if(!plugin.getDataFolder().exists()) {
                                plugin.getDataFolder().mkdir();
                            }
                            Config.load();
                            Local.load();
                            ShopDB.properLoad();
                            s.sendMessage(ChatColor.GRAY + "ChestShopUtil v" + plugin.getVersion() + ChatColor.BLUE + " reloaded.");
                        } else {
                            s.sendMessage(Local.s(LocalOutput.CANNOT_ACCESS_THIS_COMMAND));
                        }
                    } else if(args[0].equalsIgnoreCase("version")) {
                        if(s.hasPermission("csu.command.version")) {
                            s.sendMessage(ChatColor.GRAY + "ChestShopUtil v" + plugin.getVersion());
                        } else {
                            s.sendMessage(Local.s(LocalOutput.CANNOT_ACCESS_THIS_COMMAND));
                        }
                    } else if(args[0].equalsIgnoreCase("updatecheck")) {
                        if(s.hasPermission("csu.command.updatecheck")) {
                            s.sendMessage(ChatColor.BLUE + "Running update check ..");
                            
                            if(Config.checkUpdates) {
                                if(Config.useMultiThread) {
                                    Thread t = new Thread(new UpdateThread(plugin, s));
                                    t.start();
                                } else {
                                    plugin.updateCheck(s);
                                }
                            } else {
                                s.sendMessage(ChatColor.RED + "Update checking is disabled in the config!");
                            }
                        } else {
                            s.sendMessage(Local.s(LocalOutput.CANNOT_ACCESS_THIS_COMMAND));
                        }
                    } else {
                        s.sendMessage(ChatColor.RED + "Unrecognized command! Run '/csu help' for a list of commands.");
                    }
                }
            } else {
                if(args.length == 0) {
                    s.sendMessage(Local.s(LocalOutput.LINE));
                    s.sendMessage("ChestShopUtil version: " + plugin.getVersion());
                    if(plugin.updateFound) {
                        s.sendMessage("Update available: Yes");
                    } else {
                        s.sendMessage("Update available: No");
                    }
                    s.sendMessage(Local.s(LocalOutput.LINE));
                } else if(args.length > 0) {
                    if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("commands")) {
                        s.sendMessage(Local.s(LocalOutput.LINE));
                        s.sendMessage("/csu reload - Reload the plugin.");
                        s.sendMessage("/csu version - Check the version of the plugin.");
                        s.sendMessage("/csu updatecheck - Check for an available update.");
                        s.sendMessage(Local.s(LocalOutput.LINE));
                    } else if(args[0].equalsIgnoreCase("reload")) {
                        if(!plugin.getDataFolder().exists()) {
                            plugin.getDataFolder().mkdir();
                        }
                        Config.load();
                        Local.load();
                        ShopDB.properLoad();
                        s.sendMessage("ChestShopUtil v" + plugin.getVersion() + " reloaded.");
                    } else if(args[0].equalsIgnoreCase("version")) {
                        s.sendMessage("ChestShopUtil v" + plugin.getVersion());
                    } else if(args[0].equalsIgnoreCase("updatecheck")) {
                        s.sendMessage("Running update check ..");

                        if(Config.checkUpdates) {
                            if(Config.useMultiThread) {
                                Thread t = new Thread(new UpdateThread(plugin, s));
                                t.start();
                            } else {
                                plugin.updateCheck(s);
                            }
                        } else {
                            s.sendMessage("Update checking is disabled in the config!");
                        }
                    } else {
                        s.sendMessage("Unrecognized command! Run '/csu help' for a list of commands.");
                    }
                }
            }
            return true;
        }
        return false;
    }
}
