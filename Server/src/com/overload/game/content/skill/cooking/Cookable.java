package com.overload.game.content.skill.cooking;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Item;
import com.overload.util.ItemIdentifiers;

public class Cookable extends CookingConstants {
	public static enum Pizza {
		INCOMPLETE_PIZZA(	new Item(INCOMPLETE_PIZZA_ID), 	null,					  35, 80,  0,	false,  new Item(BASE_ID), 				new Item(TOMATO_ID)),
		UNCOOKED_PIZZA(		new Item(UNCOOKED_PIZZA_ID), 	null,					  35, 80,  0,	false,  new Item(INCOMPLETE_PIZZA_ID), 	new Item(CHEESE_ID)),
		PLAIN_PIZZA(		new Item(PLAIN_PIZZA_ID), 		new Item(BURNT_PIZZA_ID), 35, 143, 50,	true,   new Item(UNCOOKED_PIZZA_ID)),
		MEAT_PIZZA_1(		new Item(MEAT_PIZZA_ID), 		null, 					  45, 26,  60,	false,  new Item(PLAIN_PIZZA_ID),		new Item(MEAT_ID)),
		MEAT_PIZZA_2(		new Item(MEAT_PIZZA_ID), 		null, 					  45, 26,  60,	false,  new Item(PLAIN_PIZZA_ID),		new Item(CHICKEN_ID)),
		ANCHOVY_PIZZA(		new Item(ANCHOVY_PIZZA_ID), 	null, 					  55, 39,  70,	false,  new Item(PLAIN_PIZZA_ID),		new Item(ANCHOVIES_ID)),
		PINEAPPLE_PIZZA_1(	new Item(PINEAPPLE_PIZZA_ID), 	null, 					  65, 45,  80,	false,  new Item(PLAIN_PIZZA_ID),		new Item(PINEAPPLE_CHUNKS_ID)),
		PINEAPPLE_PIZZA_2(	new Item(PINEAPPLE_PIZZA_ID), 	null, 					  65, 45,  80,	false,  new Item(PLAIN_PIZZA_ID),		new Item(PINEAPPLE_RING_ID));
	
		/**
		 * The item received from combining the ingredients
		 */
		private final Item result;
		private final Item burntitem;
		/**
		 * The level required to make this pizza
		 */
		private final int level;

		/**
		 * The experience gained from making this pizza
		 */
		private final int experience;

		/**
		 * The change we have to burn the pizza
		 */
		private final int stopburn;

