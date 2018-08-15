package com.overload.net.packet.impl.commands.all;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.overload.game.World;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.PlayerModes;
import com.overload.game.model.dialogue.DialogueManager;
import com.overload.game.model.rights.Right;
import com.overload.game.model.rights.RightGroup;
import com.overload.net.packet.impl.commands.Command;
import com.overload.util.Misc;
import com.overload.util.PlayerPunishment;

public class Yell extends Command {

	private static final Right[] PERMITTED = {
			Right.BRONZE_DONATOR,
			Right.IRON_DONATOR,
			Right.STEEL_DONATOR,
			Right.MITHRIL_DONATOR,
			Right.ADAMANT_DONATOR,
			Right.RUNITE_DONATOR,
			Right.DRACONIC_DONATOR,
			Right.SERVER_SUPPORT,
			Right.MODERATOR,
			Right.ADMINISTRATOR,
			Right.MANAGER,
			Right.GAME_DEVELOPER,
			Right.OWNER
	};

	@Override
	public void execute(Player player, String input) {
		RightGroup rights = player.getRights();
		PlayerModes mode = player.getMode();

		Set<Right> prohibited = new HashSet<>(Arrays.asList(PERMITTED));

		long count = rights.getSet().stream().filter(r -> prohibited.stream().anyMatch(r::isOrInherits)).count();

		if (count == 0) {
			player.getPacketSender().sendMessage("You do not have the rights to access this command.");
			return;
		}
		
		if (rights.getPrimary() == Right.PLAYER || mode == PlayerModes.ULTIMATE) {
			player.getPacketSender().sendMessage("You do not have the rights to access this command");
			return;
		}

        if (PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
            player.getPacketSender().sendMessage("You are muted and cannot yell.");
            return;
        }
        if (!player.getYellDelay().finished()) {
            player.getPacketSender().sendMessage("You must wait another " + player.getYellDelay().secondsRemaining() + " seconds to do that.");
            return;
        }
        if (Misc.blockedWord(input)) {
            DialogueManager.sendStatement(player, "A word was blocked in your sentence. Please do not repeat it!");
            return;
        }
		String message = formatMessage(StringUtils.capitalize(player.getUsername().toLowerCase()), player.getRights().getPrimary().getColor(),player.getRights().getPrimary(), StringUtils.capitalize(input));

        int yellDelay = getDelay(player);
        if (yellDelay > 0) {
            player.getYellDelay().start(yellDelay);
        }
        
		World.sendMessage(message);
	}

	private String formatMessage(String username, String color, Right rights, String message) {
		String title = Misc.formatName(rights.name().toLowerCase().replace("_", " "));
		return "[<col=" + color + ">" + title + "</col>] <img=" + rights.getIconId() + "> " +username+ ": " + StringUtils.capitalize(message.toLowerCase());
	}

	private int getDelay(Player player) {
		RightGroup rights = player.getRights();

		if (rights.isOrInherits(Right.MODERATOR)) {
			return 0;
		}
		return player.getRights().isOrInherits(Right.DRACONIC_DONATOR) ? 1  :
			   player.getRights().isOrInherits(Right.RUNITE_DONATOR)   ? 5  :
			   player.getRights().isOrInherits(Right.ADAMANT_DONATOR)  ? 10 :
			   player.getRights().isOrInherits(Right.MITHRIL_DONATOR)  ? 15 :
			   player.getRights().isOrInherits(Right.STEEL_DONATOR)    ? 30 :
			   player.getRights().isOrInherits(Right.IRON_DONATOR)     ? 45 : 60;
	}


	@Override
	public Optional<String> getDescription() {
		return Optional.of("Sends a global chat message");
	}

	@Override
	public Optional<String> getParameter() {
		return Optional.of("message");
	}

	@Override
	public boolean isHidden() {
		return true;
	}
}
