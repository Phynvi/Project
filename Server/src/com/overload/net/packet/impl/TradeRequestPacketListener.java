package com.overload.net.packet.impl;

import com.overload.game.World;
import com.overload.game.entity.Entity;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Action;
import com.overload.game.model.PlayerModes;
import com.overload.game.model.PlayerStatus;
import com.overload.game.model.movement.WalkToAction;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;


public class TradeRequestPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int index = packet.readLEShort();
        if (index > World.getPlayers().capacity() || index < 0) {
            return;
        }

        Player player2 = World.getPlayers().get(index);

        if (player == null
                || player.getHitpoints() <= 0
                || !player.isRegistered()
                || player2 == null || player2.getHitpoints() <= 0
                || !player2.isRegistered()) {
            player.getMovementQueue().reset();
            return;
        }

        player.getMovementQueue().follow(player2);
        player.setWalkToTask(new WalkToAction(player, player2, 1, new Action() {
            @Override
            public void execute() {
                if (player.getMovementQueue().isFollowing(player2)) {
                    player.getMovementQueue().resetFollowing();
                    player.setEntityInteraction(null);
                }

                if (player.busy()) {
                    player.getPacketSender().sendMessage("You cannot do that right now.");
                    return;
                }

                if (player2.busy()) {
                    String msg = "That player is currently busy.";

                    if (player2.getStatus() == PlayerStatus.TRADING) {
                        msg = "That player is currently trading with someone else.";
                    }

                    player.getPacketSender().sendMessage(msg);
                    return;
                }

                if (player.getArea() != null) {
                    if (!player.getArea().canTrade(player, player2)) {
                        player.getPacketSender().sendMessage("You cannot trade here.");
                        return;
                    }
                }
                
                if (player.getMode().isEqualOrHigher(PlayerModes.IRON_MAN)) {
                    player.getPacketSender().sendMessage("Your game mode prevents you from trading.");
                    return;
                }
                
                if (player2.getMode().isEqualOrHigher(PlayerModes.IRON_MAN)) {
                    player.getPacketSender().sendMessage("Their game mode prevents you from trading.");
                    return;
                }

                if (player.getLocalPlayers().contains(player2)) {
                    player.getTrading().requestTrade(player2);
                }
            }

            @Override
            public void execute(Entity entity) {}
        }));

    }
}
