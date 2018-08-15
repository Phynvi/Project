package com.overload.game.content.skill.thieving;

import com.overload.util.ItemIdentifiers;

import java.util.HashMap;
import java.util.Map;

public enum ThievingNpcData {
	
	MAN(new int[] { 3014, 3015, 3078, 3079, 3080, 3081, 3082, 3083, 3084, 3085, 3267, 3268, 3260, 3264, 3265, 3266, 3267, 3268 }, 1, 8, 5, new int[][] {
	        {ItemIdentifiers.COINS, 30 } }),

    FARMER(new int[] {3086, 3087, 3088, 3089, 3090, 3091}, 10, 14, 5, new int[][] {
	        { ItemIdentifiers.COINS, 18 }, {ItemIdentifiers.POTATO_SEED, 1  }}),

    FEMALE_HAM(new int[] { 2540, 2541 }, 15, 18, 5, new int[][]{ {ItemIdentifiers.BUTTONS},
            {ItemIdentifiers.RUSTY_SWORD}, {ItemIdentifiers.DAMAGED_ARMOUR}, {ItemIdentifiers.FEATHER, 5},
            {ItemIdentifiers.BRONZE_ARROW}, {ItemIdentifiers.BRONZE_AXE}, {ItemIdentifiers.BRONZE_DAGGER},
            {ItemIdentifiers.BRONZE_PICKAXE}, {ItemIdentifiers.COWHIDE}, {ItemIdentifiers.IRON_AXE},
            {ItemIdentifiers.IRON_PICKAXE}, {ItemIdentifiers.LEATHER_BOOTS}, {ItemIdentifiers.LEATHER_GLOVES},
            {ItemIdentifiers.LEATHER_BODY}, {ItemIdentifiers.LOGS}, {ItemIdentifiers.THREAD}, {ItemIdentifiers.RAW_ANCHOVIES},
            {ItemIdentifiers.LOGS}, {ItemIdentifiers.RAW_CHICKEN}, {ItemIdentifiers.IRON_ORE}, {ItemIdentifiers.COAL},
            {ItemIdentifiers.STEEL_ARROW, 2}, {ItemIdentifiers.STEEL_AXE}, {ItemIdentifiers.STEEL_PICKAXE},
            {ItemIdentifiers.KNIFE}, {ItemIdentifiers.NEEDLE}, {ItemIdentifiers.STEEL_DAGGER}, {ItemIdentifiers.TINDERBOX},
            {ItemIdentifiers.UNCUT_JADE}, {ItemIdentifiers.UNCUT_OPAL}, {ItemIdentifiers.COINS, 25},
            {ItemIdentifiers.HAM_GLOVES}, {ItemIdentifiers.HAM_CLOAK}, {ItemIdentifiers.HAM_BOOTS}, {ItemIdentifiers.HAM_SHIRT},
            {ItemIdentifiers.HAM_ROBE}, {ItemIdentifiers.HAM_LOGO}, {ItemIdentifiers.HAM_HOOD},
            {ItemIdentifiers.GRIMY_GUAM_LEAF}, {ItemIdentifiers.GRIMY_MARRENTILL}, {ItemIdentifiers.GRIMY_TARROMIN},
            {ItemIdentifiers.GRIMY_HARRALANDER} }),

    MALE_HAM(new int[] { 2541 }, 20, 22, 5, new int[][] { {ItemIdentifiers.BUTTONS},
            {ItemIdentifiers.RUSTY_SWORD}, {ItemIdentifiers.DAMAGED_ARMOUR}, {ItemIdentifiers.FEATHER, 5},
            {ItemIdentifiers.BRONZE_ARROW}, {ItemIdentifiers.BRONZE_AXE}, {ItemIdentifiers.BRONZE_DAGGER},
            {ItemIdentifiers.BRONZE_PICKAXE}, {ItemIdentifiers.COWHIDE}, {ItemIdentifiers.IRON_AXE},
            {ItemIdentifiers.IRON_PICKAXE}, {ItemIdentifiers.LEATHER_BOOTS}, {ItemIdentifiers.LEATHER_GLOVES},
            {ItemIdentifiers.LEATHER_BODY}, {ItemIdentifiers.LOGS}, {ItemIdentifiers.THREAD}, {ItemIdentifiers.RAW_ANCHOVIES},
            {ItemIdentifiers.LOGS}, {ItemIdentifiers.RAW_CHICKEN}, {ItemIdentifiers.IRON_ORE}, {ItemIdentifiers.COAL},
            {ItemIdentifiers.STEEL_ARROW, 2}, {ItemIdentifiers.STEEL_AXE}, {ItemIdentifiers.STEEL_PICKAXE},
            {ItemIdentifiers.KNIFE}, {ItemIdentifiers.NEEDLE}, {ItemIdentifiers.STEEL_DAGGER}, {ItemIdentifiers.TINDERBOX},
            {ItemIdentifiers.UNCUT_JADE}, {ItemIdentifiers.UNCUT_OPAL}, {ItemIdentifiers.COINS, 25},
            {ItemIdentifiers.HAM_GLOVES}, {ItemIdentifiers.HAM_CLOAK}, {ItemIdentifiers.HAM_BOOTS}, {ItemIdentifiers.HAM_SHIRT},
            {ItemIdentifiers.HAM_ROBE}, {ItemIdentifiers.HAM_LOGO}, {ItemIdentifiers.HAM_HOOD},
            {ItemIdentifiers.GRIMY_GUAM_LEAF}, {ItemIdentifiers.GRIMY_MARRENTILL}, {ItemIdentifiers.GRIMY_TARROMIN},
            {ItemIdentifiers.GRIMY_HARRALANDER} }),

