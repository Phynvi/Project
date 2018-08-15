package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class ItemContainerOption1 implements OutgoingPacket {

    int val1;
    int val2;
    int nodeId;

    public ItemContainerOption1(int val1, int val2, int nodeId) {
        this.val1 = val1;
        this.val2 = val2;
        this.nodeId = nodeId;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(145);
        buf.putInt(val1);
        buf.putUnsignedWordA(val2);
        buf.putUnsignedWordA(nodeId);
        return buf;
    }
}
