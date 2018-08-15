package com.overload.game.content.skill.cooking;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Item;
import com.overload.game.model.Skill;
import com.overload.util.ItemIdentifiers;
import com.overload.util.Misc;

public class Pie extends ItemIdentifiers {
	public static enum Pies {
		BERRY_PIE(		new Item(ItemIdentifiers.UNCOOKED_BERRY_PIE), 	10, new Item(ItemIdentifiers.PIE_SHELL), new Item(ItemIdentifiers.REDBERRIES)),
		MEAT_PIE(		new Item(ItemIdentifiers.UNCOOKED_MEAT_PIE), 	20, new Item(ItemIdentifiers.PIE_SHELL), new Item(ItemIdentifiers.COOKED_MEAT)),
		MUD_PIE(		new Item(ItemIdentifiers.RAW_MUD_PIE), 			29, new Item(ItemIdentifiers.PIE_SHELL), new Item(ItemIdentifiers.COMPOST), 	  new Item(ItemIdentifiers.BUCKET_OF_WATER), new Item(ItemIdentifiers.CLAY)),
		APPLE_PIE(		new Item(ItemIdentifiers.UNCOOKED_APPLE_PIE), 	30, new Item(ItemIdentifiers.PIE_SHELL), new Item(ItemIdentifiers.COOKING_APPLE)),
		GARDEN_PIE(		new Item(ItemIdentifiers.RAW_GARDEN_PIE), 		34, new Item(ItemIdentifiers.PIE_SHELL), new Item(ItemIdentifiers.TOMATO),  	  new Item(ItemIdentifiers.ONION), 			 new Item(ItemIdentifiers.CABBAGE)),
		FISH_PIE(		new Item(ItemIdentifiers.RAW_FISH_PIE), 		47, new Item(ItemIdentifiers.PIE_SHELL), new Item(ItemIdentifiers.TROUT),   	  new Item(ItemIdentifiers.COD), 			 new Item(ItemIdentifiers.POTATO)),
		ADMIRAL_PIE(	new Item(ItemIdentifiers.RAW_ADMIRAL_PIE), 		70, new Item(ItemIdentifiers.PIE_SHELL), new Item(ItemIdentifiers.SALMON),  	  new Item(ItemIdentifiers.TUNA), 			 new Item(ItemIdentifiers.POTATO)),
		WILD_PIE(		new Item(ItemIdentifiers.RAW_WILD_PIE), 		85, new Item(ItemIdentifiers.PIE_SHELL), new Item(ItemIdentifiers.RAW_BEAR_MEAT), new Item(ItemIdentifiers.RAW_CHOMPY), 	 new Item(ItemIdentifiers.RAW_RABBIT)),
		SUMMER_PIE(		new Item(ItemIdentifiers.RAW_SUMMER_PIE), 		95, new Item(ItemIdentifiers.PIE_SHELL), new Item(ItemIdentifiers.STRAWBERRY), 	  new Item(ItemIdentifiers.WATERMELON),		 new Item(ItemIdentifiers.COOKING_APPLE));
	
		/**
		 * The item received from combining the ingredients
		 */
		private final Item result;
		
		/**
		 * The level required to make this pizza
		 */
		private final int level;
		
		/**
		 * The primary ingredient required
		 */
		private final Item primary;
	
		/**
		 * An array of {@link Item} objects that represent the ingredients
		 */
		private final Item[] ingredients;
	
		/**
		 * Creates a new in-game pizza that will be used in cooking
		 * 
		 * @param result the result from combining ingredients
		 * @param level the level required
		 * @param experience the experience
		 * @param ingredients the ingredients to make the result
		 */
		private Pies(Item result, int level, Item primary, Item... ingredients) {
			this.result = result;
			this.level = level;
			this.primary = primary;
			this.ingredients = ingredients;
		}

		/**
		 * The result from combining the ingredients
		 * 
		 * @return the result
		 */
		public Item getResult() {
			return result;
		}
	
		/**
		 * The level required to combine the ingredients
		 * 
		 * @return the level required
		 */
		public int getLevel() {
			return level;
		}
	
		/**
		 * An array of {@link Item} objects that represent the ingredients required to create this potion.
		 * 
		 * @return the ingredients required
		 */
		public Item[] getIngredients() {
			return ingredients;
		}
	
		/**
		 * The primary ingredient required for the potion
		 * 
		 * @return the primary ingredient
		 */
		public Item getPrimary() {
			return primary;
		}
	}
	
	public static boolean makePie(Player player, int used, int usedWith) {
		for (Pies pie : Pies.values()) {
			int ingredientUsed = pie.getPrimary().getId() == used ? usedWith : used;
			for (Item item : pie.getIngredients()) {
				if (item.getId() == ingredientUsed) {
					if (player.getSkillManager().getCurrentLevel(Skill.COOKING) >= pie.getLevel()) {
						if (player.getInventory().contains(pie.getIngredients())) {
							player.getInventory().delete(pie.getIngredients()).delete(pie.getPrimary()).add(pie.getResult());
							return true;
						} else {
							player.sendMessage("You don't have all the ingredients to make a " + Misc.capitalizeWords(pie.name().toLowerCase().replaceAll("_", " ")));
							return false;
						}
					} else {
						player.sendMessage("You need a cooking level of " + pie.getLevel() + " to make a " + Misc.capitalizeWords(pie.name().toLowerCase().replaceAll("_", " ")));
						return false;
					}
				}
			}
		}
		return false;
	}
}
