package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class PrivateMessage implements OutgoingPacket {

    private long friend;
    private String msg;

    public PrivateMessage(long friend, String msg) {
        this.friend = friend;
        this.msg = msg;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(126);
        buf.putLong(friend);
        buf.putString(msg);
        return buf;
    }


}
