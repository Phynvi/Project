package com.overload.net.packet.impl.commands.developer;

import com.overload.game.collision.Region;
import com.overload.game.collision.RegionManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

import java.util.Optional;

public class Getregion extends Command {
    /**
     * The command which is to be executed when it's called.
     *
     * @param player The player to whom the command should be applied.
     * @param input  Any additional parameters.
     */
    @Override
    public void execute(Player player, String input) {
        Optional<Region> region = RegionManager.getRegion(player.getX(), player.getY());
        player.sendMessage(
                "Region ID: " + (region.isPresent() ? region.get().getRegionId() : "UNKNOWN_REGION")
        );
    }
}
