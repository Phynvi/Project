package com.overload.game.model.dialogue;

import com.overload.game.entity.impl.player.Player;

/**
 * An abstract class for handling dialogue options.
 *
 * @author Professor Oak
 */
public abstract class DialogueOptions {

    public abstract void handleOption(Player player, int option);
}
