package com.overload.game.content.skill.herblore;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Item;

public class Mixing {
	/**
	 * A single instance of the Mixing class
	 */
	private static Mixing POTION_MIXING = new Mixing();

	/**
	 * When an item is clicked we want to establish between items that are and are not potions
	 * 
	 * @param item the item we're determining this for
	 * @return true if the item is a potion based on its item id, otherwise it will return false
	 */
	public boolean isPotion(Item item) {
		Potion potion = Potion.get(item.getId());
		return potion != null;
	}

	/**
	 * Determines if two potions have the same name. This allows us to ensure two potions are the same.
	 * 
	 * @param potion1 the first potion
	 * @param potion2 the second potion
	 * @return true if they both match.
	 */
	public boolean matches(Item potion1, Item potion2) {
		Potion p1 = Potion.get(potion1.getId());
		Potion p2 = Potion.get(potion2.getId());
		if (p1 == null || p2 == null) {
			return false;
		}
		return p1.equals(p2);
	}

	/**
	 * Mixes two single potions together to combine as one
	 * 
	 * @param player the player combining the potions
	 * @param potion1 the first potion
	 * @param potion2 the second potion
	 */
	public static boolean mix(Player player, Item item1, Item item2) {
		if (!player.getInventory().contains(item1.getId(), item1.getAmount())) {
			return false;
		}
		if (!player.getInventory().contains(item2.getId(), item2.getAmount())) {
			return false;
		}
		Potion potion1 = Potion.get(item1.getId());
		Potion potion2 = Potion.get(item2.getId());
		if (potion1 == null || potion2 == null) {
			return false;
		}
		if (potion1.isFull(item1.getId()) || potion2.isFull(item2.getId())) {
			return false;
		}
		player.getInventory().delete(item1.getId(), item1.getAmount());
		player.getInventory().delete(item2.getId(), item2.getAmount());
		int dose1 = potion1.getDosage(item1.getId());
		int dose2 = potion2.getDosage(item2.getId());
		int sum = dose1 + dose2;
		if (sum >= 4) {
			item1 = new Item(potion1.full);
			if (sum - 4 > 0) {
				item2 = new Item(potion2.getItemId(sum - 4));
			} else {
				item2 = new Item(229);
			}
		} else {
			item1 = new Item(potion1.getItemId(sum));
			item2 = new Item(229);
		}
		player.getInventory().add(item1.getId(), item1.getAmount());
		player.getInventory().add(item2.getId(), item2.getAmount());
		return true;
	}

	/**
	 * Decant all unnoted and noted potions in a player's inventory.
	 * 
	 * @param player The player of which we want to decant the inventory.
	 */
	public static void decantInventory(Player player) {
		
	}

	@SuppressWarnings("unused")
	private static void decantUnnotedItem(Player player, Item item) {
		
	}
	@SuppressWarnings("unused")
	private static void decantNotedItem(Player player, Item item) {
		
	}

	/**
	 * Retrieves the single instance of the Mixing class
	 * 
	 * @return
	 */
	public static Mixing get() {
		return POTION_MIXING;
	}

	public enum Potion {
		AGILITY(3032, 3034, 3036, 3038),
		ANTI_VENOM(12905, 12907, 12909, 12911),
		ANTI_VENOM_PLUS(12913, 12915, 12917, 12919),
		ANTIDOTE_PLUS(5943, 5945, 5947, 5949),
		ANTIDOTE_PLUS_PLUS(5952, 5954, 5956, 5958),
		ANTIFIRE(2452, 2454, 2456, 2458),
		ANTIPOISON(2446, 175, 177, 179),
		ATTACK(2428, 121, 123, 125),
		COMBAT(9739, 9741, 9743, 9745),
		DEFENCE(2432, 133, 135, 137),
		ENERGY(3008, 3010, 3012, 3014),
		FISHING(2438, 151, 153, 155),
		MAGIC(3040, 3042, 3044, 3046),
		OVERLOAD(11730, 11731, 11732, 11733),
		PRAYER(2434, 139, 141, 143),
		RANGING(2444, 169, 171, 173),
		RESTORE(2430, 127, 129, 131),
		SARADOMIN_BREW(6685, 6687, 6689, 6691),
		STRENGTH(113, 115, 117, 119),
		SUPER_ANTIPOISON(2448, 181, 183, 185),
		SUPER_ATTACK(2436, 145, 147, 149),
		SUPER_COMBAT(12695, 12697, 12699, 12701),
		SUPER_DEFENCE(2442, 163, 165, 167),
		SUPER_ENERGY(3016, 3018, 3020, 3022),
		SUPER_RESTORE(3024, 3026, 3028, 3030),
		SUPER_STRENGTH(2440, 157, 159, 161),
		ZAMORAK_BREW(2450, 189, 191, 193);

		Potion(int full, int threeQuarters, int half, int quarter) {
			this.quarter = quarter;
			this.half = half;
			this.threeQuarters = threeQuarters;
			this.full = full;
		}

		private int quarter, half, threeQuarters, full;

		public boolean isQuarter(int id) {
			return quarter == id;
		}

		public boolean isHalf(int id) {
			return half == id;
		}

		public boolean isThreeQuarters(int id) {
			return threeQuarters == id;
		}

		public boolean isFull(int id) {
			return full == id;
		}

		public int getDosage(int id) {
			return id == full ? 4 : id == threeQuarters ? 3 : id == half ? 2 : id == quarter ? 1 : 0;
		}

		public int getItemId(int dosage) {
			return dosage == 4 ? full : dosage == 3 ? threeQuarters : dosage == 2 ? half : dosage == 1 ? quarter : 0;
		}

		static Potion get(int id) {
			for (Potion p : values()) {
				if (p.full == id || p.half == id || p.quarter == id || p.threeQuarters == id) {
					return p;
				}
			}
			return null;
		}

		/**
		 * Find the first slot of a given potion in a player's inventory which does not have a full dosage.
		 * 
		 * @param player The players of we want to look at the invenotry.
		 * @param potion The potion which we want to find.
		 * @return The first inventory slot containing the potion of any dosage. Returns -1 if the player or potion is null or if the potion could not be found.
		 */
		@SuppressWarnings("unused")
		public static int getFirstIncomplete(Player player, Potion potion) {
			if (player == null || potion == null) {
				return -1;
			}
			for (int dosage = 1; dosage <= 3; dosage++) {
				return player.getInventory().getSlot(potion.getItemId(dosage));
			}
			return -1;
		}
	}
}
