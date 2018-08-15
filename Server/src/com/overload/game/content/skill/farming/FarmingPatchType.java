package com.overload.game.content.skill.farming;

import com.overload.game.model.Animation;

public enum FarmingPatchType {
	HERB_PATCH(new Animation(2282), false, true, 6/*7*/),
	ALLOTMENT_PATCH(new Animation(831), true, true, 6),
	FLOWER_PATCH(new Animation(2286), true, true, 6),
	TREE_PATCH(null, true, true, 6),
	FRUIT_TREE_PATCH(null, true, true, 0),
	BUSH_PATCH(null, true, true, 0),
	HOP_PATCH(null, true, true, 0);

	private Animation yieldAnimation;
	private boolean waterable;
	private boolean vulnerableToDisease;
	private int stateBitOffset;

	FarmingPatchType(Animation yieldAnimation, boolean waterable, boolean vulnerableToDisease, int stateBitOffset) {
		this.yieldAnimation = yieldAnimation;
		this.waterable = waterable;
		this.vulnerableToDisease = vulnerableToDisease;
		this.stateBitOffset = stateBitOffset;
	}

	public Animation getYieldAnimation() {
		return yieldAnimation;
	}

	public boolean isWaterable() {
		return waterable;
	}

	public boolean isVulnerableToDisease() {
		return vulnerableToDisease;
	}

	public int getStateBitOffset() {
		return stateBitOffset;
	}

	@Override
	public String toString() {
		return name().toLowerCase().replaceAll("_", " ");
	}
}
