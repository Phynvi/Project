package com.overload.net.packet.impl.commands.moderator;

import java.util.Optional;

import com.overload.game.World;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;
import com.overload.util.PlayerPunishment;

public class Mute extends Command {

	@SuppressWarnings("unused")
	@Override
	public void execute(Player c, String input) {
		try {
			String[] args = input.split(" ");
			if (args.length != 3) {
				throw new IllegalArgumentException();
			}
			String name = args[0].replace("_", " ");
			int duration = Integer.parseInt(args[1]);
			long muteEnd;
			if (duration == 0) {
				muteEnd = Long.MAX_VALUE;
			} else {
				muteEnd = System.currentTimeMillis() + duration * 1000 * 60;
			}
			String reason = args[2].replace("_", " ");
			
			Optional<Player> optionalPlayer = World.getPlayerByName(name);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				if (c2.getRights().isOrInherits(c.getRights().getPrimary())) {
					c.getPacketSender().sendMessage("You can't punish members of staff with a greater or equal rank than you!");
					return;
				}
				PlayerPunishment.mute(c2.getUsername());
				if (duration == 0) {
					c2.getPacketSender().sendMessage("@red@You have been permanently muted by: " + c.getUsername() + ".");
					c.getPacketSender().sendMessage("Successfully permanently " + c2.getUsername() + " for " + duration + " minutes.");
					// TODO: Log handling
				} else {
					c2.getPacketSender().sendMessage("@red@You have been muted by: " + c.getUsername() + " for " + duration + " minutes");
					c.getPacketSender().sendMessage("Successfully muted " + c2.getUsername() + " for " + duration + " minutes.");
					// TODO: Log handling
				}
			} else {
				c.getPacketSender().sendMessage(name + " is not online. You can only mute online players.");
			}
		} catch (Exception e) {
			c.getPacketSender().sendMessage("Error. Correct syntax: ::mute player duration reason.");
		}
	}

}
