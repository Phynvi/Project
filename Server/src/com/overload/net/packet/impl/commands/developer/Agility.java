package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Agility extends Command {

	@Override
	public void execute(Player player, String input) {
		com.overload.game.content.skill.agility.Agility.resetProgress(player);
	}

}
