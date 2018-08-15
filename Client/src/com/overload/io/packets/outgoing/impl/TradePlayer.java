package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class TradePlayer implements OutgoingPacket {

    int nodeId;

    public TradePlayer(int nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(139);
        buf.putUnsignedWordBigEndian(nodeId);
        return buf;
    }
}
