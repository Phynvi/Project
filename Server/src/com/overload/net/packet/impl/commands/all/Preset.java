package com.overload.net.packet.impl.commands.all;

import com.overload.game.content.presets.Presetables;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Preset extends Command {

	@Override
	public void execute(Player player, String input) {
		Presetables.open(player);
	}

}
