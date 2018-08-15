package com.overload.game.content.skill.runecrafting;

import com.overload.game.model.Position;
import com.overload.util.ItemIdentifiers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TalismanData extends ItemIdentifiers {

    public enum Talisman {
        AIR_TALISMAN(1438, 1, new Position(2841, 4828)), MIND_TALISMAN(1448, 2,
                new Position(2793, 4827)), WATER_TALISMAN(1444, 5, new Position(2720, 4831)), EARTH_TALISMAN(1440, 9,
                new Position(2655, 4829)), FIRE_TALISMAN(1442, 14,
                new Position(2576, 4846)), BODY_TALISMAN(1446, 20, new Position(2522,
                4833)), COSMIC_TALISMAN(1454, 27, new Position(2163, 4833)), CHAOS_TALISMAN(
                1452, 35, new Position(2282, 4837)), NATURE_TALISMAN(1462, 44,
                new Position(2400, 4834)), LAW_TALISMAN(1458, 54,
                new Position(2464, 4817)), DEATH_TALISMAN(1456, 65,
                new Position(2208, 4829)), BLOOD_TALISMAN(1450,
                77, new Position(1722, 3826));

        private static final Map<Integer, Talisman> talismans = new HashMap<Integer, Talisman>();

        static {
            for (Talisman t : Talisman.values()) {
                talismans.put(t.getItemId(), t);
            }
        }

        private int talismanId;
        private int levelReq;
        private Position location;

        Talisman(int talismanId, int levelReq, Position location) {
            this.talismanId = talismanId;
            this.levelReq = levelReq;
            this.location = location;
        }

        public static Optional<Talisman> forId(int itemId) {
            Talisman talisman = talismans.get(itemId);
            if (talisman != null) {
                return Optional.of(talisman);
            }
            return Optional.empty();
        }

        public int getItemId() {
            return talismanId;
        }

        public int getLevelRequirement() {
            return levelReq;
        }

        public Position getPosition() {
            return location.clone();
        }
    }
}
