package com.overload.net.packet.impl;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

public class PlayerInactivePacketListener implements PacketListener {

    //CALLED EVERY 3 MINUTES OF INACTIVITY

    @Override
    public void handleMessage(Player player, Packet packet) {
    }
}
