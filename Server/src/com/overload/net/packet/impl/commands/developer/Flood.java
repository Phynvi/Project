package com.overload.net.packet.impl.commands.developer;

import com.overload.Server;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Flood extends Command {

	@Override
	public void execute(Player player, String input) {
		int amt = Integer.parseInt(input);
		Server.getFlooder().login(amt);
	}
}
