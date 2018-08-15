package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class MagicOnPlayer implements OutgoingPacket {

    int nodeId;
    int selectedSpellId;

    public MagicOnPlayer(int nodeId, int selectedSpellId) {
        this.nodeId = nodeId;
        this.selectedSpellId = selectedSpellId;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(249);
        buf.putUnsignedWordA(nodeId);
        buf.putUnsignedWordBigEndian(selectedSpellId);
        return buf;
    }


}
