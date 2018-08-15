package com.overload.game;

import com.overload.Server;
import com.overload.game.entity.impl.CharacterList;
import com.overload.game.entity.impl.grounditem.ItemOnGround;
import com.overload.game.entity.impl.grounditem.ItemOnGroundManager;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.entity.impl.player.PlayerSaving;
import com.overload.game.entity.updating.NPCUpdating;
import com.overload.game.entity.updating.PlayerUpdating;
import com.overload.game.entity.updating.sync.GameSyncExecutor;
import com.overload.game.entity.updating.sync.GameSyncTask;
import com.overload.game.model.NodeType;
import com.overload.game.model.rights.Right;
import com.overload.game.task.TaskManager;
import com.overload.util.Misc;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents the world, processing it and its characters.
 *
 * @author Professor Oak
 * @author lare96
 */
public class World {

    /**
     * The collection of active {@link Player}s.
     */
    private static CharacterList<Player> players = new CharacterList<>(10047);

    /**
     * The collection of active {@link NPC}s.
     */
    private static CharacterList<NPC> npcs = new CharacterList<>(5000);

    /**
     * The collection of active {@link ItemOnGround}s..
     */
    private static List<ItemOnGround> items = new LinkedList<>();

    /**
     * The collection of active {@link GameObject}s..
     */
    private static List<GameObject> objects = new LinkedList<>();

    /**
     * The collection of {@link Players}s waiting to be added to the game.
     */
    private static Queue<Player> addPlayerQueue = new ConcurrentLinkedQueue<>();

    /**
     * The collection of {@link Players}s waiting to be removed from the game.
     */
    private static Queue<Player> removePlayerQueue = new ConcurrentLinkedQueue<>();

    /**
     * The collection of {@link Players}s waiting to be added to the game.
     */
    private static Queue<NPC> addNPCQueue = new ConcurrentLinkedQueue<>();

    /**
     * The collection of {@link Players}s waiting to be removed from the game.
     */
    private static Queue<NPC> removeNPCQueue = new ConcurrentLinkedQueue<>();

    /**
     * The manager for game synchronization.
     */
    private static GameSyncExecutor executor = new GameSyncExecutor();

    /**
     * Processes the world.
     */
    public static void process() {
        // Process all active {@link Task}s..
        TaskManager.process();

        ItemOnGroundManager.process();
        
        // Add pending players..
        for (int i = 0; i < GameConstants.QUEUED_LOOP_THRESHOLD; i++) {
            Player player = addPlayerQueue.poll();
            if (player == null)
                break;
            // Kick any copies before adding the new player
            World.getPlayerByName(player.getUsername()).ifPresent(e -> e.requestLogout());
            getPlayers().add(player);
        }

        // Deregister queued players.
        int amount = 0;
        Iterator<Player> $it = removePlayerQueue.iterator();
        while ($it.hasNext()) {
            Player player = $it.next();
            if (player == null || amount >= GameConstants.QUEUED_LOOP_THRESHOLD) {
                break;
            }
            if (player.canLogout() || player.getForcedLogoutTimer().finished() || Server.isUpdating()) {
                getPlayers().remove(player);
                $it.remove();
            }
            amount++;
        }

        // Add pending Npcs..
        for (int i = 0; i < GameConstants.QUEUED_LOOP_THRESHOLD; i++) {
            NPC npc = addNPCQueue.poll();
            if (npc != null) {
            	NPC n = new NPC(npc.getId(), npc.getPosition());
            	n.getMovementCoordinator().setRadius(npc.getMovementCoordinator().getRadius());
                n.setFace(npc.getFace());
            	npcs.add(n);
                addNPCQueue.remove(npc);
            }
        }

        // Removing pending npcs..
        for (int i = 0; i < GameConstants.QUEUED_LOOP_THRESHOLD; i++) {
            NPC npc = removeNPCQueue.poll();
            if (npc != null) {
                npcs.remove(npc);
                removeNPCQueue.remove(npc);
            }
        }

        // Handle synchronization tasks.
        executor.sync(new GameSyncTask(NodeType.PLAYER, false) {
            @Override
            public void execute(int index) {
                Player player = players.get(index);
                try {
                    player.process();
                } catch (Exception e) {
                    e.printStackTrace();
                    player.requestLogout();
                }
            }
        });

        executor.sync(new GameSyncTask(NodeType.NPC, false) {
            @Override
            public void execute(int index) {
                NPC npc = npcs.get(index);
                try {
                    npc.process();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        executor.sync(new GameSyncTask(NodeType.PLAYER) {
            @Override
            public void execute(int index) {
                Player player = players.get(index);
                synchronized (player) {
                    try {
                        PlayerUpdating.update(player);
                        NPCUpdating.update(player);
                    } catch (Exception e) {
                        e.printStackTrace();
                        player.requestLogout();
                    }
                }
            }
        });

        executor.sync(new GameSyncTask(NodeType.PLAYER) {
            @Override
            public void execute(int index) {
                Player player = players.get(index);
                synchronized (player) {
                    try {
                        player.resetUpdating();
                        player.setCachedUpdateBlock(null);
                        player.getSession().flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                        player.requestLogout();
                    }
                }
            }
        });

        executor.sync(new GameSyncTask(NodeType.NPC) {
            @Override
            public void execute(int index) {
                NPC npc = npcs.get(index);
                synchronized (npc) {
                    try {
                        npc.resetUpdating();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
 


    /**
     * Gets a player by their username.
     *
     * @param username The username of the player.
     * @return The player with the matching username.
     */
    public static Optional<Player> getPlayerByName(String username) {
        return players.search(p -> p != null && p.getUsername().equals(Misc.formatText(username)));
    }

    /**
     * Broadcasts a message to all players in the game.
     *
     * @param message The message to broadcast.
     */
    public static void sendMessage(String message) {
        players.forEach(p -> p.getPacketSender().sendMessage(message));
    }

    /**
     * Broadcasts a message to all staff-members in the game.
     *
     * @param message The message to broadcast.
     */
    public static void sendStaffMessage(String message) {
        players.stream().filter(p -> !Objects.isNull(p) && p.getRights().isOrInherits(Right.MODERATOR))
                .forEach(p -> p.getPacketSender().sendMessage(message));
    }

    /**
     * Saves all players in the game.
     */
    public static void savePlayers() {
        players.forEach(PlayerSaving::save);
    }

    public static CharacterList<Player> getPlayers() {
        return players;
    }

    public static CharacterList<NPC> getNpcs() {
        return npcs;
    }

    public static List<ItemOnGround> getItems() {
        return items;
    }

    public static List<GameObject> getObjects() {
        return objects;
    }

    public static Queue<Player> getAddPlayerQueue() {
        return addPlayerQueue;
    }

    public static Queue<Player> getRemovePlayerQueue() {
        return removePlayerQueue;
    }

    public static Queue<NPC> getAddNPCQueue() {
        return addNPCQueue;
    }

    public static Queue<NPC> getRemoveNPCQueue() {
        return removeNPCQueue;
    }
}
