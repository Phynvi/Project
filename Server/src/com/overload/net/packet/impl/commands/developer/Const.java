package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Const extends Command {

	@Override
	public void execute(Player player, String input) {
		String[] args = input.split(" ");
		switch (args[0].toLowerCase()) {
			case "new":
				break;
			case "enter":
				break;
			case "build":
				break;
			case "furnish":
				break;
			default:
				break;
					
		}
	}

}
