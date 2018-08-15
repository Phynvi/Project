package com.overload.net.packet.impl.commands.developer;

import java.util.Optional;

import com.overload.game.World;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Join extends Command {

	@Override
	public void execute(Player player, String input) {
		Optional<Player> joined = World.getPlayerByName(input);
		if (joined.isPresent()) {
			if (joined.get().getInstancedRegion().isPresent()) {
				player.sendMessage("Joining " + joined.get().getUsername() + "'s Instance.");
				joined.get().getInstancedRegion().get().addEntity(player);
			}
		}
	}

}