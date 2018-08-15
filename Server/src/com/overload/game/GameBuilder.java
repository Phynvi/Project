package com.overload.game;

import com.overload.game.collision.RegionManager;
import com.overload.game.content.clan.ClanChatManager;
import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.minigames.godwars.GodWarsData;
import com.overload.game.content.skill.farming.Farming;
import com.overload.game.content.skill.hunter.PuroPuro;
import com.overload.game.content.skill.thieving.ThievingNpcData;
import com.overload.game.content.skill.thieving.ThievingStallData;
import com.overload.game.definition.loader.impl.*;
import com.overload.game.task.impl.CombatPoisonEffect.CombatPoisonData;
import com.overload.net.discord.DiscordConnection;
import com.overload.net.packet.impl.CommandPacketListener;
import com.overload.util.BackgroundLoader;
import com.overload.util.PlayerPunishment;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Loads all required necessities and starts processes required for the game to
 * work.
 *
 * @author Lare96
 */
public class GameBuilder {

    /**
     * The background loader that will load various utilities in the background
     * while the bootstrap is preparing the server.
     */
    private final BackgroundLoader backgroundLoader = new BackgroundLoader();

    /**
     * Initializes this game builder effectively preparing the background startup
     * tasks and game processing.
     *
     * @throws Exception if any issues occur while starting the network.
     */
    public void initialize() throws Exception {
        // Start immediate tasks..
        RegionManager.init();

        // Start background tasks..
        backgroundLoader.init(createBackgroundTasks());
        
        GodWarsData.declare();
        
        // Start global tasks..

        // Start game engine..
        new GameEngine().init();
        // Make sure the background tasks loaded properly..
        if (!backgroundLoader.awaitCompletion())
            throw new IllegalStateException("Background load did not complete normally!");
    }

    /**
     * Returns a queue containing all of the background tasks that will be executed
     * by the background loader. Please note that the loader may use multiple
     * threads to load the utilities concurrently, so utilities that depend on each
     * other <b>must</b> be executed in the same task to ensure thread safety.
     *W
     * @return the queue of background tasks.
     */
    public Queue<Runnable> createBackgroundTasks() {
        Queue<Runnable> tasks = new ArrayDeque<>();
        tasks.add(ClanChatManager::init);
        tasks.add(PuroPuro::spawn);
        tasks.add(CombatPoisonData::init);
        tasks.add(DiscordConnection::init);
        tasks.add(PlayerPunishment::init);
        tasks.add(ThievingStallData::declare);
        tasks.add(ThievingNpcData::declare);
        tasks.add(() -> {
            try {
                DialogueHandler.initialiseDialogues();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        tasks.add(() -> {
            try {
                CommandPacketListener.initializeCommands();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });
        tasks.add(Farming::declare);

        // Load definitions..
        tasks.add(new ObjectSpawnDefinitionLoader());
        tasks.add(new ItemDefinitionLoader());
        tasks.add(new ShopDefinitionLoader());
        tasks.add(new NpcDefinitionLoader());
        tasks.add(new NpcDropDefinitionLoader());
        tasks.add(new NpcSpawnDefinitionLoader());
        tasks.add(new PresetDefinitionLoader());        
        return tasks;
    }
}
