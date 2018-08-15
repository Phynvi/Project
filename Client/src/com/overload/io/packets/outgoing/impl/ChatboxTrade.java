package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class ChatboxTrade implements OutgoingPacket {

    int plr;

    public ChatboxTrade(int plr) {
        this.plr = plr;
    }

    @Override
    public PacketBuilder create() {
    	return new PacketBuilder(139).putUnsignedWordBigEndian(plr);
    }
}
