package com.overload.net.packet.impl.commands.developer;

import com.overload.game.definition.NpcDefinition;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Pnpc extends Command {

	@Override
	public void execute(Player player, String input) {
	    int id = Integer.parseInt(input);
	    player.setSize(NpcDefinition.forId(id).getSize());
		player.setNpcTransformationId(id);
	}

}
