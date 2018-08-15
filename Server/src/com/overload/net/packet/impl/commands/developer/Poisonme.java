package com.overload.net.packet.impl.commands.developer;

import com.overload.game.content.combat.CombatFactory;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.task.impl.CombatPoisonEffect.PoisonType;
import com.overload.net.packet.impl.commands.Command;

public class Poisonme extends Command {

	@Override
	public void execute(Player player, String input) {
		CombatFactory.poisonEntity(player, PoisonType.valueOf(input.toUpperCase()));
	}

}
