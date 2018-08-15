package com.overload.game.content.skill.farming;

import org.joda.time.DateTime;

import com.overload.game.content.skill.farming.PatchTreatment;

public class FarmingPatchState {

	/**
	 * The seed planted in this patch.
	 */
	private Plantables planted;

	private int growth;

	/**
	 * The instant in time when the last growth took place.
	 */
	private DateTime lastGrowthTime;

	/**
	 * The amount of weed removed. [0..3]
	 */
	private int weedLevel = 0;

	/**
	 * The amount still left in this patch to be yielded.
	 */
	private int yield;

	/**
	 * The treatment for this patch.
	 */
	private PatchTreatment treatment = PatchTreatment.NOT_TREATED;

	/**
	 * Whether or not the patch has been watered.
	 */
	private boolean watered;

	/**
	 * Whether or not the patch is diseased.
	 */
	private boolean diseased;

	/**
	 * Whether or not the patch has died.
	 */
	private boolean dead;

	/**
	 * Is the crop immune to disease.
	 * Achievable by planting a flower which protects it, or paying a farmer.
	 */
	private boolean immune;

	/**
	 * Required.
	 */
	public FarmingPatchState() {

	}

	public int getWeedLevel() {
		return weedLevel;
	}

	public void setWeedLevel(int weedLevel) {
		this.weedLevel = weedLevel;
	}

	public Plantables getPlanted() {
		return planted;
	}

	public void setPlanted(Plantables planted) {
		this.planted = planted;
	}

	public int getGrowth() {
		return growth;
	}

	public void setGrowth(int growth) {
		this.growth = growth;
	}

	public DateTime getLastGrowthTime() {
		if (lastGrowthTime == null)
			return new DateTime(0L);
		return lastGrowthTime;
	}

	public void setLastGrowthTime(DateTime lastGrowthTime) {
		this.lastGrowthTime = lastGrowthTime;
	}

	public int getYield() {
		return yield;
	}

	public void setYield(int yield) {
		this.yield = yield;
	}

	public PatchTreatment getTreatment() {
		return treatment;
	}

	public void setTreatment(PatchTreatment treatment) {
		this.treatment = treatment;
	}

	public boolean isWatered() {
		return watered;
	}

	public void setWatered(boolean watered) {
		this.watered = watered;
	}

	public boolean isDiseased() {
		return diseased;
	}

	public void setDiseased(boolean diseased) {
		this.diseased = diseased;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isImmune() {
		return immune;
	}

	public void setImmune(boolean immune) {
		this.immune = immune;
	}
}
