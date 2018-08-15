package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class ItemOnNpc implements OutgoingPacket {

    int anInt1285;
    int nodeId;
    int anInt1283;
    int anInt1284;

    public ItemOnNpc(int anInt1285, int nodeId, int anInt1283, int anInt1284) {
        this.anInt1285 = anInt1285;
        this.nodeId = nodeId;
        this.anInt1283 = anInt1283;
        this.anInt1284 = anInt1284;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(57);
        buf.putUnsignedWordA(anInt1285);
        buf.putUnsignedWordA(nodeId);
        buf.putUnsignedWordBigEndian(anInt1283);
        buf.putUnsignedWordA(anInt1284);
        return buf;
    }
}
