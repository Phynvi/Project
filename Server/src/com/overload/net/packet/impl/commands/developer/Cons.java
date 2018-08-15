package com.overload.net.packet.impl.commands.developer;

import java.util.Optional;

import com.overload.game.World;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Cons extends Command {

	@Override
	public void execute(Player player, String input) {
		String[] args = input.split(" ");
		switch(args[0].toLowerCase()) {
			case "create":
				player.getHouse().create();
				break;
			case "build":
				player.getHouse().enter(player, player, true);
				break;
			case "enter":
				player.getHouse().enter(player, player, false);
				break;
			case "load":
				player.getHouse().load();
				break;
			case "save":
				player.getHouse().save();
				break;
			case "join":
				Optional<Player> op = World.getPlayerByName(args[1]);
				if (op.isPresent()) {
					Player p = op.get();
					player.getHouse().enter(p, player, false);
				} else {
					player.sendMessage("It doesn't look like " + args[1] + " is home.");
				}
				break;
		}
	}

}
