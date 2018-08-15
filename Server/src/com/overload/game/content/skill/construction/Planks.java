package com.overload.game.content.skill.construction;

import com.overload.util.ItemIdentifiers;

public enum Planks {
    NORMAL(ItemIdentifiers.LOGS, ItemIdentifiers.PLANK),
    MAHOGANY(ItemIdentifiers.MAHOGANY_LOGS, ItemIdentifiers.MAHOGANY_PLANK),
    OAK(ItemIdentifiers.OAK_LOGS, ItemIdentifiers.OAK_PLANK),
    TEAK(ItemIdentifiers.TEAK_LOGS, ItemIdentifiers.TEAK_PLANK),
    ;

    private int logId;
    private int plankId;

    Planks(int logId, int plankId) {
        this.logId = logId;
        this.plankId = plankId;
    }

    public static Planks getPlank(int logId) {
        for (Planks plank : Planks.values())
            if (plank.getLogId() == logId)
                return plank;
        return null;
    }

    public int getLogId() {
        return logId;
    }

    public int getPlankId() {
        return plankId;
    }
}
