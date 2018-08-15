package com.overload.game.content.skill.farming;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.EffectTimer;
import com.overload.game.model.Item;
import com.overload.game.model.Position;
import com.overload.game.model.Skill;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.net.BitConfig;
import com.overload.net.BitConfigBuilder;
import com.overload.util.Misc;

import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import static com.google.common.collect.Maps.newHashMap;

public class Allotments {

	private Player player;

	public Allotments(Player player) {
		this.player = player;
	}


	public enum AllotmentFieldsData {

		HERB_PATCH_CAMELOT(FarmingPatchType.HERB_PATCH, 8151, new FarmingPatchState(), 529, 24, new Position[] { new Position(2813, 3463) }, 2324),
		HERB_PATCH_ARDOUGNE(FarmingPatchType.HERB_PATCH, 8152, new FarmingPatchState(), 529, 24, new Position[] { new Position(2670, 3374) }, 2325),
		HERB_PATCH_CANIFIS(FarmingPatchType.HERB_PATCH, 8153, new FarmingPatchState(), 529, 24, new Position[] { new Position(3605, 3529) }, 2326),
		HERB_PATCH_FALADOR(FarmingPatchType.HERB_PATCH, 8150, new FarmingPatchState(), 529, 24, new Position[] { new Position(3058, 3311) }, 2323),

		ALLOTMENT_CAMELOT_NORTH(FarmingPatchType.ALLOTMENT_PATCH, 8552, new FarmingPatchState(), 529, 0, new Position[] { new Position(2805, 3466), new Position(2806, 3468), new Position(2805, 3467), new Position(2814, 3468) }, 2324),
		ALLOTMENT_CAMELOT_SOUTH(FarmingPatchType.ALLOTMENT_PATCH, 8553, new FarmingPatchState(), 529, 8, new Position[] { new Position(2805, 3459), new Position(2806, 3461), new Position(2802, 3459), new Position(2814, 3460) }, 2324),
		ALLOTMENT_ARDOUGNE_NORTH(FarmingPatchType.ALLOTMENT_PATCH, 8552, new FarmingPatchState(), 529, 0, new Position[] { new Position(2662, 3377), new Position(2663, 3379), new Position(2662, 3378), new Position(2671, 3379) }, 2325),
		ALLOTMENT_ARDOUGNE_SOUTH(FarmingPatchType.ALLOTMENT_PATCH, 8553, new FarmingPatchState(), 529, 8, new Position[] { new Position(2662, 3370), new Position(2663, 3372), new Position(2662, 3370), new Position(2671, 3371) }, 2325),
		ALLOTMENT_CANIFIS_SOUTH_EAST(FarmingPatchType.ALLOTMENT_PATCH, 8552, new FarmingPatchState(), 529, 8, new Position[] { new Position(3055, 3303), new Position(3059, 3304), new Position(3058, 3303), new Position(3059, 3308) }, 2326),
		ALLOTMENT_CANIFIS_NORTH_WEST(FarmingPatchType.ALLOTMENT_PATCH, 8553, new FarmingPatchState(), 529, 0, new Position[] { new Position(3597, 3525), new Position(3598, 3530), new Position(3597, 3529), new Position(3601, 3530) }, 2326),
		ALLOTMENT_FALADOR_NORTH_WEST(FarmingPatchType.ALLOTMENT_PATCH, 8552, new FarmingPatchState(), 529, 0, new Position[] { new Position(3050, 3307), new Position(3051, 3312), new Position(3050, 3311), new Position(3054, 3312) }, 2323),
		ALLOTMENT_FALADOR_SOUTH_EAST(FarmingPatchType.ALLOTMENT_PATCH, 8553, new FarmingPatchState(), 529, 8, new Position[] { new Position(3055, 3303), new Position(3059, 3304), new Position(3058, 3303), new Position(3059, 3308) }, 2323),

