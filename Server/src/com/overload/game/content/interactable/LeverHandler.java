package com.overload.game.content.interactable;

import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Position;
import com.overload.game.model.teleportation.TeleportHandler;
import com.overload.game.model.teleportation.TeleportType;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;

//Fucking hardcode bullshit until i find a dynamic way to handle levers -Adam
public class LeverHandler {
	public static boolean handle(Player player, GameObject lever) {
		switch (lever.getId()) {
			case 5959://Mage Arena Lever
				pull(player, lever, new Position(2539, 4712, 0));
				return true;
			case 5960://Wildy Mage Arena Lever
				pull(player, lever, new Position(3091, 3957, 0));
				return true;
			default:
				return false;
		}
	}

	private static void pull(Player player, GameObject lever, Position position) {
		player.sendMessage("You pull the lever...");
		player.performAnimation(2140);
		player.setPositionToFace(lever.getPosition());
		TaskManager.submit(new Task(1, player, true) {
			int tick = 0;
			@Override
			protected void execute() {
				if (tick == 1) {
					TeleportHandler.teleport(player, position, TeleportType.LEVER, false);
					stop();
				}
				tick++;
			}
			
			@Override
			public void stop() {
				setEventRunning(false);
				super.stop();
			}
			
		});
	}
}
