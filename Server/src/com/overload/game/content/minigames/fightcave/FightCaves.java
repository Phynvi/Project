package com.overload.game.content.minigames.fightcave;

import com.overload.game.World;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.CharacterList;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Boundary;
import com.overload.game.model.Position;
import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.Misc;

public class FightCaves {

	private Player player;
	private int killsRemaining;

	public FightCaves(Player player) {
		this.player = player;
	}

	public void spawn() {
		final int[][] type = Wave.getWaveForType(player);
		if(player.getWaveID() >= type.length && Boundary.FIGHT_CAVE.inside(player.getPosition())) {
			stop();
			return;
		}
		TaskManager.submit(new Task(10, player, false) {
			@Override
			public void execute() {
				if(player == null) {
					stop();
					return;
				}
				if(!Boundary.FIGHT_CAVE.inside(player.getPosition())) {
					player.setWaveID(0);
					stop();
					return;
				}
				if(player.getWaveID() >= type.length) {
					stop();
					return;
				}
				if(player.getWaveID() < type.length)
					player.sendMessage("You are now on wave "+(player.getWaveID() + 1)+" of "+type.length+".");
					if(player.getWaveID() == type.length-1) {
						player.sendMessage("Relog if jad does not spawn within a few seconds.");
					}
				setKillsRemaining(type[player.getWaveID()].length);
				for(int i = 0; i < getKillsRemaining(); i++) {
					int npcType = type[player.getWaveID()][i];
					int index = Misc.random(Wave.SPAWN_DATA.length - 1);
					int x = Wave.SPAWN_DATA[index][0];
					int y = Wave.SPAWN_DATA[index][1];

		            NPC npc = new NPC(npcType, new Position(x, y, player.getIndex() * 4));
		            npc.setHitpoints(Wave.getHp(npcType));
		            World.getNpcs().add(npc);
		            npc.setOwner(player);
//		            npc.getMovementCoordinator().setRadius(-2);
		            npc.getCombat().attack(player);
				}
				stop();
			}
			
			@Override
			public void stop() {
				setEventRunning(false);
			}
		});
	}

	public void handleNPCDeath(NPC n) {
		for (int x = 0; x < FIGHT_CAVE_NPCS.length; x++) {
			if (n.getId() == FIGHT_CAVE_NPCS[x]) {
				if (n.getOwner() == player) {
					if (n.getId() == Wave.TZ_KEK) {
						for (int i = 0; i < 2; i++) {
				            NPC npc = new NPC(Wave.TZ_KEK_SPAWN, new Position(n.getPosition().getX() + i, n.getPosition().getY(), player.getIndex() * 4));
				            npc.setHitpoints(Wave.getHp(Wave.TZ_KEK_SPAWN));
				            World.getNpcs().add(npc);
				            npc.setOwner(player);
				            npc.getMovementCoordinator().setRadius(-2);
				            npc.getCombat().attack(player);
						}
					}
					World.getNpcs().remove(n);

					if (n.getId() == Wave.TZ_KEK) {
						setKillsRemaining(getKillsRemaining()+1);
					} else {
						setKillsRemaining(getKillsRemaining()-1);
					}
				}
			}
		}
		if (getKillsRemaining() <= 0) {
			spawn();
			player.setWaveID(62);//player.getWaveID()+1);
		}
	}

	public void leaveGame() {
		if (System.currentTimeMillis() - player.getJoinFightCavesTime() < 15000) {
			player.sendMessage("You cannot leave yet, wait a couple of seconds and try again.");
			return;
		}
		killAllSpawns();
		player.sendMessage("You have left the Fight Cave minigame.");
		player.moveTo(new Position(2438, 5168, 0));
		player.setWaveID(0);
	}

	public void create() {
		player.getPacketSender().sendInterfaceRemoval();
		player.moveTo(new Position(2413, 5117, player.getIndex() * 4));
		player.sendMessage("Starting soon...");
		player.setWaveID(0);
		player.setJoinFightCavesTime(System.currentTimeMillis());
		spawn();
	}

