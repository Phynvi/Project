package com.overload.game.model.container.shop;

import com.overload.game.World;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.PlayerModes;
import com.overload.game.model.PlayerStatus;
import com.overload.game.model.syntax.impl.BuyX;
import com.overload.game.model.syntax.impl.SellX;
import com.overload.game.task.TaskManager;
import com.overload.game.task.impl.ShopRestockTask;
import com.overload.util.ItemIdentifiers;
import com.overload.util.Misc;
import com.overload.util.ShopIdentifiers;

import java.util.HashMap;
import java.util.Map;

public class ShopManager extends ShopIdentifiers {

    /**
     * A {@link Map} with all of our shops and their ids.
     */
    public static final Map<Integer, Shop> shops = new HashMap<Integer, Shop>();

    /**
     * Attempts to open the shop with the given id.
     *
     * @param player
     * @param id
     */
    public static void open(Player player, int id) {
        Shop shop = shops.get(id);
        if (shop != null) {
            open(player, shop, true);
        }
    }

    /**
     * Opens the given shop.
     *
     * @param player
     * @param shop
     */
    public static void open(Player player, Shop shop, boolean scrollReset) {
    	if (player.getMode().isEqualOrHigher(PlayerModes.IRON_MAN) && !isIronmanShop(shop)) {
			player.sendMessage("Your game mode restrics you from viewing this shop.");
			return;
		}
		if (!player.getMode().isEqualOrHigher(PlayerModes.IRON_MAN) && isIronmanShop(shop)) {
			player.sendMessage("You must be an Ironman to view this shop.");
			return;
		}
        // Update current shop
        player.setShop(shop);

        // Send shop items
        player.getPacketSender().sendItemContainer(player.getInventory(), Shop.INVENTORY_INTERFACE_ID);
        player.getPacketSender().sendInterfaceItems(Shop.ITEM_CHILD_ID, shop.getCurrentStockList());

        // Send shop name
        player.getPacketSender().sendString(Shop.NAME_INTERFACE_CHILD_ID, shop.getName());

        // Send interface set, this shows the actual shop interface id
        // along with the inventory item options.
        if (!(player.getEnterSyntax() instanceof SellX || player.getEnterSyntax() instanceof BuyX)) {
            player.getPacketSender().sendInterfaceSet(Shop.INTERFACE_ID, Shop.INVENTORY_INTERFACE_ID - 1);
        }

        // Reset scroll bar if needed.
        if (scrollReset) {
            player.getPacketSender().sendInterfaceScrollReset(Shop.SCROLL_BAR_INTERFACE_ID);
        }

        // Set scroll bar size
        //TODO: nicer code
        if (shop.getOriginalStock().length < 37) {
            player.getPacketSender().sendScrollbarHeight(Shop.SCROLL_BAR_INTERFACE_ID, 0);
        } else {
            int rows = (shop.getOriginalStock().length % 9 == 0 ? (shop.getOriginalStock().length / 9) : ((shop.getOriginalStock().length / 9) + 1));
            player.getPacketSender().sendScrollbarHeight(Shop.SCROLL_BAR_INTERFACE_ID, rows * 56);
        }

        // Update player's status..
        player.setStatus(PlayerStatus.SHOPPING);
    }

	/**
	 * Checks if the shop is Ironman+ only
	 * 
	 * @param shop
	 */
	public static boolean isIronmanShop(Shop shop) {
		return shop.isIronmanOnly();
	}

    /**
     * Refreshes the given shop for all players who are viewing it.
     *
     * @param shop
     */
    public static void refresh(Shop shop) {
        for (Player player : World.getPlayers()) {
            if (player == null) {
                continue;
            }

            // If the player's viewing the shop, simply re-open it..
            if (viewingShop(player, shop.getId())) {
                open(player, shop, false);
            }
        }
    }