		FLOWER_PATCH_CAMELOT(FarmingPatchType.FLOWER_PATCH, 7848, new FarmingPatchState(), 529, 16,  new Position[] { new Position(2809, 3463) }, 2324),
		FLOWER_PATCH_ARDOUGNE(FarmingPatchType.FLOWER_PATCH, 7849, new FarmingPatchState(), 529, 16, new Position[] { new Position(2666, 3374) }, 2325),
		FLOWER_PATCH_CANIFIS(FarmingPatchType.FLOWER_PATCH, 7850, new FarmingPatchState(), 529, 16, new Position[] { new Position(3601, 3525) }, 2326),
		FLOWER_PATCH_FALADOR(FarmingPatchType.FLOWER_PATCH, 7847, new FarmingPatchState(), 529, 16, new Position[] { new Position(3054, 3307) }, 2323);
		
		private final FarmingPatchType type;
		private int plotId;
		private FarmingPatchState patchState;
		private int configId;
		private int configBitOffset;
		private Position[] allotmentPosition;
		private int farmerBelonging;

		AllotmentFieldsData(final FarmingPatchType type, int plotId, FarmingPatchState patchState, int configId, int configBitOffset, Position[] allotmentPosition, int farmerBelonging) {
			this.type = type;
			this.plotId = plotId;
			this.patchState = patchState;
			this.configId = configId;
			this.configBitOffset = configBitOffset;
			this.allotmentPosition = allotmentPosition;
			this.farmerBelonging = farmerBelonging;
		}

		public static AllotmentFieldsData forIdPosition(int x, int y) {
			for (AllotmentFieldsData allotmentFieldsData : AllotmentFieldsData.values()) {
				Position pos = allotmentFieldsData.getAllotmentPosition()[0];
				if (pos.getX() == x && pos.getY() == y) {
					return allotmentFieldsData;
				}
			}
			for (AllotmentFieldsData allotmentFieldsData : AllotmentFieldsData.values()) {
				if (allotmentFieldsData.getAllotmentPosition().length >= 4) {
					if (FarmingConstants.inRangeArea(allotmentFieldsData.getAllotmentPosition()[0], allotmentFieldsData.getAllotmentPosition()[1], x, y) || FarmingConstants.inRangeArea(allotmentFieldsData.getAllotmentPosition()[2], allotmentFieldsData.getAllotmentPosition()[3], x, y)) {
						return allotmentFieldsData;
					}
				}
			}
			return null;
		}
		public static AllotmentFieldsData forId(int objectId) {
			for (AllotmentFieldsData allotmentFieldsData : AllotmentFieldsData.values()) {
				if (objectId == allotmentFieldsData.getPlotId()) {
					return allotmentFieldsData;
				}
			}
			return null;
		}

		public int getPlotId() {
			return plotId;
		}
		
		public FarmingPatchType getPatchType() {
			return type;
		}
		
		public FarmingPatchState getPatchState() {
			return patchState;
		}
		
		public int getConfigId() {
			return configId;
		}
		
		public int getPatchBitOffset() {
			return configBitOffset;
		}

		public Position[] getAllotmentPosition() {
			return allotmentPosition;
		}

		public int getFarmerBelonging() {
			return farmerBelonging;
		}
	}

	/* update all the patch states */

