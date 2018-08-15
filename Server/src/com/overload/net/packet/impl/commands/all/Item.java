package com.overload.net.packet.impl.commands.all;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Item extends Command {

	@Override
	public void execute(Player player, String input) {
		String[] parts = input.split(" ");
		int amount = 1;
		if(parts.length > 1) {
                amount = Integer.parseInt(parts[1]);
		}
		player.getInventory().add(new com.overload.game.model.Item(Integer.parseInt(parts[0]), amount));
	}

}
