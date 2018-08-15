package com.overload.game.content.skill.skillable.impl;

import com.overload.game.content.PetHandler;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.object.MapObjects;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Skill;
import com.overload.game.model.container.impl.Equipment;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.game.task.impl.TimedObjectReplacementTask;
import com.overload.util.Misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents the Woodcutting skill.
 *
 * @author Professor Oak
 */
public class Woodcutting extends DefaultSkillable {

    /**
     * The {@link GameObject} to cut down.
     */
    private final GameObject treeObject;
    /**
     * The {@code treeObject} as an enumerated type
     * which contains information about it, such as
     * required level.
     */
    private final Tree tree;
    /**
     * The axe we're using to cut down the tree.
     */
    private Optional<Axe> axe = Optional.empty();

    /**
     * Constructs a new {@link Woodcutting}.
     *
     * @param treeObject The tree to cut down.
     * @param tree       The tree's data
     */
    public Woodcutting(GameObject treeObject, Tree tree) {
        this.treeObject = treeObject;
        this.tree = tree;
    }

    @Override
    public void start(Player player) {
        player.getPacketSender().sendMessage("You swing your axe at the tree..");
        super.start(player);
    }

    @Override
    public void startAnimationLoop(Player player) {
        Task animLoop = new Task(4, player, true) {
            @Override
            protected void execute() {
                player.performAnimation(axe.get().getAnimation());
            }
        };
        TaskManager.submit(animLoop);
        getTasks().add(animLoop);
    }

    @Override
    public void onCycle(Player player) {
        PetHandler.onSkill(player, Skill.WOODCUTTING);
    }

    @Override
    public void finishedCycle(Player player) {
        //Add logs..
        player.getInventory().add(tree.getLogId(), 1);
        player.getPacketSender().sendMessage("You get some logs.");

        //Add exp..
        player.getSkillManager().addExperience(Skill.WOODCUTTING, tree.getXpReward());

        //Regular trees should always despawn.
        //Multi trees are random.
        if (!tree.isMulti() || Misc.getRandom(15) >= 2) {
            //Stop skilling..
            cancel(player);

            //Despawn object and respawn it after a short period of time..
            TaskManager.submit(new TimedObjectReplacementTask(treeObject, new GameObject(1343, treeObject.getPosition()), tree.getRespawnTimer()));
        }
//        else if (GameObject.class.equals("redwood"));
//        TaskManager.submit(
//                new TimedObjectReplacementTask(
//                        treeObject,
//                        new GameObject(29669, treeObject.getPosition().clone()),
//                        tree.getRespawnTimer()
//                )
    }
    @Override
    public int cyclesRequired(Player player) {
        int cycles = tree.getCycles() + Misc.getRandom(5);
        cycles -= player.getSkillManager().getMaxLevel(Skill.WOODCUTTING) * 0.1;
        if (axe.isPresent()) {
            cycles -= cycles * axe.get().getSpeed();
            if (cycles < 3) {
                cycles = 3;
            }
        }
        return cycles;
    }

    @Override
    public boolean hasRequirements(Player player) {
        //Attempt to find an axe..
        axe = Optional.empty();
        for (Axe a : Axe.values()) {
            if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == a.getId()
                    || player.getInventory().contains(a.getId())) {

                //If we have already found an axe,
                //don't select others that are worse or can't be used
                if (axe.isPresent()) {
                    if (player.getSkillManager().getMaxLevel(Skill.WOODCUTTING) < a.getRequiredLevel()) {
                        continue;
                    }
                    if (a.getRequiredLevel() < axe.get().getRequiredLevel()) {
                        continue;
                    }
                }

                axe = Optional.of(a);
            }
        }

        //Check if we found one..
        if (!axe.isPresent()) {
            player.getPacketSender().sendMessage("You don't have an axe which you can use.");
            return false;
        }