    HAM_GUARD(new int[] { 2536, 2537, 2538 }, 20, 22, 5, new int[][]{ {ItemIdentifiers.BUTTONS},
            {ItemIdentifiers.RUSTY_SWORD}, {ItemIdentifiers.DAMAGED_ARMOUR}, {ItemIdentifiers.FEATHER, 5},
            {ItemIdentifiers.BRONZE_ARROW}, {ItemIdentifiers.BRONZE_AXE}, {ItemIdentifiers.BRONZE_DAGGER},
            {ItemIdentifiers.BRONZE_PICKAXE}, {ItemIdentifiers.COWHIDE}, {ItemIdentifiers.IRON_AXE},
            {ItemIdentifiers.IRON_PICKAXE}, {ItemIdentifiers.LEATHER_BOOTS}, {ItemIdentifiers.LEATHER_GLOVES},
            {ItemIdentifiers.LEATHER_BODY}, {ItemIdentifiers.LOGS}, {ItemIdentifiers.THREAD}, {ItemIdentifiers.RAW_ANCHOVIES},
            {ItemIdentifiers.LOGS}, {ItemIdentifiers.RAW_CHICKEN}, {ItemIdentifiers.IRON_ORE}, {ItemIdentifiers.COAL},
            {ItemIdentifiers.STEEL_ARROW, 2}, {ItemIdentifiers.STEEL_AXE}, {ItemIdentifiers.STEEL_PICKAXE},
            {ItemIdentifiers.KNIFE}, {ItemIdentifiers.NEEDLE}, {ItemIdentifiers.STEEL_DAGGER}, {ItemIdentifiers.TINDERBOX},
            {ItemIdentifiers.UNCUT_JADE}, {ItemIdentifiers.UNCUT_OPAL}, {ItemIdentifiers.COINS, 25},
            {ItemIdentifiers.HAM_GLOVES}, {ItemIdentifiers.HAM_CLOAK}, {ItemIdentifiers.HAM_BOOTS}, {ItemIdentifiers.HAM_SHIRT},
            {ItemIdentifiers.HAM_ROBE}, {ItemIdentifiers.HAM_LOGO}, {ItemIdentifiers.HAM_HOOD},
            {ItemIdentifiers.GRIMY_GUAM_LEAF}, {ItemIdentifiers.GRIMY_MARRENTILL}, {ItemIdentifiers.GRIMY_TARROMIN},
            {ItemIdentifiers.GRIMY_HARRALANDER} }),

    WARRIOR_WOMAN(new int[] { 3100 }, 25, 26, 5, new int[][] { {
	    ItemIdentifiers.COINS, 125 } }),

    AL_KHARID_WARRIOR(new int[] { 3103 }, 25, 26, 5, new int[][] {
	        { ItemIdentifiers.COINS, 125 } }),

    ROGUE(new int[] { 2884}, 32, 35, 5, new int[][] { {ItemIdentifiers.COINS, 34}, {ItemIdentifiers.LOCKPICK}, {ItemIdentifiers.IRON_DAGGER_P_},
            {ItemIdentifiers.JUG_OF_WINE}, {ItemIdentifiers.AIR_RUNE, 8} }),

