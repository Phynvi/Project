package com.overload.net.packet.impl.commands.all;

import java.util.Optional;

import com.overload.game.World;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Position;
import com.overload.net.packet.impl.commands.Command;

public class Teletome extends Command {
	
	public void execute(Player player, String input) {
		String[] parts = input.split(" ");
		Optional<Player> player_ = World.getPlayerByName(parts[0]);
		player_.get().moveTo(new Position(player.getX(),player.getY(),player.getZ()));
	}

}
