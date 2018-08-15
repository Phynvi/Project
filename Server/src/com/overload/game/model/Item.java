package com.overload.game.model;

import com.overload.game.definition.ItemDefinition;

/**
 * Represents an item.
 *
 * @author Professor Oak
 */

public class Item {

    /**
     * The item id.
     */
    private int id;
    /**
     * Amount of the item.
     */
    private int amount;

    /**
     * An Item object constructor.
     *
     * @param id     Item id.
     * @param amount Item amount.
     */
    public Item(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    /**
     * An Item object constructor.
     *
     * @param id Item id.
     */
    public Item(int id) {
        this(id, 1);
    }

    /**
     * Gets the item's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the item's id.
     *
     * @param id New item id.
     */
    public Item setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * Gets the amount of the item.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the item.
     */
    public Item setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Checks if this item is valid or not.
     *
     * @return
     */
    public boolean isValid() {
        return id > 0 && amount > 0;
    }

    /**
     * Increment the amount by 1.
     */
    public void incrementAmount() {
        if ((amount + 1) > Integer.MAX_VALUE) {
            return;
        }
        amount++;
    }

    /**
     * Decrement the amount by 1.
     */
    public void decrementAmount() {
        if ((amount - 1) < 0) {
            return;
        }
        amount--;
    }

    /**
     * Increment the amount by the specified amount.
     */
    public void incrementAmountBy(int amount) {
        if ((this.amount + amount) > Integer.MAX_VALUE) {
            this.amount = Integer.MAX_VALUE;
        } else {
            this.amount += amount;
        }
    }

    /**
     * Decrement the amount by the specified amount.
     */
    public void decrementAmountBy(int amount) {
        if ((this.amount - amount) < 1) {
            this.amount = 0;
        } else {
            this.amount -= amount;
        }
    }

    /** ITEM RARITY **/
    public ItemRarity rarity;

    public Item setRarity(ItemRarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public ItemDefinition getDefinition() {
        return ItemDefinition.forId(id);
    }

    @Override
    public Item clone() {
        return new Item(id, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item))
            return false;
        Item item = (Item) o;
        return item.getId() == this.getId() && item.getAmount() == this.getAmount();
    }
    
    public Item getBrokenState(int id) {
    	return getBrokenState(new Item(id));
    }
    
    private static Item[] BROKEN_ITEMS = {
    	new Item(20445),
    	new Item(20447),
    	new Item(20449),
    	new Item(20451),
    	new Item(20453),
    	new Item(20455),
    	new Item(20457),
    	new Item(20459),
    	new Item(20461),
    	new Item(20463),
    	new Item(20465),
    	new Item(20467),
    	new Item(20469),
    	new Item(20471),
    	new Item(20473),
    	new Item(20475),
    	new Item(20477),
    	new Item(20479),
    	new Item(20481),
    	new Item(20483),
    	new Item(20485),
    	new Item(20487),
    	new Item(20489),
    	new Item(20491),
    	new Item(20493),
    	new Item(20495),
    	new Item(20497),
    	new Item(20499),
    	new Item(20501),
    	new Item(20503),
    	new Item(20505),
    	new Item(20507),
    	new Item(20509),
    	new Item(20511),
    	new Item(20513),
    	new Item(20515),
    	new Item(20537),
    	new Item(20539),
    	new Item(20541),
    	new Item(21287),
    	new Item(21289)
    };
    
    public static Item getBrokenState(Item item) {
    	for (int i = 0; i < BROKEN_ITEMS.length; i++) {
    		if (BROKEN_ITEMS[i].getDefinition().getName().toLowerCase().contains(item.getDefinition().getName().toLowerCase())) {
    			return BROKEN_ITEMS[i];
    		}
    	}
    	return null;
    }
    
}