package com.overload.game.model.syntax.impl;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.syntax.EnterSyntax;
import com.overload.net.packet.impl.SpawnItemPacketListener;

public class SpawnX implements EnterSyntax {

    private int item_id;
    private boolean toBank;

    public SpawnX(int item_id, boolean toBank) {
        this.item_id = item_id;
        this.toBank = toBank;
    }

    @Override
    public void handleSyntax(Player player, String input) {
    }

    @Override
    public void handleSyntax(Player player, int input) {
        SpawnItemPacketListener.spawn(player, item_id, input, toBank);
    }
}
