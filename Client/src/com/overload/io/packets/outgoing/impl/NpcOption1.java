package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class NpcOption1 implements OutgoingPacket {

    int nodeId;

    public NpcOption1(int nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(155);
        buf.putUnsignedWordBigEndian(nodeId);
        return buf;
    }
}
