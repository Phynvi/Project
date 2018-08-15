package com.overload.game.definition;

import com.overload.game.model.Item;

/**
 * Represents a definition for a shop.
 *
 * @author Professor Oak
 */
public class ShopDefinition {

    private int shopId;
    private String name = "";
    private boolean ironmanOnly = false;
    private int currencyId = 995;
    private Item[] originalStock;

    public int getId() {
        return shopId;
    }

    public String getName() {
        return name;
    }

    public boolean isIronmanOnly() {
        return ironmanOnly;
    }

    public int getCurrency() {
        return currencyId;
    }

    public Item[] getOriginalStock() {
        return originalStock;
    }
}
