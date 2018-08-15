package com.overload.net.packet.impl.commands.developer;

import com.overload.game.World;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Npcs extends Command {

    @Override
    public void execute(Player player, String input) {
        String[] ids = input.split(" ");
        int start = Integer.parseInt(ids[0]);
        int end = Integer.parseInt(ids[1]);
        for (int i = start; i < end; i++) {
            NPC npc = new NPC(i, player.getPosition().clone());
            World.getAddNPCQueue().add(npc);
        }
    }

}
