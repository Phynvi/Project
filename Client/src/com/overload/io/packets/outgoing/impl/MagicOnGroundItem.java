package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class MagicOnGroundItem implements OutgoingPacket {

    int val1;
    int nodeId;
    int val2;
    int selectedSpellId;

    public MagicOnGroundItem(int val1, int nodeId, int val2, int selectedSpellId) {
        this.val1 = val1;
        this.nodeId = nodeId;
        this.val2 = val2;
        this.selectedSpellId = selectedSpellId;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(181);
        buf.putUnsignedWordBigEndian(val1);
        buf.putShort(nodeId);
        buf.putUnsignedWordBigEndian(val2);
        buf.putUnsignedWordA(selectedSpellId);
        return buf;
    }


}
