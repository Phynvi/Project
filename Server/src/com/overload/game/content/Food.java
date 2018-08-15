package com.overload.game.content;

import java.util.HashMap;
import java.util.Map;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Item;
import com.overload.game.model.Priority;
import com.overload.game.model.Skill;
import com.overload.util.ItemIdentifiers;
import com.overload.util.timers.TimerKey;

/**
 * Represents food which can be consumed by the player to restore hitpoints.
 *
 * @author Professor Oak
 */

public class Food {

	/**
	 * The {@link Animation} that will be played when consuming food.
	 */
	private static final Animation ANIMATION = new Animation(829, Priority.HIGH);

	/**
	 * Handles the player eating said food type.
	 *
	 * @param player
	 *            The player eating the consumable.
	 * @param item
	 *            The food item being consumed.
	 * @param slot
	 *            The slot of the food being eaten.
	 */
	public static boolean consume(Player player, int item, int slot) {
		Edible food = Edible.types.get(item);
		// Check if {@code item} is a valid food type..
		if (food == null) {
			return false;
		}

		if (player.getArea() != null) {
			if (!player.getArea().canEat(player, item)) {
				player.getPacketSender().sendMessage("You cannot eat here.");
				return true;
			}
		}

		// Check if we're currently able to eat..
		if (player.getTimers().has(TimerKey.STUN)) {
			player.getPacketSender().sendMessage("You're currently stunned!");
			return true;
		}

		if (food == Edible.KARAMBWAN) {
			if (player.getTimers().has(TimerKey.KARAMBWAN))
				return true;
		} else {
			if (player.getTimers().has(TimerKey.FOOD)) {
				return true;
			}
		}

		player.getTimers().extendOrRegister(TimerKey.FOOD, 3);
		player.getTimers().extendOrRegister(TimerKey.COMBAT_ATTACK, 5);

		if (food == Edible.KARAMBWAN) {
			player.getTimers().register(TimerKey.KARAMBWAN, 3); // Register karambwan timer too
			player.getTimers().register(TimerKey.POTION, 3); // Register the potion timer (karambwan blocks pots)
		}

		// Close interfaces..
		player.getPacketSender().sendInterfaceRemoval();

		// Stop skilling..
		player.getSkillManager().stopSkillable();

		// Start animation..
		player.performAnimation(ANIMATION);

		// Delete food from inventory..
		player.getInventory().delete(food.item, slot);

		// Heal the player..
		int currentHp = player.getSkillManager().getCurrentLevel(Skill.HITPOINTS);
		int maxHp = player.getSkillManager().getMaxLevel(Skill.HITPOINTS);
		int healAmount = food.heal;
		
		if (food == Edible.ANGLERFISH) {
			int c = 2;
			if (currentHp >= 25) {
				c = 4;
			}
			if (currentHp >= 50) {
				c = 6;
			}
			if (currentHp >= 75) {
				c = 8;
			}
			if (currentHp >= 93) {
				c = 13;
			}
			healAmount = (int) Math.floor((currentHp/10) + c);
			if (healAmount > 22) {
				healAmount = 22;
			}
			maxHp += healAmount;
		}
		
		if (healAmount + currentHp > maxHp) {
			healAmount = maxHp - currentHp;
		}
		if (healAmount < 0) {
			healAmount = 0;
		}
		
		player.setHitpoints(player.getHitpoints() + healAmount);
		
		// Send message to player..
		String e = food == Edible.BANDAGES ? "use" : "eat";
		player.getPacketSender().sendMessage("You " + e + " the " + food.name + ".");

		// Handle cake slices..
		if (food == Edible.CAKE || food == Edible.SECOND_CAKE_SLICE) {
			player.getInventory().add(new Item(food.item.getId() + 2, 1));
		}
		if (food.name().contains("_PIE")) {
			int[][] i = getSkillBoost(player, food);
			if (i[0][0] > -1 && i[0][1] > -1) {
				player.getSkillManager().increaseCurrentLevel(
					Skill.forId(i[0][0]),
					i[0][1],
					104
				);
				if (i[1][0] > -1 && i[1][1] > -1) {
					player.getSkillManager().increaseCurrentLevel(
						Skill.forId(i[1][0]),
						i[1][1],
						104
					);
				}
			}
			
			if (food.name().contains("_PIE_")) {
				player.getInventory().add(new Item(2313, 1));
			} else {
				Edible pie_half = Edible.valueOf(food.name() + "_HALF");
				player.getInventory().add(new Item(pie_half.item.getId(), 1));
			}
		}
		return true;
	}
	
	public static int[][] getSkillBoost(Player player, Edible food) {
		int[][] i = new int[][] {
			{-1, -1},
			{-1, -1}
		};

		if (food.name().contains("GARDEN_PIE")) {
			i[0][0] = Skill.FARMING.ordinal();
			i[0][1] = 3;
		}
		if (food.name().contains("FISH_PIE")) {
			i[0][0] = Skill.FISHING.ordinal();
			i[0][1] = 3;
		}
		if (food.name().contains("ADMIRAL_PIE")) {
			i[0][0] = Skill.FISHING.ordinal();
			i[0][1] = 5;
		}
		if (food.name().contains("WILD_PIE")) {
			i[0][0] = Skill.SLAYER.ordinal();
			i[0][1] = 5;
			i[1][0] = Skill.RANGED.ordinal();
			i[1][1] = 4;
		}
		if (food.name().contains("SUMMER_PIE")) {
			i[0][0] = Skill.AGILITY.ordinal();
			i[0][1] = 5;
			player.setRunEnergy(player.getRunEnergy()+10);
		}
		
		return i;
	}

