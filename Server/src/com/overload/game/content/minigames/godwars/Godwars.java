package com.overload.game.content.minigames.godwars;

import java.util.HashMap;
import java.util.Map;

import com.overload.game.content.minigames.godwars.GodWarsData.Allegiance;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Position;
import com.overload.net.packet.PacketSender;
import com.overload.util.Misc;

public class Godwars {

	private static final int KC_REQUIRED = 40;

	private Player player;
	private Map<Allegiance, Integer> killcount;

	public Godwars(Player player) {
		this.player = player;
		initialize();
	}

	/**
	 * Sets all killcount values to 0.
	 */
	public void initialize() {
		killcount = new HashMap<>();
		for (Allegiance god : Allegiance.values()) {
			killcount.put(god, 0);
		}
	}

	/**
	 * Handles entering a boss room.
	 * 
	 * @param god The god to which the room belongs to.
	 */
	public boolean enterBossRoom(Allegiance god) {
		if (killcount.get(god) >= KC_REQUIRED) {
			killcount.put(god, killcount.get(god) - KC_REQUIRED);
		} else if (player.getInventory().contains(11942)) {
			player.getInventory().delete(11942, 1);
		} else {
			player.getPacketSender().sendMessage("You need to kill " + (KC_REQUIRED - killcount.get(god)) + " more " + Misc.capitalizeWords(god.name()) + " creatures before you can enter.");
			return false;
		}
		Position pos = player.getPosition();
		switch (god) {
		case SARADOMIN:
			pos = new Position(2907, 5265, 0);
			break;
		case ZAMORAK:
			pos = new Position(2925, 5331, 2);
			break;
		case BANDOS:
			pos = new Position(2864, 5354, 2);
			break;
		case ARMADYL:
			pos = new Position(2839, 5296, 2);
			break;
		}
		player.moveTo(pos);
		return true;
	}

	/**
	 * Increases the amount of minions slain of a certain god.
	 * 
	 * @param god The god of which the killcount should be increased.
	 */
	public void increaseKillcount(Allegiance god) {
		killcount.put(god, killcount.get(god) + 1);
		drawInterface();
	}

	public void increaseKillcountByTeleportationDevice(Allegiance god, int amount) {
		killcount.put(god, killcount.get(god) + amount);
	}

	/**
	 * Updates the killcount values on the interface.
	 */
	public void drawInterface() {
		PacketSender assistant = player.getPacketSender();
		assistant.sendString(65006, Integer.toString(killcount.get(Allegiance.ARMADYL)));
		assistant.sendString(65007, Integer.toString(killcount.get(Allegiance.BANDOS)));
		assistant.sendString(65008, Integer.toString(killcount.get(Allegiance.SARADOMIN)));
		assistant.sendString(65009, Integer.toString(killcount.get(Allegiance.ZAMORAK)));
	}
}
