package com.overload.game.content.skill.farming;

import org.joda.time.Duration;

import java.util.Arrays;


public enum Plantables {
	HERB_GUAM(       	 FarmingPatchType.HERB_PATCH, 		9,  12,  5291, 199,  249,  0x04, 0x08, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_MARRENTILL( 	 FarmingPatchType.HERB_PATCH, 		14, 15,  5292, 201,  251,  0x0b, 0x0f, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_TARROMIN(   	 FarmingPatchType.HERB_PATCH, 		19, 18,  5293, 203,  253,  0x12, 0x16, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_HARRALANDER(	 FarmingPatchType.HERB_PATCH, 		26, 24,  5294, 205,  255,  0x19, 0x1d, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_GOUT_TUBER(     FarmingPatchType.HERB_PATCH, 		29, 105, 6311, 3261, 3261, 0xc0, 0xc4, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_RANARR(     	 FarmingPatchType.HERB_PATCH, 		32, 30,  5295, 207,  257,  0x20, 0x24, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_TOADFLAX(   	 FarmingPatchType.HERB_PATCH, 		38, 38,  5296, 3050, 2998, 0x27, 0x2b, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_IRIT(       	 FarmingPatchType.HERB_PATCH, 		44, 48,  5297, 209,  259,  0x2e, 0x32, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_AVANTOE(    	 FarmingPatchType.HERB_PATCH, 		50, 61,  5298, 211,  261,  0x35, 0x39, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_KWUARM(     	 FarmingPatchType.HERB_PATCH, 		56, 78,  5299, 213,  263,  0x44, 0x48, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_SNAPDRAGON( 	 FarmingPatchType.HERB_PATCH, 		62, 99,  5300, 3051, 3000, 0x4b, 0x4f, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_CADANTINE(  	 FarmingPatchType.HERB_PATCH, 		67, 120, 5301, 215,  265,  0x52, 0x56, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_LANTADYME(  	 FarmingPatchType.HERB_PATCH, 		73, 152, 5302, 2485, 2481, 0x59, 0x5d, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_DWARF_WEED( 	 FarmingPatchType.HERB_PATCH,		79, 192, 5303, 217,  267,  0x60, 0x64, 5, 7, 	FarmingConstants.setTime(75)),
	HERB_TORSTOL(    	 FarmingPatchType.HERB_PATCH,		85, 225, 5304, 219,  269,  0x67, 0x6b, 5, 7, 	FarmingConstants.setTime(75)),

	FLOWER_MARIGOLDS( 	 FarmingPatchType.FLOWER_PATCH, 	2,  47,  5096, 6010, 6010, 0x08, 0x0c, 1, 1, 	FarmingConstants.setTime(17)),
	FLOWER_ROSEMARY(  	 FarmingPatchType.FLOWER_PATCH, 	11, 67,  5097, 6014, 6014, 0x0d, 0x11, 1, 1, 	FarmingConstants.setTime(17)),
	FLOWER_NASTURTIUM(	 FarmingPatchType.FLOWER_PATCH, 	24, 111, 5098, 6012, 6012, 0x12, 0x16, 1, 1, 	FarmingConstants.setTime(17)),
	FLOWER_WOAD(      	 FarmingPatchType.FLOWER_PATCH, 	25, 116, 5099, 1793, 1793, 0x17, 0x1b, 1, 1, 	FarmingConstants.setTime(17)),
	FLOWER_LIMPWURT(  	 FarmingPatchType.FLOWER_PATCH, 	26, 120, 5100, 225,  225,  0x1c, 0x20, 5, 7, 	FarmingConstants.setTime(17)),

	ALLOTMENT_POTATOES(  FarmingPatchType.ALLOTMENT_PATCH, 		1,  9,   5318, 1942, 1942, 0x06, 0x0a, 5, 7, 	FarmingConstants.setTime(40)),
	ALLOTMENT_ONIONS(    FarmingPatchType.ALLOTMENT_PATCH, 		5,  11,  5319, 1957, 1957, 0x0d, 0x12, 5, 7, 	FarmingConstants.setTime(40)),
	ALLOTMENT_CABBAGES(  FarmingPatchType.ALLOTMENT_PATCH, 		7,  12,  5324, 1965, 1965, 0x14, 0x18, 5, 7, 	FarmingConstants.setTime(35)),
	ALLOTMENT_TOMATOES(  FarmingPatchType.ALLOTMENT_PATCH, 		12, 14,  5322, 1982, 1982, 0x1b, 0x1f, 5, 7, 	FarmingConstants.setTime(40)),
	ALLOTMENT_SWEETCORN( FarmingPatchType.ALLOTMENT_PATCH, 		20, 19,  5320, 5986, 5986, 0x22, 0x28, 5, 7, 	FarmingConstants.setTime(55)),
	ALLOTMENT_STRAWBERRY(FarmingPatchType.ALLOTMENT_PATCH, 		31, 29,  5323, 5504, 5504, 0x2b, 0x32, 5, 7, 	FarmingConstants.setTime(55)),
	ALLOTMENT_WATERMELON(FarmingPatchType.ALLOTMENT_PATCH, 		47, 55,  5321, 5982, 5982, 0x34, 0x3e, 5, 7, 	FarmingConstants.setTime(75)),

	FRUIT_TREE_APPLE(    FarmingPatchType.FRUIT_TREE_PATCH, 27, 22,  5496, 1955, 1955, 0x08, 0x14, 5, 7, 	FarmingConstants.setTime(876)),
	FRUIT_TREE_BANANA(   FarmingPatchType.FRUIT_TREE_PATCH, 33, 28,  5497, 1963, 1963, 0x23, 0x2f, 5, 7, 	FarmingConstants.setTime(876)),
	FRUIT_TREE_ORANGE(   FarmingPatchType.FRUIT_TREE_PATCH, 39, 36,  5498, 2108, 2108, 0x48, 0x54, 5, 7, 	FarmingConstants.setTime(876)),
	FRUIT_TREE_CURRY(    FarmingPatchType.FRUIT_TREE_PATCH, 42, 40,  5499, 5970, 5970, 0x63, 0x6f, 5, 7, 	FarmingConstants.setTime(876)),
	FRUIT_TREE_PINEAPPLE(FarmingPatchType.FRUIT_TREE_PATCH, 51, 57,  5500, 2114, 2114, 0x88, 0x94, 5, 7, 	FarmingConstants.setTime(876)),
	FRUIT_TREE_PAPAYA(   FarmingPatchType.FRUIT_TREE_PATCH, 57, 72,  5501, 5972, 5972, 0xa3, 0xaf, 5, 7, 	FarmingConstants.setTime(876)),
	FRUIT_TREE_PALM(     FarmingPatchType.FRUIT_TREE_PATCH, 68, 171, 5502, 5974, 5974, 0xc8, 0xd4, 5, 7, 	FarmingConstants.setTime(876)),

	TREE_OAK(			 FarmingPatchType.TREE_PATCH, 		15, 14,  5370, 6043, 6043, 0x08, 0x0c, 5, 7, 	FarmingConstants.setTime(139)),
	TREE_WILLOW(		 FarmingPatchType.TREE_PATCH, 		30, 25,  5371, 6045, 6045, 0x0f, 0x15, 5, 7, 	FarmingConstants.setTime(220)),
	TREE_MAPLE(			 FarmingPatchType.TREE_PATCH, 		45, 45,  5372, 6047, 6047, 0x18, 0x20, 5, 7, 	FarmingConstants.setTime(300)),
	TREE_YEW(			 FarmingPatchType.TREE_PATCH, 		60, 81,  5373, 6049, 6049, 0x23, 0x2d, 5, 7, 	FarmingConstants.setTime(380)),
	TREE_MAGIC(			 FarmingPatchType.TREE_PATCH, 		75, 146, 5374, 6051, 6051, 0x30, 0x3c, 5, 7, 	FarmingConstants.setTime(460)),
	
	HOP_BARLEY(			 FarmingPatchType.HOP_PATCH, 		3,  9,   5305, 6006, 6006, 0x31, 0x35, 5, 7, 	FarmingConstants.setTime(35)),
	HOP_HAMMERSTONE(	 FarmingPatchType.HOP_PATCH, 		4,  10,  5307, 5994, 5994, 0x04, 0x08, 5, 7, 	FarmingConstants.setTime(35)),
	HOP_ASGARNIAN(		 FarmingPatchType.HOP_PATCH, 		8,  11,  5308, 5996, 5996, 0x0b, 0x10, 5, 7, 	FarmingConstants.setTime(45)),
	HOP_JUTE(			 FarmingPatchType.HOP_PATCH, 		13, 15,  5306, 5931, 5931, 0x38, 0x3d, 5, 7, 	FarmingConstants.setTime(45)),
	HOP_YANILLIAN(		 FarmingPatchType.HOP_PATCH, 		16, 15,  5309, 5998, 5998, 0x13, 0x19, 5, 7, 	FarmingConstants.setTime(54)),
	HOP_KRANDORIAN(		 FarmingPatchType.HOP_PATCH, 		21, 18,  5310, 6000, 6000, 0x1c, 0x23, 5, 7, 	FarmingConstants.setTime(65)),
	HOP_WILDBLOOD(		 FarmingPatchType.HOP_PATCH, 		28, 23,  5311, 6002, 6002, 0x26, 0x2e, 5, 7, 	FarmingConstants.setTime(75)), ;

	private FarmingPatchType type;
	private int requiredLevel;
	private int experience;
	private int seedItemId;
	private int grimy;
	private int clean;
	private int minGrowth;
	private int maxGrowth;
	private int minYield;
	private int maxYield;
	private Duration growthTime;

	Plantables(FarmingPatchType type, int requiredLevel, int experience, int seedItemId, int grimy, int clean, int minGrowth, int maxGrowth, int minYield, int maxYield, Duration growthTime) {
		this.type = type;
		this.requiredLevel = requiredLevel;
		this.experience = experience;
		this.seedItemId = seedItemId;
		this.grimy = grimy;
		this.clean = clean;
		this.minGrowth = minGrowth;
		this.maxGrowth = maxGrowth;
		this.minYield = minYield;
		this.maxYield = maxYield;
		this.growthTime = Duration.millis(
			(long) ((growthTime.getMillis() / (maxGrowth-minGrowth)) / 2)
		);
	}

	public int getClean() {
		return clean;
	}
	
	public FarmingPatchType getType() {
		return type;
	}

	public int getRequiredLevel() {
		return requiredLevel;
	}

	public int getExperience() {
		return experience;
	}

	public int getSeedItemId() {
		return seedItemId;
	}

	public int getReward() {
		return grimy;
	}

	public int getMinGrowth() {
		return minGrowth;
	}

	public int getMaxGrowth() {
		return maxGrowth;
	}

	public int getMinYield() {
		return minYield;
	}

	public int getMaxYield() {
		return maxYield;
	}

	public Duration getGrowthTime() {
		return growthTime;
	}

	public static Plantables forSeedItemId(int seedItemId) {
		return Arrays.stream(values())
				.filter(p -> p.getSeedItemId() == seedItemId)
				.findAny()
				.orElse(null);
	}

	public static Plantables forRewardItemId(int rewardItemId) {
		return Arrays.stream(values())
				.filter(p -> p.getReward() == rewardItemId)
				.findAny()
				.orElse(null);
	}
}
