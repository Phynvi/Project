package com.overload.game.content.minigames.warriors_guild;

import java.util.Optional;

import com.overload.game.World;
import com.overload.game.entity.impl.grounditem.ItemOnGround;
import com.overload.game.entity.impl.grounditem.ItemOnGround.State;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.ForceMovement;
import com.overload.game.model.Item;
import com.overload.game.model.Position;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.game.task.impl.ForceMovementTask;

public class AnimatedArmour {

	private static final int SPAWN_X = 2851;
	private static final int SPAWN_Y = 3536;
	
	public static boolean animator_west = false;

	public enum Armour {

		BRONZE(2450, 1155, 1117, 1075, 5, 10, 2, 20, 20), 
		IRON(2451, 1153, 1115, 1067, 10, 20, 4, 30, 30), 
		STEEL(2452, 1157, 1119, 1069, 15, 40, 6, 50, 50), 
		MITHRIL(2454, 1159, 1121, 1071, 50, 80, 10, 100, 100), 
		ADAMANT(2455, 1161, 1123, 1073, 60, 100, 13, 120, 120), 
		RUNE(2456, 1163, 1127, 1079, 80, 120, 18, 150, 150);

		int npcId, helm, platebody, platelegs, tokens, health, max, attack, defence;

		Armour(int npcId, int helm, int platebody, int platelegs, int tokens, int health, int max, int attack, int defence) {
			this.npcId = npcId;
			this.helm = helm;
			this.platebody = platebody;
			this.platelegs = platelegs;
			this.tokens = tokens;
			this.health = health;
			this.max = max;
			this.attack = attack;
			this.defence = defence;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getHelmId() {
			return helm;
		}

		public int getPlatebodyId() {
			return platebody;
		}

		public int getPlatelegsId() {
			return platelegs;
		}

		public int getAmountOfTokens() {
			return tokens;
		}

		public int getHealth() {
			return health;
		}

		public int getMax() {
			return max;
		}

		public int getAttack() {
			return attack;
		}

		public int getDefence() {
			return defence;
		}
	}

	private static Armour getArmourForItemId(int itemId) {
		for (Armour a : Armour.values())
			if (a.getHelmId() == itemId || a.getPlatebodyId() == itemId || a.getPlatelegsId() == itemId)
				return a;
		return null;
	}

	private static Armour getArmourForNpcId(int npcId) {
		for (Armour a : Armour.values())
			if (a.getNpcId() == npcId)
				return a;
		return null;
	}

	public static boolean isAnimatedArmourNpc(int npcId) {
		for (Armour armour : Armour.values()) {
			if (armour.npcId == npcId) {
				return true;
			}
		}
		return false;
	}

	public static boolean itemOnAnimator(final Player player, int itemId) {
		int x = player.getX(), y = player.getY();
		
		if (y != 3537) {
			player.sendMessage("You need to move closer.");
			return false;
		}
		if (x == 2851) {
			animator_west = true;
		} else {
			animator_west = false;
		}
		if (player.hasAnimatedArmour()) {
			player.sendMessage("An Animated Armour npc is already spawned.");
			return false;
		}
		final Armour armour = getArmourForItemId(itemId);
		if (armour == null) {
			player.sendMessage("This is not a feasable animated armour item.");
			return false;
		}
		if (!player.getInventory().contains(armour.getPlatebodyId(), 1) || !player.getInventory().contains(armour.getPlatelegsId(), 1)
				|| !player.getInventory().contains(armour.getHelmId(), 1)) {
			player.sendMessage("You need the helm, platebody, and platelegs to spawn the animated armour.");
			return false;
		}
		player.setAnimatedArmour(true);
		player.getInventory().delete(armour.getPlatebodyId(), 1);
		player.getInventory().delete(armour.getPlatelegsId(), 1);
		player.getInventory().delete(armour.getHelmId(), 1);
		
		final Position toWalk = new Position(0, 3);
		
		TaskManager.submit(new ForceMovementTask(player, 3,
				new ForceMovement(player.getPosition().clone(), toWalk, 0, 15, 0, 0)));
		TaskManager.submit(new Task(1, player, true) {

			@Override
			public void execute() {
	            NPC npc = new NPC(armour.getNpcId(), new Position(animator_west ? SPAWN_X : 2857, SPAWN_Y, 0));
	            npc.setHitpoints(armour.getHealth());
	            npc.getDefinition().setMaxHit(armour.getMax());
	            npc.getDefinition().setAttack(armour.getAttack());
	            npc.getDefinition().setDefence(armour.getDefence());
	            npc.getDefinition().setRespawn(-1);
	            World.getNpcs().add(npc);
	            npc.setOwner(player);
	            npc.getMovementCoordinator().setRadius(-2);
	            npc.getCombat().attack(player);
	            
				player.sendMessage("An animated armour has spawned...");
				stop();
			}

			@Override
			public void stop() {
				this.setEventRunning(false);
			}
		});
		return true;
	}

	public static void dropTokens(Player player, int npcType, int x, int y) {
		Armour npc = getArmourForNpcId(npcType);
		if (npc != null) {
			Optional<String> str = Optional.ofNullable(player.getUsername()).filter(s -> !s.isEmpty());
			World.getItems().add(
				new ItemOnGround(
					State.SEEN_BY_PLAYER,
					str,
					new Position(x, y, player.getZ()),
					new Item(8851, npc.getAmountOfTokens()),
					false,
					0
				)
			);
			player.setAnimatedArmour(false);
		}
	}

}