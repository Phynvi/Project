package com.overload.game.content.skill.prayer;

import com.overload.game.content.skill.skillable.impl.DefaultSkillable;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Graphic;
import com.overload.game.model.Skill;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;

import java.util.Optional;

public class Prayer {

    /**
     * The animation for burying a bone.
     */
    private static final Animation BONE_BURY = new Animation(827);
    /**
     * The amount of milliseconds a player must wait between
     * each bone-bury.
     */
    private static final long BONE_BURY_DELAY = 1000;
    /**
     * The experience multiplier when using bones on a gilded altar.
     */
    private static double GILDED_ALTAR_EXPERIENCE_MULTIPLIER = 3.5;

    /**
     * Checks if we should bury a bone.
     *
     * @param player
     * @param itemId
     * @return
     */
    public static boolean buryBone(Player player, int itemId) {
        Optional<BoneData.BuriableBone> b = BoneData.BuriableBone.forId(itemId);
        if (b.isPresent()) {
            if (player.getClickDelay().elapsed(BONE_BURY_DELAY)) {
                player.getSkillManager().stopSkillable();
                player.getPacketSender().sendInterfaceRemoval();
                player.performAnimation(BONE_BURY);
                player.getPacketSender().sendMessage("You dig a hole in the ground..");
                player.getInventory().delete(itemId, 1);
                TaskManager.submit(new Task(1, player, false) {
                    @Override
                    protected void execute() {
                        player.getPacketSender().sendMessage("..and bury the " + ItemDefinition.forId(itemId).getName() + ".");
                        player.getSkillManager().addExperience(Skill.PRAYER, b.get().getXp());
                        stop();
                    }
                });
                player.getClickDelay().reset();
            }
            return true;
        }
        return false;
    }

    /**
     * Handles the altar offering.
     *
     * @author Professor Oak
     */
    public static final class AltarOffering extends DefaultSkillable {
        /**
         * The {@link Animation} used for offering bones on the altar.
         */
        private static final Animation ALTAR_OFFERING_ANIMATION = new Animation(713);

        /**
         * The {@link Graphic} which will be performed by the {@link GameObject}
         * altar once bones are offered on it.
         */
        private static final Graphic ALTAR_OFFERING_GRAPHIC = new Graphic(624);

        /**
         * The {@link BoneData.BuriableBone} that's being offered.
         */
        private final BoneData.BuriableBone bone;

        /**
         * The {@link GameObject} altar which we're using
         * to offer the bones on.
         */
        private final GameObject altar;

        /**
         * The amount of bones that are being offered.
         */
        private int amount;

        /**
         * Constructs this {@link DefaultSkillable}.
         *
         * @param bone
         */
        public AltarOffering(BoneData.BuriableBone bone, GameObject altar, int amount) {
            this.bone = bone;
            this.altar = altar;
            this.amount = amount;
        }

        @Override
        public void startAnimationLoop(Player player) {
            Task task = new Task(2, player, true) {
                @Override
                protected void execute() {
                    player.performAnimation(ALTAR_OFFERING_ANIMATION);
                }
            };
            TaskManager.submit(task);
            getTasks().add(task);
        }

        @Override
        public void finishedCycle(Player player) {
            if (amount-- <= 0) {
                cancel(player);
            }
            altar.performGraphic(ALTAR_OFFERING_GRAPHIC);
            player.getInventory().delete(bone.getBoneID(), 1);
            player.getSkillManager().addExperience(Skill.PRAYER, (int) (bone.getXp() * GILDED_ALTAR_EXPERIENCE_MULTIPLIER));
            player.getPacketSender().sendMessage("The gods are pleased with your offering.");
        }

        @Override
        public int cyclesRequired(Player player) {
            return 2;
        }

        @Override
        public boolean hasRequirements(Player player) {
            //Check if player has bones..
            if (!player.getInventory().contains(bone.getBoneID())) {
                return false;
            }
            //Check if we offered all bones..
            if (amount <= 0) {
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
            return true;
        }
    }
}
