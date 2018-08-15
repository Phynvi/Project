package com.overload.game.content.skill.hunter;

import com.overload.game.World;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.game.task.impl.NPCRespawnTask;
import com.overload.util.Misc;

public class PuroPuro {

	public static final int[][] implings = {
            /**
             * Baby imps
            */
            {1635, 2612, 4318},
            {1635, 2602, 4314},
            {1635, 2610, 4338},
            {1635, 2582, 4344},
            {1635, 2578, 4344},
            {1635, 2568, 4311},
            {1635, 2583, 4295},
            {1635, 2582, 4330},
            {1635, 2600, 4303},
            {1635, 2611, 4301},
            {1635, 2618, 4329},

            /**
             * Young imps
            */
            {1636, 2591, 4332},
            {1636, 2600, 4338},
            {1636, 2595, 4345},
            {1636, 2610, 4327},
            {1636, 2617, 4314},
            {1636, 2619, 4294},
            {1636, 2599, 4294},
            {1636, 2575, 4303},
            {1636, 2570, 4299},

            /**
             * Gourment imps
            */
            {1637, 2573, 4339},
            {1637, 2567, 4328},
            {1637, 2593, 4297},
            {1637, 2618, 4305},
            {1637, 2605, 4316},
            {1637, 2596, 4333},

            /**
             * Earth imps
            */
            {1638, 2592, 4338},
            {1638, 2611, 4345},
            {1638, 2617, 4339},
            {1638, 2614, 4301},
            {1638, 2606, 4295},
            {1638, 2581, 4299},

            /**
             * Essence imps
            */
            {1639, 2602, 4328},
            {1639, 2608, 4333},
            {1639, 2609, 4296},
            {1639, 2581, 4304},
            {1639, 2570, 4318},

            /**
             * Eclectic imps
            */
            {1640, 2611, 4310},
            {1640, 2617, 4319},
            {1640, 2600, 4347},
            {1640, 2570, 4326},
            {1640, 2579, 4310},

            /**
             * Spirit imps
            */

            /**
             * Nature imps
            */
            {1641, 2581, 4310},
            {1641, 2581, 4310},
            {1641, 2603, 4333},
            {1641, 2576, 4335},
            {1641, 2588, 4345},

            /**
             * Magpie imps
            */
            {1642, 2612, 4324},
            {1642, 2602, 4323},
            {1642, 2587, 4348},
            {1642, 2564, 4320},
            {1642, 2566, 4295},

            /**
             * Ninja imps
            */
            {1643, 2570, 4347},
            {1643, 2572, 4327},
            {1643, 2578, 4318},
            {1643, 2610, 4312},
            {1643, 2594, 4341},

            /**
             * Dragon imps
            */
            {1654, 2613, 4341},
            {1654, 2585, 4337},
            {1654, 2576, 4319},
            {1654, 2576, 4294},
            {1654, 2592, 4305},
    };

	public static void spawn() {


        for (int i = 0; i < implings.length; i++) {
            NPC n = new NPC(implings[i][0], new Position(implings[i][1], implings[i][2]));
            n.getMovementCoordinator().setRadius(4);
            World.getAddNPCQueue().add(n);
        }
    }

