package com.overload.game.model.areas.impl.instance;

//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import com.overload.game.Palette;
//import com.overload.game.collision.RegionManager;
//import com.overload.game.entity.Entity;
//import com.overload.game.entity.impl.player.Player;
//import com.overload.game.model.Action;
//import com.overload.game.model.Boundary;
//import com.overload.game.model.Position;
//import com.overload.game.model.areas.Area;
//import com.overload.game.model.areas.impl.ZulrahArea;
//import com.overload.game.model.region.InstancedRegion;
//import com.overload.game.task.Task;
//import com.overload.game.task.TaskManager;

public class ZulrahInstance {

//	public ZulrahInstance(Player player) {
//		TaskManager.submit(new Task(1, player, true) {
//			int tick = 0;
//			Palette palette = null;
//			List<Area> area = new ArrayList<>();;
//			InstancedRegion instance = null;
//			Boundary copyTo = new Boundary(1152, 6080, 1215, 6143, player.getIndex() * 4);
//			Boundary copyFrom = new Boundary(2240, 3041, 2303, 3104, player.getIndex() * 4);
//
//			@Override
//			public void execute() {
//				if (tick == 0) {
//					palette = RegionManager.getBoundaryAsPalette(copyFrom);
//
//					player.getNewRegionPosition(new Position(copyTo.getX2(), copyTo.getY2(), player.getIndex() * 4));
//					player.getPacketSender().sendConstructMapRegion(palette);
//					player.moveTo(new Position(1180, 6109, player.getIndex() * 4));
//				}
//
//				if (tick >= 4) {
//                    area.add(new ZulrahArea(Arrays.asList(copyTo)));
//					instance = new InstancedRegion(
//						Optional.of(player),
//						Optional.of(new Action() {
//		                    @Override
//		                    public void execute(Entity entity) {
//		                    	if (entity.isPlayer()) {
//		                    		player.sendMessage(entity.getAsPlayer().getUsername() + " has joined your instance.");
//		                    	} else if (entity.isNpc()){
//		                    		player.sendMessage(entity.getAsNpc().getDefinition().getName() + " has joined your instance.");
//		                    	}
//		                    }
//							@Override
//							public void execute() {
//								player.sendMessage("idk enter");
//							}
//						}),
//						Optional.of(new Action() {
//		                    @Override
//		                    public void execute(Entity entity) {
//		                    	if (entity.isPlayer()) {
//		                    		player.sendMessage(entity.getAsPlayer().getUsername() + " has left your instance.");
//		                    	} else if (entity.isNpc()){
//		                    		player.sendMessage(entity.getAsNpc().getDefinition().getName() + " has left your instance.");
//		                    	}
//		                    }
//							@Override
//							public void execute() {
//								player.sendMessage("idk leave");
//							}
//						}),
//						Optional.of(area),
//						Optional.of(palette)
//					);
//                    stop();
//				}
//
//				tick++;
//			}
//
//			@Override
//			public void stop() {
//				instance.addEntity(player);
//				setEventRunning(false);
//			}
//		});
//	}

}
