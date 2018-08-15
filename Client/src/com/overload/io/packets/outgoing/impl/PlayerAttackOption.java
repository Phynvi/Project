package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class PlayerAttackOption implements OutgoingPacket {

    int nodeId;

    public PlayerAttackOption(int nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(153);
        buf.putUnsignedWordBigEndian(nodeId);
        return buf;
    }
}
