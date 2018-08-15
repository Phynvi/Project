package com.overload.game.content.minigames.wintertodt;

import com.overload.game.entity.impl.player.Player;

public enum WarmClothes {

    PYROMANCER_GARB(20704),
    PYROMANCER_ROBE(20706),
    PYROMANCER_HOOD(20708),
    PYROMANCER_BOOTS(20710);

    private final int itemId;

    WarmClothes(int itemId) {
        this.itemId = itemId;
    }

    public static int getAmount(Player player) {
        int amount = 0;
        for (WarmClothes item : values()) {
            if (player.getEquipment().contains(item.itemId)) {
                amount++;
            }
        }
        return amount > 4 ? 4 : amount;
    }

}