package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Emote extends Command {

	@Override
	public void execute(Player player, String input) {
		player.getAnimations().setStandEmote(Integer.parseInt(input));
	}

}
