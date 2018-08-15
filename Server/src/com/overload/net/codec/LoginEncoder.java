package com.overload.net.codec;

import com.overload.net.login.LoginResponsePacket;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Encodes login.
 *
 * @author Swiffy
 */
public final class LoginEncoder extends MessageToByteEncoder<LoginResponsePacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, LoginResponsePacket msg, ByteBuf out)
            throws Exception {

        out.writeByte(msg.getResponse());

        if (msg.getResponse() == LoginResponse.NORMAL.getOpcode()) {
            out.writeByte(msg.getRights().ordinal());
            out.writeByte(msg.getModes().ordinal());
        }
    }
}
