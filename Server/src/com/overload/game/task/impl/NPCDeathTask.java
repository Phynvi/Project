package com.overload.game.task.impl;

import com.overload.game.World;
import com.overload.game.content.minigames.warriors_guild.AnimatedArmour;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.npc.NPCDropGenerator;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.PlayerModes;
import com.overload.game.model.Position;
import com.overload.game.model.Priority;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;

import java.util.Optional;

/**
 * Represents an npc's death task, which handles everything an npc does before
 * and after their death animation (including it), such as dropping their drop
 * table items.
 *
 * @author relex lawl
 */

public class NPCDeathTask extends Task {

    /**
     * The npc setting off the death task.
     */
    private final NPC npc;
    /**
     * The amount of ticks on the task.
     */
    private int ticks = 2;
    /**
     * If the NPC was soloed
     */
    private boolean solo = false;
    /**
     * The player who killed the NPC
     */
    private Optional<Player> killer = Optional.empty();

    /**
     * The NPCDeathTask constructor.
     *
     * @param npc The npc being killed.
     */
    public NPCDeathTask(NPC npc) {
        super(2);
        this.npc = npc;
        this.ticks = 2;
    }

    @Override
    public void execute() {
        try {
            switch (ticks) {
                case 2:
                    // Reset and disable movement queue..
                    npc.getMovementQueue().setBlockMovement(true).reset();
                    
                    //Get if the npc was solo killed (For Ironman Accounts)
                    solo = npc.getCombat().wasSolo();
                    
                    // Get the {@link Player} who killed us..
                    killer = npc.getCombat().getKiller(false);

                    // Start death animation..
                    npc.performAnimation(new Animation(npc.getDefinition().getDeathAnim(), Priority.HIGH));

                    // Reset combat..
                    npc.getCombat().reset();

                    // Reset interacting entity..
                    npc.setEntityInteraction(null);
                    break;
                case 0:
                    if (killer.isPresent()) {
                        Player player = killer.get();
                        
                        boolean correctPlayer = false;
                        while (!correctPlayer) {
                        	if (player.getMode().isEqualOrHigher(PlayerModes.IRON_MAN) && !solo) {
                    			player.getPacketSender().sendMessage("@dre@Unfortunately you did not solo this NPC resulting in no loot.");
                    			npc.getCombat().removePlayerDamage(player);
                        	} else {
                        		correctPlayer = true;
                        	}
                        }
                		npc.getCombat().clearDamage();
                        
                        Position lootPos = npc.getPosition();
                        
                        if (player.getArea() != null) {
                            player.getArea().defeated(player, npc);
                            lootPos = player.getArea().getLootDropPosition() != null ? player.getArea().getLootDropPosition() : lootPos;
                        }

                        player.getFightCave().handleNPCDeath(npc);
                        
                        AnimatedArmour.dropTokens(player, npc.getId(), npc.getX(), npc.getY());
                        
                        player.getSlayer().killTaskMonster(npc);

                        // Drop loot for the killer..
                        NPCDropGenerator.start(player, npc, Optional.of(lootPos));
                    }
                    stop();
                    break;
            }
            ticks--;
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    @Override
    public void stop() {
        // Stop event.
        setEventRunning(false);

        // Remove from area..
        if (npc.getArea() != null) {
            npc.getArea().leave(npc);
            npc.setArea(null);
        }

        // Flag that we are no longer dying.
        npc.setDying(false);

        // Handle respawn..
        if (npc.getDefinition().getRespawn() > 0) {
            TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawn()));
        }

        // Add us to the global remove list.
        World.getRemoveNPCQueue().add(npc);
    }
}