    /**
     * Attempts to price check an item.
     *
     * @param player   The player pricechecking.
     * @param itemId   The item id to price check.
     * @param slot     The item's slot.
     * @param shopItem Are we pricechecking a shop item or player item?
     */
    public static void priceCheck(Player player, int itemId, int slot, boolean shopItem) {
        // Get the shop..
        Shop shop = player.getShop();

        // First, we will attempt to verify the shop and the item.
        boolean flag = false;

        if (shop == null || player.getStatus() != PlayerStatus.SHOPPING
                || player.getInterfaceId() != Shop.INTERFACE_ID) {
            flag = true;
        } else if (shopItem && (slot >= player.getShop().getCurrentStock().length
                || player.getShop().getCurrentStock()[slot] == null
                || player.getShop().getCurrentStock()[slot].getId() != itemId)) {
            flag = true;
        } else if (!shopItem && (slot >= player.getInventory().capacity()
                || player.getInventory().getItems()[slot].getId() != itemId)) {
            flag = true;
        }

        // If we failed to verify, simply close the shop for the player.
        if (flag) {
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }

        // Check if the shop sells the item.
        if (!buysItems(shop, itemId)) {

            // If the player is trying to pricecheck an item in their inventory,
            // let them know they can't sell it here.
            // Only allow items to be sold to shops which sell them originally.
            //if (!shopItem) {
                player.getPacketSender().sendMessage("You cannot sell this item to this shop.");
            //}

            return;
        }

        // Get the item's definition..
        ItemDefinition def = ItemDefinition.forId(itemId);

        // Get the item's value..
        int itemValue = getItemValue(player, def, shop);

        // Get the currency's name..
        String currency = getCurrencyName(player, def, shop.getId());

        // If player isn't price checking a shop item..
        if (!shopItem) {

            // Player trying to price check an unsellable item.
            if (!def.isSellable()) {
                player.getPacketSender().sendMessage("This item cannot be sold to a shop.");
                return;
            }

            // Apply taxes.
            if (itemValue > 1) {
                itemValue = (int) (itemValue * Shop.SALES_TAX_MODIFIER);
            }
        }

        // Verify value..
        if (itemValue <= 0) {
            player.getPacketSender().sendMessage("This item has no value.");
            return;
        }

        // Send value..
        String message = "@dre@" + def.getName() + "@bla@" + (!shopItem ? ": shop will buy for " : " currently costs ")
                + "@dre@" + Misc.insertCommasToNumber(Integer.toString(itemValue)) + " " + currency + ".";
        player.getPacketSender().sendMessage(message);
    }
    
    public static int priceCheck(int itemId) {
    	ItemDefinition itemDef = ItemDefinition.forId(itemId);
        int ret = itemDef.getValue();
        if (ret <= 0)
        	ret = itemDef.getDropValue();
        return ret;
    }

    public static void buyItem(Player player, int slot, int itemId, int amount) {

        // Get the shop..
        Shop shop = player.getShop();

        // First, we will attempt to verify the shop and the item.
        boolean flag = false;

        if (shop == null || player.getStatus() != PlayerStatus.SHOPPING
                || player.getInterfaceId() != Shop.INTERFACE_ID) {
            flag = true;
        } else if (slot >= player.getShop().getCurrentStock().length || player.getShop().getCurrentStock()[slot] == null
                || player.getShop().getCurrentStock()[slot].getId() != itemId) {
            flag = true;
        }

        // Check if we failed the verification.
        if (flag) {
            return;
        }

        // Max buy limit..
        if (amount > 5000) {
            player.getPacketSender().sendMessage("You can only buy a maximum of 5000 at a time.");
            return;
        }

        // Get the item's definition..
        ItemDefinition itemDef = ItemDefinition.forId(itemId);

        // Get the item's value..
        int itemValue = getItemValue(player, itemDef, shop);
        if (itemValue <= 0) {
            return;
        }

        // Used for checking if a player
        // actually managed to buy an item.
        boolean bought = false;

        // Start buying the item.
        for (int i = amount; i > 0; i--) {

            // Get the player's currency amount..
            int currencyAmount = getCurrencyAmount(player, itemDef, shop);

            // Make sure the item is still in the shop..
            if (player.getShop().getCurrentStock()[slot] == null) {
                break;
            }

            // The amount of this item in the shop.
            int shopItemAmount = shop.getCurrentStock()[slot].getAmount();

            // Verify the item's amount.
            if (shopItemAmount <= 1 && !deletesItems(shop.getId())) {
                player.getPacketSender().sendMessage("This item is currently out of stock. Come back later.");
                break;
            }

            // Inventory space..
            if (player.getInventory().isFull()) {
                if (!(itemDef.isStackable() && player.getInventory().contains(itemId))) {
                    player.getInventory().full();
                    break;
                }
            }

            // Check if we can afford the item or not.
            if (currencyAmount < itemValue) {
                player.getPacketSender().sendMessage("You can't afford that.");
                break;
            }

            // Handle actual purchase..
            if (!itemDef.isStackable()) {

                // Deduct player's currency..
                decrementCurrency(player, itemDef, itemValue, shop);

                // Remove item from shop..
                shop.removeItem(itemId, 1);

                // Add item to player's inventory..
                player.getInventory().add(itemId, 1);

                // Flag as bought..
                bought = true;

            } else {
                int canBeBought = (currencyAmount / itemValue);

                if (canBeBought >= i) {
                    canBeBought = i;
                }

                // Make sure player can't buy more than the stock amount allows.
                if (canBeBought >= shopItemAmount) {
                    canBeBought = deletesItems(shop.getId()) ? shopItemAmount : shopItemAmount - 1;
                }

                if (canBeBought == 0)
                    break;

                // Deduct player's currency..
                decrementCurrency(player, itemDef, itemValue * canBeBought, shop);

                // Remove items from shop..
                shop.removeItem(itemId, canBeBought);

                // Add items to player's inventory..
                player.getInventory().add(itemId, canBeBought);

                // Flag as bought..
                bought = true;
                break;
            }
        }

        if (bought) {
            ShopManager.refresh(shop);
            if (!shop.isRestocking()) {
                TaskManager.submit(new ShopRestockTask(shop));
                shop.setRestocking(true);
            }
        }
    }

