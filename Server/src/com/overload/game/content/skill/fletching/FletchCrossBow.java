package com.overload.game.content.skill.fletching;

import com.overload.util.ItemIdentifiers;

public class FletchCrossBow extends ItemIdentifiers{

    public enum FletchableCrossbow {
        BRONZE_CROSSBOW(WOODEN_STOCK, BRONZE_LIMBS, BRONZE_CROSSBOW_U_, 9, 12),
        IRON_CROSSBOW(OAK_STOCK, IRON_LIMBS, IRON_CROSSBOW_U_, 39, 44),
        STEEL_CROSSBOW(WILLOW_STOCK, STEEL_LIMBS, STEEL_CROSSBOW_U_, 46, 54),
        MITHRIL_CROSSBOW(MAPLE_STOCK, MITHRIL_LIMBS, MITHRIL_CROSSBOW_U_, 54, 64),
        ADAMANT_CROSSBOW(MAHOGANY_STOCK, ADAMANTITE_LIMBS, ADAMANT_CROSSBOW_U_, 61, 82),
        RUNE_CROSSBOW(YEW_STOCK, RUNITE_LIMBS, RUNITE_CROSSBOW_U_, 69, 100),;

        private final int stock, limbs, unstrung, level, limbsExp;

        FletchableCrossbow(int stock, int limbs, int unstrung, int level, int limbsExp) {
            this.stock = stock;
            this.limbs = limbs;
            this.unstrung = unstrung;
            this.level = level;
            this.limbsExp = limbsExp;
        }

        public int getStock() {
            return stock;
        }

        public int getUnstrung() {
            return unstrung;
        }

        public int getLimbs() {
            return limbs;
        }

        public int getLevel() {
            return level;
        }


        public int getLimbsExp() {
            return limbsExp;
        }
    }
}
