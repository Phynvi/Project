package com.overload.game.content.skill.skillable.impl;

import com.overload.game.content.PetHandler;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.grounditem.ItemOnGroundManager;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Item;
import com.overload.game.model.Projectile;
import com.overload.game.model.Skill;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.Chance;
import com.overload.util.Misc;

import javax.tools.Tool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the Fishing skill.
 *
 * @author Professor Oak
 * @author Lare96
 */
public class Fishing extends DefaultSkillable {

    /**
     * All of the possible items you can get from a casket.
     */
    public static final Item[] CASKET_ITEMS = {new Item(1061), new Item(592), new Item(1059), new Item(995, 100000), new Item(4212), new Item(995, 50000), new Item(401), new Item(995, 150000), new Item(407)};
    /**
     * The fish spot we're fishing from.
     */
    private final NPC fishSpot;

    /**
     * The {@link FishingTool} we're using
     * to fish.
     */
    private final FishingSpot spot;

    /**
     * Constructs a new fishing instance.
     *
     * @param fishSpot The fish spot we're using to fish from.
     */
    public Fishing(NPC fishSpot, FishingSpot spot) {
        this.fishSpot = fishSpot;
        this.spot = spot;
    }

    @Override
    public boolean hasRequirements(Player player) {
    	if (spot == null)
    		return false;
    	FishingTool tool = spot.getTool();
    	if (tool == null)
    		return false;
        //Make sure player has tool..
        if (!player.getInventory().contains(tool.getId())) {
            player.getPacketSender().sendMessage("You need a " + ItemDefinition.forId(tool.getId()).getName().toLowerCase() + " to do this.");
            return false;
        }

        //Check fishing level for tool..
        if (player.getSkillManager().getCurrentLevel(Skill.FISHING) < tool.getLevel()) {
            player.getPacketSender().sendMessage("You need a Fishing level of at least " + tool.getLevel() + " to do this.");
            return false;
        }

        //Make sure player has required bait..
        if (tool.getNeeded() > 0) {
            if (!player.getInventory().contains(tool.getNeeded())) {
                player.getPacketSender().sendMessage("You do not have any " + ItemDefinition.forId(tool.getNeeded()).getName().toLowerCase() + "(s).");
                return false;
            }
        }
        return super.hasRequirements(player);
    }

    @Override
    public void start(Player player) {
        player.getPacketSender().sendMessage("You begin to fish..");
        super.start(player);
    }

    @Override
    public void startAnimationLoop(Player player) {
    	if (spot == null)
    		return;
    	FishingTool tool = spot.getTool();
    	if (tool == null)
    		return;
        Task animLoop = new Task(4, player, true) {
            @Override
            protected void execute() {
                player.performAnimation(new Animation(tool.getAnimation()));
            }
        };
        TaskManager.submit(animLoop);
        getTasks().add(animLoop);
    }

    @Override
    public void onCycle(Player player) {
    	if (spot == null)
    		return;
    	FishingTool tool = spot.getTool();
    	if (tool == null)
    		return;
        PetHandler.onSkill(player, Skill.FISHING);

        //Handle random event..
        if (Misc.getRandom(1400) == 1) {
            AttackToolRandomEvent attackTool = new AttackToolRandomEvent(player, tool, fishSpot);
            TaskManager.submit(attackTool);
            cancel(player);
        }
    }