    public static void sellItem(Player player, int slot, int itemId, int amount) {

        // Get the shop..
        Shop shop = player.getShop();

        // First, we will attempt to verify the shop and the item.
        boolean flag = false;

        if (shop == null || player.getStatus() != PlayerStatus.SHOPPING
                || player.getInterfaceId() != Shop.INTERFACE_ID) {
            flag = true;
        } else if (slot >= player.getInventory().capacity()
                || player.getInventory().getItems()[slot].getId() != itemId) {
            flag = true;
        }

        // Check if we failed the verification.
        if (flag) {
            return;
        }

        // Check if shop buys items..
        if (!buysItems(shop, itemId)) {
            player.getPacketSender().sendMessage("You cannot sell this item to this shop.");
            return;
        }

        // Check if this item can be sold via their definition
        ItemDefinition itemDef = ItemDefinition.forId(itemId);
        if (!itemDef.isSellable()) {
            player.getPacketSender().sendMessage("This item cannot be sold.");
            return;
        }

        // Check if player has the correct amount of the item
        // they're trying to sell
        int playerAmount = player.getInventory().getAmount(itemId);
        if (amount > playerAmount)
            amount = playerAmount;
        if (amount == 0)
            return;

        // Only allow 5k max.
        if (amount > 5000) {
            player.getPacketSender().sendMessage("You can only buy a maximum of 5000 at a time.");
            return;
        }

        // Get item value..
        int itemValue = getItemValue(player, itemDef, shop);

        // Apply taxes.
        if (itemValue > 1) {
            itemValue = (int) (itemValue * Shop.SALES_TAX_MODIFIER);
        }

        // Verify item value..
        if (itemValue <= 0) {
            player.getPacketSender().sendMessage("This item has no value.");
            return;
        }

        // A flag which indicates if an item was sold.
        boolean sold = false;

        // Perform sale..
        for (int amountRemaining = amount; amountRemaining > 0; amountRemaining--) {
            // Check if the shop is full..
            if (shop.isFull()) {
                player.getPacketSender().sendMessage("The shop is currently full.");
                break;
            }

            // Check if player still has the item..
            if (!player.getInventory().contains(itemId)) {
                break;
            }

            // Verify inventory space..
            if (player.getInventory().getFreeSlots() == 0) {
                boolean allow = false;

                // If we're selling the exact amount of what we have..
                if (itemDef.isStackable()) {
                    if (amount == player.getInventory().getAmount(itemId)) {
                        allow = true;
                    }
                }

                // If their inventory has the coins..
                if (getCurrencyName(player, itemDef, shop.getId()).equals("coins")) {
                    if (player.getInventory().contains(ItemIdentifiers.COINS)) {
                        allow = true;
                    }
                }

                if (!allow) {
                    player.getInventory().full();
                    break;
                }
            }

            if (!itemDef.isStackable()) {
                // Remove item from player's inventory..
                player.getInventory().delete(itemId, 1);

                // Add player currency..
                incrementCurrency(player, itemDef, itemValue, shop);

                // Add item to shop..
                shop.addItem(itemId, 1);

                sold = true;
            } else {

                // Remove item from player's inventory..
                player.getInventory().delete(itemId, amountRemaining);

                // Add player currency..
                incrementCurrency(player, itemDef, itemValue * amountRemaining, shop);

                // Add item to shop..
                shop.addItem(itemId, amountRemaining);

                sold = true;
                break;
            }
        }

        // Refresh shop..
        if (sold) {
            ShopManager.refresh(shop);
            if (!shop.isRestocking()) {
                TaskManager.submit(new ShopRestockTask(shop));
                shop.setRestocking(true);
            }
        }
    }

