package com.overload.io.packets.outgoing.impl;

import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class SendSyntax implements OutgoingPacket {

    private String chat;

    public SendSyntax(String chat) {
        this.chat = chat;
    }

    @Override
    public PacketBuilder create() {
    	PacketBuilder buf = new PacketBuilder(60);
        buf.putString(chat);
        return buf;
    }


}
