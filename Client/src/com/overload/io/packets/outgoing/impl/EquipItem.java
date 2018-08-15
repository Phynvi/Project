package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class EquipItem implements OutgoingPacket {

    int slot;
    int interfaceId;
    int nodeId;

    public EquipItem(int nodeId, int slot, int interfaceId) {
        this.slot = slot;
        this.interfaceId = interfaceId;
        this.nodeId = nodeId;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(41);
        buf.putShort(nodeId);
        buf.putUnsignedWordA(slot);
        buf.putUnsignedWordA(interfaceId);
        return buf;
    }
}
