package com.overload.net.packet.impl.commands.admin;

import java.util.Optional;

import com.overload.game.World;
import com.overload.game.content.combat.CombatFactory;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Kick extends Command {

	@Override
	public void execute(Player player, String input) {
		String player2 = input;
		Optional<Player> plr = World.getPlayerByName(player2);
		if(!plr.isPresent()) {
			player.getPacketSender().sendMessage("Player "+player2+" is not online.");
			return;
		}
		if(CombatFactory.inCombat(plr.get())) {
			player.getPacketSender().sendMessage("Player "+player2+" is in combat!");
			return;
		}
		player.getPacketSender().sendMessage("Player "+player2+" was successfully kicked.");
		plr.get().requestLogout();
	}

}