	/**
	 * Catches an Impling
	 * @param player	The player catching an Imp
	 */
	public static void catchImpling(Player player, final NPC imp) {
		ImpData implingData = ImpData.forId(imp.getId());
		if(player.getInterfaceId() > 0 || player == null || imp == null || implingData == null || !imp.isRegistered() || !player.getClickDelay().elapsed(2000))
			return;
		if(player.getSkillManager().getCurrentLevel(Skill.HUNTER) < implingData.levelReq) {
			player.getPacketSender().sendMessage("You need a Hunter level of at least "+implingData.levelReq+" to catch this impling.");
			return;
		}
		if(!player.getInventory().contains(10010) && !player.getEquipment().contains(10010)) {
			player.getPacketSender().sendMessage("You do not have any net to catch this impling with.");
			return;
		}
		if(!player.getInventory().contains(11260)) {
			player.getPacketSender().sendMessage("You do not have any empty jars to hold this impling with.");
			return;
		}
		player.performAnimation(new Animation(6605));
		boolean sucess = player.getSkillManager().getCurrentLevel(Skill.HUNTER) > 8 ? Misc.getRandom(player.getSkillManager().getCurrentLevel(Skill.HUNTER) / 2) > 1 : true;
		if(sucess) {
			if(imp.isRegistered()) {
				World.getRemoveNPCQueue().remove(imp);
				TaskManager.submit(new NPCRespawnTask(imp, imp.getDefinition().getRespawn()));
				player.getInventory().delete(11260, 1).add(implingData.impJar, 1);
				player.getPacketSender().sendMessage("You successfully catch the impling.");
				player.getSkillManager().addExperience(Skill.HUNTER, implingData.XPReward);
			}
		} else
			player.getPacketSender().sendMessage("You failed to catch the impling.");
		player.getClickDelay().reset();
	}
	/**
	 * Handles pushing through walls in Puro puro
	 * @param player	The player pushing a wall
	 */
	public static void goThroughWheat(final Player player, GameObject object) {
		if(!player.getClickDelay().elapsed(2000))
			return;
		player.getClickDelay().reset();
		int x = player.getPosition().getX(), x2 = x;
		int y = player.getPosition().getY(), y2 = y;
        if(x == 2584) { x2 = 2582; } else if(x == 2582) {
            x2 = 2584; } else 	if(x == 2599) {
            x2 = 2601; } else if(x == 2601) {
            x2 = 2599; } else if (x == 2581) {
            x2 = 2579; } else if (x == 2579) {
            x2 = 2581; } else if (x == 2602) {
            x2 = 2604; } else if (x == 2604) {
            x2 = 2602; } else if (x == 2578) {
            x2 = 2576; } else if (x == 2576) {
            x2 = 2578; } else if (x == 2605) {
            x2 = 2607; } else if (x == 2607) {
            x2 = 2605; } else if (x == 2575) {
            x2 = 2573; } else if (x == 2573) {
            x2 = 2575; } else if (x == 2608) {
            x2 = 2610; } else if (x == 2610) {
            x2 = 2608; } else if (x == 2572) {
            x2 = 2570; } else if (x == 2570) {
            x2 = 2572; } else if (x == 2611) {
            x2 = 2613; } else if (x == 2613) {
            x2 = 2567; } else if (x == 2569) {
            x2 = 2579; } else if (x == 2567) {
            x2 = 2569; } else if (x == 2614) {
            x2 = 2616; } else if (x == 2616) {
            x2 = 2614; } else if (x == 2566) {
            x2 = 2564; } else if (x == 2564) {
            x2 = 2566; } else if (x == 2617) {
            x2 = 2619; } else if (x == 2619) {
            x2 = 2617; }
        if(y== 4312) {
            y2 = 4310;
        } else if(y == 4310) {
            y2 = 4312; }else if(y == 4327) {
            y2 = 4329; } else if(y == 4329) {
            y2 = 4327; } else if(y == 4309) {
            y2 = 4307; } else if(y == 4307) {
            y2 = 4309; } else if(y == 4330) {
            y2 = 4327; } else if(y == 4329) {
            y2 = 4332; } else if(y == 4332) {
            y2 = 4327; } else if(y == 4306) {
            y2 = 4304; } else if(y == 4304) {
            y2 = 4306; } else if(y == 4333) {
            y2 = 4335; } else if(y == 4335) {
            y2 = 4333; } else if(y == 4303) {
            y2 = 4301; } else if(y == 4301) {
            y2 = 4303; } else if(y == 4336) {
            y2 = 4338; } else if(y == 4338) {
            y2 = 4336; } else if(y == 4300) {
            y2 = 4298; } else if(y == 4298) {
            y2 = 4300; } else if(y == 4339) {
            y2 = 4341; } else if(y == 4341) {
            y2 = 4339; } else if(y == 4297) {
            y2 = 4295; } else if(y == 4295) {
            y2 = 4297; } else if(y == 4345) {
            y2 = 4347; } else if(y == 4347) {
            y2 = 4345; }
		x2 -= x;
		y2 -= y;
		player.getPacketSender().sendMessage("You use your strength to push through the wheat.");
		final int goX = x2, goY = y2;
		TaskManager.submit(new Task(1, player, false) {
			int tick = 0;
			@Override
			protected void execute() {
				if(tick == 1) {
					player.setSkillAnimation(6594).setCrossingObstacle(true);
					player.getUpdateFlag().flag(Flag.APPEARANCE).isUpdateRequired();
					player.getMovementQueue().walkStep(goX, goY);
					player.getSkillManager().addExperience(Skill.STRENGTH, 100);
				} else if(tick == 2)
					stop();
				tick++;
			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.setSkillAnimation(-1).setCrossingObstacle(false);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			}
		});
	}

	/**
	 * Handles Impling Jars looting
	 * @param player	The player looting the jar
	 */
	public static void lootJar(final Player player, Item jar, JarData jarData) {
		if(player == null || jar == null || jarData == null || !player.getClickDelay().elapsed(2000))
			return;
		if(player.getInventory().getFreeSlots() < 2) {
			player.getPacketSender().sendMessage("You need at least 2 free inventory space to loot this.");
			return;
		}
		player.getInventory().delete(jar);
		player.getInventory().add(11260, 1);
		int randomCommonItem = Misc.getRandom(JarData.getLootRarity(jarData, 0));
		int randomUncommonItem = JarData.getLootRarity(jarData, 0)+Misc.getRandom(JarData.getLootRarity(jarData, 1));
		int randomRareItem = JarData.getLootRarity(jarData, 2);
		int randomVeryRareItem = JarData.getLootRarity(jarData, 3);
		Item reward = null;
		switch(JarData.getRar()) {
		case 0:
			reward = jarData.loot[randomCommonItem];
			if(reward != null)
				player.getInventory().add(reward);
			break;
		case 1:
			reward = jarData.loot[randomUncommonItem];
			if(reward != null)
				player.getInventory().add(reward);
			break;
		case 2:
			reward = jarData.loot[randomRareItem];
			if(reward != null)
				player.getInventory().add(reward);
			break;
		case 3:
			reward = jarData.loot[randomVeryRareItem];
			if(reward != null)
				player.getInventory().add(reward);
			break;
		}
		String rewardName = reward.getDefinition().getName();
		String s = Misc.anOrA(rewardName);
		if(reward.getAmount() > 1) {
			s = ""+reward.getAmount()+"";
			if(!rewardName.endsWith("s")) {
				if(rewardName.contains("potion")) {
					String l = rewardName.substring(0, rewardName.indexOf(" potion"));
					String l2 = rewardName.substring(rewardName.indexOf("potion"), 8);
					l2 += rewardName.contains("(3)") ? "(3)" : "(4)";
					rewardName = ""+l+" potions "+l2+"";
				} else
					rewardName = rewardName + "s";
			}
		}
		player.getPacketSender().sendMessage("You loot the "+jar.getDefinition().getName()+" and find "+s+" "+rewardName+".");
		player.getClickDelay().reset();
	}

}
