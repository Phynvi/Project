package com.overload.game.model.syntax.impl;

import com.overload.game.content.clan.ClanChatManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.syntax.EnterSyntax;

public class JoinClanChat implements EnterSyntax {

    @Override
    public void handleSyntax(Player player, String input) {
        ClanChatManager.join(player, input);
    }

    @Override
    public void handleSyntax(Player player, int input) {

    }
}
