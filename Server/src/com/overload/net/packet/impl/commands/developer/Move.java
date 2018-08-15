package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Move extends Command {

	@Override
	public void execute(Player player, String input) {
		String[] args = input.split(" ");
		if (args.length == 1) {
			switch (args[0].toLowerCase()) {
				case "up":
					player.moveTo(player.getPosition().add(0, 0, 1));
					break;
				case "down":
					player.moveTo(player.getPosition().add(0, 0, -1));
					break;
				case "north":
					player.moveTo(player.getPosition().add(1, 0, 0));
					break;
				case "east":
					player.moveTo(player.getPosition().add(0, 1, 0));
					break;
				case "south":
					player.moveTo(player.getPosition().add(-1, 0, 0));
					break;
				case "west":
					player.moveTo(player.getPosition().add(0, -1, 0));
					break;
			}
		} else if (args.length == 2) {
			int amount = Integer.parseInt(args[1]);
			switch (args[0].toLowerCase()) {
				case "up":
					player.moveTo(player.getPosition().add(0, 0, amount));
					break;
				case "down":
					player.moveTo(player.getPosition().add(0, 0, -amount));
					break;
				case "north":
					player.moveTo(player.getPosition().add(amount, 0, 0));
					break;
				case "east":
					player.moveTo(player.getPosition().add(0, amount, 0));
					break;
				case "south":
					player.moveTo(player.getPosition().add(-amount, 0, 0));
					break;
				case "west":
					player.moveTo(player.getPosition().add(0, -amount, 0));
					break;
			}
		} else {
			player.getPacketSender().sendMessage("Usage - ::move [up/down/north/east/south/west] <amount>");
		}
	}
	
}
