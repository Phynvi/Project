package com.overload.game.content.skill.thieving;

import com.overload.game.World;
import com.overload.game.content.combat.hit.HitDamage;
import com.overload.game.content.combat.hit.HitMask;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Item;
import com.overload.game.model.Position;
import com.overload.game.model.Skill;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.Misc;

public class HomeStalls extends Task {

	private Player player;

	private stallData data;

	private HomeStalls(int delay, Player player, stallData data) {
		super(delay, player, true);
		this.player = player;
		this.data = data;
	}

	@Override
	public void execute() {
		if (!meetsRequirements(player, data)) {
			stop();
			return;
		}
		successfull(player, data);
		stop();
	}

	@Override
	public void stop() {
	    setEventRunning(false);
	}

	private enum stallData {
		FOOD("food", 4875, 3162, 1500, 1, 30),
		GENERAL("general", 4876, 1887, 2500, 25, 50),
		CRAFT("crafting", 4874, 1635, 5500, 50, 70),
		MAGIC("magic", 4877, 13680, 1, 75, 90),
		SCIMITAR("scimitar", 4878, 6721, 12000, 85, 125);

		private String name;
		private int objectId;
		private int itemId;
		private int levelReq;
		private int xpGained;

		 stallData(String name, int objectId, int itemId, int itemAmount, int levelReq, int xpGained) {
			this.name = name;
			this.objectId = objectId;
			this.itemId = itemId;
			this.levelReq = levelReq;
			this.xpGained = xpGained;
		}

		public static stallData getObjectById(int id) {
			for (stallData data : stallData.values())
				if (data.objectId == id)
					return data;
			return null;
		}
	}

	public static void attempt(Player player, int id, Position position) {
        stallData data = stallData.getObjectById(id);

        if (data == null) {
            return;
        }

        if (player.busy()) {
            return;
        }
        if (!meetsRequirements(player, data)) {
            return;
        }
        // Make sure we aren't spam clicking..
        if (player.getClickDelay().elapsed(1500)) {
            player.performAnimation(new Animation(832));
            TaskManager.submit(new HomeStalls(4, player, data));
            player.getClickDelay().reset();
        }
    }

    private static boolean meetsRequirements(Player player, stallData stall) {
		if (stall == null) {
			return false;
		}
		if (player.getInventory().getFreeSlots() == 0) {
			player.sendMessage("You don't have enough inventory spaces left to hold this.");
			return false;
		}
		if (player.getSkillManager().getCurrentLevel(Skill.THIEVING) < stall.levelReq) {
			player.sendMessage("You need a thieving level of " + stall.levelReq + " to do this!");
			return false;
		}
		return true;
	}

	private static void successfull(Player player, stallData stall) {
        if (player.getClickDelay().elapsed(1500)) {
            player.performAnimation(new Animation(832));
            player.getInventory().add(new Item(stall.itemId, 1));
            player.getSkillManager().addExperience(Skill.THIEVING, stall.xpGained);
            player.sendMessage("You steal some items from the @or3@" + stall.name + " stall.");
            player.getClickDelay().elapsed(1000);
            handleRandom(player);
            handleRaccoon(player);
            player.getClickDelay().reset();
        }
    }

private static void handleRandom(Player player) {
	int random = Misc.randomNumber(100);
	if (random == 1) {
		player.performAnimation(new Animation(4367));
		player.sendMessage("Some mystical force drops you from the sky causing damage.");
        player.getCombat().getHitQueue().addPendingDamage(new HitDamage(Misc.getRandom(3), HitMask.RED));
		player.forceChat(Misc.randomElement(FORCED_CHAT));
	}
}

private final static String FORCED_CHAT[] = { "Ow!", "Ouch!", "What the-?", "Noooo!", "I want to live!", "Somebody help me!", };

private static void handleRaccoon(Player player) {
	int random = Misc.randomNumber(3000);
	if (random == 1) {
			player.getInventory().add(new Item(20663)); {
			player.sendMessage("@blu@You receive a pet Raccoon!");
			World.sendMessage("@mbl@" + " " + player.getUsername() + " has recieved a pet Raccoon while Thieving!");
                }
            }
        }
    }