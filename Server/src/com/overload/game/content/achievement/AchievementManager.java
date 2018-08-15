package com.overload.game.content.achievement;

import java.io.BufferedWriter;

import com.overload.game.content.achievement.Achievement.Achievements;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;

public class AchievementManager {
	
    Player player;
    public int currentInterface;
    private static final int MAXIMUM_TIER_ACHIEVEMENTS = 100;
    private static final int MAXIMUM_TIERS = 3;
    private int[][] amountRemaining = new int[MAXIMUM_TIERS][MAXIMUM_TIER_ACHIEVEMENTS];
    private boolean[][] completed = new boolean[MAXIMUM_TIERS][MAXIMUM_TIER_ACHIEVEMENTS];

    public int points;

    /**
     * WARNING: ADD TO THE END OF THE LIST.
     */
    private int boughtItems[][] = {{7409, -1}, {13659, -1}, {20120, -1}, {88, -1}, {13281, -1}, {2379, -1}, {20235, -1}, {13845, -1}, {13846, -1}, {13847, -1},
            {13848, -1}, {13849, -1}, {13850, -1}, {13851, -1}, {13852, -1}, {13853, -1}, {13854, -1}, {13855, -1}, {13856, -1}, {13857, -1}, {20220, -1},
            {20221, -1}, {20222, -1},};

    public AchievementManager(Player player) {
        this.player = player;
    }

    public void print(BufferedWriter writer, int tier) {
        try {
            for (Achievements achievement : Achievements.ACHIEVEMENTS) {
                if (achievement.getTier().ordinal() == tier) {
                    if (amountRemaining[tier][achievement.getId()] > 0) {
                        writer.write(achievement.name().toLowerCase() + " = " + amountRemaining[tier][achievement.getId()] + "\t" + completed[tier][achievement.getId()]);
                        writer.newLine();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void read(String name, int tier, int amount, boolean state) {
        for (Achievements achievement : Achievements.ACHIEVEMENTS) {
            if (achievement.getTier().ordinal() == tier) {
                if (achievement.name().toLowerCase().equals(name)) {
                    this.setComplete(tier, achievement.getId(), state);
                    this.setAmountRemaining(tier, achievement.getId(), amount);
                    break;
                }
            }
        }
    }

    public void drawInterface(int tier) {
    	
    }

    public void viewAchievement(int achievement, int tier) {
        
    }

    public void kill(NPC npc) {
        
    }

    public void claimCape() {
        
    }

    public boolean hasCompletedAll() {
        return false;
    }

    public boolean completedTier(AchievementTier tier) {
        return false;
    }

    public boolean isComplete(int tier, int index) {
        return completed[tier][index];
    }

    public boolean setComplete(int tier, int index, boolean state) {
        return this.completed[tier][index] = state;
    }

    public int getAmountRemaining(int tier, int index) {
        return amountRemaining[tier][index];
    }

    public void setAmountRemaining(int tier, int index, int amountRemaining) {
        this.amountRemaining[tier][index] = amountRemaining;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isAchievementItem(int itemId) {
        for (int i = 0; i < boughtItems.length; i++)
            if (boughtItems[i][0] == itemId)
                return true;
        return false;
    }

    public boolean hasBoughtItem(int itemId) {
        for (int i = 0; i < boughtItems.length; i++)
            if (boughtItems[i][0] == itemId)
                if (boughtItems[i][1] != -1)
                    return true;
        return false;
    }

    public void setBoughtItem(int itemId) {
        for (int i = 0; i < boughtItems.length; i++)
            if (boughtItems[i][0] == itemId)
                boughtItems[i][1] = 1;
    }

    public int[][] getBoughtItems() {
        return this.boughtItems;
    }

    public void setBoughtItem(int index, int value) {
        if (index > this.boughtItems.length - 1)
            return;
        this.boughtItems[index][1] = value;
    }

}
