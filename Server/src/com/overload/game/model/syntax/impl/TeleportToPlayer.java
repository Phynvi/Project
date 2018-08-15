package com.overload.game.model.syntax.impl;

import java.util.Optional;

import com.overload.game.World;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.syntax.EnterSyntax;

public class TeleportToPlayer implements EnterSyntax {

    @Override
    public void handleSyntax(Player player, String input) {
    	Optional<Player> optionalPlayer = World.getPlayerByName(input);
    	if (optionalPlayer.isPresent()) {
    		player.moveTo(optionalPlayer.get().getPosition());
    	}
		player.getPacketSender().sendInterfaceRemoval();
    }

    @Override
    public void handleSyntax(Player player, int input) {

    }
}
