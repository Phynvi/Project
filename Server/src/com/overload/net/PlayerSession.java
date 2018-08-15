package com.overload.net;

import com.overload.game.World;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.codec.LoginResponse;
import com.overload.net.codec.PacketDecoder;
import com.overload.net.codec.PacketEncoder;
import com.overload.net.login.LoginDetailsMessage;
import com.overload.net.login.LoginEvaluated;
import com.overload.net.login.LoginResponsePacket;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketBuilder;
import com.overload.net.packet.PacketConstants;
import com.overload.net.packet.impl.UnusedPacketListener;
import com.overload.util.Misc;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The session handler dedicated to a player that will handle input and output
 * operations.
 *
 * @author Lare96
 * @author Swiffy
 * @editor Professor Oak
 */
public class PlayerSession {

    /**
     * The queue of packets that will be handled on the next sequence.
     */
    private final Queue<Packet> packetsQueue = new ConcurrentLinkedQueue<>();

    /**
     * The channel that will manage the connection for this player.
     */
    private final Channel channel;

    /**
     * The player I/O operations will be executed for.
     */
    private Player player;

    /**
     * Creates a new {@link PlayerSession}.
     *
     * @param channel The SocketChannel.
     */
    public PlayerSession(SocketChannel channel) {
        this.channel = channel;
        this.player = new Player(this);
    }

    /**
     * Attempts to finalize a player's login.
     *
     * @param msg The player's login information.
     */
    public void finalizeLogin(LoginDetailsMessage msg) {
        SocketChannel channel = (SocketChannel) msg.getContext().channel();

        // Update the player
        player.setUsername(msg.getUsername()).setLongUsername(Misc.stringToLong(msg.getUsername()))
                .setPassword(msg.getPassword()).setHostAddress(msg.getHost());

        // Get the response code
        int response = LoginEvaluated.evaluate(player, msg);

        // Write the response and flush the channel

        ChannelFuture future = channel.writeAndFlush(new LoginResponsePacket(response, player.getRights().getPrimary(),player.getMode()));
        // Close the channel after sending the response if it wasn't a successful login
        if (response != LoginResponse.NORMAL.getOpcode()) {
            future.addListener(ChannelFutureListener.CLOSE);
            return;
        }

        // Wait...
        future.awaitUninterruptibly();

        // Replace decoder/encoder to packets
        channel.pipeline().replace("encoder", "encoder", new PacketEncoder(msg.getEncryptor()));

        channel.pipeline().replace("decoder", "decoder", new PacketDecoder(msg.getDecryptor()));

        // Queue the login
        if (!World.getAddPlayerQueue().contains(player)) {
            World.getAddPlayerQueue().add(player);
        }

    }

    /**
     * Queues a recently decoded packet received from the channel.
     *
     * @param msg The packet that should be queued.
     */
    public void queuePacket(Packet msg) {
        int total_size = (packetsQueue.size());
        if (total_size >= NetworkConstants.PACKET_PROCESS_LIMIT) {
            return;
        }
        if (PacketConstants.PACKETS[msg.getOpcode()] instanceof UnusedPacketListener) {
        	return;
        }
        packetsQueue.add(msg);
    }

    /**
     * Processes all of the queued messages from the {@link PacketDecoder} by
     * polling the internal queue, and then handling them via the
     * handleInputMessage. This method is called EACH GAME CYCLE.
     *
     * @param priorityOnly Should only prioritized packets be read?
     */
    public void processPackets() {
        for (int i = 0; i < NetworkConstants.PACKET_PROCESS_LIMIT; i++) {
            Packet packet = packetsQueue.poll();
            if (packet == null) {
                break;
            }
            processPacket(packet);
        }
    }

    /**
     * Handles an incoming packet.
     *
     * @param packet The packet to handle.
     */
    public void processPacket(Packet packet) {
    	//player.sendMessage("Opcode: " + packet.getOpcode());
        try {
            PacketConstants.PACKETS[packet.getOpcode()].handleMessage(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            packet.getBuffer().release();
        }
    }

    /**
     * Queues the {@code msg} for this session to be encoded and sent to the client.
     *
     * @param builder the packet to queue.
     */
    public void write(PacketBuilder builder) {
        if (!channel.isOpen()) {
            return;
        }
        try {
            Packet packet = builder.toPacket();
            channel.write(packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Flushes this channel.
     */
    public void flush() {
        if (!channel.isOpen()) {
            return;
        }
        channel.flush();
    }

    /**
     * Gets the player I/O operations will be executed for.
     *
     * @return the player I/O operations.
     */
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Channel getChannel() {
        return channel;
    }
}
