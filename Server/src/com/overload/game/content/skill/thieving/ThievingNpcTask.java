package com.overload.game.content.skill.thieving;

import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.combat.hit.HitDamage;
import com.overload.game.content.combat.hit.HitMask;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.Misc;
import com.overload.util.timers.TimerKey;

public class ThievingNpcTask extends Task {

    public static boolean attemptThieving(Player player, NPC npc) {
        if (player.busy()) {
            return false;
        }
        ThievingNpcData data = ThievingNpcData.getNpcById(npc.getId());
        if (data == null)
            return false;
        if (player.getTimers().has(TimerKey.STUN)) {
            return false;
        }

        if (!meetsRequirements(player, data)) {
            return false;
        }
        
        if (player.getInventory().getFreeSlots() == 0) {
            player.sendMessage("You do not have any inventory space.");
            return false;
        }
        
        player.sendMessage("You attempt to pick the " + npc.getDefinition().getName() + "'s pocket.");
        player.performAnimation(new Animation(881));
        TaskManager.submit(new ThievingNpcTask(3, player, data, npc));
        return true;
    }

	private static void failedAttempt(Player player, NPC npc) {
        npc.forceChat("What are you doing in me pockets?");
        npc.setPositionToFace(player.getPosition());
        npc.performAnimation(new Animation(422));
        player.sendMessage("You fail to pick the " + npc.getDefinition().getName() + "'s pocket.");
        player.performGraphic(new Graphic(80, GraphicHeight.HIGH));
        player.getCombat().getHitQueue().addPendingDamage(new HitDamage(5, HitMask.RED));
        player.performAnimation(new Animation(401));
        CombatFactory.stun(player, 3, true);
        player.getCombat().reset();
    }

	private static boolean meetsRequirements(Player player, ThievingNpcData data) {
        if(!player.getClickDelay().elapsed(1500))
            return false;
		if (player.getSkillManager().getCurrentLevel(Skill.THIEVING) < data.getLevelRequired()) {
			player.sendMessage("You need a Thieving level of " + data.getLevelRequired() + " to pickpocket this npc.");
			return false;
		}

		return true;
	}

	private static void successfulAttempt(Player player, ThievingNpcData data, NPC npc) {
        player.getSkillManager().addExperience(Skill.THIEVING, data.getExperience());

        Item stolen;

        if (data == ThievingNpcData.MASTER_FARMER || data == ThievingNpcData.MASTER_FARMER2) {
            int roll = Misc.randomNumber(SEEDS[0].length);

            if (SEEDS[0][roll] == -1)
                stolen = new Item(SEEDS[1][Misc.randomNumber(SEEDS[1].length)]);
            else
                stolen = new Item(SEEDS[0][roll]);
        } else {
            int roll = Misc.randomNumber(data.getItems().length);
            stolen = new Item(data.getItems()[roll][0], data.getItems()[roll][1]);
        }

        player.getInventory().add(stolen);

        player.sendMessage("You successfully pick the " + npc.getDefinition().getName() + "'s pocket.");
    }

	private static boolean successfulAttemptChance(Player player, ThievingNpcData data) {
		return Skill.isSuccess(player, Skill.THIEVING, (int) (data.levelRequired * 0.7));
	}

	private Player player;

	private ThievingNpcData data;

	private NPC npc;

	public static final int[][] SEEDS = { { 5291, 5292, 5293, -1, 5294, 5318, 5319, 5324, 5322, 5320, 5096, 5097, 5098, -1, 5318, 5318, 5318, 5318, -1, 5318, 5319, 5319, 5319, 5319, -1, 5324, 5324, 5324 }, { 5295, 5296, 5297, 5298, 14870, 5299, 5300, 5301, 5302, 5303, 5304, 5323, 5321, 5099, 5100, 14589 } };

	private ThievingNpcTask(int delay, Player player, ThievingNpcData data, NPC npc) {
		super(delay, player, false);
		this.player = player;
		this.data = data;
		this.npc = npc;
	}

	@Override
	public void execute() {
		if (successfulAttemptChance(player, data))
			successfulAttempt(player, data, npc);
		else {
			failedAttempt(player, npc);
		}
		stop();
	}

	@Override
	public void stop() {
	    setEventRunning(false);
	}
}
