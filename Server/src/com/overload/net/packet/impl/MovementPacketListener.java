package com.overload.net.packet.impl;

import com.overload.game.content.Dueling.DuelRule;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Position;
import com.overload.game.model.dialogue.DialogueManager;
import com.overload.game.model.rights.Right;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketConstants;
import com.overload.net.packet.PacketListener;
import com.overload.util.timers.TimerKey;

/**
 * This packet listener is called when a player has clicked on either the
 * mini-map or the actual game map to move around.
 *
 * @author Gabriel Hannason
 */
public class MovementPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int size = packet.getSize();

        if (player.getHitpoints() <= 0) {
            return;
        }
        
        /*
		 * if (packet.getOpcode() == 248) size -= 14;
		 */
        player.getCombat().setCastSpell(null);
        player.getCombat().reset();
        player.getSkillManager().stopSkillable();
        player.setWalkToTask(null);

        if (packet.getOpcode() != PacketConstants.COMMAND_MOVEMENT_OPCODE) {

        }

        if (!checkReqs(player, packet.getOpcode())) {
            return;
        }

        // Close all interfaces except for the floating
        // world map.
        if (player.getInterfaceId() != 54000) {
            player.getPacketSender().sendInterfaceRemoval();
        }

        final int steps = (size - 5) / 2;
        if (steps < 0) {
            return;
        }
        boolean shiftTeleport = packet.readByte() == 1;
        final int firstStepX = packet.readLEShortA();
        final int[][] path = new int[steps][2];
        for (int i = 0; i < steps; i++) {
            path[i][0] = packet.readByte();
            path[i][1] = packet.readByte();
        }
        final int firstStepY = packet.readLEShort();
        final Position[] positions = new Position[steps + 1];
        positions[0] = new Position(firstStepX, firstStepY, player.getPosition().getZ());
        for (int i = 0; i < steps; i++) {
            positions[i + 1] = new Position(path[i][0] + firstStepX, path[i][1] + firstStepY,
                    player.getPosition().getZ());
        }

        // Get the ending position..
        Position end = positions[positions.length - 1];

        
        
        // Validate shift teleport..
        if (shiftTeleport) {
    		if (!player.getRights().isOrInherits(Right.MANAGER)) {
                shiftTeleport = false;
            }
        }

        if (!shiftTeleport) {
            // Validate positions.
            if (player.getPosition().getDistance(end) >= 64) {
                return;
            }

            // Add walking points to movement queue..
            if (player.getMovementQueue().addFirstStep(positions[0])) {
                for (int i = 1; i < positions.length; i++) {
                    player.getMovementQueue().addStep(positions[i]);
                }
            }
        } else {
            player.moveTo(end);
        }
    }

    public boolean checkReqs(Player player, int opcode) {
        if (player.getTimers().has(TimerKey.FREEZE)) {
        	player.getPacketSender().sendMessage("A magical spell has made you unable to move.");
            return false;
        }

        if (!player.getTrading().getButtonDelay().finished() || !player.getDueling().getButtonDelay().finished()) {
            player.getPacketSender().sendMessage("You cannot do that right now.");
            return false;
        }

        // Duel, disabled movement?
        if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_MOVEMENT.ordinal()]) {
            if (opcode != PacketConstants.COMMAND_MOVEMENT_OPCODE) {
                DialogueManager.sendStatement(player, "Movement has been disabled in this duel!");
            }
            return false;
        }

        // Stun
        if (player.getTimers().has(TimerKey.STUN)) {
            player.getPacketSender().sendMessage("You're stunned!");
            return false;
        }

        if (player.isNeedsPlacement() || player.getMovementQueue().isMovementBlocked()) {
            return false;
        }
        
        return true;
    }
}
