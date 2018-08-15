package com.overload.net.packet.impl.commands.admin;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Unipmute extends Command {

	@Override
	public void execute(Player player, String input) {
		player.getPacketSender().sendMessage("Unipmutes can only be handled manually.");
	}

}
