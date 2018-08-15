package com.overload.game.model.syntax.impl;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.PlayerStatus;
import com.overload.game.model.container.shop.ShopManager;
import com.overload.game.model.syntax.EnterSyntax;

public class SellX implements EnterSyntax {

    private final int slot, itemId;

    public SellX(int itemId, int slot) {
        this.itemId = itemId;
        this.slot = slot;
    }

    @Override
    public void handleSyntax(Player player, String input) {

    }

    @Override
    public void handleSyntax(Player player, int input) {
        if (player.getStatus() == PlayerStatus.SHOPPING) {
            ShopManager.sellItem(player, slot, itemId, input);
        }
    }

}
