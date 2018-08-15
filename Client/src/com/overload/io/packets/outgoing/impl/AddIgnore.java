package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class AddIgnore implements OutgoingPacket {

	private long friend;

	public AddIgnore(long ignore) {
		this.friend = ignore;
	}

	@Override
	public PacketBuilder create() {
		return new PacketBuilder(133).putLong(friend);
	}

}