	public void updateAllotmentsStates() {
		final Map<Integer, BitConfigBuilder> configMap = newHashMap();

		// We cannot send each patch config 1 by 1 since they are packed, and therefore since
		// different patches may have different config ids, we eagerly construct them.
		player.getPatches().entrySet().stream()
				.filter(p -> {
					for (Position pos : AllotmentFieldsData.valueOf(p.getKey()).getAllotmentPosition()) {
						if (player.getPosition().getDistance(pos) <= 56)
							return true;
					}
					return false;
				})
				.forEach(p -> {
					final AllotmentFieldsData key = AllotmentFieldsData.valueOf(p.getKey());
					final FarmingPatchType type = key.getPatchType();
					final FarmingPatchState patch = player.getPatches().get(key.name()).getPatchState();
					final BitConfigBuilder config = configMap.getOrDefault(key.getConfigId(), new BitConfigBuilder(key.getConfigId()));

					if (patch.getPlanted() != null) {
						config.set(patch.getGrowth(), key.getPatchBitOffset());
					} else {
						config.set(patch.getWeedLevel(), key.getPatchBitOffset());
					}

					if (patch.isWatered()) {
						if (type == FarmingPatchType.ALLOTMENT_PATCH || type == FarmingPatchType.FLOWER_PATCH) {
							config.set(1 << type.getStateBitOffset(), key.getPatchBitOffset());
						}
					} else if (patch.isDiseased()) {
						if (type == FarmingPatchType.ALLOTMENT_PATCH || type == FarmingPatchType.FLOWER_PATCH) {
							config.set(2 << type.getStateBitOffset(), key.getPatchBitOffset());
						} else if (type == FarmingPatchType.HERB_PATCH) {
							config.set(1 << type.getStateBitOffset(), key.getPatchBitOffset());
						}
					} else if (patch.isDead()) {
						if (type == FarmingPatchType.ALLOTMENT_PATCH || type == FarmingPatchType.FLOWER_PATCH) {
							config.set(3 << type.getStateBitOffset(), key.getPatchBitOffset());
						} else if (type == FarmingPatchType.HERB_PATCH) {
							config.set(0xAB, key.getPatchBitOffset());
						}
					}

					configMap.put(key.getConfigId(), config);
				});

		configMap.entrySet().stream().forEach(e -> {
			final BitConfig config = e.getValue().build();
//			System.out.println("[BINARY] Config " + config.getId() + ": " + Integer.toBinaryString(config.getValue()));
//			System.out.println("[NORMAL] Config " + config.getId() + ": " + config.getValue());
			player.getPacketSender().sendConfigByte(config.getId(), config.getValue());
		});
		
	}

	public void doCalculations(AllotmentFieldsData patch) {
		FarmingPatchState patchState = patch.getPatchState();
		final DateTime now = DateTime.now(DateTimeZone.UTC);
		
		// Check if whatever is planted can grow
		if (patchState.getPlanted() != null) {
			if (!isPatchFullyGrown(patchState)) {
				// Check if the current stage is ready to grow
				if (now.isAfter(patchState.getLastGrowthTime().plus(patchState.getPlanted().getGrowthTime()))) {
					patchState.setLastGrowthTime(now);
					patchState.setWatered(false);

					// Choose whether to disease, kill, or grow the crop
					if (!patchState.isDead() && !patchState.isDiseased() && randomlyDisease(patch) && patch.getPatchType() != FarmingPatchType.HERB_PATCH) {
						patchState.setDiseased(true);
					} else if (patchState.isDiseased()) {
						patchState.setDiseased(false);
						patchState.setDead(true);
					} else if (!patchState.isDead()) {
						patchState.setGrowth(patchState.getGrowth() + 1);

						if (isPatchFullyGrown(patchState)) {
							// The plantable has finished growing.
							patchState.setYield(Misc.random(patchState.getPlanted().getMinYield() + 5, patchState.getPlanted().getMaxYield() + 5) + patchState.getTreatment().getYieldIncrease());
						}
					}
				}
			} else {
				// The plantable is fully grown..
			}
		} else {
			// Check if weed should grow back
			if (patchState.getWeedLevel() > 0 && (patchState.getLastGrowthTime() == null || now.isAfter(patchState.getLastGrowthTime().plus(Duration.standardMinutes(2)))) && patchState.getTreatment() == PatchTreatment.NOT_TREATED) {
				patchState.setLastGrowthTime(now);
				patchState.setWeedLevel(patchState.getWeedLevel() - 1);
			}
		}
	}
	
	private boolean randomlyDisease(AllotmentFieldsData patch) {
		if (!patch.getPatchType().isVulnerableToDisease()) {
			return false;
		}

		int modifier = 0;
		modifier += (patch.getPatchState().getTreatment().getYieldIncrease() * 3);
		modifier += (patch.getPatchState().isWatered() ? 3 : 0);

		return Misc.random(10 + modifier) == 1;
	}

