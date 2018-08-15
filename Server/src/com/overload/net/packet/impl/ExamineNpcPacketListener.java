package com.overload.net.packet.impl;

import com.overload.game.definition.NpcDefinition;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

public class ExamineNpcPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int npcId = packet.readShort();
        
        if (npcId <= 0) {
            return;
        }

        NpcDefinition npcDef = NpcDefinition.forId(npcId);
        if (npcDef != null) {
            player.getPacketSender().sendMessage(npcDef.getExamine());
        }
    }

}
