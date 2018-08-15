package com.overload.net.packet.impl;

import com.overload.game.content.clan.ClanChatManager;
import com.overload.game.content.presets.Presetables;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.container.impl.Bank;
import com.overload.game.model.teleportation.TeleportHandler;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

public class InterfaceActionClickOpcode implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int interfaceId = packet.readInt();
		int action = packet.readByte();

		if (player == null || player.getHitpoints() <= 0 || player.isTeleporting()) {
			return;
		}

		if (Bank.handleButton(player, interfaceId, action)) {
			return;
		}
		
		if (ClanChatManager.handleButton(player, interfaceId, action)) {
			return;
		}
		
		if (Presetables.handleButton(player, interfaceId)) {
			return;
		}
		
		if (TeleportHandler.handleButton(player, interfaceId, action)) {
			return;
		}
	}
}
