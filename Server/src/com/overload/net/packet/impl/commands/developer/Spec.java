package com.overload.net.packet.impl.commands.developer;

import com.overload.game.content.combat.CombatSpecial;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Spec extends Command {

	@Override
	public void execute(Player player, String input) {
		int amt = Integer.parseInt(input);
		player.setSpecialPercentage(amt);
		CombatSpecial.updateBar(player);
	}

}
