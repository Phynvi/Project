package com.overload.game.content.interactable;

import java.util.Optional;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.definition.ObjectDefinition;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.object.MapObjects;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Position;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;

public class LadderHandler {
	
	private static final int NORTH = 0;
	private static final int EAST = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;
	
	public static boolean climb(Player player, GameObject object, Optional<String> climbDirection) {
		Position ladderUp = object.getPosition().clone().addZ(1);
		Position ladderDown = object.getPosition().clone().takeZ(1);
		Optional<GameObject> upLadderOptional = MapObjects.get(ladderUp);
		Optional<GameObject> downLadderOptional = MapObjects.get(ladderDown);

		GameObject upLadder = null;
		GameObject downLadder = null;

		if (upLadderOptional.isPresent())
			upLadder = upLadderOptional.get();
		if (downLadderOptional.isPresent())
			downLadder = downLadderOptional.get();
		
		if (upLadder == null || !isLadder(upLadder)) {
			upLadder = null;
		}
		
		if (downLadder == null || !isLadder(downLadder)) {
			downLadder = null;
		}
		
		if (climbDirection.isPresent() && climbDirection != null) {
			if (climbDirection.get() == "up") {
				if (upLadder != null)
					climbUp(player, upLadder);
				else
					ladderError(player, object);
				return true;
			} else if (climbDirection.get() == "down") {
				if (downLadder != null)
					climbDown(player, downLadder);
				else
					ladderError(player, object);
				return true;
			}
		}
		if (upLadder != null && downLadder != null) {
			DialogueHandler.sendOptions(player, new String[] { "Climb Up", "Climb Down", "Stay Here" });
			player.setLadderClicked(object);
			player.setNpcTalking(-10);
			player.setDialogueState(1);
            player.setDialogueClickType(DialogueHandler.GLOBAL_OBJECT_CLICK);
            player.setDialogueClickId(object.getId());
			return true;
		} else if (upLadder != null && downLadder == null) {
			climbUp(player, upLadder);
			return true;
		} else if (player.getZ() > 0 && upLadder == null && downLadder != null) {
			climbDown(player, downLadder);
			return true;
		} else if (upLadder == null && downLadder == null && isTrapdoor(player, object) && player.getZ() == 0) {
			trapDoor(player, object);
			return true;
		} else {
			ladderError(player, object);
			return false;
		}
	}
	
	public static boolean isLadder(GameObject object) {
		ObjectDefinition def = object.getDefinition();
		String name = def.getName();
		if (name != null) {
			return name.toLowerCase().contains("ladder");
		}
		return false;
	}
	
	public static boolean isTrapdoor(Player player, GameObject object) {
		String name = object.getDefinition().getName();
		if (name != null) {
			if (object.getDefinition().interactions != null) {
				for (String s : object.getDefinition().interactions) {
					if (s != null)
						if (s.toLowerCase().contains("climb-down") || s.toLowerCase().contains("climb-up") && player.getZ() == 0)
							return true;
				}
			}
			return name.toLowerCase().contains("trapdoor") || name.toLowerCase().contains("ladder");
		}
		return false;
	}
	
	public static void ladderError(Player player, GameObject object) {
		player.sendMessage("@dre@Please take a screenshot of this text and send to Discord Bugs.");
		player.sendMessage("@dre@Unknown Ladder/Trapdoor: " + object.getPosition().toString());
	}
	
	public static void climbDown(Player player, GameObject object) {
		int face = object.getFace();
		Position pos = object.getPosition().clone();

		if (face == NORTH) pos.takeY(1);
		if (face == EAST)  pos.addX( 1);
		if (face == SOUTH) pos.addY( 1);
		if (face == WEST)  pos.takeX(1);
		
		player.performAnimation(new Animation(827));
		player.getPacketSender().sendMessage("You climb down the ladder..");
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
		
		player.performAnimation(new Animation(828));
		player.getPacketSender().sendMessage("You climb up the ladder..");
		TaskManager.submit(new Task(1) {
			@Override
			public void execute() {
				player.moveTo(pos);
				stop();
			}
		});
	}
	
	public static void trapDoor(Player player, GameObject object) {
		Position newPos = object.getPosition().clone();
		int anim = 827;
		if (player.getPosition().getY() < 9000) {
			newPos.addY(6400);
		} else if (player.getPosition().getY() > 9000) {
			newPos.takeY(6400);
			anim = 828;
		}

		for (int y = 0; y < 30; y++) {
			if (y > 0) {
				if (anim == 827)
					newPos.addY(1);
				else
					newPos.takeY(1);
			}
			for (int x = -1; x <= 1; x++) {
				Position tmpPos = newPos.clone();
				tmpPos.addX(x);

				Optional<GameObject> destinationLadder = MapObjects.get(tmpPos);
				if (destinationLadder.isPresent() && (isLadder(destinationLadder.get()) || isTrapdoor(player, destinationLadder.get()))) {
					
					int face = destinationLadder.get().getFace();
					final Position destPos = destinationLadder.get().getPosition().clone();
					if (face == NORTH) destPos.addY(1);
					if (face == EAST)  destPos.takeX( 1);
					if (face == SOUTH) destPos.takeY( 1);
					if (face == WEST)  destPos.addX(1);
					
					player.performAnimation(new Animation(anim));
					if (anim == 827)
						player.getPacketSender().sendMessage("You climb down the trapdoor..");
					else
						player.getPacketSender().sendMessage("You climb up the ladder..");
						
					TaskManager.submit(new Task(1) {
						@Override
						public void execute() {
							player.moveTo(destPos);
							stop();
						}
					});
					return;
				}
			}
		}
	}
}
