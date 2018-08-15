package com.overload.net.packet.impl;

import com.overload.game.collision.RegionManager;
import com.overload.game.content.minigames.barrows.Barrows;
import com.overload.game.content.skill.farming.Farming;
import com.overload.game.entity.impl.grounditem.ItemOnGroundManager;
import com.overload.game.entity.impl.npc.NpcAggression;
import com.overload.game.entity.impl.object.ObjectManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;


public class RegionChangePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {       
        if (player.isAllowRegionChangePacket()) {
            RegionManager.loadMapFiles(player.getPosition().getX(), player.getPosition().getY());
            player.getPacketSender().deleteRegionalSpawns();
            ItemOnGroundManager.onRegionChange(player);
            ObjectManager.onRegionChange(player);
            Barrows.brotherDespawn(player);
            Farming.onRegionChange(player);
            player.getAggressionTolerance().start(NpcAggression.NPC_TOLERANCE_SECONDS); //Every 4 minutes, reset aggression for npcs in the region.
            player.setAllowRegionChangePacket(false);
        }
    }
}
