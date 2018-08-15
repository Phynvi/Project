package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class FinalizedRegionChange implements OutgoingPacket {

	@Override
    public PacketBuilder create() {
		return new PacketBuilder(121);
    }


}
