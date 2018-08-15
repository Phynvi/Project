package com.overload.game.content.minigames;

import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;

public interface Minigame {

	/**
	 * Handles death
	 */
	public boolean handleDeath(Player player);

	/**
	 * Handles teleport
	 * 
	 * @return can teleport
	 */
	public boolean handleTeleport(Player player);

	/**
	 * Handles logout
	 * 
	 * @param logout
	 *            sends logout action
	 * @return can logout
	 */
	public boolean handleLogout(Player player, boolean logout);

	/**
	 * Lose items on death
	 * 
	 * @return lose items
	 */
	public boolean safeMinigame(Player player);

	/**
	 * If players can fight in this minigame
	 * 
	 * @param player
	 *            the player
	 * @return can fight
	 */
	public boolean canFight(Player player);

	/**
	 * Killed the other npc
	 * 
	 * @param player
	 *            the player
	 * @param npc
	 *            the npc
	 */
	public void killedNpc(Player player, NPC npc);

	/**
	 * Killed the other player
	 * 
	 * @param player
	 *            the player
	 * @param otherPlayer
	 *            the other player
	 */
	public void killedPlayer(Player player, Player otherPlayer);

	/**
	 * Sends the interface
	 * 
	 * @param player
	 *            the player
	 */
	public void sendInterface(Player player);

	/**
	 * Gets the allowed type
	 * 
	 * @return the allowed type
	 */
	public MinigameManager.AllowedType getAllowedType();
}
