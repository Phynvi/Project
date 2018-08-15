package com.overload.game.model.syntax.impl;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.syntax.EnterSyntax;

public class TransformIntoNPC implements EnterSyntax {

    @Override
    public void handleSyntax(Player player, String input) {
    	int id = Integer.parseInt(input);
		player.setNpcTransformationId(id);
		player.getPacketSender().sendInterfaceRemoval();
    }

    @Override
    public void handleSyntax(Player player, int input) {
		player.setNpcTransformationId(input);
		player.getPacketSender().sendInterfaceRemoval();
    }
}
