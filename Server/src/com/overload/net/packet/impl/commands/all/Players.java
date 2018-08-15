package com.overload.net.packet.impl.commands.all;

import com.overload.game.World;
import com.overload.game.content.combat.bountyhunter.BountyHunter;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Players extends Command {

	@Override
	public void execute(Player player, String input) {
		player.getPacketSender().sendConsoleMessage("There are currently "+World.getPlayers().size()+" players online and "+BountyHunter.PLAYERS_IN_WILD.size()+" players in the Wilderness.");
	}

}
