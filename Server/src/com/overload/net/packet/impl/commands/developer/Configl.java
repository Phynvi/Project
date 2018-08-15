package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.net.packet.impl.commands.Command;

public class Configl extends Command {

	@Override
	public void execute(Player player, String input) {
		String[] parts = input.split(" ");
		if (parts.length > 3) {
			TaskManager.submit(new Task(1, player, true) {
				int ticks = Integer.parseInt(parts[1]);
				@Override
				protected void execute() {
					
					if (ticks > Integer.parseInt(parts[2]))
						stop();
					else
						player.getPacketSender().sendConfigByte(Integer.parseInt(parts[0]), ticks);
					ticks++;
				}
				
			});
		} else {
			for (int i = Integer.parseInt(parts[1]); i < Integer.parseInt(parts[2]); i++) {
				player.getPacketSender().sendConfigByte(Integer.parseInt(parts[0]), i);
			}
		}
	}

}
