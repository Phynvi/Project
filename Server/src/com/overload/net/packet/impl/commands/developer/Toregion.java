package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Position;
import com.overload.net.packet.impl.commands.Command;

public class Toregion extends Command {

	@Override
	public void execute(Player player, String input) {
		int regionId = Integer.parseInt(input);
		
		int _x = (regionId >> 8) << 6;
		int _y = (regionId & 0xFF) << 6;
    	
    	player.moveTo(new Position(_x, _y, 0));
	}

}
