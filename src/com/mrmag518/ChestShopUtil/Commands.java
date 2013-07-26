package com.mrmag518.ChestShopUtil;

import com.Acrobot.Breeze.Utils.BlockUtil;
import com.Acrobot.Breeze.Utils.MaterialUtil;
import com.Acrobot.Breeze.Utils.PriceUtil;
import com.Acrobot.ChestShop.ChestShop;
import com.Acrobot.ChestShop.Configuration.Messages;
import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.PreShopCreationEvent.CreationOutcome;
import com.Acrobot.ChestShop.Signs.ChestShopSign;
import com.mrmag518.ChestShopUtil.Files.Config;
import com.mrmag518.ChestShopUtil.Files.Local;
import com.mrmag518.ChestShopUtil.Files.LocalOutput;
import com.mrmag518.ChestShopUtil.Files.ShopDB;
import com.mrmag518.ChestShopUtil.Util.EcoHandler;
import com.mrmag518.ChestShopUtil.Util.Log;
import com.mrmag518.ChestShopUtil.Util.UpdateThread;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

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
                            
                            if(ShopDB.use()) {
                                plugin.startTimeCheck();
                            }
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
                        
                        if(ShopDB.use()) {
                            plugin.startTimeCheck();
                        }
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
        } else if(cmd.getName().equalsIgnoreCase("shopedit")) {
            if(s instanceof Player) {
                Player p = (Player)s;
                
                if(p.hasPermission("csu.command.shopedit.user")) {
                    if(args.length == 0) {
                        p.sendMessage(Local.s(LocalOutput.LINE));
                        p.sendMessage(Local.s(LocalOutput.SHOPEDIT_USAGE_L1));
                        p.sendMessage(Local.s(LocalOutput.SHOPEDIT_USAGE_L2));
                        p.sendMessage(Local.s(LocalOutput.LINE));
                    } else if(args.length >= 2) {
                        String a0 = args[0].toLowerCase();
                        String input = getFinalArg(args, 1);
                        
                        if(a0.equals("owner") || a0.equals("name") || a0.equals("username") || a0.equals("player") || a0.equals("user") || a0.equals("line1") || a0.equals("1")) {
                            editSign(p, ChestShopSign.NAME_LINE, input);
                        } else if(a0.equals("amount") || a0.equals("quantity") || a0.equals("line3") || a0.equals("2")) {
                            editSign(p, ChestShopSign.QUANTITY_LINE, input);
                        } else if(a0.equals("price") || a0.equals("value") || a0.equals("money") || a0.equals("line3") || a0.equals("3")) {
                            editSign(p, ChestShopSign.PRICE_LINE, input);
                        } else if(a0.equals("item") || a0.equals("material") || a0.equals("line4") || a0.equals("4")) {
                            editSign(p, ChestShopSign.ITEM_LINE, input);
                        } else {
                            p.sendMessage(Local.s(LocalOutput.SHOPEDIT_INVALID_ARG));
                        }
                    } else {
                        p.sendMessage(Local.s(LocalOutput.SHOPEDIT_INVALID_ARG));
                    }
                } else {
                    s.sendMessage(Local.s(LocalOutput.CANNOT_ACCESS_THIS_COMMAND));
                }
            } else {
                s.sendMessage("Player command only.");
            }
            return true;
        }
        return false;
    }
    
    private void editSign(Player p, byte line, String input) {
        Block b = p.getTargetBlock(null, 5);
        
        if(b != null) {
            if(BlockUtil.isSign(b)) {
                Sign sign = (Sign)b.getState();
                
                if(ChestShopSign.isValid(sign)) {
                    if(line == ChestShopSign.NAME_LINE) {
                        if(!p.hasPermission("csu.command.shopedit.admin")) {
                            p.sendMessage(Local.s(LocalOutput.SHOPEDIT_CANT_MODIFY_OWNER));
                            return;
                        }
                        
                        if(!EcoHandler.hasAtleast(p.getName(), Config.shopeditOwnerFee)) {
                            p.sendMessage(Local.s(LocalOutput.TOO_POOR).replace("%money%", String.valueOf(Config.shopeditOwnerFee)));
                            return;
                        }
                        String[] lines = {input, sign.getLine(1), sign.getLine(2), sign.getLine(3)};
                        
                        if(ChestShopSign.isValidPreparedSign(lines)) {
                            PreShopCreationEvent event = new PreShopCreationEvent(p, sign, lines);
                            ChestShop.callEvent(event);
                            
                            if(!event.isCancelled()) {
                                EcoHandler.take(p.getName(), Config.shopeditOwnerFee);
                                sign.setLine(line, event.getSignLine(line));
                                sign.update();
                                p.sendMessage(Local.s(LocalOutput.SHOPEDIT_OWNER_EDIT_SUCCESS).replace("%owner%", input));
                            }
                        } else {
                            p.sendMessage(Local.s(LocalOutput.INVALID_NAME).replace("%name%", input));
                        }
                    } else if(line == ChestShopSign.QUANTITY_LINE) {
                        if(!isOwn(p.getName(), sign)) {
                            if(!p.hasPermission("csu.command.shopedit.admin")) {
                                p.sendMessage(Local.s(LocalOutput.SHOPEDIT_CANT_MODIFY_OTHERS));
                                return;
                            }
                        }
                        
                        if(!EcoHandler.hasAtleast(p.getName(), Config.shopeditAmountFee)) {
                            p.sendMessage(Local.s(LocalOutput.TOO_POOR).replace("%money%", String.valueOf(Config.shopeditAmountFee)));
                            return;
                        }
                        String[] lines = {sign.getLine(0), input, sign.getLine(2), sign.getLine(3)};
                        
                        if(ChestShopSign.isValidPreparedSign(lines)) {
                            PreShopCreationEvent event = new PreShopCreationEvent(p, sign, lines);
                            ChestShop.callEvent(event);
                            
                            if(!event.isCancelled()) {
                                EcoHandler.take(p.getName(), Config.shopeditAmountFee);
                                sign.setLine(line, event.getSignLine(line));
                                sign.update();
                                p.sendMessage(Local.s(LocalOutput.SHOPEDIT_AMOUNT_EDIT_SUCCESS).replace("%amount%", input));
                            }
                        } else {
                            p.sendMessage(Local.s(LocalOutput.INVALID_AMOUNT).replace("%amount%", input));
                        }
                    } else if(line == ChestShopSign.PRICE_LINE) {
                        if(!isOwn(p.getName(), sign)) {
                            if(!p.hasPermission("csu.command.shopedit.admin")) {
                                p.sendMessage(Local.s(LocalOutput.SHOPEDIT_CANT_MODIFY_OTHERS));
                                return;
                            }
                        }
                        
                        if(!EcoHandler.hasAtleast(p.getName(), Config.shopeditPriceFee)) {
                            p.sendMessage(Local.s(LocalOutput.TOO_POOR).replace("%money%", String.valueOf(Config.shopeditPriceFee)));
                            return;
                        }
                        String[] lines = {sign.getLine(0), sign.getLine(1), input, sign.getLine(3)};
                        
                        if(ChestShopSign.isValidPreparedSign(lines)) {
                            PreShopCreationEvent event = new PreShopCreationEvent(p, sign, lines);
                            ChestShop.callEvent(event);
                            
                            if(!event.isCancelled()) {
                                EcoHandler.take(p.getName(), Config.shopeditPriceFee);
                                sign.setLine(line, event.getSignLine(line));
                                sign.update();
                                p.sendMessage(Local.s(LocalOutput.SHOPEDIT_PRICE_EDIT_SUCCESS).replace("%price%", input));
                            }
                        } else {
                            p.sendMessage(Local.s(LocalOutput.INVALID_PRICE).replace("%price%", input));
                        }
                    } else if(line == ChestShopSign.ITEM_LINE) {
                        if(!isOwn(p.getName(), sign)) {
                            if(!p.hasPermission("csu.command.shopedit.admin")) {
                                p.sendMessage(Local.s(LocalOutput.SHOPEDIT_CANT_MODIFY_OTHERS));
                                return;
                            }
                        }
                        
                        if(!EcoHandler.hasAtleast(p.getName(), Config.shopeditItemFee)) {
                            p.sendMessage(Local.s(LocalOutput.TOO_POOR).replace("%money%", String.valueOf(Config.shopeditItemFee)));
                            return;
                        }
                        String[] lines = {sign.getLine(0), sign.getLine(1), sign.getLine(2), input};
                        
                        if(ChestShopSign.isValidPreparedSign(lines)) {
                            ItemStack is = MaterialUtil.getItem(input);
                            String signName = MaterialUtil.getSignName(is);
                            PreShopCreationEvent event = new PreShopCreationEvent(p, sign, lines);
                            ChestShop.callEvent(event);
                            
                            if(!event.isCancelled()) {
                                EcoHandler.take(p.getName(), Config.shopeditItemFee);
                                sign.setLine(line, event.getSignLine(line));
                                sign.update();
                                p.sendMessage(Local.s(LocalOutput.SHOPEDIT_ITEM_EDIT_SUCCESS).replace("%item%", signName));
                            }
                        } else {
                            p.sendMessage(Local.s(LocalOutput.INVALID_ITEM).replace("%item%", input));
                        }
                    }
                } else {
                    p.sendMessage(Local.s(LocalOutput.INVALID_SIGN));
                }
            } else {
                p.sendMessage(Local.s(LocalOutput.NOT_SIGN));
            }
        } else {
            p.sendMessage(Local.s(LocalOutput.NOT_SIGN));
        }
    }
    
    // Credits to Essentials for this method.
    private String getFinalArg(final String[] args, final int start) {
        final StringBuilder bldr = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i != start) bldr.append(" ");
            bldr.append(args[i]);
        }
        return bldr.toString();
    }
    
    private boolean isOwn(String player, Sign sign) {
        return sign.getLine(ChestShopSign.NAME_LINE).equalsIgnoreCase(player);
    }
}