	public void stop() {
		reward();
		player.moveTo(new Position(2438, 5168, 0));
		DialogueHandler.sendStatement(player, "Congratulations for finishing Fight Caves");
		player.setWaveInfo(player.getWaveInfo() + 1);
		player.setWaveID(0);
		player.setRunEnergy(100);
		killAllSpawns();
	}

	public void handleDeath() {
		player.moveTo(new Position(2438, 5168, 0));
		DialogueHandler.sendStatement(player, "Unfortunately you died on wave " + player.getWaveID() + ". Better luck next time.");
		player.setWaveID(0);
		killAllSpawns();
	}
	
	public static final int[] FIGHT_CAVE_NPCS = {
		3116, //TZ_KIH
		3118, //TZ_KEK
		3120, //TZ_KEK_SPAWN
		3121, //TOK_XIL
		3123, //YT_MEJKOT
		3125, //KET_ZEK
		3127  //TZTOK_JAD
	};

	public void killAllSpawns() {
		CharacterList<NPC> npcs = World.getNpcs();
		for (int i = 0; i < npcs.size(); i++) {
			if (npcs.get(i) != null) {
				for (int x = 0; x < FIGHT_CAVE_NPCS.length; x++) {
					if (npcs.get(i).getId() == FIGHT_CAVE_NPCS[x]) {
						if (npcs.get(i).getOwner() == player) {
							World.getNpcs().remove(npcs.get(i));
						}
					}
				}
			}
		}
	}
	
	public void gamble() {
		if (!player.getInventory().contains(FIRE_CAPE)) {
			player.sendMessage("You do not have a firecape.");
			return;
		}
		player.getInventory().delete(FIRE_CAPE, 1);
		
		if (Misc.random(100) == 18) {
			if (!player.getInventory().contains(13225) && !player.getBank(0).contains(13225) && player.getCurrentPet().getId() != 13225) {
				World.sendMessage("[@red@PET@bla@] @cr20@<col=255> " + player.getUsername() + "</col> received a pet from <col=255>TzTok-Jad</col>.");
				if (player.getInventory().isFull()) {
					player.getBank(0).add(13225, 1);
					DialogueHandler.npcChat(player, 2180, new String[] {
							"Congratulations! You have won the Jad Pet!",
							"It has been sent to your bank as your inventory is full."
					});
				} else {
					player.getInventory().add(13225, 1);
					DialogueHandler.npcChat(player, 2180, new String[] {
							"Congratulations! You have won the Jad Pet!",
							"It has been sent to you Inventory."
					});
				}
			}
		} else {
			DialogueHandler.npcChat(player, 2180,  new String[] {
					"Unfortunately you haven't won the Jad pet this time.",
					"Better luck next time!",
			});
		}
	}

	private static final int[] REWARD_ITEMS = { 6571, 6528, 11128, 6523, 6524, 6525, 6526, 6527, 6568, 6523, 6524, 6525, 6526, 6527, 6568 };

	public static final int FIRE_CAPE = 6570;

	public static final int TOKKUL = 6529;

	public void reward() {
		int item = REWARD_ITEMS[Misc.random(REWARD_ITEMS.length - 1)];
		if (player.getInventory().isFull()) {
			player.getBank(0).add(FIRE_CAPE, 1);
			player.getBank(0).add(item, 1);
		} else {
			player.getInventory().add(FIRE_CAPE, 1);
			player.getInventory().add(item, 1);
		}
		World.sendMessage(player.getUsername() + " has completed 63 waves of jad and received " + ItemDefinition.forId(item).getName() + ".");
	}

	public int getKillsRemaining() {
		return killsRemaining;
	}

	public void setKillsRemaining(int remaining) {
		this.killsRemaining = remaining;
	}

}