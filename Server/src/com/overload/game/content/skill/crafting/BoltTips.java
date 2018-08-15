package com.overload.game.content.skill.crafting;

import com.overload.game.model.*;
import com.overload.util.ItemIdentifiers;

import java.util.HashMap;
import java.util.Map;

public class BoltTips extends ItemIdentifiers {

    public enum BoltTip {
        B1(new Item(OPAL_BOLT_TIPS, 12), new Item(OPAL), 11, 2, new AnimationLoop(new Animation(890), 3)),
        B2(new Item(JADE_BOLT_TIPS, 12), new Item(JADE), 26, 3, new AnimationLoop(new Animation(891), 3)),
        B11(new Item(PEARL_BOLT_TIPS, 24), new Item(OYSTER_PEARL), 41, 4, new AnimationLoop(new Animation(891), 3)),
        B3(new Item(TOPAZ_BOLT_TIPS, 12), new Item(RED_TOPAZ), 48, 4, new AnimationLoop(new Animation(892), 3)),
        B4(new Item(SAPPHIRE_BOLTS, 12), new Item(SAPPHIRE), 56, 4, new AnimationLoop(new Animation(888), 3)),
        B5(new Item(EMERALD_BOLT_TIPS, 12), new Item(EMERALD), 58, 6, new AnimationLoop(new Animation(889), 3)),
        B6(new Item(RUBY_BOLT_TIPS, 12), new Item(RUBY), 63, 7, new AnimationLoop(new Animation(887), 3)),
        B7(new Item(DIAMOND_BOLT_TIPS, 12), new Item(DIAMOND), 65, 9, new AnimationLoop(new Animation(886), 3)),
        B8(new Item(DRAGON_BOLT_TIPS, 12), new Item(DRAGONSTONE), 71, 9, new AnimationLoop(new Animation(885), 3)),
        B9(new Item(ONYX_BOLT_TIPS, 24), new Item(ONYX), 73, 10, new AnimationLoop(new Animation(885), 3)),
        B10(new Item(AMETHYST_BOLT_TIPS, 15), new Item(AMETHYST), 83, 60, new AnimationLoop(new Animation(885), 3)),
        ;

        private final Item cut;
        private final Item bolt;
        private final int level;
        private final int exp;
        private final AnimationLoop animLoop;

        BoltTip(Item cut, Item bolt, int level, int exp, AnimationLoop animLoop) {
            this.cut = cut;
            this.bolt = bolt;
            this.level = level;
            this.exp = exp;
            this.animLoop = animLoop;
        }

        public Item getCut() {
            return cut;
        }

        public Item getUncut() {
            return bolt;
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

        static Map<Integer, BoltTip> map = new HashMap<>();
        static {
            for(BoltTip c : BoltTip.values()) {
                map.put(c.getUncut().getId(), c);
            }
        }
    }
}

