package com.overload.net.packet.impl;

import com.overload.game.content.combat.CombatSpecial;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

/**
 * This packet listener handles the action when pressing
 * a special attack bar.
 *
 * @author Professor Oak
 */

public class SpecialAttackPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        @SuppressWarnings("unused")
        int specialBarButton = packet.readInt();

        if (player.getHitpoints() <= 0) {
            return;
        }

        CombatSpecial.activate(player);
    }
}