	public boolean waterPatch(int objectX, int objectY, int itemId) {
	    
		final AllotmentFieldsData patch = AllotmentFieldsData.forIdPosition(objectX, objectY);
		if (patch == null) {
			return false;
		}
		Plantables planted = patch.getPatchState().getPlanted();
		if (planted == null) {
			return false;
		}
		if (patch.getPatchState().isWatered() || patch.getPatchState().getGrowth() == patch.getPatchState().getPlanted().getMaxGrowth()) {
			player.getPacketSender().sendMessage("This patch doesn't need watering.");
			return true;
		}
		player.getInventory().delete(itemId, 1);
		player.getInventory().add(itemId == 5333 ? itemId - 2 : itemId - 1, 1);

		player.getPacketSender().sendMessage("You water the patch.");
		player.performAnimation(new Animation(FarmingConstants.WATERING_CAN_ANIM));
		player.setCurrentTask(new Task(5, player, false) {
			@Override
			public void execute() {
				player.performAnimation(new Animation(FarmingConstants.WATERING_CAN_ANIM));
				patch.getPatchState().setWatered(true);
				stop();
			}

			@Override
			public void stop() {
				updateAllotmentsStates();
				player.performAnimation(new Animation(65535));
                player.getMovementQueue().reset();
				player.getCurrentTask().setEventRunning(false);
			}
		});
		TaskManager.submit(player.getCurrentTask());
		player.getCurrentTask().setEventRunning(true);
		return true;
	}

	public boolean clearPatch(int objectX, int objectY) {
		final AllotmentFieldsData patch = AllotmentFieldsData.forIdPosition(objectX, objectY);
		return clearPatch(patch, objectX, objectY);
	}
	public boolean clearPatch(AllotmentFieldsData patch, int x, int y) {
        if (player.busy()) {
            return false;
        }
		int finalAnimation;
		int finalDelay;
		if (player.getPatches().get(patch.name()) == null || (!player.getInventory().contains(FarmingConstants.RAKE) && !player.getInventory().contains(FarmingConstants.SPADE))) {
			return false;
		}
		if (player.getPatches().get(patch.name()).getPatchState().getWeedLevel() == 3) {
			return false;
		}
		if (player.getPatches().get(patch.name()).getPatchState().getWeedLevel() < 3) {
			if (!player.getInventory().contains(FarmingConstants.RAKE)) {
				player.getPacketSender().sendMessage( "You need a rake to clear this patch.");
				return false;
			} else {
				finalAnimation = FarmingConstants.RAKING_ANIM;
				finalDelay = 5;
			}
		} else {
			if (!player.getInventory().contains(FarmingConstants.SPADE)) {
				player.getPacketSender().sendMessage( "You need a spade to clear this patch.");
				return false;
			} else {
				finalAnimation = FarmingConstants.SPADE_ANIM;
				finalDelay = 3;
			}
		}
		final int animation = finalAnimation;
		player.performAnimation(new Animation(animation));
		player.setCurrentTask(new Task(finalDelay, player, false) {
			@Override
			public void execute() {
				player.performAnimation(new Animation(animation));
				if (player.getPatches().get(patch.name()).getPatchState().getWeedLevel() <= 2) {
					player.getPatches().get(patch.name()).getPatchState().setWeedLevel(player.getPatches().get(patch.name()).getPatchState().getWeedLevel()+1);
					player.getInventory().add(6055, 1);
					player.getSkillManager().addExperience(Skill.FARMING, 10);
				} else if (animation != FarmingConstants.SPADE_ANIM) {
					player.getPatches().get(player.getPatches().get(patch.name()).name()).getPatchState().setWeedLevel(3);
					stop();
				} else {
					player.getPacketSender().sendMessage("You start digging the farming patch...");
					
					
					player.getFarming().getAllotment().clearPatch(x, y);
					stop();
				}
				if (player.getPatches().get(patch.name()).getPatchState().getWeedLevel() == 3 && animation != FarmingConstants.SPADE_ANIM) {
					stop();
					return;
				}
				updateAllotmentsStates();
			}

			@Override
			public void stop() {
				updateAllotmentsStates();
				resetAllotment(player.getPatches().get(patch.name()).getPatchState());
				player.performAnimation(new Animation(65535));
                player.getMovementQueue().reset();
				this.setEventRunning(false);
			}
		});
		
		TaskManager.submit(player.getCurrentTask());
		player.getCurrentTask().setEventRunning(true);

		return true;
	}

	/* planting the seeds */

