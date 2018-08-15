package com.overload.net.packet.impl;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

public class CloseInterfacePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        player.getPacketSender().sendInterfaceRemoval();
    }
}
