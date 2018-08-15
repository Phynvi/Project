package com.overload.game.model.syntax;

import com.overload.game.entity.impl.player.Player;

public interface EnterSyntax {

    public abstract void handleSyntax(Player player, String input);

    public abstract void handleSyntax(Player player, int input);
}
