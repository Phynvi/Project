package com.overload.net.packet.impl;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

/**
 * Represents an unused packet listener.
 */

public class UnusedPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {

    }
}