        //Check if we have the required level to cut down this {@code tree} using the {@link Axe} we found..
        if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < axe.get().getRequiredLevel()) {
            player.getPacketSender().sendMessage("You don't have an axe which you have the required Woodcutting level to use.");
            return false;
        }

        //Check if we have the required level to cut down this {@code tree}..
        if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < tree.getRequiredLevel()) {
            player.getPacketSender().sendMessage("You need a Woodcutting level of at least " + tree.getRequiredLevel() + " to cut this tree.");
            return false;
        }

        //Finally, check if the tree object remains there.
        //Another player may have cut it down already.
        if (!MapObjects.exists(treeObject)) {
            return false;
        }

        return super.hasRequirements(player);
    }

    @Override
    public boolean loopRequirements() {
        return true;
    }

    @Override
    public boolean allowFullInventory() {
        return false;
    }

    /**
     * Holds data related to the axes
     * that can be used for this skill.
     */
    public enum Axe {
        BRONZE_AXE(1351, 1, 0.03, new Animation(879)),
        IRON_AXE(1349, 1, 0.05, new Animation(877)),
        STEEL_AXE(1353, 6, 0.09, new Animation(875)),
        BLACK_AXE(1361, 6, 0.11, new Animation(873)),
        MITHRIL_AXE(1355, 21, 0.13, new Animation(871)),
        ADAMANT_AXE(1357, 31, 0.16, new Animation(869)),
        RUNE_AXE(1359, 41, 0.19, new Animation(867)),
        DRAGON_AXE(6739, 61, 0.25, new Animation(2846)),
        INFERNAL(13241, 61, 0.3, new Animation(2117));

        private final int id;
        private final int requiredLevel;
        private final double speed;
        private final Animation animation;

        Axe(int id, int level, double speed, Animation animation) {
            this.id = id;
            this.requiredLevel = level;
            this.speed = speed;
            this.animation = animation;
        }

        public int getId() {
            return id;
        }

        public int getRequiredLevel() {
            return requiredLevel;
        }

        public double getSpeed() {
            return speed;
        }

        public Animation getAnimation() {
            return animation;
        }
    }

    /**
     * Holds data related to the trees
     * which can be used to train this skill.
     */
    public enum Tree {
        NORMAL(     1,  25,  1511,  "tree",          10, 8,  false),
        EVERGREEN(  1,  25,  1511,  "evergreen",     10, 8,  false),
        DEAD_TREE(  1,  25,  1511,  "dead tree",     10, 8,  false),
        ACHEY(      1,  25,  2862,  "achey tree",    13, 9,  false),
        OAK(        15, 38,  1521,  "oak",           200, 11, true),
        WILLOW(     30, 68,  1519,  "willow",   200, 14, true),
        TEAK(       35, 85,  6333,  "teak tree",     20, 16, true),
        DRAMEN(     36, 88,  771,   "dramen tree",   20, 17, true),
        MAPLE(      45, 100, 1517,  "maple tree",    20, 18, true),
        MAHOGANY(   50, 125, 6332,  "mahogany tree", 20, 20, true),
        YEW(        60, 175, 1515,  "yew",      200, 28, true),
        MAGIC(      75, 250, 1513,  "magic tree",    20, 40, true),
        REDWOOD(    90, 380, 19669, "redwood",  20, 43, true);

        private static final Map<String, Tree> trees = new HashMap<>();

        static {
            for (Tree t : Tree.values()) {
                trees.put(t.name, t);
            }
        }

        private final String name;
        private final int requiredLevel;
        private final int xpReward;
        private final int logId;
        private final int cycles;
        private final int respawnTimer;
        private final boolean multi;

        Tree(int req, int xp, int log, String name, int cycles, int respawnTimer, boolean multi) {
            this.requiredLevel = req;
            this.xpReward = xp;
            this.logId = log;
            this.name = name.toLowerCase();
            this.cycles = cycles;
            this.respawnTimer = respawnTimer;
            this.multi = multi;
        }

        public static Optional<Tree> forObjectId(String name) {
            if (name == null)
                return Optional.empty();

            Tree tree = trees.get(name.toLowerCase());
            if (tree != null) {
                return Optional.of(tree);
            }
            return Optional.empty();
        }

        public boolean isMulti() {
            return multi;
        }

        public int getCycles() {
            return cycles;
        }

        public int getRespawnTimer() {
            return respawnTimer;
        }

        public int getLogId() {
            return logId;
        }

        public int getXpReward() {
            return xpReward;
        }

        public int getRequiredLevel() {
            return requiredLevel;
        }
    }
}