    /**
     * Get's the item value for a given item in a shop.
     *
     * @param player
     * @param itemDef
     * @param shop
     * @return
     */
    private static int getItemValue(Player player, ItemDefinition itemDef, Shop shop) {

        switch (itemDef.getId()) {
        
        }
        int ret = itemDef.getValue();
        if (ret <= 0)
        	ret = itemDef.getDropValue();
        return ret;
    }

    /**
     * Get's the currency name for a given item in a shop.
     *
     * @param player
     * @param itemDef
     * @param shopId
     * @return
     */
    private static String getCurrencyName(Player player, ItemDefinition itemDef, int shopId) {

        switch (shopId) {
            case POINTS_SHOP:  // Points shop example
                return "points";
            case BOUNTY_HUNTER_STORE:
                return "blood money";
        }

        return "coins";
    }

    /**
     * Gets a player's currency amount for the given item in a shop.
     *
     * @param player
     * @param itemDef
     * @param shop
     * @return
     */
    private static int getCurrencyAmount(Player player, ItemDefinition itemDef, Shop shop) {
        switch (shop.getId()) {
            case POINTS_SHOP:
                return player.getPoints();
        }
    	return player.getInventory().getAmount(shop.getCurrency());
    }

    /**
     * Decrements a player's currency
     *
     * @param player
     * @param itemDef
     * @param shop
     */
    private static void decrementCurrency(Player player, ItemDefinition itemDef, int amount, Shop shop) {
        if (shop.getId() == POINTS_SHOP) {
            player.setPoints(player.getPoints() - amount);
        } else {
        	player.getInventory().delete(shop.getCurrency(), amount);
        }
    }

    /**
     * Increments a player's currency
     * Used when selling things to a store.
     *
     * @param player
     * @param itemDef
     * @param amount
     * @param shop
     */
    private static void incrementCurrency(Player player, ItemDefinition itemDef, int amount, Shop shop) {
        if (shop.getId() == POINTS_SHOP) {
            player.setPoints(player.getPoints() + amount);
        } else {
        	player.getInventory().add(shop.getCurrency(), amount);
        }
    }

    /**
     * Does this shop buy items?
     *
     * @param shop
     * @param itemId
     * @return
     */
    public static boolean buysItems(Shop shop, int itemId) {
        // Disabling selling items to a shop
        if (shop.getId() == PVP_SHOP) {
			return false;
		}

        // Disabling selling certain items to a shop
		if (shop.getId() == PVP_SHOP) {
			switch (itemId) {
			case ItemIdentifiers.HEAVY_BALLISTA:
				return false;
			}
		}

        // Disable selling a specific item to any shop
		switch (itemId) {
		case ItemIdentifiers.HEAVY_BALLISTA:
			return false;
		}

        if (shop.getId() == GENERAL_STORE || shop.getId() == GENERAL_STORE_2) {
            return true;
        }

        // Makes sure the item we're trying to sell already exists
        // in the shop.
        if (shop.getAmount(itemId, true) >= 1) {
            return true;
        }

        return false;
    }

    /**
     * Does the given shop fully delete items?
     *
     * @param shopId
     * @return
     */
    public static boolean deletesItems(int shopId) {
        if (shopId == GENERAL_STORE) {
            return true;
        }
        return false;
    }

    /**
     * Does the given shop restock on items?
     *
     * @param shopId
     * @return
     */
    public static boolean restocksItem(int shopId) {
        if (shopId == GENERAL_STORE) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the player is viewing the given shop.
     *
     * @param player
     * @param id
     * @return
     */
    public static boolean viewingShop(Player player, int id) {
        return player.getShop() != null && player.getShop().getId() == id;
    }
}