		/**
		 * If the item needs to be used on a fire to be made
		 */
		private final boolean requiresfire;
		
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
		private Pizza(Item result, Item burntitem, int level, int experience, int stopburn, boolean requiresfire, Item primary, Item... ingredients) {
			this.result = result;
			this.burntitem = burntitem;
			this.level = level;
			this.experience = experience;
			this.stopburn = stopburn;
			this.requiresfire = requiresfire;
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
		 * The result from burning
		 * 
		 * @return the burnt item
		 */
		public Item getBurntItem() {
			return burntitem;
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
		 * The total amount of experience gained in the cooking skill
		 * 
		 * @return the experience gained
		 */
		public int getExperience() {
			return experience;
		}
	
		/**
		 * The level required to stop burning
		 * 
		 * @return the level required
		 */
		public int getStopBurn() {
			return stopburn;
		}
	
		/**
		 * Does the item need a fire to be made?
		 * 
		 * @return if it needs to be used on a fire
		 */
		public boolean needsFire() {
			return requiresfire;
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

	public static enum Pie {
		BERRY_PIE(		new Item(ItemIdentifiers.REDBERRY_PIE), new Item(ItemIdentifiers.BURNT_PIE), 10, 78,  50,	true,  new Item(ItemIdentifiers.UNCOOKED_BERRY_PIE)),
		MEAT_PIE(		new Item(ItemIdentifiers.MEAT_PIE), 	new Item(ItemIdentifiers.BURNT_PIE), 20, 105, 60,	true,  new Item(ItemIdentifiers.UNCOOKED_MEAT_PIE)),
		MUD_PIE(		new Item(ItemIdentifiers.MUD_PIE), 		new Item(ItemIdentifiers.BURNT_PIE), 29, 128, 69,	true,  new Item(ItemIdentifiers.RAW_MUD_PIE)),
		APPLE_PIE(		new Item(ItemIdentifiers.APPLE_PIE), 	new Item(ItemIdentifiers.BURNT_PIE), 30, 130, 70,	true,  new Item(ItemIdentifiers.UNCOOKED_APPLE_PIE)),
		GARDEN_PIE(		new Item(ItemIdentifiers.GARDEN_PIE), 	new Item(ItemIdentifiers.BURNT_PIE), 34, 138, 74,	true,  new Item(ItemIdentifiers.RAW_GARDEN_PIE)),
		FISH_PIE(		new Item(ItemIdentifiers.FISH_PIE), 	new Item(ItemIdentifiers.BURNT_PIE), 47, 164, 77,	true,  new Item(ItemIdentifiers.RAW_FISH_PIE)),
		ADMIRAL_PIE(	new Item(ItemIdentifiers.ADMIRAL_PIE), 	new Item(ItemIdentifiers.BURNT_PIE), 70, 210, 94,	true,  new Item(ItemIdentifiers.RAW_ADMIRAL_PIE)),
		WILE_PIE(		new Item(ItemIdentifiers.WILD_PIE), 	new Item(ItemIdentifiers.BURNT_PIE), 85, 240, 100,	true,  new Item(ItemIdentifiers.RAW_WILD_PIE)),
		SUMMER_PIE(		new Item(ItemIdentifiers.SUMMER_PIE), 	new Item(ItemIdentifiers.BURNT_PIE), 95, 264, 100,	true,  new Item(ItemIdentifiers.RAW_SUMMER_PIE));
	
		/**
		 * The item received from combining the ingredients
		 */
		private final Item result;
		private final Item burntitem;
		/**
		 * The level required to make this pizza
		 */
		private final int level;

		/**
		 * The experience gained from making this pizza
		 */
		private final int experience;

		/**
		 * The change we have to burn the pizza
		 */
		private final int stopburn;

		/**
		 * If the item needs to be used on a fire to be made
		 */
		private final boolean rangeOnly;
		
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
		private Pie(Item result, Item burntitem, int level, int experience, int stopburn, boolean rangeOnly, Item primary, Item... ingredients) {
			this.result = result;
			this.burntitem = burntitem;
			this.level = level;
			this.experience = experience;
			this.stopburn = stopburn;
			this.rangeOnly = rangeOnly;
			this.primary = primary;
			this.ingredients = ingredients;
		}
		
		public static int getExperienceFromPrimary(int primary) {
			for (Pie pie : Pie.values()) {
				if (pie.getPrimary().getId() == primary) {
					return pie.getExperience();
				}
			}
			return 0;
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
		 * The result from burning
		 * 
		 * @return the burnt item
		 */
		public Item getBurntItem() {
			return burntitem;
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
		 * The total amount of experience gained in the cooking skill
		 * 
		 * @return the experience gained
		 */
		public int getExperience() {
			return experience;
		}
	
		/**
		 * The level required to stop burning
		 * 
		 * @return the level required
		 */
		public int getStopBurn() {
			return stopburn;
		}
	
		/**
		 * Does the item need a fire to be made?
		 * 
		 * @return if it needs to be used on a fire
		 */
		public boolean isRangeOnly() {
			return rangeOnly;
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

	public  enum Fish {
		SHRIMP(317, 315, 7954, 1, 30, 33, "shrimp"),
        ANCHOVIES(321, 319, 323, 1, 30, 34, "anchovies"),
        TROUT(335, 333, 343, 15, 70, 50, "trout"),
        COD(341, 339, 343, 18, 75, 54, "cod"),
        SALMON(331, 329, 343, 25, 90, 58, "salmon"),
        TUNA(359, 361, 367, 30, 100, 58, "tuna"),
        LOBSTER(377, 379, 381, 40, 120, 74, "lobster"),
        BASS(363, 365, 367, 40, 130, 75, "bass"),
        SWORDFISH(371, 373, 375, 45, 140, 86, "swordfish"),
        MONKFISH(7944, 7946, 7948, 62, 150, 91, "monkfish"),
        SHARK(383, 385, 387, 80, 210, 94, "shark"),
        SEA_TURTLE(395, 397, 399, 82, 212, 105, "sea turtle"),
        MANTA_RAY(389, 391, 393, 91, 217, 99, "manta ray"),
        DARK_CRAB(11934, 11936, 11938, 90, 225, 100, "dark crab"),;


		private static final Map<Integer, Fish> cookables = new HashMap<Integer, Fish>();

        static {
            for (Fish c : Fish.values()) {
                cookables.put(c.getRawItem(), c);
                cookables.put(c.getCookedItem(), c);
            }
        }

        int rawItem, cookedItem, burntItem, levelReq, xp, stopBurn;
        String name;

        Fish(int rawItem, int cookedItem, int burntItem, int levelReq, int xp, int stopBurn, String name) {
            this.rawItem = rawItem;
            this.cookedItem = cookedItem;
            this.burntItem = burntItem;
            this.levelReq = levelReq;
            this.xp = xp;
            this.stopBurn = stopBurn;
            this.name = name;
        }

        public static Optional<Fish> getForItem(int item) {
        	Fish c = cookables.get(item);
            if (c != null) {
                return Optional.of(c);
            }
            return Optional.empty();
        }

        public int getRawItem() {
            return rawItem;
        }

        public int getCookedItem() {
            return cookedItem;
        }

        public int getBurntItem() {
            return burntItem;
        }

        public int getLevelReq() {
            return levelReq;
        }

        public int getXp() {
            return xp;
        }

        public int getStopBurn() {
            return stopBurn;
        }

        public String getName() {
            return name;
        }
	}

	private Object type = null;

	public Cookable(Object type) {
		this.type = type;
	}
	
	public Object get() {
		if (this.isFish()) {
			return (Fish) this.type;
		} else if (this.isPizza()) {
			return (Pizza) this.type;
		}
		return (Pie) this.type;
	}
	
	public Fish getAsFish() {
		return (Fish) this.type;
	}

	public Pizza getAsPizza() {
		return (Pizza) this.type;
	}

	public Pie getAsPie() {
		return (Pie) this.type;
	}
	
	public boolean isFish() {
		return type instanceof Fish;
	}
	
	public boolean isPizza() {
		return type instanceof Pizza;
	}
	
	public boolean isPie() {
		return type instanceof Pie;
	}
	
	public static Object getItemById(int id) {
		for (Fish fish : Fish.values()) {
			if (fish.getRawItem() == id)
				return fish;
		}
		for (Pie pie : Pie.values()) {
			if (pie.getPrimary().getId() == id)
				return pie;
		}
		for (Pizza pizza : Pizza.values()) {
			for (Item ingredients : pizza.getIngredients()) {
				if (ingredients.getId() == id)
					return pizza;
			}
			if (pizza.getPrimary().getId() == id)
				return pizza;
		}
		return null;
	}
	
	public boolean hasRequirements(Player player, boolean allowFullInv) {
        //Check inventory slots..
        if (!allowFullInv) {
            if (player.getInventory().getFreeSlots() == 0) {
                player.getInventory().full();
                return false;
            }
        }

        //Check if busy..
        if (player.busy()) {
            return false;
        }

        return true;
    }


	public Item getResult() {
		Item item = null;
		if (this.type != null) {
			if (this.isFish()) {
				item = new Item(((Fish) this.type).getCookedItem());
			} else if (this.isPizza()) {
				item = ((Pizza) this.type).getResult();
			} else if (this.isPie()) {
				item = ((Pie) this.type).getResult();
			}
		}
		return item;
	}

	public int getLevel() {
		if (this.type != null) {
			if (this.isFish()) {
				return ((Fish) this.type).getLevelReq();
			} else if (this.isPizza()) {
				return ((Pizza) this.type).getLevel();
			} else if (this.isPie()) {
				return ((Pie) this.type).getLevel();
			}
		}
		return -1;
	}

	public int getExperience() {
		if (this.type != null) {
			if (this.isFish()) {
				return ((Fish) this.type).getXp();
			} else if (this.isPizza()) {
				return ((Pizza) this.type).getExperience();
			} else if (this.isPie()) {
				return ((Pie) this.type).getExperience();
			}
		}
		return -1;
	}
}