	public void plant(AllotmentFieldsData patch, Plantables plantable) {
		player.performAnimation(new Animation(FarmingConstants.SEED_DIBBING));
		player.setCurrentTask(new Task(3, player, false) {
			@Override
			public void execute() {
				final Item seed = new Item(plantable.getSeedItemId(), 1);
				player.getSkillManager().addExperience(Skill.FARMING, plantable.getExperience() * 2);
				
				player.performAnimation(new Animation(FarmingConstants.SEED_DIBBING));
				player.getInventory().delete(seed);
				player.getPacketSender().sendMessage("You plant a " + seed.getDefinition().getName() + " in the " + patch.getPatchType().toString());
				player.getPatches().get(patch.name()).getPatchState().setLastGrowthTime(DateTime.now(DateTimeZone.UTC));
				player.getPatches().get(patch.name()).getPatchState().setGrowth(plantable.getMinGrowth());
				player.getPatches().get(patch.name()).getPatchState().setPlanted(plantable);
				
				stop();
			}

			@Override
			public void stop() {
				updateAllotmentsStates();
				player.performAnimation(new Animation(65535));
                player.getMovementQueue().reset();
				player.getCurrentTask().setEventRunning(false);
			}
		});
		TaskManager.submit(player.getCurrentTask());
		player.getCurrentTask().setEventRunning(true);
	}	
	public boolean plantSeed(int objectX, int objectY, final int seedId) {
		final AllotmentFieldsData patch = AllotmentFieldsData.forIdPosition(objectX, objectY);
		FarmingPatchState patchState = player.getPatches().get(patch.name()).getPatchState();
		if (patchState.getWeedLevel() == 3 && patchState.getPlanted() == null) {
			final Plantables plantable = Plantables.forSeedItemId(seedId);

			// Check if the plantable can be planted in this patch type
			if (plantable != null && player.getPatches().get(patch.name()).getPatchType() == plantable.getType()) {
				if (player.getSkillManager().getCurrentLevel(Skill.FARMING) < plantable.getRequiredLevel()) {
					player.getPacketSender().sendMessage("You need a Farming level of " + plantable.getRequiredLevel() + " to plant that.");
					return false;
				} else if (player.getInventory().contains(FarmingConstants.SEED_DIBBER)) {
					plant(player.getPatches().get(patch.name()), plantable);
					return true;
				} else {
					player.getPacketSender().sendMessage("You need a Seed dibber to plant seeds.");
				}
			}
		}
		return false;
	}

	/* harvesting the plant resulted */

	public boolean harvest(int objectX, int objectY) {
	
		final AllotmentFieldsData patch = AllotmentFieldsData.forIdPosition(objectX, objectY);
		if (patch == null) {
			return false;
		}
		if (patch.getPatchState().getYield() <= 0) {
			clearPatch(patch, objectX, objectY);
			player.performAnimation(new Animation(-1));
			return false;
		}

		if (patch.getPatchType() == FarmingPatchType.ALLOTMENT_PATCH) {
			if (!player.getInventory().contains(FarmingConstants.SPADE)) {
				player.getPacketSender().sendMessage("You need a spade to do that.");
				return false;
			}
		}
		player.performAnimation(patch.getPatchType().getYieldAnimation());

		player.getPacketSender().sendMessage("You begin to harvest the " + patch.getPatchType().toString() + ".");
		player.setCurrentTask(new Task(2, player, false) {
			@Override
			public void execute() {
				player.performAnimation(patch.getPatchType().getYieldAnimation());
				if (player.getInventory().getFreeSlots() <= 0) {
					stop();
					return;
				}
				if (patch.getPatchState().getYield() == 0) {
					patch.getPatchState().setYield((int) (1 + (patch.getPatchState().getPlanted().getMinYield() + Misc.random((patch.getPatchState().getPlanted().getMaxYield() + (player.getEquipment().contains(7409) ? 15 : 0)) - patch.getPatchState().getPlanted().getMinYield())) * (1)));
				}
				if (patch.getPatchState().getYield() == 1) {
					resetAllotment(patch.getPatchState());
					patch.getPatchState().setWeedLevel(3);
					stop();
					return;
				}
				patch.getPatchState().setYield(patch.getPatchState().getYield() - 1);
				player.getPacketSender().sendMessage("You harvest the crop, and get some vegetables.");
				player.getInventory().add(patch.getPatchState().getPlanted().getReward(), 1);
				player.getSkillManager().addExperience(Skill.FARMING, (patch.getPatchState().getPlanted() != null ? patch.getPatchState().getPlanted().getExperience() * 1 : 0));
			}

			@Override
			public void stop() {
				updateAllotmentsStates();
				player.performAnimation(new Animation(65535));
                player.getMovementQueue().reset();
				player.getCurrentTask().setEventRunning(false);
			}
		});
		TaskManager.submit(player.getCurrentTask());
		player.getCurrentTask().setEventRunning(true);
		return true;
	}

