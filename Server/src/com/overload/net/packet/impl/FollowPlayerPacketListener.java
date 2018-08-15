package com.overload.net.packet.impl;

import com.overload.game.World;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

/**
 * Handles the follow player packet listener Sets the player to follow when the
 * packet is executed
 *
 * @author Gabriel Hannason
 */
public class FollowPlayerPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {

        if (player.busy()) {
            return;
        }

        int otherPlayersIndex = packet.readLEShort();
        if (otherPlayersIndex < 0 || otherPlayersIndex > World.getPlayers().capacity())
            return;
        Player leader = World.getPlayers().get(otherPlayersIndex);
        if (leader == null)
            return;
        player.setEntityInteraction(leader);
        player.getMovementQueue().follow(leader);
    }

}
