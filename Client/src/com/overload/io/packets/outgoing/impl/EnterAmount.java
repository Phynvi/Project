package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class EnterAmount implements OutgoingPacket {

    private int amount;

    public EnterAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public PacketBuilder create() {
    	return new PacketBuilder(208).putInt(amount);
    }

}
