package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Position;
import com.overload.net.packet.impl.commands.Command;

public class Boss extends Command {
    @Override
    public void execute(Player player, String input) {
        switch (input.toLowerCase()) {
            case "zulrah":
                player.moveTo(new Position(2268, 3069, player.getIndex() * 4));
                break;
            case "corp":
                player.moveTo(new Position(2974, 4384, 2));
                break;
            case "vorkath":
                player.moveTo(new Position(2274, 4042, player.getIndex() * 4));
                break;
            case "skorpia":
                player.moveTo(new Position(3232, 10339, player.getIndex() * 4));
                break;
        }
    }
}
