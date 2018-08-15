package com.overload.game.content.interactable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.overload.game.definition.ObjectDefinition;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.object.MapObjects;
import com.overload.game.entity.impl.object.ObjectManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Item;
import com.overload.game.model.Position;
import com.overload.game.model.container.impl.Equipment;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.game.task.impl.TimedObjectReplacementTask;
import com.overload.util.Misc;
import com.overload.util.ObjectIdentifiers;

public class Shortcut extends ObjectIdentifiers {
	private static enum Obstacles {
		VINES(			new Animation(879),  40,  5, 15, 21731, 21732, 21733, 21734, 21735),
		LOGS(			null, 				 100, 0, 0,  20882),
		STEPPING_STONES(null, 				 100, 0, 0,  19040),
		PIPES(			null, 				 100, 0, 0,  16509, 20210, 21728),
		WEBS(			new Animation(1833), 40,  0, 15, 733),
		SPIKEY_CHAINS(	new Animation(1833), 100, 0, 0, 16537, 16538)
		;
		
		//Animation to run during the task
		Animation animation;
		//Percentage of success (out of 100)
		int chance;
		//Minimum ticks to wait before success/fail
		int minimumTicksToWait;
		//Maximum ticks to wait before fail
		int maximumTicksToWait;
		
		List<Integer> objectIds = new ArrayList<>();
		
		Obstacles(Animation animation, int chance, int minimumTicksToWait, int maximumTicksToWait, int... objectIds) {
			this.animation = animation;
			this.chance = chance;
			this.minimumTicksToWait = minimumTicksToWait;
			this.maximumTicksToWait = maximumTicksToWait;
			for (int i : objectIds) {
				this.objectIds.add(i);
			}
		}
		
		public Animation getAnimation() {
			return animation;
		}
		
		public int getChance() {
			return chance;
		}
		
		public int getMaximumTicksToWait() {
			return maximumTicksToWait;
		}
		
		public int getMinimumTicksToWait() {
			return minimumTicksToWait;
		}
		
		public List<Integer> getObjects() {
			return objectIds;
		}
	}
	
	public static boolean handle(Player player, GameObject object) {
		final ObjectDefinition def = ObjectDefinition.forId(object.getId());
		
		String type = Obstacles.VINES.getObjects().contains(object.getId()) 		  ?	"VINES" 		  :
					  Obstacles.LOGS.getObjects().contains(object.getId()) 			  ? "LOGS" 			  :
					  Obstacles.STEPPING_STONES.getObjects().contains(object.getId()) ? "STEPPING_STONES" :
					  Obstacles.PIPES.getObjects().contains(object.getId())			  ?	"PIPES" 		  :
					  Obstacles.WEBS.getObjects().contains(object.getId()) 			  ? "WEBS" 			  :
					  Obstacles.SPIKEY_CHAINS.getObjects().contains(object.getId())   ? "SPIKEY_CHAINS"   :
					  null;
		
		if (type == null)
			return false;
		
		if (type == "SPIKEY_CHAINS") {
			return climb(player, object);
		}
		
		return move(player,
					object.getFace(),
					new int[] {
						def.getSizeX(),
						def.getSizeY()
					},
					object,
					type);
	}

	private static final int NORTH = 0;
	private static final int EAST = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;

	private static boolean climb(Player player, GameObject object) {
		Position upPos = object.getPosition().clone().addZ(1);
		Position downPos = object.getPosition().clone().takeZ(1);
		Optional<GameObject> upOptional = MapObjects.get(upPos);
		Optional<GameObject> downOptional = MapObjects.get(downPos);

		GameObject up = null;
		GameObject down = null;

		if (upOptional.isPresent())
			up = upOptional.get();
		if (downOptional.isPresent())
			down = downOptional.get();
		
		if (up != null && down == null) {
			climbUp(player, up);
			return true;
		} else if (up == null && down != null) {
			climbDown(player, down);
			return true;
		}
		return false;
	}
	
