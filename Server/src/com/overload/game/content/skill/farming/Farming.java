package com.overload.game.content.skill.farming;

import com.overload.game.World;
import com.overload.game.content.skill.farming.Allotments.AllotmentFieldsData;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Item;
import com.overload.game.model.Position;
import com.overload.game.model.Skill;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;

public class Farming {
	/**
	 * Special plant one constructor & getter
	 */
	private SpecialPlantOne specialPlantOne;

	public SpecialPlantOne getSpecialPlantOne() {
		return specialPlantOne;
	}
	
	public void setSpecialPlantOne(SpecialPlantOne specialPlantOne) {
		this.specialPlantOne = specialPlantOne;
	}

	/**
	 * Special plant one constructor & getter
	 */
	private SpecialPlantTwo specialPlantTwo;

	public SpecialPlantTwo getSpecialPlantTwo() {
		return specialPlantTwo;
	}
	
	public void setSpecialPlantTwo(SpecialPlantTwo specialPlantTwo) {
		this.specialPlantTwo = specialPlantTwo;
	}

	/**
	 * Compost constructor & getter
	 */
	private Compost compost;

	public Compost getCompost() {
		return compost;
	}
	
	public void setCompost(Compost compost) {
		this.compost = compost;
	}

	/**
	 * Allotoments constructor & getter
	 */
	private Allotments allotment;

	public Allotments getAllotment() {
		return allotment;
	}
	
	public void setAllotment(Allotments allotment) {
		this.allotment = allotment;
	}


	/**
	 * Seedling constructor & getter
	 */
	private Seedling seedling;

	public Seedling getSeedling() {
		return seedling;
	}
	
	public void setSeedling(Seedling seedling) {
		this.seedling = seedling;
	}

	private long farmingTimer = 0L;
	
	public long getFarmingTimer() {
		return farmingTimer;
	}
	
	public void setFarmingTimer(long farmingTimer) {
		this.farmingTimer = farmingTimer;
	}
	
	public Farming(Player player) {
		allotment = new Allotments(player);
		seedling = new Seedling(player);
		compost = new Compost(player);
		specialPlantOne = new SpecialPlantOne(player);
		specialPlantTwo	 = new SpecialPlantTwo(player);
	}
	
	public void doCalculations(Player player) {
		player.getPatches().entrySet().stream()
		.filter(p -> {
			for (Position pos : AllotmentFieldsData.valueOf(p.getKey()).getAllotmentPosition()) {
				if (player.getPosition().getDistance(pos) <= 56)
					return true;
			}
			return false;
		})
		.forEach(p -> {
			AllotmentFieldsData patch = AllotmentFieldsData.valueOf(p.getKey());
			allotment.doCalculations(player.getPatches().get(patch.name()));
			allotment.updateAllotmentsStates();
		});
	}
	
	public static boolean rakePatch(Player player, int x, int y) {
		return player.getFarming().getAllotment().clearPatch(x, y);
	}
	
