package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.container.shop.ShopManager;
import com.overload.net.packet.impl.commands.Command;

public class Shop extends Command {

	@Override
	public void execute(Player player, String input) {
		ShopManager.open(player, Integer.parseInt(input));
		player.getPacketSender().sendMessage("Opening Shop: " + Integer.parseInt(input));
	}

}
