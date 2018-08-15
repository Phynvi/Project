package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class ItemOnItem implements OutgoingPacket {

    int slot;
    int nodeId;
    int interfaceId;
    int anInt1285;
    int anInt1283;
    int anInt1284;


    public ItemOnItem(int slot, int anInt1283, int nodeId, int anInt1284, int anInt1285, int interfaceId) {
        this.nodeId = nodeId;
        this.slot = slot;
        this.interfaceId = interfaceId;
        this.anInt1283 = anInt1283;
        this.anInt1284 = anInt1284;
        this.anInt1285 = anInt1285;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(53);
        buf.putShort(slot);
        buf.putUnsignedWordA(anInt1283);
        buf.putSignedBigEndian(nodeId);
        buf.putShort(anInt1284);
        buf.putUnsignedWordBigEndian(anInt1285);
        buf.putShort(interfaceId);
        return buf;
    }
}
