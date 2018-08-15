package com.overload.game.content.skill.agility;

import com.overload.game.World;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Skill;
import com.overload.game.model.container.impl.Equipment;
import com.overload.util.Misc;

public class Agility {

	public static boolean handleObject(Player p, GameObject object) {
		if(object.getId() == 2309) {
			if(p.getSkillManager().getMaxLevel(Skill.AGILITY) < 55) {
				p.getPacketSender().sendMessage("You need an Agility level of at least 55 to enter this course.");
				return true;
			}
		}
		ObstacleData agilityObject = ObstacleData.forId(object.getId());
		if(agilityObject != null) {
			if(p.isCrossingObstacle())
				return true;
			p.setPositionToFace(object.getPosition());
			p.setPosition(p.getPosition());
			p.setCrossingObstacle(true);
			agilityObject.cross(p);
			if (Misc.getRandom(1600) == 546) {
				p.getInventory().add(20659 ,1);
				World.sendMessage("[<img=10>]<col=1a4fa5> "+p.getUsername()+"@bla@ has received a <col=1a4fa5>Giant squirrel@bla@ pet from agility!");
			}
		}
		return false;
	}

	public static boolean passedAllObstacles(Player player) {
		for(boolean crossedObstacle : player.getCrossedObstacles()) {
			if(!crossedObstacle)
				return false;
		}
		return true;
	}

	public static void resetProgress(Player player) {
		for(int i = 0; i < player.getCrossedObstacles().length; i++)
			player.setCrossedObstacle(i, false);
	}
	
	public static boolean isSuccessive(Player player) {
		return Misc.getRandom(player.getSkillManager().getCurrentLevel(Skill.AGILITY) / 2) > 1;
	}
	
	public static void addExperience(Player player, int experience) {
		boolean agile = player.getEquipment().get(Equipment.BODY_SLOT).getId() == 11854 && player.getEquipment().get(Equipment.LEG_SLOT).getId() == 11856
				&& player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 11852 && player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 11850;
		player.getSkillManager().addExperience(Skill.AGILITY, agile ? (experience *= 1.5) : experience);
	}
}
