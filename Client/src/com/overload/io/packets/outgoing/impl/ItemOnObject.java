package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class ItemOnObject implements OutgoingPacket {

    int anInt1285;
    int anInt1283;
    int anInt1284;
    int id;
    int val1;
    int val2;

    public ItemOnObject(int anInt1284, int id, int val1, int anInt1283, int val2, int anInt1285) {
        this.id = id;
        this.val1 = val1;
        this.val2 = val2;
        this.anInt1283 = anInt1283;
        this.anInt1284 = anInt1284;
        this.anInt1285 = anInt1285;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(192);
        buf.putShort(anInt1284);
        buf.putShort(id);
        buf.putSignedBigEndian(val1);
        buf.putUnsignedWordBigEndian(anInt1283);
        buf.putSignedBigEndian(val2);
        buf.putShort(anInt1285);
        return buf;
    }
}