	/* putting compost onto the plant */

	private void treat(AllotmentFieldsData patch, PatchTreatment treatment) {
		player.performAnimation(new Animation(FarmingConstants.PUTTING_COMPOST));
		player.setCurrentTask(new Task(7, player, false) {
			@Override
			public void execute() {
				player.performAnimation(new Animation(FarmingConstants.PUTTING_COMPOST));
				player.getInventory().delete(new Item(treatment.getItemId(), 1));
				player.getSkillManager().addExperience(Skill.FARMING, 26 * 2);
				patch.getPatchState().setTreatment(treatment);
				player.getPacketSender().sendMessage("You treat the " + patch.getPatchType().toString() + " with " + treatment.name().toLowerCase() + ".");
				
				stop();
			}

			@Override
			public void stop() {
				updateAllotmentsStates();
				player.performAnimation(new Animation(65535));
                player.getMovementQueue().reset();
				player.getCurrentTask().setEventRunning(false);
				
			}
		});
		TaskManager.submit(player.getCurrentTask());
		player.getCurrentTask().setEventRunning(true);
	}
	public boolean treatPatch(int objectX, int objectY, final int itemId) {
		if (itemId != 6032 && itemId != 6034) {
			return false;
		}
		final AllotmentFieldsData patch = AllotmentFieldsData.forIdPosition(objectX, objectY);
		if (patch == null) {
			return false;
		}
		FarmingPatchState patchState = patch.getPatchState();
		
		if (patchState.getWeedLevel() == 3 && patchState.getTreatment() == PatchTreatment.NOT_TREATED) {
			final PatchTreatment treatment = PatchTreatment.forItemId(itemId);

			if (treatment != null) {
				treat(patch, treatment);
				return true;
			}
		}
		return false;
	}

	/* inspecting a plant */


    public void inspectSpell() {
        for (AllotmentFieldsData patch : AllotmentFieldsData.values()) {
            if (patch == null) {
                continue;
            }
            final StringBuilder builder = new StringBuilder();
            FarmingPatchState patchState = patch.getPatchState();

            builder.append("[" + Misc.capitalizeWords(patch.name().toLowerCase().replaceAll("_", " ")) + "]");

            if (patchState.getTreatment() == PatchTreatment.NOT_TREATED) {
                builder.append(" The soil has not been treated.");
            } else if (patchState.getTreatment() == PatchTreatment.COMPOST){
                builder.append(" The soil has been treated with ").append(patchState.getTreatment().name().toLowerCase()).append(".");
            }

            if (patchState.getWeedLevel() < 3) {
               player.sendMessage("[" + Misc.capitalizeWords(patch.name().toLowerCase().replaceAll("_", " ")) + "] The soil has not been treated. The patch needs weeding.");
            } else if (patchState.getWeedLevel() == 3) {
                player.sendMessage("[" + Misc.capitalizeWords(patch.name().toLowerCase().replaceAll("_", " ")) + "] The soil has not been treated. The patch is empty and weeded.");
            } else if (patchState.getPlanted() != null) {
                if (isPatchFullyGrown(patchState)) {
                    builder.append(" The patch is fully grown.");
                } else if (patchState.isDiseased()) {
                    builder.append(" The patch is diseased and needs attending to before it dies.");
                } else if (patchState.isDead()) {
                    builder.append(" The patch has become infected by disease and has died.");
                } else {
                    builder.append(" The patch has something growing in it.");
                }
            }
            player.sendMessage(builder.toString());
        }
    }

