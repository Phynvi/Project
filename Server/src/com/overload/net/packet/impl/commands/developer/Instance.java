package com.overload.net.packet.impl.commands.developer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.overload.game.Palette;
import com.overload.game.collision.RegionManager;
import com.overload.game.entity.Entity;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Action;
import com.overload.game.model.Position;
import com.overload.game.model.areas.Area;
import com.overload.game.model.areas.impl.ZulrahArea;
import com.overload.game.model.region.InstancedRegion;
import com.overload.game.task.TaskManager;
import com.overload.game.task.Task;
import com.overload.net.packet.impl.commands.Command;

public class Instance extends Command {
	
	@Override
	public void execute(Player player, String input) {
		
		int xPos = 1214;
		int yPos = 6142;
		
		TaskManager.submit(new Task(1, player, true) {
			
			int tick = 0;
			Palette palette = null;
			List<Area> bounds = new ArrayList<>();;
			InstancedRegion instance = null;
			Position pos = player.getPosition();
			Position newPos = null;
			
			@Override
			public void execute() {
				if (tick == 0) {
					palette = RegionManager.getAsPalette(pos.getX(), pos.getY());
					newPos = RegionManager.getPlayerPositionInRegion(player, pos.getX(), pos.getY(), xPos, yPos);
					player.getNewRegionPosition(new Position(xPos, yPos, player.getIndex() * 4));
					player.getPacketSender().sendConstructMapRegion(palette);
					player.moveTo(newPos);
				}
				
				if (tick == 1) {
					bounds.add(new ZulrahArea());//(1152, 6080, 1215, 6143, player.getIndex()*4));
				}
				
				if (tick == 4) {
					instance = new InstancedRegion(
							Optional.of(player),
							Optional.of(new Action() {
			                    @Override
			                    public void execute(Entity entity) {
			                    	if (entity.isPlayer())
			                    		player.sendMessage(entity.getAsPlayer().getUsername() + " has joined your instance.");
			                    }
								@Override
								public void execute() {}
							}),
							Optional.of(new Action() {
			                    @Override
			                    public void execute(Entity entity) {
			                    	if (entity.isPlayer())
			                    		player.sendMessage(entity.getAsPlayer().getUsername() + " has left your instance.");
			                    }
								@Override
								public void execute() {}
							}),
							Optional.of(bounds),
							Optional.of(palette)
					);
				}
				
				if (tick == 5) {
					stop();
				}
				
				tick++;
			}
			
			@Override
			public void stop() {
				instance.addEntity(player);
				setEventRunning(false);
			}
		});
	}
	
}
