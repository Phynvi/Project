package com.overload.game.content.skill.smithing;

import com.google.common.collect.ImmutableSet;
import com.overload.game.model.Item;
import com.overload.game.model.RequiredItem;
import com.overload.util.ItemIdentifiers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SmithingBarData extends ItemIdentifiers {

    public enum Bar {

            BRONZE_BAR(2349,     new RequiredItem[]{new RequiredItem(new Item(438), true), new RequiredItem(new Item(436), true)}, 1, 120, 2405, new int[][] {{3987, 1}, {3986, 5}, {2807, 10}, {2414, -1}}, Optional.of(SmithEquipmentData.SmithableEquipment.BRONZE_ITEMS)),
            IRON_BAR(2351,       new RequiredItem[]{new RequiredItem(new Item(440), true)}, 15, 540, 2406, new int[][]{{3991, 1}, {3990, 5}, {3989, 10}, {3988, -1}}, Optional.of(SmithEquipmentData.SmithableEquipment.IRON_ITEMS)),
            SILVER_BAR(2355,     new RequiredItem[]{new RequiredItem(new Item(442), true)}, 20, 725, 2407, new int[][]{{3995, 1}, {3994, 5}, {3993, 10}, {3992, -1}}, Optional.empty()),
            STEEL_BAR(2353,      new RequiredItem[]{new RequiredItem(new Item(440), true), new RequiredItem(new Item(453, 2), true)}, 30, 1350, 2409, new int[][]{{3999, 1}, {3998, 5}, {3997, 10}, {3996, -1}}, Optional.of(SmithEquipmentData.SmithableEquipment.STEEL_ITEMS)),
            GOLD_BAR(2357,       new RequiredItem[]{new RequiredItem(new Item(444), true)}, 40, 2400, 2410, new int[][]{{4003, 1}, {4002, 5}, {4001, 10}, {4000, -1}}, Optional.empty()),
            MITHRIL_BAR(2359,    new RequiredItem[]{new RequiredItem(new Item(447), true), new RequiredItem(new Item(453, 4), true)}, 50, 3450, 2411, new int[][]{{7441, 1}, {7440, 5}, {6397, 10}, {4158, -1}}, Optional.of(SmithEquipmentData.SmithableEquipment.MITHRIL_ITEMS)),
            ADAMANTITE_BAR(2361, new RequiredItem[]{new RequiredItem(new Item(449), true), new RequiredItem(new Item(453, 6), true)}, 70, 4500, 2412, new int[][]{{7446, 1}, {7444, 5}, {7443, 10}, {7442, -1}}, Optional.of(SmithEquipmentData.SmithableEquipment.ADAMANT_ITEMS)),
            RUNITE_BAR(2363,     new RequiredItem[]{new RequiredItem(new Item(451), true), new RequiredItem(new Item(453, 8), true)}, 85, 5560, 2413, new int[][]{{7450, 1}, {7449, 5}, {7448, 10}, {7447, -1}}, Optional.of(SmithEquipmentData.SmithableEquipment.RUNE_ITEMS)),;

        private static Map<Integer, Bar> smeltables = new HashMap<Integer, Bar>();

        static {
            for (Bar s : Bar.values()) {
                smeltables.put(s.getBar(), s);
            }
        }

        private final int bar;
        private final RequiredItem[] ores;
        private final int levelReq;
        private final int xpReward;
        private final int frame;
        private final int[][] buttons;
        private final Optional<ImmutableSet<SmithEquipmentData.SmithableEquipment>> items;

        Bar(int bar, RequiredItem[] ores, int levelReq, int xpReward, int frame, int[][] buttons, Optional<ImmutableSet<SmithEquipmentData.SmithableEquipment>> items) {
            this.bar = bar;
            this.ores = ores;
            this.levelReq = levelReq;
            this.xpReward = xpReward;
            this.frame = frame;
            this.buttons = buttons;
            this.items = items;
        }

        public static Optional<Bar> forBarId(int barId) {
            Bar smeltable = smeltables.get(barId);
            if (smeltable != null) {
                return Optional.of(smeltable);
            }
            return Optional.empty();
        }

        public int getBar() {
            return bar;
        }

        public RequiredItem[] getOres() {
            return ores;
        }

        public int getLevelReq() {
            return levelReq;
        }

        public int getXpReward() {
            return xpReward;
        }

        public int getFrame() {
            return frame;
        }

        public Optional<ImmutableSet<SmithEquipmentData.SmithableEquipment>> getItems() {
            return items;
        }

        public int[][] getButtons() {
            return buttons;
        }
    }
}
