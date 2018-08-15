package com.overload.game.content.skill.crafting;

import com.overload.game.model.Animation;
import com.overload.game.model.Item;
import com.overload.util.ItemIdentifiers;

import java.util.HashMap;
import java.util.Map;

public class LeatherData extends ItemIdentifiers {

    private static final Animation HIDE_CRAFT = new Animation(1249);

    public enum HideData {
        GREEN(GREEN_DRAGON_LEATHER, 2,
                new LeatherMaking(new Item(GREEN_DHIDE_VAMB), 57, 62, HIDE_CRAFT),
                new LeatherMaking(new Item(GREEN_DHIDE_BODY), 63, 124, HIDE_CRAFT),
                new LeatherMaking(new Item(GREEN_DHIDE_CHAPS), 60, 186, HIDE_CRAFT)),
        BLUE(BLUE_DRAGON_LEATHER, 2,
                new LeatherMaking(new Item(BLUE_DHIDE_BODY), 15, 10, HIDE_CRAFT),
                new LeatherMaking(new Item(BLUE_DHIDE_VAMB), 24, 16, HIDE_CRAFT),
                new LeatherMaking(new Item(BLUE_DHIDE_CHAPS), 20, 33, HIDE_CRAFT)),
        RED(RED_DRAGON_LEATHER, 2,
                new LeatherMaking(new Item(RED_DHIDE_BODY), 30, 15, HIDE_CRAFT),
                new LeatherMaking(new Item(RED_DHIDE_VAMB), 39, 22, HIDE_CRAFT),
                new LeatherMaking(new Item(RED_DHIDE_CHAPS), 35, 66, HIDE_CRAFT)),
        BLACK(BLACK_DRAGON_LEATHER, 2,
                new LeatherMaking(new Item(BLACK_DHIDE_BODY), 45, 20, HIDE_CRAFT),
                new LeatherMaking(new Item(BLACK_DHIDE_CHAPS), 54, 32, HIDE_CRAFT),
                new LeatherMaking(new Item(BLACK_DHIDE_VAMB), 55, 116, HIDE_CRAFT)),
        HARD(HARD_LEATHER, 2,
                new LeatherMaking(new Item(HARDLEATHER_BODY), 60, 25, HIDE_CRAFT),
                new LeatherMaking(new Item(HARDLEATHER_GLOVES), 69, 50, HIDE_CRAFT)),
        SNAKE(SNAKE_HIDE, 2,
                new LeatherMaking(new Item(SNAKESKIN_BANDANA), 60, 25, HIDE_CRAFT),
                new LeatherMaking(new Item(SNAKESKIN_BODY), 69, 50, HIDE_CRAFT),
                new LeatherMaking(new Item(SNAKESKIN_BOOTS), 65, 135, HIDE_CRAFT),
                new LeatherMaking(new Item(SNAKESKIN_VAMBRACES), 70, 150, HIDE_CRAFT)),
        MAGIC(YAK_HIDE, 2,
                new LeatherMaking(new Item(YAK_HIDE_ARMOUR), 75, 30, HIDE_CRAFT)),;

        static Map<Integer, HideData> hides = new HashMap<>();

        static {
            for (HideData l : HideData.values()) {
                hides.put(l.getHideId(), l);
                hides.put(l.getAmount(), l);
            }
        }

        private final int hideId;
        private final int amount;
        private final LeatherMaking[] result;

        HideData(int hideId, int amount, LeatherMaking... result) {
            this.hideId = hideId;
            this.amount = amount;
            this.result = result;
        }

        public int getAmount() {
            return amount;
        }

        public int getHideId() {
            return hideId;
        }

        public LeatherMaking[] getResult() {
            return result;
        }
    }
}
