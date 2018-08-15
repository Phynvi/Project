package com.overload.net.packet.impl;

import com.overload.game.World;
import com.overload.game.content.combat.magic.CombatSpell;
import com.overload.game.content.combat.magic.CombatSpells;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

public class MagicOnPlayerPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int playerIndex = packet.readShortA();

        if (player == null || player.getHitpoints() <= 0) {
            return;
        }

        if (playerIndex < 0 || playerIndex > World.getPlayers().capacity())
            return;
        int spellId = packet.readLEShort();
        if (spellId < 0) {
            return;
        }

        Player attacked = World.getPlayers().get(playerIndex);

        if (attacked == null || attacked.equals(player)) {
            player.getMovementQueue().reset();
            return;
        }


        if (attacked.getHitpoints() <= 0) {
            player.getMovementQueue().reset();
            return;
        }

        CombatSpell spell = CombatSpells.getCombatSpell(spellId);

        if (spell == null) {
            player.getMovementQueue().reset();
            return;
        }

        player.setPositionToFace(attacked.getPosition());
        player.getCombat().setCastSpell(spell);

        player.getCombat().attack(attacked);
    }

}