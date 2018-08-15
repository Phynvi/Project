package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Graphic;
import com.overload.net.packet.impl.commands.Command;

public class Anim extends Command {

    @Override
    public void execute(Player player, String input) {
        if (input == "" || input == null)
            return;
        String[] args = input.split(" ");
        if (args.length > 0) {
            int anim = Integer.parseInt(args[0]);
            if (anim < 0)
                return;
            player.performAnimation(new Animation(anim));

            if (args.length > 1) {
                int gfx = Integer.parseInt(args[1]);
                player.getPacketSender().sendGraphic(new Graphic(gfx), player.getPosition());
            }
        }
    }
}