	private static boolean move(Player player, int face, int[] objSize, GameObject object, String type) {
		Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
		if (item == null)
			return false;
		if (type == "WEBS" && !(item.getDefinition().getName().contains("sword") || item.getDefinition().getName().contains("dagger") || item.getDefinition().getName().contains(" axe"))) {
			return false;
		}
		Position p = player.getPosition();
		Position pos = object.getPosition();
		
		final String msgStart;
		final String msgSuccess;
		final String msgFailure;
		
		int chance = Obstacles.valueOf(type).getChance();
		
		if (type == "VINES") {
			if (face == NORTH) p.addY(p.getY() < pos.getY() ?   objSize[1] + 1  : -(objSize[1] + 1));
			if (face == EAST)  p.addX(p.getX() < pos.getX() ? -(objSize[0] + 1) :   objSize[0] + 1);
			if (face == SOUTH) p.addY(p.getY() < pos.getY() ? -(objSize[1] + 1) :   objSize[1] + 1);
			if (face == WEST)  p.addX(p.getX() < pos.getX() ?   objSize[0] + 1  : -(objSize[0] + 1));
			msgStart = "You attempt to cut through the vines...";
			msgSuccess = "You successfully cut through the vines.";
			msgFailure = "You failed to cut through the vines.";
		} else if (type == "LOGS") {
			if (face == NORTH) p.addY(p.getY() < pos.getY() ?   objSize[1] + 1  : -(objSize[1] + 1));
			if (face == EAST)  p.addX(p.getX() < pos.getX() ? -(objSize[0] + 1) :   objSize[0] + 1);
			if (face == SOUTH) p.addY(p.getY() < pos.getY() ? -(objSize[1] + 1) :   objSize[1] + 1);
			if (face == WEST)  p.addX(p.getX() < pos.getX() ?   objSize[0] + 1  : -(objSize[0] + 1));
			msgStart = "You attempt to walk across the log...";
			msgSuccess = "You successfully walk across the log.";
			msgFailure = "You fell off the log.";
		} else if (type == "WEBS") {
			msgStart = "You attempt to cut through the web...";
			msgSuccess = "You successfully cut through the web.";
			msgFailure = "You failed to cut through the web.";
		} else if (type == "PIPES") {
			
			int add = 0;
			
			if (face == NORTH) {
				GameObject obj = null;
				int i = 0;
				while (obj == null && i < 5) {
					Position p2 = new Position(pos.getX() + i, pos.getY(), pos.getZ());
					if (ObjectManager.exists(p2)) {
						obj = ObjectManager.get(p2).get();
					}
					i++;
				}
				add = i;
			}
			if (face == EAST)  {
				GameObject obj = null;
				int i = 0;
				while (obj == null && i < 5) {
					Position p2 = new Position(pos.getX(), pos.getY() + i, pos.getZ());
					if (ObjectManager.exists(p2)) {
						obj = ObjectManager.get(p2).get();
					}
					i++;
				}
				add = i;
			}
			if (face == SOUTH) {
				GameObject obj = null;
				int i = 0;
				while (obj == null && i < 5) {
					Position p2 = new Position(pos.getX() - i, pos.getY(), pos.getZ());
					if (ObjectManager.exists(p2)) {
						obj = ObjectManager.get(p2).get();
					}
					i++;
				}
				add = i;
			}
			if (face == WEST)  {
				GameObject obj = null;
				int i = 0;
				while (obj == null && i < 5) {
					Position p2 = new Position(pos.getX(), pos.getY() - i, pos.getZ());
					if (ObjectManager.exists(p2)) {
						obj = ObjectManager.get(p2).get();
					}
					i++;
				}
				add = i;
			}
			
			if (face == NORTH) { p.addX(p.getX() < pos.getX() ?   objSize[1] + add  : -(objSize[1] + add)); p.setY(pos.getY()); }
			if (face == EAST)  { p.addY(p.getY() < pos.getY() ? -(objSize[0] + add) :   objSize[0] + add);  p.setX(pos.getX()); }
			if (face == SOUTH) { p.addX(p.getX() > pos.getX() ? -(objSize[1] + add) :   objSize[1] + add);  p.setY(pos.getY()); }
			if (face == WEST)  { p.addY(p.getY() > pos.getY() ?   objSize[0] + add  : -(objSize[0] + add)); p.setX(pos.getX()); }
			msgStart = "You crawl through the pipe";
			msgSuccess = "";
			msgFailure = "";
			//return false;
		} else {
			msgStart = "";
			msgSuccess = "";
			msgFailure = "";
		}
		
		final boolean hasAnim = Obstacles.valueOf(type).getAnimation() != null;
		
		if (hasAnim)
			player.performAnimation(Obstacles.valueOf(type).getAnimation());
		player.getMovementQueue().setBlockMovement(true);
		TaskManager.submit(new Task(1, player, true) {
			int ticks = 0;
			@Override
			protected void execute() {
				if (ticks == 0)
					player.sendMessage(msgStart);
				if (ticks % 4 == 0 || (type == "WEBS" && ticks % 2 == 0))
					player.performAnimation(Obstacles.valueOf(type).getAnimation());
				if (ticks >= Obstacles.valueOf(type).getMaximumTicksToWait() && Obstacles.valueOf(type).getMaximumTicksToWait() > 0) {
					if (msgFailure != "" && msgFailure != null)
						player.sendMessage(msgFailure);
					stop();
				} else if (ticks >= Obstacles.valueOf(type).getMinimumTicksToWait()) {
					if (Misc.random(100) < chance) {
						if (msgSuccess != "" && msgSuccess != null)
							player.sendMessage(msgSuccess);
						if (type == "WEBS") {
							GameObject temp = object.clone();
							temp.setId(734);
							temp.setSpawnedFor(Optional.of(player));
							TaskManager.submit(new TimedObjectReplacementTask(object, temp, 5));
							player.getPacketSender().sendAnimationReset();
							stop();
						} else {
							player.moveTo(p);
							player.getPacketSender().sendAnimationReset();
							stop();
						}
					}
				}
				
				ticks++;
			}
			
			@Override
			public void stop() {
				player.getMovementQueue().setBlockMovement(false);
				this.setEventRunning(false);
			}
			
		});
		
		return true;
	}
	
	public static void climbDown(Player player, GameObject object) {
		int face = object.getFace();
		Position pos = object.getPosition().clone();

		if (face == NORTH) pos.takeY(1);
		if (face == EAST)  pos.addX( 1);
		if (face == SOUTH) pos.addY( 1);
		if (face == WEST)  pos.takeX(1);
		
		player.performAnimation(new Animation(827));
		player.getPacketSender().sendMessage("You climb down the chain..");
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
		player.getPacketSender().sendMessage("You climb up the chain..");
		TaskManager.submit(new Task(1) {
			@Override
			public void execute() {
				player.moveTo(pos);
				stop();
			}
		});
	}
}
