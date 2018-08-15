package com.overload.net.packet.impl;

import com.overload.game.content.PetHandler;
import com.overload.game.entity.impl.grounditem.ItemOnGroundManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Item;
import com.overload.game.model.areas.impl.WildernessArea;
import com.overload.game.model.container.impl.Inventory;
import com.overload.game.model.rights.Right;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

/**
 * This packet listener is called when a player drops an item they have placed
 * in their inventory.
 *
 * @author relex lawl
 */

public class DropItemPacketListener implements PacketListener {

    public static void destroyItemInterface(Player player, Item item, boolean instant) {// Destroy item created by Remco
    	if (instant) {
			player.getPacketSender().sendInterfaceRemoval();
			if (item.getId() != -1) {
				player.getInventory().delete(item);
			}
    	} else {
	        player.setDestroyItem(item.getId());
	        String[][] info = { // The info the dialogue gives
	                {"Are you sure you want to discard this item?", "14174"}, {"Yes.", "14175"}, {"No.", "14176"},
	                {"", "14177"}, {"This item will vanish once it hits the floor.", "14182"},
	                {"You cannot get it back if discarded.", "14183"}, {item.getDefinition().getName(), "14184"}};
	        player.getPacketSender().sendItemOnInterface(14171, item.getId(), 0, item.getAmount());
	        for (int i = 0; i < info.length; i++)
	            player.getPacketSender().sendString(Integer.parseInt(info[i][1]), info[i][0]);
	        player.getPacketSender().sendChatboxInterface(14170);
    	}
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        int id = packet.readUnsignedShortA();
        int interface_id = packet.readUnsignedShort();
        int itemSlot = packet.readUnsignedShortA();

        if (player == null || player.getHitpoints() <= 0) {
            return;
        }

        if (interface_id != Inventory.INTERFACE_ID) {
            return;
        }

        if (player.getHitpoints() <= 0)
            return;
        
        if (itemSlot < 0 || itemSlot >= player.getInventory().capacity())
            return;

        if (player.busy()) {
        	player.getPacketSender().sendInterfaceRemoval();
        }

        Item item = player.getInventory().getItems()[itemSlot];
        if (item == null)
            return;
        if (item.getId() != id || item.getAmount() <= 0) {
            return;
        }
        
        // Check if we're dropping a pet..
        if (PetHandler.drop(player, id, false)) {
            return;
        }

        boolean destroy = false;
		if (player.getRights().isOrInherits(Right.MANAGER)) {
			destroy = true;
        }

        player.getPacketSender().sendInterfaceRemoval();

        // Stop skilling..
        player.getSkillManager().stopSkillable();

        if (item.getDefinition().isDropable() && !destroy) {
        	
        	// Items dropped in the Wilderness should go global immediately.
            if (player.getArea() instanceof WildernessArea) {
            	ItemOnGroundManager.registerGlobal(player, item);
            } else {
            	ItemOnGroundManager.register(player, item);
            }
            
            player.getInventory().setItem(itemSlot, new Item(-1, 0)).refreshItems();
        } else {
            destroyItemInterface(player, item, destroy);
        }
    }
}
