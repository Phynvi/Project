package com.overload.net.packet.impl.commands.all;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Test extends Command {

	@Override
	public void execute(Player player, String input) {
		player.sendMessage("YOOO");
		
	}
}
