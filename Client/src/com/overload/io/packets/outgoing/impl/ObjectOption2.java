package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class ObjectOption2 implements OutgoingPacket {

    int id;
    int val1;
    int val2;

    public ObjectOption2(int id, int val1, int val2) {
        this.id = id;
        this.val1 = val1;
        this.val2 = val2;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(252);
        buf.putSignedBigEndian(id);
        buf.putUnsignedWordBigEndian(val1);
        buf.putUnsignedWordA(val2);
        return buf;
    }
}
