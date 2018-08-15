package com.overload.net.packet.impl.commands.admin;

import java.util.Optional;

import com.overload.game.World;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;
import com.overload.util.PlayerPunishment;

public class Ipmute extends Command {

	@Override
	public void execute(Player player, String input) {
		Optional<Player> player2 = World.getPlayerByName(input);
		if(!player2.isPresent()) {
			player.getPacketSender().sendMessage("Player "+player2+" is not online.");
			return;
		}
		if(PlayerPunishment.IPMuted(player2.get().getHostAddress())){
			player.getPacketSender().sendMessage("Player "+player2.get().getUsername()+"'s IP is already IPMuted.");
			return;
		}
		PlayerPunishment.addMutedIP(player2.get().getHostAddress());
		player.getPacketSender().sendMessage("Player "+player2.get().getUsername()+" was successfully IPMuted.");
		player2.get().getPacketSender().sendMessage("You have been IPMuted by "+player.getUsername()+".");
	}

}
