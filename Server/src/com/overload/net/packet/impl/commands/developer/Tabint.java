package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Tabint extends Command {
    @Override
    public void execute(Player player, String input) {
        player.getPacketSender().sendTabInterface(3, Integer.parseInt(input));
    }
}
