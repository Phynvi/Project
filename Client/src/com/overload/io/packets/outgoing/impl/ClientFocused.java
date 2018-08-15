package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class ClientFocused implements OutgoingPacket {

    private boolean focused;

    public ClientFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public PacketBuilder create() {
    	return new PacketBuilder(3).putByte(focused ? 1 : 0);
    }


}
