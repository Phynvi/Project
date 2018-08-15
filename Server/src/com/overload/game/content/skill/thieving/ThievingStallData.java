package com.overload.game.content.skill.thieving;

import java.util.HashMap;

public enum ThievingStallData {
	
	KELDAGRIM_BAKER_STALL(6163, 1, 634, 15, new int[][] {
			ThievingStallTask.THEIVING_ITEMS[0],
			ThievingStallTask.THEIVING_ITEMS[1],
			ThievingStallTask.THEIVING_ITEMS[2],
			ThievingStallTask.THEIVING_ITEMS[3],
			ThievingStallTask.THEIVING_ITEMS[4],
			ThievingStallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_CROSSBOW_STALL(17031, 10, 634, 30, new int[][] {
			ThievingStallTask.THEIVING_ITEMS[1],
			ThievingStallTask.THEIVING_ITEMS[2],
			ThievingStallTask.THEIVING_ITEMS[3],
			ThievingStallTask.THEIVING_ITEMS[4],
			ThievingStallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_CRAFTING_STALL(6166, 25, 634, 40, new int[][] {
			ThievingStallTask.THEIVING_ITEMS[2],
			ThievingStallTask.THEIVING_ITEMS[3],
			ThievingStallTask.THEIVING_ITEMS[4],
			ThievingStallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_CLOTHES_STALL(6165, 50, 634, 50, new int[][] {
			ThievingStallTask.THEIVING_ITEMS[3],
			ThievingStallTask.THEIVING_ITEMS[4],
			ThievingStallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_SILVER_STALL(6164, 65, 634, 115, new int[][] {
			ThievingStallTask.THEIVING_ITEMS[4],
			ThievingStallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_GEM_STALL(6162, 90, 634, 150, new int[][] {
			ThievingStallTask.THEIVING_ITEMS[6] }),

	SEED_STALL_LUMB(7053, 35, 634, 50, new int[][]{ { 5319, 1 }, { 5324, 1 }, { 5322, 1 }, { 5320, 1 }, { 5096, 1 }, { 5097, 1 },
            { 5098, 1 }, { 5318, 1 }, { 5319, 1 }, { 6036, 1 } }),

	RELLEKKA_FISH(4277, 85, 634, 144, new int[][] { { 383, 1 }, { 7944, 1 },
			{ 377, 1 }, { 7944, 1 }, { 377, 1 }, { 327, 1 },
			{ 327, 1 }, { 327, 1 } }),

	RELLEKKA_FUR(4278, 20, 634, 50, new int[][] { { 6814, 1 } }),

	;

	public static final void declare() {
		for (ThievingStallData data : values())
			stalls.put(Integer.valueOf(data.getObjectId()), data);
	}

	int objectId;
	int levelRequired;
	int replacementId;
	int experience;
	int[][] rewards;

	private static HashMap<Integer, ThievingStallData> stalls = new HashMap<Integer, ThievingStallData>();

	public static ThievingStallData getObjectById(int id) {
		return stalls.get(Integer.valueOf(id));
	}

	 ThievingStallData(int id, int level, int replace, int experience, int[][] rewards) {
		objectId = id;
		levelRequired = level;
		replacementId = replace;
		this.experience = experience;
		this.rewards = rewards;
	}

	public int getExperience() {
		return experience;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getObjectId() {
		return objectId;
	}

	public int getReplacementId() {
		return replacementId;
	}

	public int[][] getRewards() {
		return rewards;
	}
}
