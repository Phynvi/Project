package com.overload.game.content.interactable;

import java.util.Optional;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.definition.ObjectDefinition;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.object.MapObjects;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Position;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;

public class StairHandler {
	
	private static final int NORTH = 0;
	private static final int EAST = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;
	
	public static boolean override(Player player, GameObject object, Optional<String> climbDirection) {
		int id = object.getId();
		switch (id) {
			case 16671:
			case 16672:
			case 16673:
				Position pos = player.getPosition().clone();
				if (id == 16671) {
					pos.addZ(1);
					climbDirection = Optional.of("up");
				} else if (id == 16672) {
					if (climbDirection.isPresent()) {
						if (climbDirection.get() == "up") {
							pos.addZ(1);
						} else if (climbDirection.get() == "down") {
							pos.takeZ(1);
						}
					} else {
						DialogueHandler.sendOptions(player, new String[] { "Climb Up", "Climb Down", "Stay Here" });
						player.setLadderClicked(object);
						player.setNpcTalking(-10);
						player.setDialogueState(1);
			            player.setDialogueClickType(DialogueHandler.GLOBAL_OBJECT_CLICK);
			            player.setDialogueClickId(object.getId());
					}
				} else if (id == 16673) {
					pos.takeZ(1);
					climbDirection = Optional.of("down");
				}
				if (climbDirection.isPresent()) {
					player.getPacketSender().sendMessage("You climb " + climbDirection.get() + " the stairs..");
					player.moveTo(pos);
				}
				return true;
			default:
				return false;
		}
	}
	
	public static boolean climb(Player player, GameObject object, Optional<String> climbDirection) {
		if (override(player, object, climbDirection))
			return true;
		Position stairsUp = object.getPosition().clone().addZ(1);
		Position stairsDown = object.getPosition().clone().takeZ(1);
		Position stairsUpTmp = object.getPosition().clone().addZ(1);
		Position stairsDownTmp = object.getPosition().clone().takeZ(1);
		Optional<GameObject> upStairsOptional = Optional.empty();
		Optional<GameObject> downStairsOptional = Optional.empty();
		GameObject upStairs = null;
		GameObject downStairs = null;
		for (int y = -(object.getDefinition().getSizeY()); y < (object.getDefinition().getSizeY()); y++) {
			int diff = y == 0 ? 0 : y > 0 ? 1 : -1;
			if (diff == 0) {
				stairsUpTmp = stairsUp.clone();
				stairsDownTmp = stairsDown.clone();
			}
			stairsUpTmp.addY(diff);
			stairsDownTmp.addY(diff);
			for (int x = -(object.getDefinition().getSizeX()); x < (object.getDefinition().getSizeX()); x++) {
				if (upStairs != null || downStairs != null)
					continue;
				
				System.out.print(stairsDownTmp.toString() + "\n");

				diff = x == 0 ? 0 : x > 0 ? 1 : -1;
				if (diff == 0) {
					stairsUpTmp = stairsUp.clone();
					stairsDownTmp = stairsDown.clone();
				}
				stairsUpTmp.addX(diff);
				stairsDownTmp.addX(diff);
				
				upStairsOptional = MapObjects.get(stairsUp);
				downStairsOptional = MapObjects.get(stairsDown);
		
				if (upStairsOptional.isPresent())
					upStairs = upStairsOptional.get();
				if (downStairsOptional.isPresent())
					downStairs = downStairsOptional.get();

			}
		}
		if (upStairs == null || !isStairs(upStairs)) {
			upStairs = null;
		}
		
		if (downStairs == null || !isStairs(downStairs)) {
			downStairs = null;
		}
		
		if (climbDirection.isPresent() && climbDirection != null) {
			if (climbDirection.get() == "up") {
				if (upStairs != null)
					climbUp(player, upStairs);
				else
					stairError(player, object);
				return true;
			} else if (climbDirection.get() == "down") {
				if (downStairs != null)
					climbDown(player, downStairs);
				else
					stairError(player, object);
				return true;
			}
		}
		if (upStairs != null && downStairs != null) {
			DialogueHandler.sendOptions(player, new String[] { "Climb Up", "Climb Down", "Stay Here" });
			player.setLadderClicked(object);
			player.setNpcTalking(-10);
			player.setDialogueState(1);
            player.setDialogueClickType(DialogueHandler.GLOBAL_OBJECT_CLICK);
            player.setDialogueClickId(object.getId());
			return true;
		} else if (upStairs != null && downStairs == null) {
			climbUp(player, upStairs);
			return true;
		} else if (player.getZ() > 0 && upStairs == null && downStairs != null) {
			climbDown(player, downStairs);
			return true;
		} else {
			stairError(player, object);
			return false;
		}
	}
	
	public static boolean isStairs(GameObject object) {
		ObjectDefinition def = object.getDefinition();
		String name = def.getName();
		if (name != null) {
			return name.toLowerCase().contains("stair");
		}
		return false;
	}
	
	public static void stairError(Player player, GameObject object) {
		player.sendMessage("@dre@Please take a screenshot of this text and send to Discord Bugs.");
		player.sendMessage("@dre@Unknown Staircase: " + object.getPosition().toString());
	}
	
	public static void climbDown(Player player, GameObject object) {
		int face = object.getFace();
		Position pos = object.getPosition().clone();

		if (face == NORTH) pos.takeY(1);
		if (face == EAST)  pos.addX( 1);
		if (face == SOUTH) pos.addY( 1);
		if (face == WEST)  pos.takeX(1);
		
		player.getPacketSender().sendMessage("You climb down the stairs..");
		TaskManager.submit(new Task(1) {
			@Override
			public void execute() {
				player.moveTo(pos);
				stop();
			}
		});
	}
	
	public static void climbUp(Player player, GameObject object) {
		int face = object.getFace();
		Position pos = object.getPosition().clone();

		if (face == NORTH) pos.takeY(1);
		if (face == EAST)  pos.addX( 1);
		if (face == SOUTH) pos.addY( 1);
		if (face == WEST)  pos.takeX(1);
		
		player.getPacketSender().sendMessage("You climb up the stairs..");
		TaskManager.submit(new Task(1) {
			@Override
			public void execute() {
				player.moveTo(pos);
				stop();
			}
		});
	}
}
