package com.overload.net.packet.impl;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

/**
 * This {@link PacketListener} receives the client's current plane
 * and compares it to the player's server-sided one.
 * 
 * If they do not match, we will manually send the proper plane
 * to the client.
 * 
 * This fixed the exploit where players would use third-party softwares
 * to teleport to different planes.
 * 
 * @author Gabriel Hannason
 */

public class HeightCheckPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int plane = packet.readByte();
        
        if (player.getPosition().getZ() >= 0 && player.getPosition().getZ() < 4) {
            if (plane != player.getPosition().getZ()) {
                if (player.getMovementQueue().canMove()) {
                    player.getMovementQueue().reset();
                    player.setNeedsPlacement(true); 
                    player.getPacketSender().sendHeight();
                    player.getPacketSender().sendInterfaceRemoval();
                    player.setWalkToTask(null);
                }
            }
        }
    }
}
