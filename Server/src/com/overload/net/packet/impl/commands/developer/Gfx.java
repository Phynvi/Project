package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Graphic;
import com.overload.net.packet.impl.commands.Command;

public class Gfx extends Command {

	@Override
	public void execute(Player player, String input) {
		int gfx = Integer.parseInt(input);
		player.performGraphic(new Graphic(gfx));
	}

}
