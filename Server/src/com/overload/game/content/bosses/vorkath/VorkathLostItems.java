package com.overload.game.content.bosses.vorkath;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.entity.impl.grounditem.ItemOnGroundManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Item;
import com.overload.game.model.PlayerModes;
import com.overload.game.model.container.shop.ShopManager;

import java.util.ArrayList;

public class VorkathLostItems extends ArrayList<Item> {
    private static final long serialVersionUID = 6531932481922987725L;

    /**
     * The player that has lost items
     */
    private final Player player;

    /**
     * Creates a new class for managing lost items by a single player
     *
     * @param player the player who lost items
     */
    public VorkathLostItems(final Player player) {
        this.player = player;
    }

    /**
     * Stores the players items into a list and deletes their items
     */
    public void store() {
        for (int i = 0; i < (28-player.getInventory().getFreeSlots()); i++) {
            Item broken = Item.getBrokenState(player.getInventory().getItems()[i]);
            if (broken != null)
                add(broken);
            else
                add(player.getInventory().getItems()[i]);
        }
        for (int i = 0; i < (11-player.getEquipment().getFreeSlots()); i++) {
            Item broken = Item.getBrokenState(player.getEquipment().getItems()[i]);
            if (broken != null)
                add(broken);
            else
                add(player.getEquipment().getItems()[i]);
        }
        player.getInventory().resetItems().refreshItems();
        player.getEquipment().resetItems().refreshItems();
    }

    public int getItemsValue() {
        int value = 0;
        for (Item item : this) {
            long tempValue = item.getId() - 1 == 995 ? 1 : ShopManager.priceCheck(item.getId());
            value += tempValue * item.getAmount();
        }
        return value > 500000 ? 500000 : value;
    }

    public void retain() {
        int price = getItemsValue();
        if (!player.getInventory().contains(995, price)) {
            DialogueHandler.npcChat(player, 1617, "You need at least " + price + "GP to claim your items.");
            DialogueHandler.close(player, true);
            return;
        }
        for (Item item : this) {
            if (player.getMode().isEqualOrHigher(PlayerModes.ULTIMATE)) {
                if (player.getInventory().getFreeSlots() > 0) {
                    player.getInventory().add(item);
                } else {
                    ItemOnGroundManager.register(player, item);
                }
            } else {
                if (player.getInventory().getFreeSlots() > 0) {
                    player.getInventory().add(item);
                } else {
                    player.getBank(0).add(item);
                }
            }
        }
        clear();
        player.getInventory().delete(995, price);
        if (!player.getMode().isEqualOrHigher(PlayerModes.ULTIMATE)) {
            DialogueHandler.npcChat(player, 1617, new String[] {
                    "You have retained all of your lost items for " + price + "GP.",
                    "Your items are in your inventory.",
                    "@red@If there was not enough space, they were dropped."
            });
            player.setNpcTalking(1617);
            player.setDialogueState(-1);
        } else {
            DialogueHandler.npcChat(player, 1617, new String[] {
                    "You have retained all of your lost items for " + price + "GP.",
                    "Your items are in your inventory.",
                    "@red@If there was not enough space, they were sent to your bank."
            });
            player.setNpcTalking(1617);
            player.setDialogueState(-1);
        }
    }
}
