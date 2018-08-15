package com.overload.game.content.skill.fletching;

import com.overload.util.ItemIdentifiers;

public class FletchAmmo extends ItemIdentifiers {

    public enum FletchableAmmo {
        HEADLESS_ARROWS(52, 314, 53, 15, 1),

        BRONZE_ARROWS(53, 39, 882, 20, 1),
        IRON_ARROWS(53, 40, 884, 38, 15),
        STEEL_ARROWS(53, 41, 886, 75, 30),
        MITHRIL_ARROWS(53, 42, 888, 113, 45),
        ADAMANT_ARROWS(53, 43, 890, 150, 60),
        RUNE_ARROWS(53, 44, 892, 188, 75),
        DRAGON_ARROWS(53, 11237, 11212, 225, 90),

        BRONZE_DARTS(314, 819, 806, 2, 10),
        IRON_DARTS(314, 820, 807, 4, 22),
        STEEL_DARTS(314, 821, 808, 8, 37),
        MITHRIL_DARTS(314, 822, 809, 12, 52),
        ADAMANT_DARTS(314, 823, 810, 15, 67),
        RUNE_DARTS(314, 824, 811, 20, 81),
        DRAGON_DARTS(314, 11232, 11230, 25, 95),

        BRONZE_BOLTS(314, 9375, 877, 5, 9),
        OPAL_BOLTS(877, 45, 879, 16, 11),
        IRON_BOLTS(314, 9377, 9140, 15, 39),
        PEARL_BOLTS(9140, 46, 880, 32, 41),
        SILVER_BOLTS(314, 9382, 9145, 25, 43),
        STEEL_BOLTS(314, 9378, 9141, 35, 46),
        RED_TOPAZ_BOLTS(9141, 9188, 9336, 39, 48),
        BARBED_BOLTS(877, 47, 881, 95, 51),
        MITHRIL_BOLTS(314, 9379, 9142, 50, 54),
        BROAD_BOLTS(314, 11876, 11875, 30, 55),
        SAPPHIRE_BOLTS(9142, 9189, 9337, 47, 56),
        EMERALD_BOLTS(9142, 9190, 9338, 58, 55),
        ADAMANTITE_BOLTS(314, 9380, 9143, 70, 61),
        RUBY_BOLTS(9143, 9191, 9339, 63, 63),
        DIAMOND_BOLTS(9143, 9192, 9340, 70, 65),
        RUNITE_BOLTS(314, 9381, 9144, 100, 69),
        DRAGONSTONE_BOLTS(9144, 9193, 9341, 82, 71),
        ONYX_BOLTS(9144, 9194, 9342, 94, 73),;

        public final int item1, item2, outcome, xp, levelReq;

        private FletchableAmmo(int item1, int item2, int outcome, int xp, int levelReq) {
            this.item1 = item1;
            this.item2 = item2;
            this.outcome = outcome;
            this.xp = xp;
            this.levelReq = levelReq;
        }

        public int getItem1() {
            return item1;
        }

        public int getItem2() {
            return item2;
        }

        public int getOutcome() {
            return outcome;
        }

        public int getXp() {
            return xp;
        }

        public int getLevelReq() {
            return levelReq;
        }
    }

}
