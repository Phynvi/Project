package com.overload.game.task.impl;

import com.overload.game.World;
import com.overload.game.content.skill.hunter.Hunter;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.task.Task;

/**
 * A {@link Task} implementation which handles the respawn of an npc.
 *
 * @author Professor Oak
 */
public class NPCRespawnTask extends Task {

    /**
     * The {@link NPC} which is going to respawn.
     */
    private final NPC npc;

    public NPCRespawnTask(NPC npc, int ticks) {
        super(ticks);
        this.npc = npc;
    }

    @Override
    public void execute() {
        if(npc.getId() > 5548 && npc.getId() < 5552) {
            Hunter.HUNTER_NPC_LIST.add(npc);
        }
        World.getAddNPCQueue().add(npc);

        npc.getMovementQueue().setBlockMovement(false).reset();

        // Stop the task
        stop();
    }
}
