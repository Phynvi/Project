package com.overload.game.content.skill.prayer;

import com.overload.util.ItemIdentifiers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BoneData extends ItemIdentifiers {

    public enum BuriableBone {
        BONES(526, 5),
        BAT_BONES(530, 6),
        WOLF_BONES(2859, 6),
        BIG_BONES(532, 15),
        BABYDRAGON_BONES(534, 30),
        JOGRE_BONE(3125, 15),
        ZOGRE_BONES(4812, 23),
        LONG_BONES(10976, 15),
        CURVED_BONE(10977, 15),
        SHAIKAHAN_BONES(3123, 25),
        DRAGON_BONES(536, 72),
        FAYRG_BONES(4830, 84),
        RAURG_BONES(4832, 96),
        OURG_BONES(14793, 140),
        DAGANNOTH_BONES(6729, 125),
        WYVERN_BONES(6816, 72),
        LAVA_DRAGON_BONES(11943, 85);

        static final Map<Integer, BuriableBone> bones = new HashMap<Integer, BuriableBone>();

        static {
            for (BuriableBone b : BuriableBone.values()) {
                bones.put(b.boneId, b);
            }
        }

        private int boneId;
        private int xp;

        BuriableBone(int boneId, int buryXP) {
            this.boneId = boneId;
            this.xp = buryXP;
        }

        public static Optional<BuriableBone> forId(int itemId) {
            BuriableBone b = bones.get(itemId);
            if (b != null) {
                return Optional.of(b);
            }
            return Optional.empty();
        }

        public int getBoneID() {
            return boneId;
        }

        public int getXp() {
            return xp;
        }
    }
}
