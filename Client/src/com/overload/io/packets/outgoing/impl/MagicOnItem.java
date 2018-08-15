package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class MagicOnItem implements OutgoingPacket {

    int slot;
    int nodeId;
    int interfaceId;
    int selectedSpellId;

    public MagicOnItem(int slot, int nodeId, int interfaceId, int selectedSpellId) {
        this.slot = slot;
        this.nodeId = nodeId;
        this.interfaceId = interfaceId;
        this.selectedSpellId = selectedSpellId;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(237);
        buf.putShort(slot);
        buf.putUnsignedWordA(nodeId);
        buf.putShort(interfaceId);
        buf.putUnsignedWordA(selectedSpellId);
        return buf;
    }


}
