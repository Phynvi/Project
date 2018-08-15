package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class NpcOption2 implements OutgoingPacket {

    int nodeId;

    public NpcOption2(int nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(17);
        buf.putSignedBigEndian(nodeId);
        return buf;
    }
}
