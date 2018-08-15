package com.overload.game.model.syntax.impl;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.container.impl.Bank;
import com.overload.game.model.syntax.EnterSyntax;

public class SearchBank implements EnterSyntax {

    @Override
    public void handleSyntax(Player player, String input) {
        Bank.search(player, input);
    }

    @Override
    public void handleSyntax(Player player, int input) {

    }

}
