package com.overload.game.content.dialogues.impl;

import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;

public abstract class Dialogue {
    public abstract void execute(Player player, NPC npc);
    public abstract boolean handleActions(Player player, NPC npc);
    
    public abstract boolean handleTwo(Player player, NPC npc);
    public abstract boolean handleThree(Player player, NPC npc);
    public abstract boolean handleFour(Player player, NPC npc);
    public abstract boolean handleFive(Player player, NPC npc);
}
