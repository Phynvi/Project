package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class AddFriend implements OutgoingPacket {

    private long friend;

    public AddFriend(long friend) {
        this.friend = friend;
    }

    @Override
    public PacketBuilder create() {
    	return new PacketBuilder(188).putLong(friend);
    }


}
