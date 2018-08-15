package com.overload.net.packet.impl;

import com.overload.game.content.clan.ClanChatManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.ChatMessage;
import com.overload.game.model.dialogue.DialogueManager;
import com.overload.game.model.logs.HandleLogs;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketConstants;
import com.overload.net.packet.PacketListener;
import com.overload.util.Misc;
import com.overload.util.PlayerPunishment;

/**
 * This packet listener manages the spoken text by a player. Either sent to the
 * regular chatbox or to a clanchat channel.
 *
 * @author Gabriel Hannason
 */

public class ChatPacketListener implements PacketListener {

	private static boolean allowChat(Player player, String text) {
		if (text == null || text.isEmpty()) {
			return false;
		}
		if (PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
			player.getPacketSender().sendMessage("You are muted and cannot chat.");
			return false;
		}
		if (Misc.blockedWord(text)) {
			DialogueManager.sendStatement(player, "A word was blocked in your sentence. Please do not repeat it!");
			return false;
		}
		return true;
	}

	@Override
	public void handleMessage(Player player, Packet packet) {

		if (!player.getMessageBlock().elapsed(5000)) {
			player.sendMessage("@red@Your messages have been blocked for spamming, stop typing for 5 seconds.");
			player.getMessageBlock().setTime(5000);
			return;
		}
		
		if (player.getMessageCount() >= 5) {
			player.getMessageBlock().setTime(5000);
			player.setMessageCount(0);
			player.sendMessage("@red@Your messages have been blocked for spamming, stop typing for 5 seconds.");
			return;
		}
		
		if (player.getMessageDelay().elapsed(750)) {
			player.setMessageCount(0);
			player.getMessageDelay().setTime(750);
		} else {
			player.incrementMessageCount();
			player.getMessageDelay().setTime(750);
		}
		
		switch (packet.getOpcode()) {
		case PacketConstants.CLAN_CHAT_OPCODE:
			String clanMessage = packet.readString();
			if (!allowChat(player, clanMessage)) {
				return;
			}
			ClanChatManager.sendMessage(player, clanMessage);
			HandleLogs.executeLog(player, "[Clan] " + clanMessage, HandleLogs.LogType.CHAT);
			break;
		case PacketConstants.REGULAR_CHAT_OPCODE:
			int color = packet.readByte();
			int effect = packet.readByte();
			String chatMessage = packet.readString();
			if (!allowChat(player, chatMessage)) {
				return;
			}
			if (player.getChatMessageQueue().size() >= 5) {
				return;
			}
			player.getChatMessageQueue().add(new ChatMessage(color, effect, chatMessage));
			HandleLogs.executeLog(player, chatMessage, HandleLogs.LogType.CHAT);
			break;
		}
	}
}
