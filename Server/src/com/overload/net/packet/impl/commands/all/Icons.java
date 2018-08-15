package com.overload.net.packet.impl.commands.all;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Icons extends Command {

	@Override
	public void execute(Player player, String input) {
		for (int index = 682; index <= 697; index++) {
			player.getPacketSender().sendMessage("<img=" + index + "> Christian: Helllo");
		}
		
	}
}
