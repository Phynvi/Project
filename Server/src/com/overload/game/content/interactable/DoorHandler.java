package com.overload.game.content.interactable;

import com.overload.game.content.minigames.godwars.GodWarsData.Allegiance;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Position;
import com.overload.game.model.Skill;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.game.task.impl.TimedObjectReplacementTask;

public class DoorHandler {
	
	public static boolean clickDoor(Player player, GameObject door) {
		if (handleDoorOverride(player, door))
			return true;
		
		int face = door.getFace();
		
		int newFace = (face - 1) < 0 ? 3 : (face - 1);
		GameObject tmpDoor = new GameObject(door.getId(), door.getPosition(), door.getType(), newFace);
		
		Task t = new TimedObjectReplacementTask(door, tmpDoor, 60);
		TaskManager.submit(t);
		
		return true;
	}

	private static boolean handleDoorOverride(Player player, GameObject door) {
		switch (door.getId()) {
			case 26461: //Bandos Entrance Door
				if (player.getSkillManager().getCurrentLevel(Skill.STRENGTH) >= 70) {
					if (player.getInventory().contains(2347)) {
						if (player.getPosition().getX() == 2850) {
			    			player.moveTo(new Position(2851, 5333, 2));
			    		} else {
			    			player.moveTo(new Position(2850, 5333, 2));
			    		}
					} else {
						player.getPacketSender().sendMessage("It looks like i need to hit the door with something... perhaps a hammer?");
					}
				} else {
					player.getPacketSender().sendMessage("You need a strength level of atleast 70 to do this.");
				}
				return true;
			case 26502: //Armadyl Godwars Door
				player.getGodwars().enterBossRoom(Allegiance.ARMADYL);
				return true;
			case 26503: //Bandos Godwars Door
				player.getGodwars().enterBossRoom(Allegiance.BANDOS);
				return true;
			case 26504: //Saradomin Godwars Door
				player.getGodwars().enterBossRoom(Allegiance.SARADOMIN);
				return true;
			case 26505: //Zamorak Godwars Door
				player.getGodwars().enterBossRoom(Allegiance.ZAMORAK);
				return true;
		}
		return false;
	}

}