	public static void preparePatch(Player player, int x, int y, String clickType, Item item) {
        final AllotmentFieldsData patch = AllotmentFieldsData.forIdPosition(x, y);
        if (patch == null)
        	return;
        final FarmingPatchState patchState = patch.getPatchState();
		if (clickType == "first") {
			if (player.getFarming().getAllotment().isPatchFullyGrown(patchState)) {
				player.getFarming().getAllotment().harvest(x, y);
			} else if (patchState.isDiseased()) {
				if (player.getInventory().contains(FarmingConstants.ITEM_PLANT_CURE.getId())) {
					player.getFarming().getAllotment().curePlant(patch);
				} else {
					player.getPacketSender().sendMessage("You need plant cure to cure this patch.");
				}
			} else if (patchState.isDead()) {
				player.getFarming().getAllotment().clearPatch(patch, x, y);
			} else if (patchState.getWeedLevel() < 3) {
				rakePatch(player, x, y);
			}
		} else if (clickType == "second") {
			player.getFarming().getAllotment().inspect(x, y);
		} else if (clickType == "itemPatch") {
			// Curing
			if (item.getId() == FarmingConstants.ITEM_PLANT_CURE.getId() && !player.getFarming().getAllotment().isPatchFullyGrown(patchState)
					&& patchState.isDiseased() && player.getInventory().contains(FarmingConstants.ITEM_PLANT_CURE.getId())) {
				player.getFarming().getAllotment().curePlant(patch);
			}

			// Raking
			if (item.getId() == FarmingConstants.RAKE && patchState.getWeedLevel() < 3
					&& player.getInventory().contains(FarmingConstants.RAKE)) {
				rakePatch(player, x, y);
			}

			// Treating
			if (patchState.getWeedLevel() == 3 && patchState.getTreatment() == PatchTreatment.NOT_TREATED) {
				final PatchTreatment treatment = PatchTreatment.forItemId(item.getId());

				if (treatment != null) {
					player.getFarming().getAllotment().treatPatch(x, y, treatment.getItemId());
				}
			}

			// Watering
			if (patchState.getPlanted() != null && !patchState.isWatered() && FarmingConstants.isWateringCan(item.getId()) && patchState.getWeedLevel() == 3) {
				if (patch.getPatchType().isWaterable() && !player.getFarming().getAllotment().isPatchFullyGrown(patchState)
						&& !patchState.isDiseased() && !patchState.isDead()) {
					player.getFarming().getAllotment().waterPatch(x, y, item.getId());
				}
			}

			// Planting
			if (patchState.getWeedLevel() == 3 && patchState.getPlanted() == null) {
				final Plantables plantable = Plantables.forSeedItemId(item.getId());

				// Check if the plantable can be planted in this patch type
				if (plantable != null && patch.getPatchType() == plantable.getType()) {
					if (player.getSkillManager().getCurrentLevel(Skill.FARMING) < plantable.getRequiredLevel()) {
						player.getPacketSender().sendMessage("You need a Farming level of " + plantable.getRequiredLevel() + " to plant that.");
					} else if (player.getInventory().contains(FarmingConstants.SEED_DIBBER)) {
						player.getFarming().getAllotment().plant(patch, plantable);
					} else {
						player.getPacketSender().sendMessage("You need a Seed dibber to plant seeds.");
					}
				}
			}
		}
	}
	
	public static boolean inspectObject(Player player, int x, int y) {
		// allotments
		if (player.getFarming().getAllotment().inspect(x, y)) {
			return true;
		}
		return false;
	}

	public static boolean harvest(Player player, AllotmentFieldsData patch, int x, int y) {
		// allotments

		if (player.getFarming().getAllotment().harvest(x, y)) {
			return true;
		}
		return false;
	}
	
	public static void declare() {
		TaskManager.submit(new Task(10, true) {
			@Override
			public void execute() {
				for (Player player : World.getPlayers()) {
					if (player == null || player.getFarming() == null) {
						continue;
					}

					player.getFarming().farmingTimer++;
					player.getFarming().doCalculations(player);
				}
			}

			@Override
			public void stop() {
			}
		});
	}

	public static long getMinutesCounter(Player player) {
		return player.getFarming().farmingTimer;
	}

	public static boolean handleClickOption(String clickType, Player player, int x, int y, Item item) {
		if (item != null) {
			if (clickType == "itemPatch") {
				preparePatch(player, x, y, clickType, item);
			} else if (clickType == "itemCompost") {
				player.getCompost().fillCompostBin(x, y, item.getId());
			}
		} else {
	        final AllotmentFieldsData patch = AllotmentFieldsData.forIdPosition(x, y);
	        if (patch == null) return false;
	        final FarmingPatchState patchState = patch.getPatchState();
			if (clickType == "first") {
				if (player.getFarming().getAllotment().isPatchFullyGrown(patchState)) {
					harvest(player, patch, x, y);
				} else if (patchState.isDiseased()) {
					if (player.getInventory().contains(FarmingConstants.ITEM_PLANT_CURE)) {
						player.getFarming().getAllotment().curePlant(patch);
					} else {
						player.getPacketSender().sendMessage("You need plant cure to cure this patch.");
					}
				} else if (patchState.isDead()) {
					player.getFarming().getAllotment().clearPatch(patch, x, y);
				} else if (patchState.getWeedLevel() < 3) {
					rakePatch(player, x, y);
				}
			} else if (clickType == "second") {
				inspectObject(player, x, y);
			}
		}
		return false;
	}
	
	public static void onRegionChange(Player player) {
		player.getFarming().getAllotment().updateAllotmentsStates();
	}
}