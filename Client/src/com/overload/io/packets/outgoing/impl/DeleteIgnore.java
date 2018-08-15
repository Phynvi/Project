package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class DeleteIgnore implements OutgoingPacket {

	private long ignore;

	public DeleteIgnore(long ignore) {
		this.ignore = ignore;
	}

	@Override
	public PacketBuilder create() {
		return new PacketBuilder(74).putLong(ignore);
	}

}
