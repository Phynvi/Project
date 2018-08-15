package com.overload.net.packet.impl.commands.developer;

import com.overload.game.content.CrystalChest;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Dump extends Command {

	@Override
	public void execute(Player player, String input) {
		CrystalChest.dump(player);
	}

}
