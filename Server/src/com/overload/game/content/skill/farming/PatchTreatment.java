package com.overload.game.content.skill.farming;

import java.util.Arrays;

public enum PatchTreatment {
	NOT_TREATED(-1, 0),
	COMPOST(6032, 1),
	SUPERCOMPOST(6034, 2);

	private int itemId;
	private int yieldIncrease;

	PatchTreatment(int itemId, int yieldIncrease) {
		this.itemId = itemId;
		this.yieldIncrease = yieldIncrease;
	}

	public int getItemId() {
		return itemId;
	}

	public int getYieldIncrease() {
		return yieldIncrease;
	}

	public static PatchTreatment forItemId(int itemId) {
		return Arrays.stream(values()).filter(t -> t.getItemId() == itemId).findFirst().orElse(null);
	}
}
