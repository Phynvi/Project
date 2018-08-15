package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class SpawnTabClick implements OutgoingPacket {

    private int item;
    private boolean spawnX;
    private boolean toBank;

    public SpawnTabClick(int item, boolean spawnX, boolean toBank) {
        this.item = item;
        this.spawnX = spawnX;
        this.toBank = toBank;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(187);
        buf.putInt(item);
        buf.putByte(spawnX ? 1 : 0);
        buf.putByte(toBank ? 1 : 0);
        return buf;
    }

}
