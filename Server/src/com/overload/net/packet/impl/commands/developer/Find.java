package com.overload.net.packet.impl.commands.developer;

import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Find extends Command {

	@Override
	public void execute(Player player, String input) {
		String name = input;
		for(ItemDefinition def : ItemDefinition.definitions.values()) {
			if(def.getName().toLowerCase().contains(name)) {
				player.getPacketSender().sendConsoleMessage("Found item, id: "+def.getId()+", name: "+def.getName());
			}
		}
	}

}
