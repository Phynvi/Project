package com.overload.game.content.combat.magic.spells;

import com.overload.game.entity.impl.player.Player;

public enum SuperheatData{

        BRONZE_BAR(2349, new int[][] { { 438, 1 }, { 436, 1 } }, 1, 16),
        BLURITE_BAR(9467, new int[][] { { 688, 1 } }, 8, 8),
        IRON_BAR(2351, new int[][] { { 440, 1 } }, 15, 12),
        SILVER_BAR(2355, new int[][] { { 442, 1 } }, 20, 13),
        STEEL_BAR(2353, new int[][] { { 440, 1 }, { 453, 2 } }, 30, 17),
        GOLD_BAR(2357, new int[][] { { 444, 1 } }, 40, 22),
        MITHRIL_BAR(2359, new int[][] { { 447, 1 }, { 453, 4 } }, 50, 30),
        ADAMANT_BAR(2361, new int[][] { { 449, 1 }, { 453, 6 } }, 70, 37),
        RUNITE_BAR(2363, new int[][] { { 451, 1 }, { 453, 8 } }, 85, 50);

        private final int productId;
        private final int[][] requiredItems;
        private final int levelRequired;
        private final int experience;

            SuperheatData(int productId, int[][] requiredItems, int levelRequired, int experience) {
            this.productId = productId;
            this.requiredItems = requiredItems;
            this.levelRequired = levelRequired;
            this.experience = experience;
        }

        public int getProduct() {
            return productId;
        }

        public int[][] getRequired() {
            return requiredItems;
        }

        public int getLevel() {
            return levelRequired;
        }

        public int getExperience() {
            return experience;
        }

        public static SuperheatData getItem(Player player, int item) {
            for (SuperheatData data: SuperheatData.values()) {
                for (int i = 0; i < data.getRequired().length; i++) {
                    if (item == data.getRequired()[i][0])
                        return data;
                }
            }
            player.sendMessage("You can't superheat this item!");
            return null;
        }

    }