    CAVE_GOBLIN(new int[] { 2268, 2269, 2270, 2271, 2272, 2273, 2274, 2275, 2276, 2277, 2278, 2279, 2280, 2281, 2282, 2283, 2284, 2285 }, 36, 40, 5,
            new int[][] { { ItemIdentifiers.COINS, 1000 } ,{ItemIdentifiers.COINS, 10}, {ItemIdentifiers.IRON_ORE},
            {ItemIdentifiers.TINDERBOX}, {ItemIdentifiers.SWAMP_TAR},
            {ItemIdentifiers.OIL_LANTERN}, {ItemIdentifiers.TORCH},
            {ItemIdentifiers.GREEN_GLOOP_SOUP},
            {ItemIdentifiers.FROGSPAWN_GUMBO}, {ItemIdentifiers.FROGBURGER},
            {ItemIdentifiers.COATED_FROGS_LEGS}, {ItemIdentifiers.BAT_SHISH},
            {ItemIdentifiers.FINGERS}, {ItemIdentifiers.BULLSEYE_LANTERN},
            {ItemIdentifiers.CAVE_GOBLIN_WIRE} }),

    GUARD(new int[] { 1546, 1547, 1548, 1549, 1550, 3010, 3011, 3094, 3245, 3267, 3268, 3269, 3270, 3271, 3272, 3273, 3274, 3283 }, 40, 46, 5,
            new int[][] { { ItemIdentifiers.COINS, 125 } }),

    FREMENNIK_CITIZEN(new int[] { 2462 }, 45, 46, 5, new int[][] {
	        { ItemIdentifiers.COINS, 125 } }),

    BEARDED_POLLNIVNIAN_BANDIT(new int[] { 1880 }, 45, 46, 5, new int[][] {
	        { ItemIdentifiers.COINS, 40 } }),

    DESERT_BANDIT(new int[] { 690 }, 53, 79, 5, new int[][] {
	        { ItemIdentifiers.COINS, 30 } }),

    KNIGHT_OF_ARDOUGNE(new int[] { 3111, 3108 }, 55, 84, 5, new int[][] {
	        { ItemIdentifiers.COINS, 50 } }),

    POLLNIVNIAN_BANDIT(new int[] { 734, 735 }, 55,84, 5, new int[][]{
	        { ItemIdentifiers.COINS, 50 } }),

    YANILLE_WATCHMAN(new int[] { 6086, 6087, 6094, 6095, 6096 }, 65, 137, 5, new int[][] {
	        { ItemIdentifiers.COINS, 60 } }),

    MENAPHITE_THUG(new int[] { 1904 }, 65, 137, 5, new int[][] {
	        { ItemIdentifiers.COINS, 60 } }),

    PALADIN(new int[] { 20 }, 70, 151, 5, new int[][] {
	        { ItemIdentifiers.COINS, 80 } }),

    MONKEY_KNIFE_FIGHTER(new int[] { -1 }, 70, 150, 0, new int[][] {
	        { ItemIdentifiers.COINS, 50 } }),

    NOME(new int[] { -1 }, 75, 198, 5, new int[][] {
	        { ItemIdentifiers.COINS, 300 } }),

    HERO(new int[] { 21 }, 80, 273, 6, new int[][] {
	        { ItemIdentifiers.COINS, 300 } }),

    ELVES(new int[] { 1183 }, 85, 353, 6, new int[][] {
	        { ItemIdentifiers.COINS, 280 }, { ItemIdentifiers.COINS, 350 } }),

    DWARF_TRADERS(new int[] { -1 }, 90, 556, 4, new int[][] {
	        { ItemIdentifiers.COINS, 400 } }),

    MASTER_FARMER(new int[] { 2234 }, 90, 556, 6, null),

    MASTER_FARMER2(new int[] { 3299 }, 90, 556, 6, null),

	// end special cases
	;

	public static void declare() {
		for (ThievingNpcData data : values()) {
			for (int i : data.npcIds) {
				npcs.put(i, data);
			}
		}
	}

	int[] npcIds;
	int levelRequired;
	int stunTime;
	int[][] retrievableItems;
	int experience;

	private static Map<Integer, ThievingNpcData> npcs = new HashMap<>();

	public static ThievingNpcData getNpcById(int id) {
		return npcs.get(Integer.valueOf(id));
	}

	 ThievingNpcData(int[] ids, int level, int exp, int time, int[][] items) {
		npcIds = ids;
		levelRequired = level;
		experience = exp;
		stunTime = time;
		retrievableItems = items;
	}

	public int getExperience() {
		return experience;
	}

	public int[][] getItems() {
		return retrievableItems;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int[] getNpcIds() {
		return npcIds;
	}

	public int getStunTime() {
		return stunTime;
	}
}