	/**
	 * Represents all types of food currently available.
	 *
	 * @author relex lawl
	 */
	public enum Edible {
		/*
		 * Fish food types players can get by fishing or purchasing from other entities.
		 */
		KEBAB(new Item(1971), 4),
		CHEESE(new Item(1985), 4),
		CAKE(new Item(1891), 5),
		SECOND_CAKE_SLICE(new Item(1893), 5),
		THIRD_CAKE_SLICE(new Item(1895), 5),
		BANDAGES(new Item(14640), 12),
		JANGERBERRIES(new Item(247), 2),
		WORM_CRUNCHIES(new Item(2205), 7),
		EDIBLE_SEAWEED(new Item(403), 4),
		ANCHOVIES(new Item(319), 1),
		SHRIMPS(new Item(315), 3),
		SARDINE(new Item(325), 4),
		COD(new Item(339), 7),
		TROUT(new Item(333), 7),
		PIKE(new Item(351), 8),
		SALMON(new Item(329), 9),
		TUNA(new Item(361), 10),
		LOBSTER(new Item(379), 12),
		BASS(new Item(365), 13),
		SWORDFISH(new Item(373), 14),
		MONKFISH(new Item(7946), 16),
		SHARK(new Item(385), 20),
		SEA_TURTLE(new Item(397), 21),
		MANTA_RAY(new Item(391), 22),
		KARAMBWAN(new Item(3144), 18),
		ANGLERFISH(new Item(13441), 22),
		/*
		 * Baked goods food types a player can make with the cooking skill.
		 */
		POTATO(new Item(1942), 1),
		BAKED_POTATO(new Item(6701), 4),
		POTATO_WITH_BUTTER(new Item(6703), 14),
		CHILLI_POTATO(new Item(7054), 14),
		EGG_POTATO(new Item(7056), 16),
		POTATO_WITH_CHEESE( new Item(6705), 16),
		MUSHROOM_POTATO(new Item(7058), 20),
		TUNA_POTATO(new Item(7060), 20),

		/*
		 * Fruit food types which a player can get by picking from certain trees or
		 * hand-making them (such as pineapple chunks/rings).
		 */
		SPINACH_ROLL(new Item(1969), 2),
		BANANA(new Item(1963), 2),
		BANANA_(new Item(18199), 2),
		CABBAGE(new Item(1965), 2),
		ORANGE(new Item(2108), 2),
		PINEAPPLE_CHUNKS(new Item(2116), 2),
		PINEAPPLE_RINGS(new Item(2118), 2),
		PEACH(new Item(6883), 8),

		/*
		 * Other food types.
		 */
		PURPLE_SWEETS(new Item(4561), 3),
		
		/*
		 * Pizzas
		 */
		MEAT_PIZZA(new Item(2293), 14),
		
		/*
		 * Pies
		 */
		BERRY_PIE(		 new Item(ItemIdentifiers.REDBERRY_PIE), 	    5),
		BERRY_PIE_HALF(	 new Item(ItemIdentifiers.HALF_A_REDBERRY_PIE), 5),
		MEAT_PIE(		 new Item(ItemIdentifiers.MEAT_PIE), 		    6),
		MEAT_PIE_HALF(	 new Item(ItemIdentifiers.HALF_A_MEAT_PIE),	    6),
		APPLE_PIE(		 new Item(ItemIdentifiers.APPLE_PIE), 		    7),
		APPLE_PIE_HALF(	 new Item(ItemIdentifiers.HALF_AN_APPLE_PIE),	7),
		GARDEN_PIE( 	 new Item(ItemIdentifiers.GARDEN_PIE), 			6), //+3 Farming
		GARDEN_PIE_HALF( new Item(ItemIdentifiers.HALF_A_GARDEN_PIE), 	6), //+3 Farming
		FISH_PIE(	 	 new Item(ItemIdentifiers.FISH_PIE), 			6), //+3 Fishing
		FISH_PIE_HALF(	 new Item(ItemIdentifiers.HALF_A_FISH_PIE), 	6), //+3 Fishing
		ADMIRAL_PIE(	 new Item(ItemIdentifiers.ADMIRAL_PIE), 		8), //+5 Fishing
		ADMIRAL_PIE_HALF(new Item(ItemIdentifiers.HALF_AN_ADMIRAL_PIE), 8), //+5 Fishing
		WILD_PIE(		 new Item(ItemIdentifiers.WILD_PIE), 			11),//+5 Slayer  | +4 Range
		WILD_PIE_HALF(	 new Item(ItemIdentifiers.HALF_A_WILD_PIE), 	11),//+5 Slayer  | +4 Range
		SUMMER_PIE( 	 new Item(ItemIdentifiers.SUMMER_PIE), 			11),//+5 Agility | +20% Energy
		SUMMER_PIE_HALF( new Item(ItemIdentifiers.HALF_A_SUMMER_PIE), 	11),//+5 Agility | +20% Energy
	
		;

		static Map<Integer, Edible> types = new HashMap<Integer, Edible>();

		static {
			for (Edible type : Edible.values()) {
				types.put(type.item.getId(), type);
			}
		}

		private Item item;
		private int heal;
		private String name;

		private Edible(Item item, int heal) {
			this.item = item;
			this.heal = heal;
			this.name = (toString().toLowerCase().replaceAll("__", "-").replaceAll("_", " "));
		}

		public int getHeal() {
			return heal;
		}
	}
}
