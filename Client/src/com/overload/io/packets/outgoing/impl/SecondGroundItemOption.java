package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class SecondGroundItemOption implements OutgoingPacket {

    int nodeId;
    int val1;
    int val2;

    public SecondGroundItemOption(int val1, int nodeId, int val2) {
        this.val1 = val1;
        this.nodeId = nodeId;
        this.val2 = val2;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(235);
        buf.putUnsignedWordBigEndian(val1);
        buf.putShort(nodeId);
        buf.putUnsignedWordBigEndian(val2);
        return buf;
    }
}
