package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class ObjectOption3 implements OutgoingPacket {

    int id;
    int val1;
    int val2;

    public ObjectOption3(int val1, int val2, int id) {
        this.id = id;
        this.val1 = val1;
        this.val2 = val2;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(70);
        buf.putUnsignedWordBigEndian(val1);
        buf.putShort(val2);
        buf.putSignedBigEndian(id);
        return buf;
    }
}
