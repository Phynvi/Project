package com.overload.game.content.skill.thieving;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Item;
import com.overload.game.model.Position;
import com.overload.game.model.Skill;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.Misc;

public class ThievingStallTask extends Task {

    public static final int[][] THEIVING_ITEMS = {{1277, 1}, {1281, 1}, {1211, 1}, {1285, 1}, {1331, 1}, {1289, 1}, {995, Misc.randomNumber(10000)}};

    public static void attemptStealFromStall(Player player, int id, Position position) {
        ThievingStallData data = ThievingStallData.getObjectById(id);

        if (data == null) {
            return;
        }

        if (player.busy()) {
            return;
        }
        if (!meetsRequirements(player, data)) {
            return;
        }
        if (player.getClickDelay().elapsed(1000)) {
            player.performAnimation(new Animation(832));
            TaskManager.submit(new ThievingStallTask(1, player, data));
            player.getClickDelay().reset();
        }
    }

    private static boolean meetsRequirements(Player player, ThievingStallData data) {
        if (player.getInventory().getFreeSlots() == 0) {
            player.sendMessage("You don't have enough inventory spaces left to hold this.");
            return false;
        }
        if (player.getSkillManager().getCurrentLevel(Skill.THIEVING) <= data.getLevelRequired()) {
            player.sendMessage("You need a thieving level of " + data.getLevelRequired() + " to steal from this stall.");
            return false;
        }
        return true;
    }

    private Player player;

    private ThievingStallData data;

    public ThievingStallTask(int delay, Player player, ThievingStallData data) {
        super(delay, player, true);
        this.player = player;
        this.data = data;
    }

    @Override
    public void execute() {
        if (!meetsRequirements(player, data)) {
            stop();
            return;
        }
        successfulAttempt(player, data);
        stop();
    }

    @Override
    public void stop() {
        setEventRunning(false);
    }

    private void successfulAttempt(Player player, ThievingStallData data) {
        if (player.getClickDelay().elapsed(1000)) {
            int randomItem = Misc.randomNumber(data.getRewards().length);
            player.getInventory().add(new Item(data.getRewards()[randomItem][0], data.getRewards()[randomItem][1]));

            if (data.getRewards()[randomItem][0] == 995) {
                player.sendMessage("You steal " + data.getRewards()[randomItem][1] + "gp from the stall.");
            }

            player.getSkillManager().addExperience(Skill.THIEVING, data.getExperience());
            player.getClickDelay().reset();
        }
    }
}
