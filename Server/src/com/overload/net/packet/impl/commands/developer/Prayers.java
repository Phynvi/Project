package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Prayers extends Command {

	@Override
	public void execute(Player player, String input) {
		player.setPreserveUnlocked(!player.isPreserveUnlocked());
		player.setRigourUnlocked(!player.isRigourUnlocked());
		player.setAuguryUnlocked(!player.isAuguryUnlocked());
		
		player.getPacketSender().sendConfigByte(709, player.isPreserveUnlocked() ? 1 : 0);
		player.getPacketSender().sendConfigByte(711, player.isRigourUnlocked() ? 1 : 0);
		player.getPacketSender().sendConfigByte(713, player.isAuguryUnlocked() ? 1 : 0);
	}

}
