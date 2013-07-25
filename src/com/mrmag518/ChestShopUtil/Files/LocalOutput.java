package com.mrmag518.ChestShopUtil.Files;

public enum LocalOutput {
    SHOP_PREFIX(Local.getConfig().getString("SHOP_PREFIX")),
    LINE(Local.getConfig().getString("LINE")),
    BUY_PRICE_EXCEEDS_MAX(Local.getConfig().getString("BUY_PRICE_EXCEEDS_MAX")),
    SELL_PRICE_EXCEEDS_MAX(Local.getConfig().getString("SELL_PRICE_EXCEEDS_MAX")),
    CANNOT_CREATE_SHOP_IN_THIS_WORLD(Local.getConfig().getString("CANNOT_CREATE_SHOP_IN_THIS_WORLD")),
    BUY_PRICE_EXCEEDS_MAX_FOR_ITEM(Local.getConfig().getString("BUY_PRICE_EXCEEDS_MAX_FOR_ITEM")),
    SELL_PRICE_EXCEEDS_MAX_FOR_ITEM(Local.getConfig().getString("SELL_PRICE_EXCEEDS_MAX_FOR_ITEM")),
    //ITEM_CURRENCY_PRICE_EXCEEDS_MAX(Local.getConfig().getString("ITEM_CURRENCY_PRICE_EXCEEDS_MAX")),
    //NOT_ENOUGH_ITEMS_IN_INV(Local.getConfig().getString("NOT_ENOUGH_ITEMS_IN_INV")),
    //ITEM_VALUE_ABOVE_SHOP_PRICE(Local.getConfig().getString("ITEM_VALUE_ABOVE_SHOP_PRICE")),
    //ITEM_CURRENCY_BUY_SUCCESS(Local.getConfig().getString("ITEM_CURRENCY_BUY_SUCCESS")),
    SHOP_CREATION_LIMIT_REACHED(Local.getConfig().getString("SHOP_CREATION_LIMIT_REACHED")),
    ITEM_IS_DISALLOWED(Local.getConfig().getString("ITEM_IS_DISALLOWED")),
    CANNOT_TRADE_IN_THIS_WORLD(Local.getConfig().getString("CANNOT_TRADE_IN_THIS_WORLD")),
    CANNOT_TRADE_AT_THIS_MOMENT(Local.getConfig().getString("CANNOT_TRADE_AT_THIS_MOMENT")),
    CANNOT_ACCESS_THIS_COMMAND(Local.getConfig().getString("CANNOT_ACCESS_THIS_COMMAND")),
    COOLDOWN_ACTIVE(Local.getConfig().getString("COOLDOWN_ACTIVE")),
    CANT_BUY_MORE_ADMINSHOP(Local.getConfig().getString("CANT_BUY_MORE_ADMINSHOP")),
    CANT_SELL_MORE_ADMINSHOP(Local.getConfig().getString("CANT_SELL_MORE_ADMINSHOP")),
    CANT_BUY_MORE_SHOP(Local.getConfig().getString("CANT_BUY_MORE_SHOP")),
    CANT_SELL_MORE_SHOP(Local.getConfig().getString("CANT_SELL_MORE_SHOP")),
    BUY_OVERFLOW_LIMIT(Local.getConfig().getString("BUY_OVERFLOW_LIMIT")),
    SELL_OVERFLOW_LIMIT(Local.getConfig().getString("SELL_OVERFLOW_LIMIT")),
    SHOPEDIT_USAGE_L1(Local.getConfig().getString("SHOPEDIT_USAGE_L1")),
    SHOPEDIT_USAGE_L2(Local.getConfig().getString("SHOPEDIT_USAGE_L2")),
    SHOPEDIT_CANT_MODIFY_OTHERS(Local.getConfig().getString("SHOPEDIT_CANT_MODIFY_OTHERS")),
    NOT_SIGN(Local.getConfig().getString("NOT_SIGN")),
    INVALID_SIGN(Local.getConfig().getString("INVALID_SIGN")),
    SHOPEDIT_OWNER_EDIT_SUCCESS(Local.getConfig().getString("SHOPEDIT_OWNER_EDIT_SUCCESS")),
    INVALID_NUMBER(Local.getConfig().getString("INVALID_NUMBER")),
    INVALID_AMOUNT(Local.getConfig().getString("INVALID_AMOUNT")),
    SHOPEDIT_AMOUNT_EDIT_SUCCESS(Local.getConfig().getString("SHOPEDIT_AMOUNT_EDIT_SUCCESS"));
    
    private String output;
    
    private LocalOutput(String msg) {
        output = msg;
    }
    
    public String getRawOutput() {
        return output;
    }
}
