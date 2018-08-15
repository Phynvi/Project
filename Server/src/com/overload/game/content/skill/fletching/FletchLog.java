package com.overload.game.content.skill.fletching;

import com.overload.game.model.Animation;
import com.overload.game.model.Item;
import com.overload.util.ItemIdentifiers;

import java.util.HashMap;
import java.util.Map;

public class FletchLog extends ItemIdentifiers {

    private static final Animation CUTTING_LOGS_ANIM = new Animation(1248);

    public enum FletchableLog {
        REGULAR(LOGS,
                new Fletching.FletchableItem(new Item(ARROW_SHAFT, 15), 1, 5, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(WOODEN_STOCK), 9, 6, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(SHORTBOW_U_), 5, 10, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(LONGBOW_U_), 10, 20, CUTTING_LOGS_ANIM)),
        OAK(OAK_LOGS,
                new Fletching.FletchableItem(new Item(ARROW_SHAFT, 30), 15, 10, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(OAK_STOCK), 24, 16, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(OAK_SHORTBOW_U_), 20, 33, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(OAK_LONGBOW_U_), 25, 50, CUTTING_LOGS_ANIM)),
        WILLOW(WILLOW_LOGS,
                new Fletching.FletchableItem(new Item(ARROW_SHAFT, 45), 30, 15, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(WILLOW_STOCK), 39, 22, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(WILLOW_SHORTBOW_U_), 35, 66, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(WILLOW_LONGBOW_U_), 40, 83, CUTTING_LOGS_ANIM)),
        MAPLE(MAPLE_LOGS,
                new Fletching.FletchableItem(new Item(ARROW_SHAFT, 60), 45, 20, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(MAPLE_STOCK), 54, 32, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(MAPLE_SHORTBOW_U_), 50, 100, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(MAPLE_LONGBOW_U_), 55, 116, CUTTING_LOGS_ANIM)),
        YEW(YEW_LOGS,
                new Fletching.FletchableItem(new Item(ARROW_SHAFT, 75), 60, 25, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(YEW_STOCK), 69, 50, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(YEW_SHORTBOW_U_), 65, 135, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(YEW_LONGBOW_U_), 70, 150, CUTTING_LOGS_ANIM)),
        MAGIC(MAGIC_LOGS,
                new Fletching.FletchableItem(new Item(ARROW_SHAFT, 90), 75, 30, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(MAGIC_SHORTBOW_U_), 80, 166, CUTTING_LOGS_ANIM),
                new Fletching.FletchableItem(new Item(MAGIC_LONGBOW_U_), 85, 183, CUTTING_LOGS_ANIM)),;

        static Map<Integer, FletchableLog> logs = new HashMap<>();

        static {
            for (FletchableLog l : FletchableLog.values()) {
                logs.put(l.getLogId(), l);
            }
        }

        private final int logId;
        private final Fletching.FletchableItem[] fletchable;

        FletchableLog(int logId, Fletching.FletchableItem... fletchable) {
            this.logId = logId;
            this.fletchable = fletchable;
        }

        public int getLogId() {
            return logId;
        }

        public Fletching.FletchableItem[] getFletchable() {
            return fletchable;
        }
    }
}
