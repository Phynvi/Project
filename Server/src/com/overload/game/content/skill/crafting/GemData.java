package com.overload.game.content.skill.crafting;

import com.overload.game.model.Animation;
import com.overload.game.model.AnimationLoop;
import com.overload.game.model.Item;
import com.overload.util.ItemIdentifiers;

import java.util.HashMap;
import java.util.Map;

public class GemData extends ItemIdentifiers {

    public enum CraftableGem {
        G1(new Item(OPAL), new Item(UNCUT_OPAL), 1, 15, new AnimationLoop(new Animation(890), 3)),
        G2(new Item(JADE), new Item(UNCUT_JADE), 13, 20, new AnimationLoop(new Animation(891), 3)),
        G3(new Item(RED_TOPAZ), new Item(UNCUT_RED_TOPAZ), 16, 25, new AnimationLoop(new Animation(892), 3)),
        G4(new Item(SAPPHIRE), new Item(UNCUT_SAPPHIRE), 20, 50, new AnimationLoop(new Animation(888), 3)),
        G5(new Item(EMERALD), new Item(UNCUT_EMERALD), 27, 68, new AnimationLoop(new Animation(889), 3)),
        G6(new Item(RUBY), new Item(UNCUT_RUBY), 34, 85, new AnimationLoop(new Animation(887), 3)),
        G7(new Item(DIAMOND), new Item(UNCUT_DIAMOND), 43, 108, new AnimationLoop(new Animation(886), 3)),
        G8(new Item(DRAGONSTONE), new Item(UNCUT_DRAGONSTONE), 55, 138, new AnimationLoop(new Animation(885), 3)),
        G9(new Item(ONYX), new Item(UNCUT_ONYX), 67, 168, new AnimationLoop(new Animation(885), 3)),
        G10(new Item(ZENYTE), new Item(UNCUT_ZENYTE), 89, 200, new AnimationLoop(new Animation(885), 3)),
        ;

        private final Item cut;
        private final Item uncut;
        private final int level;
        private final int exp;
        private final AnimationLoop animLoop;

        CraftableGem(Item cut, Item uncut, int level, int exp, AnimationLoop animLoop) {
            this.cut = cut;
            this.uncut = uncut;
            this.level = level;
            this.exp = exp;
            this.animLoop = animLoop;
        }

        public Item getCut() {
            return cut;
        }

        public Item getUncut() {
            return uncut;
        }

        public int getLevel() {
            return level;
        }

        public int getExp() {
            return exp;
        }

        public AnimationLoop getAnimationLoop() {
            return animLoop;
        }

        static Map<Integer, CraftableGem> map = new HashMap<>();
        static {
            for(CraftableGem c : CraftableGem.values()) {
                map.put(c.getUncut().getId(), c);
            }
        }
    }
}

