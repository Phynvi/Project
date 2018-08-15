package com.overload.net.packet.impl;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.ByteBufUtils;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketConstants;
import com.overload.net.packet.PacketListener;

/**
 * This packet manages the input taken from chat box interfaces that allow
 * input, such as withdraw x, bank x, enter name of friend, etc.
 *
 * @author Gabriel Hannason
 */

public class EnterInputPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {

        if (player == null || player.getHitpoints() <= 0) {
            return;
        }

        switch (packet.getOpcode()) {
            case PacketConstants.ENTER_SYNTAX_OPCODE:
                String name = ByteBufUtils.readString(packet.getBuffer());
                if (name == null)
                    return;
                if (player.getEnterSyntax() != null) {
                    player.getEnterSyntax().handleSyntax(player, name);
                }
                break;
            case PacketConstants.ENTER_AMOUNT_OPCODE:
                int amount = packet.readInt();
                if (amount <= 0)
                    return;
                if (player.getEnterSyntax() != null) {
                    player.getEnterSyntax().handleSyntax(player, amount);
                }
                player.setEnterSyntax(null);
                break;
        }
    }
}
