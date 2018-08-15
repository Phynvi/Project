package com.overload.net.packet.impl;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

/**
 * Represents a packet used for handling dialogues. This specific packet
 * currently handles the action for clicking the "next" option during a
 * dialogue.
 *
 * @author Professor Oak
 */

public class DialoguePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {

		if (player == null || player.getHitpoints() <= 0) {
			return;
		}

        DialogueHandler.proceed(player);

		//DialogueManager.next(player);
	}
}