    @Override
    public void finishedCycle(Player player) {
        /** Random stop for that 'old school' rs feel :) */
        if (Misc.getRandom(90) == 0) {
            cancel(player);
        }
    	if (spot == null)
    		return;
    	FishingTool tool = spot.getTool();
    	if (tool == null)
    		return;

        /** Catch multiple fish with a big net. */
        if (tool == FishingTool.BIG_NET) {
            int amount = Misc.getRandom(4) + 1;
            int freeSlots = player.getInventory().getFreeSlots();

            if (!(freeSlots >= amount)) {
                amount = freeSlots;
            }

            for (int i = 0; i < amount; i++) {

                /** Get a random fish for us. */
                Fish caught = determineFish(player, spot);

                /** Catch the fish. */
                player.getPacketSender().sendMessage("You catch a " + caught.name().toLowerCase().replace("_", " ") + ".");
                player.getInventory().add(new Item(caught.getId()));
                player.getSkillManager().addExperience(Skill.FISHING, caught.getExperience());
            }

            /** Catch fish normally. */
        } else {
            /** Get a random fish for us. */
            Fish caught = determineFish(player, spot);

            /** Catch the fish. */
            player.getPacketSender().sendMessage("You catch a " + caught.name().toLowerCase().replace("_", " ") + ".");
            player.getInventory().add(new Item(caught.getId()));
            player.getSkillManager().addExperience(Skill.FISHING, caught.getExperience());

            /** Delete the item needed for the tools. */
            if (tool.getNeeded() > 0) {
                player.getInventory().delete(new Item(tool.getNeeded()));
            }
        }
    }

    @Override
    public boolean loopRequirements() {
        return true;
    }

    @Override
    public boolean allowFullInventory() {
        return false;
    }

    @Override
    public int cyclesRequired(Player player) {
        return (11 - (int) Math.floor(player.getSkillManager().getCurrentLevel(Skill.FISHING) / 10));
    }

    /**
     * Gets a random fish to be caught for the player based on fishing level and
     * rarity.
     *
     * @param player the player that needs a fish.
     * @param tool   the tool this player is fishing with.
     */
    private Fish determineFish(Player player, FishingSpot spot) {
        List<Fish> fishList = new ArrayList<Fish>();

        /** Determine which fish are able to be caught. */
        for (Fish fish : spot.getFish()) {
            if (fish.getLevel() <= player.getSkillManager().getCurrentLevel(Skill.FISHING)) {
                fishList.add(fish);
            }
        }

        /** Filter the fish based on rarity. */
        for (Iterator<Fish> iterator = fishList.iterator(); iterator.hasNext(); ) {
            Fish fish = iterator.next();

            if (fishList.size() == 1) {
                /** Return this fish if it's the only one left in the list. */
                return fish;
            }

            if (!fish.getChance().success()) {
                iterator.remove();
            }
        }

        /** Return a random fish from the list. */
        return Misc.randomElement(fishList);
    }

    public enum FishingSpot {
    	//Naming Format
    	//Option1_Option2_OptionSelected
    	LURE_BAIT_1(1526, 1, FishingTool.FLY_FISHING_ROD, Fish.TROUT, Fish.SALMON),
    	LURE_BAIT_2(1526, 2, FishingTool.FISHING_ROD, Fish.SARDINE, Fish.HERRING, Fish.PIKE),
    	
//        NET(303, 1, -1, 3, 621, Fish.SHRIMP, Fish.ANCHOVY),
//        BIG_NET(305, 16, -1, 3, 620, Fish.MACKEREL, Fish.OYSTER, Fish.COD, Fish.BASS, Fish.CASKET),
//        FISHING_ROD(307, 5, 313, 1, 622, Fish.SARDINE, Fish.HERRING, Fish.PIKE, Fish.SLIMY_EEL, Fish.CAVE_EEL, Fish.LAVA_EEL),
//        FLY_FISHING_ROD(309, 20, 314, 1, 622, Fish.TROUT, Fish.SALMON),
//        HARPOON(311, 35, -1, 4, 618, Fish.TUNA, Fish.SWORDFISH),
//        SHARK_HARPOON(311, 35, -1, 6, 618, Fish.SHARK),
//        LOBSTER_POT(301, 40, -1, 4, 619, Fish.LOBSTER)
    	;
    	
    	int spotId;
    	
    	int option;
    	FishingTool tool;

        private Fish[] fish;
    	
    	FishingSpot(int spotId, int option, FishingTool tool, Fish... fish) {
    		this.spotId = spotId;
    		this.option = option;
    		this.tool = tool;
            this.fish = fish;
    	}
    	
    	public static FishingTool getToolById(int id, int option) {
    		for(FishingSpot spot : FishingSpot.values()) {
    			if (spot.getSpotId() == id && spot.getOption() == option) {
    				return spot.getTool();
    			}
    		}
    		return null;
    	}
    	
