package com.mrmag518.ChestShopUtil.Files;

public enum LocalOutput {
    SHOP_PREFIX(Local.getLocal().getString("SHOP_PREFIX")),
    LINE(Local.getLocal().getString("LINE")),
    BUY_PRICE_EXCEEDS_MAX(Local.getLocal().getString("BUY_PRICE_EXCEEDS_MAX")),
    SELL_PRICE_EXCEEDS_MAX(Local.getLocal().getString("SELL_PRICE_EXCEEDS_MAX")),
    CANNOT_CREATE_SHOP_IN_THIS_WORLD(Local.getLocal().getString("CANNOT_CREATE_SHOP_IN_THIS_WORLD")),
    BUY_PRICE_EXCEEDS_MAX_FOR_ITEM(Local.getLocal().getString("BUY_PRICE_EXCEEDS_MAX_FOR_ITEM")),
    SELL_PRICE_EXCEEDS_MAX_FOR_ITEM(Local.getLocal().getString("SELL_PRICE_EXCEEDS_MAX_FOR_ITEM")),
    //ITEM_CURRENCY_PRICE_EXCEEDS_MAX(Local.getConfig().getString("ITEM_CURRENCY_PRICE_EXCEEDS_MAX")),
    //NOT_ENOUGH_ITEMS_IN_INV(Local.getConfig().getString("NOT_ENOUGH_ITEMS_IN_INV")),
    //ITEM_VALUE_ABOVE_SHOP_PRICE(Local.getConfig().getString("ITEM_VALUE_ABOVE_SHOP_PRICE")),
    //ITEM_CURRENCY_BUY_SUCCESS(Local.getConfig().getString("ITEM_CURRENCY_BUY_SUCCESS")),
    SHOP_CREATION_LIMIT_REACHED(Local.getLocal().getString("SHOP_CREATION_LIMIT_REACHED")),
    ITEM_IS_DISALLOWED(Local.getLocal().getString("ITEM_IS_DISALLOWED")),
    CANNOT_TRADE_IN_THIS_WORLD(Local.getLocal().getString("CANNOT_TRADE_IN_THIS_WORLD")),
    CANNOT_TRADE_AT_THIS_MOMENT(Local.getLocal().getString("CANNOT_TRADE_AT_THIS_MOMENT")),
    CANNOT_ACCESS_THIS_COMMAND(Local.getLocal().getString("CANNOT_ACCESS_THIS_COMMAND")),
    COOLDOWN_ACTIVE(Local.getLocal().getString("COOLDOWN_ACTIVE")),
    CANT_BUY_MORE_ADMINSHOP(Local.getLocal().getString("CANT_BUY_MORE_ADMINSHOP")),
    CANT_SELL_MORE_ADMINSHOP(Local.getLocal().getString("CANT_SELL_MORE_ADMINSHOP")),
    CANT_BUY_MORE_SHOP(Local.getLocal().getString("CANT_BUY_MORE_SHOP")),
    CANT_SELL_MORE_SHOP(Local.getLocal().getString("CANT_SELL_MORE_SHOP")),
    BUY_OVERFLOW_LIMIT(Local.getLocal().getString("BUY_OVERFLOW_LIMIT")),
    SELL_OVERFLOW_LIMIT(Local.getLocal().getString("SELL_OVERFLOW_LIMIT")),
    SHOPEDIT_USAGE_L1(Local.getLocal().getString("SHOPEDIT_USAGE_L1")),
    SHOPEDIT_USAGE_L2(Local.getLocal().getString("SHOPEDIT_USAGE_L2")),
    SHOPEDIT_CANT_MODIFY_OTHERS(Local.getLocal().getString("SHOPEDIT_CANT_MODIFY_OTHERS")),
    NOT_SIGN(Local.getLocal().getString("NOT_SIGN")),
    INVALID_SIGN(Local.getLocal().getString("INVALID_SIGN")),
    SHOPEDIT_OWNER_EDIT_SUCCESS(Local.getLocal().getString("SHOPEDIT_OWNER_EDIT_SUCCESS")),
    INVALID_AMOUNT(Local.getLocal().getString("INVALID_AMOUNT")),
    SHOPEDIT_AMOUNT_EDIT_SUCCESS(Local.getLocal().getString("SHOPEDIT_AMOUNT_EDIT_SUCCESS")),
    SHOPEDIT_PRICE_EDIT_SUCCESS(Local.getLocal().getString("SHOPEDIT_PRICE_EDIT_SUCCESS")),
    SHOPEDIT_CANT_MODIFY_OWNER(Local.getLocal().getString("SHOPEDIT_CANT_MODIFY_OWNER")),
    SHOPEDIT_ITEM_EDIT_SUCCESS(Local.getLocal().getString("SHOPEDIT_ITEM_EDIT_SUCCESS")),
    SHOPEDIT_INVALID_ARG(Local.getLocal().getString("SHOPEDIT_INVALID_ARG")),
    INVALID_NAME(Local.getLocal().getString("INVALID_NAME")),
    INVALID_PRICE(Local.getLocal().getString("INVALID_PRICE")),
    INVALID_ITEM(Local.getLocal().getString("INVALID_ITEM")),
    TOO_POOR(Local.getLocal().getString("TOO_POOR"));
    
    private String output;
    
    private LocalOutput(String msg) {
        output = msg;
    }
    
    public String getRawOutput() {
        return output;
    }
}
