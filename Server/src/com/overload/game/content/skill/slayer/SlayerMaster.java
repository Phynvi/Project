package com.overload.game.content.skill.slayer;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.overload.util.Misc;

public class SlayerMaster {

	private final int id;

	private final int level;

	private final SlayerTask[] available;

	private final int[] pointRewards;

	public static final List<SlayerMaster> MASTERS = Misc.jsonArrayToList(Paths.get("data", "definitions", "slayer.json"), SlayerMaster[].class);

	public SlayerMaster(int id, int level, int[] pointRewards, SlayerTask[] available) {
		Preconditions.checkArgument(pointRewards.length == 6, "The 'pointRewards' variable must have a length of 6.");
		Preconditions.checkArgument(Arrays.stream(pointRewards).allMatch(Misc::isNonNegative));
		this.id = id;
		this.level = level;
		this.pointRewards = pointRewards;
		this.available = available;
	}

	public int getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getPointReward(int index) {
		Preconditions.checkArgument(index > -1 && index < 6, "Index must be non-negative and less than three.");
		return pointRewards[index];
	}

	public SlayerTask[] getAvailable() {
		return available;
	}

	public static Optional<SlayerMaster> get(int id) {
		return MASTERS.stream().filter(master -> master.id == id).findFirst();
	}

	public static Optional<SlayerTask> get(String name) {
		return MASTERS.stream().flatMap(master -> Arrays.stream(master.available).filter(task -> task.getPrimaryName().equals(name))).findFirst();
	}
    
}

