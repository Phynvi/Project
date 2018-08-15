package com.overload.game.model.syntax.impl;

import com.overload.game.World;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.syntax.EnterSyntax;

public class SpawnAggressiveNPC implements EnterSyntax {

    @Override
    public void handleSyntax(Player player, String input) {
    	int id = Integer.parseInt(input);
		NPC npc = new NPC(id, player.getPosition().clone());
		npc.getDefinition().setAggressive(true);
		World.getAddNPCQueue().add(npc);
		player.getPacketSender().sendInterfaceRemoval();
    }

    @Override
    public void handleSyntax(Player player, int input) {
		NPC npc = new NPC(input, player.getPosition().clone());
		npc.getDefinition().setAggressive(true);
		World.getAddNPCQueue().add(npc);
		player.getPacketSender().sendInterfaceRemoval();
    }
}