    public boolean inspect(int objectX, int objectY) {
        final AllotmentFieldsData patch = AllotmentFieldsData.forIdPosition(objectX, objectY);
        if (patch == null) {
            return false;
        }
		final StringBuilder builder = new StringBuilder();
        FarmingPatchState patchState = patch.getPatchState();
        
		builder.append("This is a ").append(patch.getPatchType().toString()).append(".");

		if (patchState.getTreatment() == PatchTreatment.NOT_TREATED) {
			builder.append(" The soil has not been treated.");
		} else {
			builder.append(" The soil has been treated with ").append(patchState.getTreatment().name().toLowerCase()).append(".");
		}
		
        if (patchState.getWeedLevel() < 3) {
        	DialogueHandler.sendStatement(player, "This is a," + " " + patch.getPatchType().name().toLowerCase().replaceAll("_", " ") + ", The soil has not been treated. The patch needs weeding.");
        } else if (patchState.getWeedLevel() == 3) {
        	DialogueHandler.sendStatement(player, "This is a, " + " " + patch.getPatchType().name().toLowerCase().replaceAll("_", " ") + ", The soil has not been treated. The patch is empty and weeded.");
        } else if (patchState.getPlanted() != null) {
           player.getPacketSender().sendMessage("You bend down and start to inspect the patch...");
           player.performAnimation(new Animation(1331));
           player.getPacketSender().sendEffectTimer(0 , EffectTimer.FREEZE);
			TaskManager.submit(new Task(5, player, false) {
				@Override
				public void execute() {
					if (isPatchFullyGrown(patchState)) {
						builder.append(" The patch is fully grown.");
					} else if (patchState.isDiseased()) {
						builder.append(" The patch is diseased and needs attending to before it dies.");
					} else if (patchState.isDead()) {
						builder.append(" The patch has become infected by disease and has died.");
					} else {
						builder.append(" The patch has something growing in it.");
					}
				    stop();
				}
				@Override
				public void stop() {
					player.performAnimation(new Animation(1332));
                    player.getMovementQueue().reset();
					
				}
			});
		}
        player.sendMessage(builder.toString());
		return true;
	}
	
	public boolean isPatchFullyGrown(final FarmingPatchState patch) {
		return patch.getPlanted() != null && patch.getGrowth() >= patch.getPlanted().getMaxGrowth();
	}

	/* protects the patch with the flowers */

	public void handleFlowerProtection(final FarmingPatchState patch) {
//		Plantables planted = patch.getPlanted();
//		if (patch == null) {
//			return;
//		}
	}

	/* Curing the plant */



	public boolean curePlant(int x, int y, int item) {
		final AllotmentFieldsData patch = AllotmentFieldsData.forIdPosition(x, y);
		if (player.getInventory().contains(FarmingConstants.ITEM_PLANT_CURE)) {
			return curePlant(patch);
		}
		return false;
	}
	public boolean curePlant(AllotmentFieldsData patch) {
		if (!patch.getPatchState().isDiseased()) {
			player.getPacketSender().sendMessage("This plant doesn't need to be cured.");
			return true;
		}
		player.getInventory().delete(FarmingConstants.ITEM_PLANT_CURE.getId(), 1);
		player.getInventory().add(229, 1);
		player.performAnimation(new Animation(FarmingConstants.CURING_ANIM));
		TaskManager.submit(new Task(7, player, true) {
			@Override
			public void execute() {
				player.getPacketSender().sendMessage("You cure the plant with a plant cure.");
				patch.getPatchState().setDiseased(false);
				stop();
			}

			@Override
			public void stop() {
				updateAllotmentsStates();
				player.performAnimation(new Animation(65535));
				player.getMovementQueue().reset();
			}
		});

		return true;
	}

	@SuppressWarnings("unused")
	private void resetAllotments() {
		for (AllotmentFieldsData patch : getAllotmentFieldData()) {
			resetAllotment(patch.getPatchState());
		}
	}

	/* reseting the patches */

	private void resetAllotment(FarmingPatchState patch) {
		patch.setGrowth(0);
		patch.setPlanted(null);
		patch.setTreatment(PatchTreatment.NOT_TREATED);
		patch.setWatered(false);
		patch.setDiseased(false);
		patch.setDead(false);
		patch.setImmune(false);
		updateAllotmentsStates();
	}

	public AllotmentFieldsData[] getAllotmentFieldData() {
		return AllotmentFieldsData.values();
	}
}