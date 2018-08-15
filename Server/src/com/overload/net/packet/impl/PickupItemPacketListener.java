package com.overload.net.packet.impl;

import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.Entity;
import com.overload.game.entity.impl.grounditem.ItemOnGround;
import com.overload.game.entity.impl.grounditem.ItemOnGroundManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Action;
import com.overload.game.model.PlayerModes;
import com.overload.game.model.Position;
import com.overload.game.model.logs.HandleLogs;
import com.overload.game.model.movement.WalkToAction;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

import java.util.Optional;

/**
 * This packet listener is used to pick up ground items that exist in the world.
 *
 * @author relex lawl
 */

public class PickupItemPacketListener implements PacketListener {

    @Override
    public void handleMessage(final Player player, Packet packet) {
        final int y = packet.readLEShort();
        final int itemId = packet.readShort();
        final int x = packet.readLEShort();
        final Position position = new Position(x, y, player.getPosition().getZ());


		if (player.isDebug()) {
            player.getPacketSender().sendMessage("Pick up item: " + Integer.toString(itemId) + ". " + position.toString());
        }

        if (player.busy()) {
            return;
        }

        if (!player.getLastItemPickup().elapsed(300))
            return;

        player.setWalkToTask(new WalkToAction(player, position, 1, new Action() {
            @Override
            public void execute() {
                // Make sure distance isn't way off..
                if (Math.abs(player.getPosition().getX() - x) > 25 || Math.abs(player.getPosition().getY() - y) > 25) {
                    player.getMovementQueue().reset();
                    return;
                }

                // Check if we can hold it..
                if (!(player.getInventory().getFreeSlots() > 0 || (player.getInventory().getFreeSlots() == 0
                        && ItemDefinition.forId(itemId).isStackable() && player.getInventory().contains(itemId)))) {
                    player.getInventory().full();
                    return;
                }

                Optional<ItemOnGround> item = ItemOnGroundManager.getGroundItem(Optional.of(player.getUsername()),
                        itemId, position);
                if (item.isPresent()) {
                    if (player.getInventory().getAmount(item.get().getItem().getId())
                            + item.get().getItem().getAmount() > Integer.MAX_VALUE
                            || player.getInventory().getAmount(item.get().getItem().getId())
                            + item.get().getItem().getAmount() <= 0) {
                        player.getPacketSender()
                                .sendMessage("You cannot hold that amount of this item. Clear your inventory!");
                        return;
                    }
                    
                    boolean ironmanPickup = false;
                    
                    if (player.getMode().isEqualOrHigher(PlayerModes.IRON_MAN)) {
                    	if (item.get().getOwner().isPresent()) {
                    		if (!item.get().getOwner().get().equalsIgnoreCase(player.getUsername())) {
                    			ironmanPickup = true;
                    		}
                    	} else {
                    		ironmanPickup = true;
                    	}
                    }
                    
                    if (!ironmanPickup) {
	                    ItemOnGroundManager.deregister(item.get());
	                    ItemDefinition def = ItemDefinition.forId(itemId);
	                    HandleLogs.executeLog(player, def.getName() + " [" + position.getX() + "," + position.getY() + "," + position.getZ() + "] " , HandleLogs.LogType.PICKUP);
	                    player.getInventory().add(item.get().getItem());
	                    player.getLastItemPickup().reset();
                    } else {
                        player.getPacketSender().sendMessage("Your game mode prevents you from picking up other players loot!");
                        return;
                    }
                }
            }

            @Override
            public void execute(Entity entity) {}
        }));
    }
}
