package com.overload.game.content.skill.thieving;

import com.overload.game.content.combat.hit.HitDamage;
import com.overload.game.content.combat.hit.HitMask;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Item;
import com.overload.game.model.Skill;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.Misc;

public class WallSafes extends Task {

	private Player player;

    private WallSafes(int delay, Player player) {
		super(delay, player, true);
		this.player = player;
	}

    private static Item ITEMS[] = { new Item(1617), new Item(1619), new Item(1621), new Item(1623), new Item(1623), new Item(995, 20), new Item(995, 40) };

    private static Item RANDOM() {
		return ITEMS[(int) (Math.random() * ITEMS.length)];
	}

	public static int timer(Player player) {
		if (player.getInventory().contains(new Item(5560))) {
			return (10 - (int) Math.floor(player.getSkillManager().getCurrentLevel(Skill.THIEVING) / 10) + Misc.random(5));
		} else {
			return (10 - (int) Math.floor(player.getSkillManager().getCurrentLevel(Skill.THIEVING) / 10) + Misc.random(11)) + 20;
		}
	}

	public static int chance(Player player) {
		return (Misc.random((int) Math.floor(player.getSkillManager().getCurrentLevel(Skill.THIEVING) / 10) + 1));
	}

	public static boolean can(Player player) {
		if (player.isCracking) {
			player.sendMessage("You are currently cracking a safe!");
			return false;
		}
		if (player.getSkillManager().getCurrentLevel(Skill.THIEVING) < 50) {
            player.sendMessage("You need a thieving level atleast 50 to crack safes!");
			return false;
		}
		if (player.getInventory().getFreeSlots() < 1) {
            player.sendMessage("You do not have any space left in your inventory.");
			return false;
		}
		return true;
	}

	public static void crack(Player player) {
		if (!can(player) || player.getClickDelay().elapsed() < 1000) {
			return;
		}
		player.isCracking = true;
		player.sendMessage("You attempt to crack the safe... ");
		player.performAnimation(new Animation(881));
		player.getMovementQueue().reset();
		TaskManager.submit(new WallSafes(timer(player), player));

	}

	@Override
	public void execute() {
		if (chance(player) == 0) {
			player.sendMessage("You slip and trigger a trap!");
			if (player.getSkillManager().getCurrentLevel(Skill.THIEVING) == 99) {
				player.getCombat().getHitQueue().addPendingDamage(new HitDamage(Misc.getRandom(3), HitMask.RED));
			} else if (player.getSkillManager().getCurrentLevel(Skill.THIEVING) > 79) {
                player.getCombat().getHitQueue().addPendingDamage(new HitDamage(Misc.getRandom(3), HitMask.RED));
			} else if (player.getSkillManager().getCurrentLevel(Skill.THIEVING) > 49) {
                player.getCombat().getHitQueue().addPendingDamage(new HitDamage(Misc.getRandom(4), HitMask.RED));
			} else {
                player.getCombat().getHitQueue().addPendingDamage(new HitDamage(Misc.getRandom(5), HitMask.RED));
			}
			this.stop();
			player.performAnimation(new Animation(404));
			player.isCracking = false;
			return;
		}
        player.sendMessage("You get some loot.");
		player.getInventory().add(RANDOM());
		player.getSkillManager().addExperience(Skill.THIEVING, 100);
		player.isCracking = false;
		player.getClickDelay().reset();
		this.stop();
	}

	@Override
	public void stop() {
	}

}
