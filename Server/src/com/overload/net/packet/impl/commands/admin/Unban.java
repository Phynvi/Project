package com.overload.net.packet.impl.commands.admin;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.entity.impl.player.PlayerSaving;
import com.overload.net.packet.impl.commands.Command;
import com.overload.util.PlayerPunishment;

public class Unban extends Command {

	@Override
	public void execute(Player player, String input) {
		String player2 = input;
		if(!PlayerSaving.playerExists(player2)) {
			player.getPacketSender().sendMessage("Player "+player2+" is not online.");
			return;
		}
		if(!PlayerPunishment.banned(player2)) {
			player.getPacketSender().sendMessage("Player "+player2+" is not banned!");
			return;
		}
		PlayerPunishment.unban(player2);
		player.getPacketSender().sendMessage("Player "+player2+" was successfully unbanned.");
	}

}
