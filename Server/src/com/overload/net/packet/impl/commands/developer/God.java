package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.entity.updating.PlayerUpdating;
import com.overload.net.packet.impl.commands.Command;

public class God extends Command {

	@Override
	public void execute(Player player, String input) {
		player.setInvulnerable(!player.isInvulnerable());
		player.getPacketSender().sendMessage("You are " + (player.isInvulnerable() ? "now Invunerable." : "no longer Invunerable."));
		PlayerUpdating.update(player, true);
	}
}