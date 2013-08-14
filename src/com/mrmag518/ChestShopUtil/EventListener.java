package com.mrmag518.ChestShopUtil;

import com.Acrobot.Breeze.Utils.MaterialUtil;
import com.Acrobot.Breeze.Utils.PriceUtil;
import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.Acrobot.ChestShop.Events.ShopDestroyedEvent;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import com.Acrobot.ChestShop.Events.TransactionEvent.TransactionType;
import com.Acrobot.ChestShop.Signs.ChestShopSign;
import com.mrmag518.ChestShopUtil.Files.Config;
import com.mrmag518.ChestShopUtil.Files.Local;
import com.mrmag518.ChestShopUtil.Files.LocalOutput;
import com.mrmag518.ChestShopUtil.Files.ShopDB;
import com.mrmag518.ChestShopUtil.Util.Cooldown;
import com.mrmag518.ChestShopUtil.Util.Log;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {
    public static CSU plugin;
    public EventListener(CSU instance) {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void handlePreCreation(PreShopCreationEvent event) {
        Player p = event.getPlayer();
        String world = p.getWorld().getName();
        String price_line = event.getSignLine(ChestShopSign.PRICE_LINE).toLowerCase();
        
        for(String s : Config.disallowedCreateWorlds) {
            if(s.equalsIgnoreCase(world)) {
                if(!p.hasPermission("csu.world.create." + world.toLowerCase()) && !p.hasPermission("csu.world.create.*")) {
                    event.setOutcome(PreShopCreationEvent.CreationOutcome.OTHER);
                    event.getSign().getBlock().breakNaturally();
                    p.sendMessage(Local.s(LocalOutput.CANNOT_CREATE_SHOP_IN_THIS_WORLD));
                    return;
                }
                break;
            }
        }
        
        if(Config.cooldown > 0) {
            if(!Cooldown.hasCompleted(p.getName(), Config.cooldown)) {
                event.setOutcome(PreShopCreationEvent.CreationOutcome.OTHER);
                event.getSign().getBlock().breakNaturally();
                p.sendMessage(Local.s(LocalOutput.COOLDOWN_ACTIVE).replace("%cooldown%", String.valueOf(Config.cooldown)));
            }
        }
        
        if(Config.maxShops > 0) {
            int canCreate = -1;
            
            if(p.hasPermission("csu.maxshops.*")) {
                return;
            }
            
            for(int i = 0; i < ShopDB.maxShopPermValueCap; i++) {
                 if(p.hasPermission("csu.maxshops." + i)) {
                     canCreate = i;
                     break;
                 }
            }
            
            if(canCreate > ShopDB.maxShopPermValueCap) {
                canCreate = ShopDB.maxShopPermValueCap;
            } else if(canCreate < 0) {
                if(ShopDB.getShopsMade(p.getName()) >= Config.maxShops) {
                    event.setOutcome(PreShopCreationEvent.CreationOutcome.OTHER);
                    event.getSign().getBlock().breakNaturally();
                    p.sendMessage(Local.s(LocalOutput.SHOP_CREATION_LIMIT_REACHED).replace("%limit%", String.valueOf(Config.maxShops)));
                }
                return;
            }
            
            if(ShopDB.getShopsMade(p.getName()) >= canCreate) {
                event.setOutcome(PreShopCreationEvent.CreationOutcome.OTHER);
                event.getSign().getBlock().breakNaturally();
                p.sendMessage(Local.s(LocalOutput.SHOP_CREATION_LIMIT_REACHED).replace("%limit%", String.valueOf(canCreate)));
                return;
            }
        }
        ItemStack shopItem = MaterialUtil.getItem(event.getSignLine(ChestShopSign.ITEM_LINE));
        int shopItemID = shopItem.getTypeId();
        byte shopItemData = shopItem.getData().getData();
        
        for(String s : Config.disallowedItems) {
            if(s != null) {
                int id;
                byte data = 0;
                
                if(s.contains(":")) {
                    String[] split = s.split(":");
                    
                    try {
                        id = Integer.parseInt(split[0]);
                        data = Byte.parseByte(split[1]);
                    } catch(NumberFormatException e) {
                        Log.severe("Error parsing id and data value of string line '" + s + "' in the 'Disallowed-Items' list!");
                        return;
                    } 
                } else {
                    try {
                        id = Integer.parseInt(s);
                    } catch(NumberFormatException e) {
                        Log.severe("Error parsing id value of string line '" + s + "' in the 'Disallowed-Items' list!");
                        return;
                    }
                }
                
                if(id >= 0) {
                    if(shopItemData > 0) {
                        if(id == shopItemID && data == shopItemData) {
                            if(!p.hasPermission("csu.bypass.item." + id + ":" + data) && !p.hasPermission("csu.bypass.item.*")) {
                                event.setOutcome(PreShopCreationEvent.CreationOutcome.OTHER);
                                event.getSign().getBlock().breakNaturally();
                                p.sendMessage(Local.s(LocalOutput.ITEM_IS_DISALLOWED).replace("%item%", MaterialUtil.getName(shopItem)));
                                return;
                            }
                            break;
                        }
                    } else if(data == 0) {
                        if(id == shopItemID) {
                            if(!p.hasPermission("csu.bypass.item." + id) && !p.hasPermission("csu.bypass.item.*")) {
                                event.setOutcome(PreShopCreationEvent.CreationOutcome.OTHER);
                                event.getSign().getBlock().breakNaturally();
                                p.sendMessage(Local.s(LocalOutput.ITEM_IS_DISALLOWED).replace("%item%", MaterialUtil.getName(shopItem)));
                                return;
                            }
                            break;
                        }
                    }
                }
            }
        }
        
        if(Config.maxBuyPrice > 0) {
            double price = 0;
            
            if(PriceUtil.hasBuyPrice(price_line)) {
                price = PriceUtil.getBuyPrice(price_line);
            }
            
            if(price > Config.maxBuyPrice) {
                if(!p.hasPermission("csu.bypass.global-maxbuy")) {
                    event.setOutcome(PreShopCreationEvent.CreationOutcome.OTHER);
                    event.getSign().getBlock().breakNaturally();
                    p.sendMessage(Local.s(LocalOutput.BUY_PRICE_EXCEEDS_MAX).replace("%max%", String.valueOf(Config.maxBuyPrice)));
                    return;
                }
            }
        }
        
        if(Config.maxSellPrice > 0) {
            double price = 0;
            
            if(PriceUtil.hasSellPrice(price_line)) {
                price = PriceUtil.getSellPrice(price_line);
            }
            
            if(price > Config.maxSellPrice) {
                if(!p.hasPermission("csu.bypass.global-maxsell")) {
                    event.setOutcome(PreShopCreationEvent.CreationOutcome.OTHER);
                    event.getSign().getBlock().breakNaturally();
                    p.sendMessage(Local.s(LocalOutput.SELL_PRICE_EXCEEDS_MAX).replace("%max%", String.valueOf(Config.maxBuyPrice)));
                }
            }
        }
        
        if(PriceUtil.hasBuyPrice(price_line)) {
            if(!Config.maxBuyPrices.isEmpty()) {
                for(String s : Config.maxSellPrices) {
                    if(s.contains(";")) {
                        String[] split = s.split(";");
                        ItemStack config_item = MaterialUtil.getItem(split[0]);

                        if(config_item.isSimilar(shopItem)) {
                            double configPrice;

                            try {
                                configPrice = Double.parseDouble(split[1]);
                            } catch(NumberFormatException e) {
                                Log.severe("Error parsing a double value while looping trough the 'Max-Buy-Prices' list!");
                                return;
                            }

                            if(configPrice > 0) {
                                double shopPrice = PriceUtil.getBuyPrice(price_line);
                                
                                if(shopPrice > configPrice) {
                                    if(!p.hasPermission("csu.bypass.maxbuy." + shopItem.getTypeId()) && !p.hasPermission("csu.bypass.maxbuy.*")) {
                                        event.setOutcome(PreShopCreationEvent.CreationOutcome.OTHER);
                                        event.getSign().getBlock().breakNaturally();
                                        p.sendMessage(Local.s(LocalOutput.BUY_PRICE_EXCEEDS_MAX_FOR_ITEM).replace("%max%", String.valueOf(configPrice)));
                                        return;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        
        if(PriceUtil.hasSellPrice(price_line)) {
            if(!Config.maxSellPrices.isEmpty()) {
                for(String s : Config.maxSellPrices) {
                    if(s.contains(";")) {
                        String[] split = s.split(";");
                        ItemStack config_item = MaterialUtil.getItem(split[0]);

                        if(config_item.isSimilar(shopItem)) {
                            double configPrice;

                            try {
                                configPrice = Double.parseDouble(split[1]);
                            } catch(NumberFormatException e) {
                                Log.severe("Error parsing a double value while looping trough the 'Max-Sell-Prices' list!");
                                return;
                            }

                            if(configPrice > 0) {
                                double shopPrice = PriceUtil.getSellPrice(price_line);
                                
                                if(shopPrice > configPrice) {
                                    if(!p.hasPermission("csu.bypass.maxsell." + shopItem.getTypeId()) && !p.hasPermission("csu.bypass.maxsell.*")) {
                                        event.setOutcome(PreShopCreationEvent.CreationOutcome.OTHER);
                                        event.getSign().getBlock().breakNaturally();
                                        p.sendMessage(Local.s(LocalOutput.SELL_PRICE_EXCEEDS_MAX_FOR_ITEM).replace("%max%", String.valueOf(configPrice)));
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void handlePostCreation(ShopCreatedEvent event) {
        if(Config.maxShops > 0) {
            String shopOwner = event.getSignLine(ChestShopSign.NAME_LINE);
            
            if(!ChestShopSign.isAdminShop(shopOwner)) {
                ShopDB.incrementShopsMade(shopOwner);
            }
        }
        
        if(Config.cooldown > 0) {
            Cooldown.addPlayer(event.getPlayer().getName());
        }
    }
    
    @EventHandler
    public void handleShopDestroy(ShopDestroyedEvent event) {
        if(Config.maxShops > 0) {
            String shopOwner = event.getSign().getLine(ChestShopSign.NAME_LINE);
            
            if(!ChestShopSign.isAdminShop(shopOwner)) {
                ShopDB.decrementShopsMade(shopOwner);
            }
        }
    }
    
    @EventHandler
    public void handlePreTransaction(PreTransactionEvent event) {
        if(event.isCancelled()) {
            return;
        }
        Player p = event.getClient();
        World world = event.getClient().getWorld();
        
        for(String s : Config.disallowedTradeWorlds) {
            if(s != null) {
                if(world.getName().equalsIgnoreCase(s)) {
                    if(!p.hasPermission("csu.bypass.trade." + world.getName().toLowerCase())  || !p.hasPermission("csu.bypass.trade.*")) {
                        event.setCancelled(PreTransactionEvent.TransactionOutcome.OTHER);
                        p.sendMessage(Local.s(LocalOutput.CANNOT_TRADE_IN_THIS_WORLD));
                    }
                    break;
                }
            }
        }
        
        if(ShopDB.use()) {
            int amount = Integer.parseInt(event.getSign().getLine(ChestShopSign.QUANTITY_LINE));
            
            if(ChestShopSign.isAdminShop(event.getSign())) {
                if(event.getTransactionType() == TransactionType.BUY) {
                    int j = Config.maxDailyAdminShopBuy;
                    
                    if(j > 0) {
                        if(p.hasPermission("csu.daily.adminshop.maxbuy.*")) {
                            return;
                        }
                        int i = ShopDB.getDailyBoughtAmount(p.getName(), true);
                        
                        if(i >= j) {
                            event.setCancelled(PreTransactionEvent.TransactionOutcome.OTHER);
                            p.sendMessage(Local.s(LocalOutput.CANT_BUY_MORE_ADMINSHOP).replace("%limit%", String.valueOf(j)));
                        } else {
                            if(i+amount > j) {
                                int overflow = (i + amount) - j;
                                
                                if(overflow > Config.maxBuyOverflow) {
                                    event.setCancelled(PreTransactionEvent.TransactionOutcome.OTHER);
                                    p.sendMessage(Local.s(LocalOutput.BUY_OVERFLOW_LIMIT));
                                }
                            }
                        }
                    }
                } else if(event.getTransactionType() == TransactionType.SELL) {
                    int j = Config.maxDailyAdminShopSell;
                    
                    if(j > 0) {
                        if(p.hasPermission("csu.daily.adminshop.maxsell.*")) {
                            return;
                        }
                        int i = ShopDB.getDailySoldAmount(p.getName(), true);
                        
                        if(i >= j) {
                            event.setCancelled(PreTransactionEvent.TransactionOutcome.OTHER);
                            p.sendMessage(Local.s(LocalOutput.CANT_SELL_MORE_ADMINSHOP).replace("%limit%", String.valueOf(Config.maxDailyAdminShopSell)));
                        } else {
                            if(i+amount > j) {
                                int overflow = (i + amount) - j;
                                
                                if(overflow > Config.maxSellOverflow) {
                                    event.setCancelled(PreTransactionEvent.TransactionOutcome.OTHER);
                                    p.sendMessage(Local.s(LocalOutput.SELL_OVERFLOW_LIMIT));
                                }
                            }
                        }
                    }
                }
            } else {
                if(event.getTransactionType() == TransactionType.BUY) {
                    int j = Config.maxDailyShopBuy;
                    
                    if(j > 0) {
                        if(p.hasPermission("csu.daily.shop.maxbuy.*")) {
                            return;
                        }
                        int i = ShopDB.getDailyBoughtAmount(p.getName(), false);
                        
                        if(i >= j) {
                            event.setCancelled(PreTransactionEvent.TransactionOutcome.OTHER);
                            p.sendMessage(Local.s(LocalOutput.CANT_BUY_MORE_SHOP).replace("%limit%", String.valueOf(Config.maxDailyShopBuy)));
                        } else {
                            if(i+amount > j) {
                                int overflow = (i + amount) - j;
                                
                                if(overflow > Config.maxBuyOverflow) {
                                    event.setCancelled(PreTransactionEvent.TransactionOutcome.OTHER);
                                    p.sendMessage(Local.s(LocalOutput.BUY_OVERFLOW_LIMIT));
                                }
                            }
                        }
                    }
                } else if(event.getTransactionType() == TransactionType.SELL) {
                    int j = Config.maxDailyShopSell;
                    
                    if(j > 0) {
                        if(p.hasPermission("csu.daily.shop.maxsell.*")) {
                            return;
                        }
                        int i = ShopDB.getDailySoldAmount(p.getName(), false);
                        
                        if(i >= j) {
                            event.setCancelled(PreTransactionEvent.TransactionOutcome.OTHER);
                            p.sendMessage(Local.s(LocalOutput.CANT_SELL_MORE_SHOP).replace("%limit%", String.valueOf(Config.maxDailyShopSell)));
                        } else {
                            if(i+amount > j) {
                                int overflow = (i + amount) - j;
                                
                                if(overflow > Config.maxSellOverflow) {
                                    event.setCancelled(PreTransactionEvent.TransactionOutcome.OTHER);
                                    p.sendMessage(Local.s(LocalOutput.SELL_OVERFLOW_LIMIT));
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if(!p.hasPermission("csu.bypass.time-period")) {
            for(String s : Config.tradePeriods) {
                if(s != null) {
                    if(!s.contains("-")) {
                        Log.severe("Disallowed-Time-Periods contains a line which is not formatted correctly! (" + s + ")");
                    } else {
                        String[] split = s.split("-");
                        long startTick;
                        long endTick;

                        try {
                            startTick = Long.parseLong(split[0]);
                            endTick = Long.parseLong(split[1]);
                        } catch(NumberFormatException e) {
                            Log.severe("Disallowed-Time-Periods contains a line which is not formatted correctly! (" + s +")");
                            continue;
                        }

                        if(startTick < 1 && endTick < 1) {
                            continue;
                        } else {
                            long currTick = world.getTime();

                            if(endTick < startTick) {
                                Log.severe("The end tick can't be lower than the start tick! (" +s + ")");
                                continue;
                            }

                            if(currTick >= startTick && currTick <= endTick) {
                                event.setCancelled(PreTransactionEvent.TransactionOutcome.OTHER);
                                p.sendMessage(Local.s(LocalOutput.CANNOT_TRADE_AT_THIS_MOMENT));
                            }
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void handlePostTransaction(TransactionEvent event) {
        Player p = event.getClient();
        
        if(ShopDB.use()) {
            int amount = Integer.parseInt(event.getSign().getLine(ChestShopSign.QUANTITY_LINE));
            
            if(ChestShopSign.isAdminShop(event.getSign())) {
                if(event.getTransactionType() == TransactionType.BUY) {
                    if(Config.maxDailyAdminShopBuy > 0) {
                        ShopDB.incrementDailyBought(p.getName(), true, amount);
                    }
                } else if(event.getTransactionType() == TransactionType.SELL) {
                    if(Config.maxDailyAdminShopSell > 0) {
                        ShopDB.incrementDailySold(p.getName(), true, amount);
                    }
                }
            } else {
                if(event.getTransactionType() == TransactionType.BUY) {
                    if(Config.maxDailyShopBuy > 0) {
                        ShopDB.incrementDailyBought(p.getName(), false, amount);
                    }
                } else if(event.getTransactionType() == TransactionType.SELL) {
                    if(Config.maxDailyShopSell > 0) {
                        ShopDB.incrementDailySold(p.getName(), false, amount);
                    }
                }
            }
        }
    }
    
    /*
    @EventHandler
    public void handleItemCurrency(PreTransactionEvent event) {
        if(event.isCancelled()) {
            return;
        }
        Player p = event.getClient();
        
        if(Config.getConfig().getBoolean("Item-Currency.Enabled")) {
            Properties.SHOW_TRANSACTION_INFORMATION_CLIENT = false;
            Properties.SHOW_TRANSACTION_INFORMATION_OWNER = false;
            
            double maxPrice = Config.getConfig().getDouble("Item-Currency.Max-Price");
            double shopPrice = event.getPrice();
            
            if(shopPrice > maxPrice) {
                p.sendMessage(Local.s(LocalOutput.ITEM_CURRENCY_PRICE_EXCEEDS_MAX).replace("%max%", String.valueOf(maxPrice)));
            } else {
                Material item = Material.getMaterial(Config.getConfig().getInt("Item-Currency.Item"));
                
                if(item != null && item != Material.AIR) {
                    ItemStack item_is = MaterialUtil.getItem(item.name());
                    int amount = Integer.valueOf(event.getSign().getLine(ChestShopSign.QUANTITY_LINE));
                    
                    if(event.getTransactionType() == TransactionType.BUY) {
                        int amountInInv = InventoryUtil.getAmount(item_is, p.getInventory());
                        double value = Config.getConfig().getDouble("Item-Currency.ItemValue");
                        
                        if(value > shopPrice) {
                            event.setCancelled(PreTransactionEvent.TransactionOutcome.OTHER);
                            p.sendMessage(Local.s(LocalOutput.ITEM_VALUE_ABOVE_SHOP_PRICE));
                        } else {
                            double canAfford = amountInInv * value;
                            
                            
                            if(shopPrice > canAfford) {
                                event.setCancelled(PreTransactionEvent.TransactionOutcome.OTHER);
                                p.sendMessage(Local.s(LocalOutput.NOT_ENOUGH_ITEMS_IN_INV).replace("%item%", MaterialUtil.getName(item_is)));
                            } else {
                                int toRemove = amount;
                                int canRemove = 0;
                                
                                if(shopPrice > value) {
                                    String s = String.valueOf(shopPrice);
                                    canRemove = Integer.parseInt(s);
                                    
                                    if(value == 1) {
                                        int i = (int) shopPrice;
                                        if(i > toRemove) {
                                            
                                        }
                                    } else {
                                        
                                    }
                                }
                                
                                item_is.setAmount(toRemove);
                                InventoryUtil.remove(item_is, p.getInventory());
                            }
                        }
                    } else if(event.getTransactionType() == TransactionType.SELL) {
                        
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void handleItemCurrency(TransactionEvent event) {
        if(Config.getConfig().getBoolean("Item-Currency.Enabled")) {
            double price = event.getPrice();
            int amount = Integer.valueOf(event.getSign().getLine(ChestShopSign.QUANTITY_LINE));
            
            ItemStack bought_is = MaterialUtil.getItem(event.getSign().getLine(ChestShopSign.ITEM_LINE));
            String bought = MaterialUtil.getName(bought_is);
            
            Material curr_mat = Material.getMaterial(Config.getConfig().getInt("Item-Currency.Item"));
            ItemStack curr_is = MaterialUtil.getItem(curr_mat.name());
            String curr_item = MaterialUtil.getName(curr_is);
            
            if(event.getTransactionType() == TransactionType.BUY) {
                String output = Local.s(LocalOutput.ITEM_CURRENCY_BUY_SUCCESS)
                        .replace("%bought%", String.valueOf(amount))
                        .replace("%shop_item%", bought)
                        .replace("%owner%", event.getOwner().getName())
                        .replace("%amount%", "x")
                        .replace("%curr_item%", curr_item);
                
                event.getClient().sendMessage(output);
            }
        }
    }
    */
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void handleJoin(PlayerJoinEvent event) {
        if(plugin.updateFound) {
            Player p = event.getPlayer();
            
            if(p.isOp() || p.hasPermission("csu.getupdate")) {
                final String prefix = "§9[§7ChestShopUtil§9] ";
                
                p.sendMessage(prefix + "A new update is available! (§7" + plugin.verionFound + "§9)");
                p.sendMessage(prefix + "Currently running version §7"+ plugin.getVersion());
            }
        }
    }
}
