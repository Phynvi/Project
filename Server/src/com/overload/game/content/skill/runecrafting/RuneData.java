package com.overload.game.content.skill.runecrafting;

import com.overload.util.ItemIdentifiers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RuneData extends ItemIdentifiers {

    public enum Rune {

            AIR_RUNE(556, 1, 5, 14897, false),
            MIND_RUNE(558, 2, 6, 14898, false),
            WATER_RUNE(555, 5, 7, 14899, false),
            EARTH_RUNE(557, 9, 8, 14900, false),
            FIRE_RUNE(554, 14, 9, 14901, false),
            BODY_RUNE(559, 20, 10, 14902, false),
            COSMIC_RUNE(564, 27, 11, 14903, true),
            CHAOS_RUNE(562, 35, 12, 14906, true),
            ASTRAL_RUNE(9075, 40, 13, 14911, true),
            NATURE_RUNE(561, 44, 14, 14905, true),
            LAW_RUNE(563, 54, 15, 14904, true),
            DEATH_RUNE(560, 65, 16, 14907, true),
            BLOOD_RUNE(565, 75, 27, 27978, true);

        private static final Map<Integer, Rune> runes = new HashMap<>();

        static {
            for (Rune rune : Rune.values()) {
                runes.put(rune.getObjectId(), rune);
            }
        }

        private int runeID;
        private int levelReq;
        private int xpReward;
        private int objectId;
        private boolean pureRequired;

        Rune(int rune, int levelReq, int xpReward, int altarObjectID, boolean pureRequired) {
            this.runeID = rune;
            this.levelReq = levelReq;
            this.xpReward = xpReward;
            this.objectId = altarObjectID;
            this.pureRequired = pureRequired;
        }

        public static Optional<Rune> forId(int objectId) {
            Rune rune = runes.get(objectId);
            if (rune != null) {
                return Optional.of(rune);
            }
            return Optional.empty();
        }

        public int getRuneID() {
            return runeID;
        }

        public int getLevelRequirement() {
            return levelReq;
        }

        public int getXP() {
            return xpReward;
        }

        public int getObjectId() {
            return objectId;
        }

        public boolean isPureRequired() {
            return pureRequired;
        }
    }

}
