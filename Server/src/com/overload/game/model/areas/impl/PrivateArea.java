package com.overload.game.model.areas.impl;

import com.overload.game.World;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Boundary;
import com.overload.game.model.areas.Area;

import java.util.ArrayList;
import java.util.List;

public abstract class PrivateArea extends Area {

    public final List<Player> players;
    public final List<NPC> npcs;

    public PrivateArea(List<Boundary> boundaries) {
        super(boundaries);
        players = new ArrayList<>();
        npcs = new ArrayList<>();
    }

    @Override
    public void leave(Character character) {
        remove(character);

        if (players.isEmpty()) {
            destroy();
        }
    }

    @Override
    public void enter(Character character) {
        add(character);
    }

    public void remove(Character character) {
        if (character.isPlayer()) {
            players.removeIf(p -> character.getAsPlayer().equals(p));
        } else {
            npcs.removeIf(n -> character.getAsNpc().equals(n));
        }
        character.setArea(null);
    }

    public void add(Character character) {
        if (character.isPlayer()) {
            Player p = character.getAsPlayer();
            if (!players.contains(p)) {
                players.add(p);
            }
        } else {
            NPC n = character.getAsNpc();
            if (!npcs.contains(n)) {
                npcs.add(n);
            }
        }
        character.setArea(this);
    }

    public void destroy() {
        for (NPC npc : npcs) {
            if (npc.isRegistered()) {
                World.getRemoveNPCQueue().add(npc);
            }
        }
        npcs.clear();
        players.clear();
    }
}
