package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Rune extends Command {
    /**
     * The command which is to be executed when it's called.
     *
     * @param player The player to whom the command should be applied.
     * @param input  Any additional parameters.
     */
    @Override
    public void execute(Player player, String input) {

        player.getInventory().add(554, 100000);
        player.getInventory().add(555, 100000);
        player.getInventory().add(556, 100000);
        player.getInventory().add(557, 100000);
        player.getInventory().add(558, 100000);
        player.getInventory().add(559, 100000);
        player.getInventory().add(560, 100000);
        player.getInventory().add(561, 100000);
        player.getInventory().add(562, 100000);
        player.getInventory().add(563, 100000);
        player.getInventory().add(564, 100000);
        player.getInventory().add(565, 100000);
        player.getInventory().add(566, 100000);

    }
}
