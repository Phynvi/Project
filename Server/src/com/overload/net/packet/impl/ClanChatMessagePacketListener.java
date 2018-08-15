package com.overload.net.packet.impl;

import com.overload.game.content.clan.ClanChatManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

public class ClanChatMessagePacketListener implements PacketListener {

    @SuppressWarnings("unused")
	@Override
    public void handleMessage(Player player, Packet packet) {
        String crowns = packet.readString();
        String message = packet.readString();

        ClanChatManager.sendMessage(player, message);
    }

}
