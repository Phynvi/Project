package com.overload.net.codec;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.NetworkConstants;
import com.overload.net.PlayerSession;
import com.overload.net.packet.Packet;
import com.overload.net.security.IsaacRandom;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Decodes packets that are received from the player's channel.
 *
 * @author Swiffy
 */
public final class PacketDecoder extends ByteToMessageDecoder {

	private final IsaacRandom random;

	public PacketDecoder(IsaacRandom random) {
		this.random = random;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {

		if (!buffer.isReadable()) {
			return;
		}

		PlayerSession session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
		if (session == null || session.getPlayer() == null) {
			buffer.discardReadBytes();
			return;
		}

		Player player = session.getPlayer();

		if (player.packetOpcode == -1) {
			if (buffer.isReadable()) {
				player.packetOpcode = buffer.readUnsignedByte();
				player.packetOpcode = (player.packetOpcode - random.nextInt()) & 0xFF;
			} else {
				buffer.discardReadBytes();
				return;
			}
		}

		if (player.packetSize == -1) {
			if (buffer.isReadable()) {
				player.packetSize = buffer.readUnsignedByte();
			} else {
				buffer.discardReadBytes();
				return;
			}
		}

		if (buffer.isReadable(player.packetSize)) {
			byte[] data = new byte[player.packetSize];
			buffer.readBytes(data);
			out.add(new Packet(player.packetOpcode, Unpooled.copiedBuffer(data)));
			player.packetOpcode = -1;
			player.packetSize = -1;
		}
	}
}
