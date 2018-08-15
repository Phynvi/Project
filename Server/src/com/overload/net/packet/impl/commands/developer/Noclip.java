package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Noclip extends Command {

	@Override
	public void execute(Player player, String input) {
		player.getPacketSender().sendEnableNoclip();
		player.getPacketSender().sendConsoleMessage("Noclip enabled.");
	}

}