    	public static FishingSpot getSpotByTool(FishingTool tool) {
    		for(FishingSpot spot : FishingSpot.values()) {
    			if (spot.getTool() == tool) {
    				return spot;
    			}
    		}
    		return null;
    	}
    	
    	public int getSpotId() {
    		return spotId;
    	}
    	
    	public int getOption() {
    		return option;
    	}
    	
    	public FishingTool getTool() {
    		return tool;
    	}
    	
        public Fish[] getFish() {
            return fish;
        }
    };

    public enum FishingTool {
        NET(303, 1, -1, 3, 621),
        BIG_NET(305, 16, -1, 3, 620),
        FISHING_ROD(307, 5, 313, 1, 622),
        FLY_FISHING_ROD(309, 20, 314, 1, 622),
        HARPOON(311, 35, -1, 4, 618),
        SHARK_HARPOON(311, 35, -1, 6, 618),
        LOBSTER_POT(301, 40, -1, 4, 619);

        /**
         * The item id of the tool.
         */
        private int id;

        /**
         * The level you need to be to use this tool.
         */
        private int level;

        /**
         * The id of an item needed to use this tool.
         */
        private int needed;

        /**
         * The speed of this tool.
         */
        private int speed;

        /**
         * The animation performed when using this tool.
         */
        private int animation;

        /**
         * Creates a new {@link Tool}.
         *
         * @param id        the item id of the tool.
         * @param level     the level you need to be to use this tool.
         * @param needed    the id of an item needed to use this tool.
         * @param speed     the speed of this tool.
         * @param animation the animation performed when using this tool.
         * @param fish      the fish you can catch with this tool.
         */
        private FishingTool(int id, int level, int needed, int speed, int animation) {
            this.id = id;
            this.level = level;
            this.needed = needed;
            this.speed = speed;
            this.animation = animation;
        }

        /**
         * Gets the item id of this tool.
         *
         * @return the item id.
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the level you need to be to use this tool.
         *
         * @return the level needed.
         */
        public int getLevel() {
            return level;
        }

        /**
         * Gets the id of an item needed to use this tool.
         *
         * @return the item needed.
         */
        public int getNeeded() {
            return needed;
        }

        /**
         * Gets the speed of this tool.
         *
         * @return the speed.
         */
        public int getSpeed() {
            return speed;
        }

        /**
         * Gets the animation performed when using this tool.
         *
         * @return the animation.
         */
        public int getAnimation() {
            return animation;
        }
    }

    public enum Fish {
        SHRIMP(317, 1, Chance.VERY_COMMON, 10),
        SARDINE(327, 5, Chance.VERY_COMMON, 20),
        HERRING(345, 10, Chance.VERY_COMMON, 30),
        ANCHOVY(321, 15, Chance.SOMETIMES, 40),
        MACKEREL(353, 16, Chance.VERY_COMMON, 20),
        CASKET(405, 16, Chance.ALMOST_IMPOSSIBLE, 100),
        OYSTER(407, 16, Chance.EXTREMELY_RARE, 80),
        TROUT(335, 20, Chance.VERY_COMMON, 50),
        COD(341, 23, Chance.VERY_COMMON, 45),
        PIKE(349, 25, Chance.VERY_COMMON, 60),
        SLIMY_EEL(3379, 28, Chance.EXTREMELY_RARE, 65),
        SALMON(331, 30, Chance.VERY_COMMON, 70),
        TUNA(359, 35, Chance.VERY_COMMON, 80),
        CAVE_EEL(5001, 38, Chance.SOMETIMES, 80),
        LOBSTER(377, 40, Chance.VERY_COMMON, 90),
        BASS(363, 46, Chance.SOMETIMES, 100),
        SWORDFISH(371, 50, Chance.COMMON, 100),
        LAVA_EEL(2148, 53, Chance.VERY_COMMON, 60),
        SHARK(383, 76, Chance.COMMON, 110);

