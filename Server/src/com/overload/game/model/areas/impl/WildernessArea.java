package com.overload.game.model.areas.impl;

import com.overload.game.content.Obelisks;
import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.combat.bountyhunter.BountyHunter;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Boundary;
import com.overload.game.model.areas.Area;
import com.overload.game.model.rights.Right;

import java.util.Arrays;
import java.util.Optional;

public class WildernessArea extends Area {

	public static int getLevel(int y) {
		return ((((y > 6400 ? y - 6400 : y) - 3520) / 8) + 1);
	}

	public static boolean multi(int x, int y) {
		if (x >= 3155 && y >= 3798 || x >= 3020 && x <= 3055 && y >= 3684 && y <= 3711
				|| x >= 3150 && x <= 3195 && y >= 2958 && y <= 3003 || x >= 3645 && x <= 3715 && y >= 3454 && y <= 3550
				|| x >= 3150 && x <= 3199 && y >= 3796 && y <= 3869
				|| x >= 2994 && x <= 3041 && y >= 3733 && y <= 3790) {
			return true;
		}
		return false;
	}

	public WildernessArea() {
		super(Arrays.asList(new Boundary(2940, 3392, 3525, 3968), new Boundary(2986, 3012, 10338, 10366),
				new Boundary(3653, 3720, 3441, 3538), new Boundary(3650, 3653, 3457, 3472),
				new Boundary(3150, 3199, 3796, 3869), new Boundary(2994, 3041, 3733, 3790)));
	}

	@Override
	public void enter(Character character) {
		if (character.isPlayer()) {
			Player player = character.getAsPlayer();
			player.getPacketSender().sendInteractionOption("Attack", 2, true);
			player.getPacketSender().sendWalkableInterface(197);
			BountyHunter.updateInterface(player);
			if (!BountyHunter.PLAYERS_IN_WILD.contains(player)) {
				BountyHunter.PLAYERS_IN_WILD.add(player);
			}
		}
	}

	@Override
	public void leave(Character character) {
		if (character.isPlayer()) {
			Player player = character.getAsPlayer();
			player.getPacketSender().sendWalkableInterface(-1);
			player.getPacketSender().sendInteractionOption("null", 2, true);
			player.getPacketSender().sendWalkableInterface(-1);
			player.setWildernessLevel(0);
			BountyHunter.PLAYERS_IN_WILD.remove(player);
		}
	}

	@Override
	public void process(Character character) {
		if (character.isPlayer()) {
			Player player = character.getAsPlayer();
			player.setWildernessLevel(getLevel(player.getPosition().getY()));
			player.getPacketSender().sendString(199, "Level: " + player.getWildernessLevel());
		}
	}

	@Override
	public boolean canTeleport(Player player) {
		if (player.getWildernessLevel() > 20 && !player.getRights().isOrInherits(Right.GAME_DEVELOPER)) {
			player.getPacketSender().sendMessage("Teleport spells are blocked in this level of Wilderness.");
			player.getPacketSender()
					.sendMessage("You must be below level 20 of Wilderness to use teleportation spells.");
			return false;
		}
		return true;
	}

	@Override
	public boolean canAttack(Character attacker, Character target) {
		if (attacker.isPlayer() && target.isPlayer()) {

			Player a = attacker.getAsPlayer();
			Player t = target.getAsPlayer();

			int combatDifference = CombatFactory.combatLevelDifference(a.getSkillManager().getCombatLevel(),
					t.getSkillManager().getCombatLevel());
			if (combatDifference > a.getWildernessLevel() + 5 || combatDifference > t.getWildernessLevel() + 5) {
				a.getPacketSender().sendMessage("Your combat level difference is too great to attack that player here.")
						.sendMessage("Move deeper into the wilderness first.");
				a.getMovementQueue().reset();
				return false;
			}
			if (!(t.getArea() instanceof WildernessArea)) {
				a.getPacketSender()
						.sendMessage("That player cannot be attacked, because they are not in the Wilderness.");
				a.getMovementQueue().reset();
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canTrade(Player player, Player target) {
		return true;
	}

	@Override
	public boolean isMulti(Character character) {
		int x = character.getPosition().getX();
		int y = character.getPosition().getY();
		return multi(x, y);
	}

	@Override
	public boolean canEat(Player player, int itemId) {
		return true;
	}

	@Override
	public boolean canDrink(Player player, int itemId) {
		return true;
	}

	@Override
	public boolean dropItemsOnDeath(Player player, Optional<Player> killer) {
		return true;
	}

	@Override
	public boolean handleDeath(Player player, Optional<Player> killer) {
		return false;
	}

	@Override
	public void onPlayerRightClick(Player player, Player rightClicked, int option) {
	}

	@Override
	public void defeated(Player player, Character character) {
		if (character.isPlayer()) {
			BountyHunter.onDeath(player, character.getAsPlayer());
		}
	}

	@Override
	public boolean handleObjectClick(Player player, int objectId, int type) {
		if (Obelisks.activate(objectId)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isWilderness() {
		return true;
	}
}
