package com.overload.game.content.minigames;

import com.overload.game.entity.impl.player.Player;

public class MinigameManager {

	/**
	 * Represents the allowed type of belongings
	 */
	public enum AllowedType {
		/**
		 * No inventory items allowed but rest are
		 */
		NO_INVENTORY,
		/**
		 * No equipment items allowed but rest are
		 */
		NO_EQUIPMENT,
		/**
		 * No equipment or inventory items but familiars allowed
		 */
		NO_ITEMS,
		/**
		 * Nothing is allowed
		 */
		NOTHING,
	}

	/**
	 * Checks the entry
	 * 
	 * @param player
	 *            the player
	 * @return enters
	 */
	public static boolean checkEntry(Player player) {
		/**
		 * The minigame
		 */
		Minigame mini = player.getMinigame();
		/**
		 * No minigame
		 */
		if (mini == null) {
			return false;
		}
		/**
		 * No inventory
		 */
		player.getPacketSender().sendInterfaceRemoval();
		if (mini.getAllowedType().equals(AllowedType.NO_ITEMS)
				|| mini.getAllowedType().equals(AllowedType.NO_INVENTORY)
				|| mini.getAllowedType().equals(AllowedType.NOTHING)) {
			for (int i = 0; i < player.getInventory().getItems().length; i++) {
				if (player.getInventory().getItems()[i] != null) {
					player.getPacketSender().sendMessage("You cannot bring any items in!");
					return false;
				}
			}
		}
		/**
		 * No equipment
		 */
		if (mini.getAllowedType().equals(AllowedType.NO_ITEMS)
				|| mini.getAllowedType().equals(AllowedType.NO_EQUIPMENT)
				|| mini.getAllowedType().equals(AllowedType.NOTHING)) {
			for (int i = 0; i < player.getEquipment().getItems().length; i++) {
				if (player.getEquipment().getItems()[i]	== null) {
					player.getPacketSender().sendMessage("You cannot bring any equipment!");
					return false;
				}
			}
		}
		return true;
	}
}