        /**
         * The item id of the fish.
         */
        private int id;

        /**
         * The level needed to be able to catch the fish.
         */
        private int level;

        /**
         * The chance of catching this fish (when grouped with other fishes).
         */
        private Chance chance;

        /**
         * The experience gained from catching this fish.
         */
        private int experience;

        /**
         * Creates a new {@link Fish}.
         *
         * @param id         the item id of the fish.
         * @param level      the level needed to be able to catch the fish.
         * @param chance     the chance of catching this fish (when grouped with other
         *                   fishes).
         * @param experience the experience gained from catching this fish.
         */
        private Fish(int id, int level, Chance chance, int experience) {
            this.id = id;
            this.level = level;
            this.chance = chance;
            this.experience = experience;
        }

        /**
         * Gets the item id of the fish.
         *
         * @return the item id.
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the level needed to be able to catch the fish.
         *
         * @return the level.
         */
        public int getLevel() {
            return level;
        }

        /**
         * Gets the chance of catching this fish (when grouped with other
         * fishes).
         *
         * @return the chance.
         */
        public Chance getChance() {
            return chance;
        }

        /**
         * Gets the experience gained from catching this fish.
         *
         * @return the experience.
         */
        public int getExperience() {
            return experience;
        }
    }

	public static FishingTool getTool(int npcId, int option) {
		return FishingSpot.getToolById(npcId, option);
	}

	public static FishingSpot getSpot(int npcId, int option) {
		return FishingSpot.getSpotByTool(getTool(npcId, option));
	}

    /**
     * Represents a random event which attacks a player's tool, forcing it
     * to drop onto the ground.
     * <p>
     * This is a custom version of the OSRS "Big fish random event", which was
     * deleted in a update. The NPC "big fish" was deleted along with it,
     * so we simply shoot the projectile from the fish spot.
     *
     * @author Professor Oak
     * @author Lare96
     */
    private static final class AttackToolRandomEvent extends Task {
        /**
         * The defence animation the player will perform
         * when attacked.
         */
        private static final Animation DEFENCE_ANIM = new Animation(404);

        /**
         * The projectile which will be fired towards
         * the player from the fishing spot.
         */
        private static final int PROJECTILE_ID = 94;

        /**
         * The player being attacked by a fish.
         */
        private final Player player;

        /**
         * The tool the player is using when being
         * attacked by a fish. This will be removed
         * from inventory and put on the ground.
         */
        private final FishingTool tool;

        /**
         * The fishing spot's position. The
         * attacking npc.
         */
        private final NPC fishSpot;

        /**
         * This {@link Task}'s current tick.
         */
        private int tick;

        /**
         * Did we delete the player's tool?
         */
        private boolean deletedTool;

        /**
         * Creates this task.
         *
         * @param player
         * @param tool
         * @param fishSpot
         */
        public AttackToolRandomEvent(Player player, FishingTool tool, NPC fishSpot) {
            super(1, player, true);
            this.player = player;
            this.tool = tool;
            this.fishSpot = fishSpot;
        }

        @Override
        protected void execute() {
            switch (tick) {
                case 0:
                    //Fire projectile at player.
                    new Projectile(fishSpot, player, PROJECTILE_ID, 40, 70, 31, 33, 0).sendProjectile();
                    break;
                case 2:
                    //Defence animation..
                    player.performAnimation(DEFENCE_ANIM);
                    break;
                case 3:
                    //Delete tool from inventory and put on ground..
                    if (player.getInventory().contains(tool.getId())) {
                        player.getInventory().delete(tool.getId(), 1);
                        deletedTool = true;
                    }
                    break;
                case 4:
                    //Spawn tool on ground if it was deleted from inventory..
                    if (deletedTool) {
                        ItemOnGroundManager.register(player, new Item(tool.getId()));
                        player.getPacketSender().sendMessage("A big fish attacked and you were forced to drop your " + ItemDefinition.forId(tool.getId()).getName().toLowerCase() + ".");
                    }

                    //Stop task..
                    stop();
                    break;
            }
            tick++;
        }
    }
}
