package com.overload.game.content.clan;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.grounditem.ItemOnGroundManager;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Item;
import com.overload.game.model.PlayerModes;
import com.overload.game.model.Position;
import com.overload.game.model.rights.Right;
import com.overload.game.model.syntax.impl.JoinClanChat;
import com.overload.game.model.syntax.impl.NameClanChat;
import com.overload.util.Misc;
import com.overload.util.PlayerPunishment;

/**
 * 100% Runescape clanchat system.
 * @author Adam Maxwell (Jowcey)
 */
public class ClanChatManager {

	private static final String FILE_DIRECTORY = "./data/saves/clans/";

	private static ClanChat[] clans = new ClanChat[3000];

	public static ClanChat[] getClans() {
		return clans;
	}

	public static ClanChat getClanChat(int index) {
		return clans[index];
	}

	public static ClanChat getClanChatChannel(Player player) {
		for(ClanChat clan : clans) {
			if(clan == null || clan.getOwnerName() == null)
				continue;
			if(clan.getOwnerName().equals(player.getUsername())) {
				return clan;
			}
		}
		return null;
	}

	public static void init() {
		try {
			File dir = (new File(FILE_DIRECTORY));
			if (!dir.exists()) {
				dir.mkdir();
			}
			for (File file : dir.listFiles()) {
				if(!file.exists())
					continue;
				DataInputStream input = new DataInputStream(new FileInputStream(file));
				String name = input.readUTF();
				String owner = input.readUTF();
				int index = input.readShort();
				ClanChat clan = new ClanChat(owner, owner, index);
				clan.setName(name);
				clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_ENTER, ClanChatRank.forId(input.read()));
				clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_KICK, ClanChatRank.forId(input.read()));
				clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_TALK, ClanChatRank.forId(input.read()));
				int totalRanks = input.readShort();
				for (int i = 0; i < totalRanks; i++) {
					clan.getRankedNames().put(input.readUTF(), ClanChatRank.forId(input.read()));
				}
				int totalBans = input.readShort();
				for (int i = 0; i < totalBans; i++) {
					clan.addBannedName(input.readUTF());
				}
				clans[index] = clan;
				input.close();
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void writeFile(ClanChat clan) {
		try {
			File file = new File(FILE_DIRECTORY + clan.getOwnerName());
			if (file.exists())
				file.createNewFile();
			DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
			output.writeUTF(clan.getName());
			output.writeUTF(clan.getOwnerName());
			output.writeShort(clan.getIndex());
			output.write(clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER] != null ? clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER].ordinal() : -1);
			output.write(clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK] != null ? clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK].ordinal() : -1);
			output.write(clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK] != null ? clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK].ordinal() : -1);
			output.writeShort(clan.getRankedNames().size());
			for (Entry<String, ClanChatRank> iterator : clan.getRankedNames().entrySet()) {
				String name = iterator.getKey();
				int rank = iterator.getValue().ordinal();
				output.writeUTF(name);
				output.write(rank);
			}
			output.writeShort(clan.getBannedNames().size());
			for(BannedMember ban : clan.getBannedNames()) {
				output.writeUTF(ban.getName());
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		for (ClanChat clan : clans) {
			if (clan != null) {
				writeFile(clan);
			}
		}
	}

	public static void createClan(Player player, String name) {
		player.getPacketSender().sendInterfaceRemoval();
		if(getClanChatChannel(player) != null) {
			player.getPacketSender().sendMessage("You have already created a clanchat channel.");
			return;
		}
		File file = new File(FILE_DIRECTORY + player.getUsername());
		if (file.exists())
			file.delete();
		ClanChat createdCc = create(player, name);
		if(createdCc != null) {
			if(player.getCurrentClanChat() == null) {
				join(player, createdCc);
			}
			player.getPacketSender().sendMessage("You now have a clanchat channel. To enter the chat, simply use your name as keyword.");
		}
	}

	public static void deleteClan(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		if (player.getCurrentClanChat() != null) {
			player.getPacketSender().sendMessage("Please leave the clanchat channel you are currently in before doing this.");
			return;
		}
		if(getClanChatChannel(player) == null) {
			player.getPacketSender().sendMessage("You have not created a clanchat channel yet.");
			return;
		}
		delete(player);
	}

	public static ClanChat create(Player player, String name) {
		File file = new File(FILE_DIRECTORY + player.getUsername());
		if (file.exists()) {
			player.getPacketSender().sendMessage("Your clan channel is already public!");
			return null;
		}
		int index = getIndex();
		if (index == -1) { // Too many clans
			player.getPacketSender().sendMessage("An error occured! Please contact an administrator and report this.");
			return null;
		}

		clans[index] = new ClanChat(player, name, index);
		clans[index].getRankedNames().put(player.getUsername(), ClanChatRank.OWNER);
		
		if (player.getCurrentClanChat() == null) {
			join(player, clans[index]);
		} else {
			player.getPacketSender().sendMessage("You have created a clan chat channel. To join it, simply enter your username as").sendMessage("keyword.");
		}

		writeFile(clans[index]);
		return clans[index];
	}

	public static void join(Player player, String channel) {
		if(channel == null || channel.equals("") || channel.equals("null")) {
			return;
		}
		if (player.getCurrentClanChat() != null) {
			player.getPacketSender().sendMessage("You are already in a clan channel.");
			return;
		}
		channel = channel.toLowerCase();
		for (ClanChat clan : clans) {
			if(clan == null) {
				continue;
			}
			if(clan.getName().toLowerCase().equals(channel) || 
					clan.getOwnerName().toLowerCase().equals(channel)) {

				join(player, clan);
				return;
			}
		}

		player.getPacketSender().sendMessage("That channel does not exist.");
	}

	public static void updateList(ClanChat clan) {
		Collections.sort(clan.getMembers(), new Comparator<Player>() {
			@Override
			public int compare(Player o1, Player o2) {
				ClanChatRank rank1 = clan.getRank(o1);
				ClanChatRank rank2 = clan.getRank(o2);
				if(rank1 == null && rank2 == null) {
					return 1;
				}
				if(rank1 == null && rank2 != null) {
					return 1;
				} else if(rank1 != null && rank2 == null) {
					return -1;
				}
				if(rank1.ordinal() == rank2.ordinal()) {
					return 1;
				}
				if(rank1 == ClanChatRank.OWNER) {
					return -1;
				} else if(rank2 == ClanChatRank.OWNER) {
					return 1;
				}
				if(rank1.ordinal() > rank2.ordinal()) {
					return -1;
				}
				return 1;
			}
		});
		
		for (Player member : clan.getMembers()) {
			if (member != null) {
				int childId = 37144;
				for (Player others : clan.getMembers()) {
					if (others != null) {
						ClanChatRank rank = clan.getRank(others);

						int image = -1;
						if(rank != null) {
							image = rank.getSpriteId();
						}

						String prefix = image >= 0 ? ("<img=" + (image) +  ">") : "";
						String rightImage = others.getRights().getIconId() != -1 ? ("<img=" + (others.getRights().getIconId()) +  ">") : "";
						String modeImage = others.getMode().getIconId() != -1 ? ("<img=" + (others.getMode().getIconId()) +  ">") : "";
						member.getPacketSender().sendString(childId, prefix + rightImage + modeImage + others.getUsername());
						childId++;
					}
				}
				for (int i = childId; i < 37244; i++) {
					member.getPacketSender().sendString(i, "");
				}
				ClanChatRank rank = clan.getRank(member);

				if(rank != null) {
					if(rank == ClanChatRank.OWNER || rank == ClanChatRank.STAFF ||
							clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK] != null && 
							rank.ordinal() >= clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK].ordinal()) {

						member.getPacketSender().sendShowClanChatOptions(true);

					} else {
						member.getPacketSender().sendShowClanChatOptions(false);
					}
				} else {
					member.getPacketSender().sendShowClanChatOptions(false);
				}


			}
		}
	}

	public static void sendMessage(Player player, String message) {
		if(PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
			player.getPacketSender().sendMessage("You are muted and cannot chat.");
			return;
		}
		ClanChat clan = player.getCurrentClanChat();
		if (clan == null) {
			player.getPacketSender().sendMessage("You're not in a clanchat channel.");
			return;
		}
		ClanChatRank rank = clan.getRank(player);
		if(clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK] != null) {
			if (rank == null || rank.ordinal() < clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK].ordinal()) {
				player.getPacketSender().sendMessage("You do not have the required rank to speak in this channel.");
				return;
			}
		}
		String nameColor = "@bla@";
		String chatColor = "<col=993D00>";
		for (Player memberPlayer : clan.getMembers()) {
			if (memberPlayer != null) {
				if(memberPlayer.getRelations().getIgnoreList().contains(player.getLongUsername()))
					continue;

				String rankImage = player.getRights().getIconId() != -1 ? "<img="+player.getRights().getIconId()+"> " : " ";
				String modeImage = player.getMode().getIconId() != -1 ? "<img="+player.getMode().getIconId()+"> " : " ";
				memberPlayer.getPacketSender().sendSpecialMessage(player.getUsername(), 16, "@bla@[@blu@"+player.getCurrentClanChat().getName() +"@bla@]<col=3300CC> "+nameColor+""+rankImage+""+modeImage +
						Misc.capitalizeWords(player.getUsername()) + ": " + chatColor +
						Misc.capitalize(message));
			}
		}
	}

	public static void sendMessage(ClanChat clan, String message) {
		for (Player member : clan.getMembers()) {
			if (member != null) {
				member.getPacketSender().sendMessage(message);
			}
		}
	}

	public static void leave(Player player, boolean kicked) {
		final ClanChat clan = player.getCurrentClanChat();
		if (clan == null) {
			return;
		}
		player.getPacketSender().sendString(37139, "Talking in: N/A");
		player.getPacketSender().sendString(37140, "Owner: N/A");
		//player.getPacketSender().sendString(29454, "Lootshare: N/A");
		player.getPacketSender().sendString(37135, "Join Chat");
		player.setCurrentClanChat(null);
		clan.removeMember(player.getUsername());
		for (int i = 37144; i < 37244; i++) {
			player.getPacketSender().sendString(i, "");
		}
		player.getPacketSender().sendShowClanChatOptions(false);
		updateList(clan);
		if(kicked) {
			player.setClanChatName("");
		}
		player.getPacketSender().sendMessage(kicked ? "You have been kicked from the channel." : "You have left the channel.");
	}

	private static void join(Player player, ClanChat clan) {
		if (clan.getOwnerName().equals(player.getUsername())) {
			if (clan.getOwner() == null) {
				clan.setOwner(player);
			}
			clan.giveRank(player, ClanChatRank.OWNER);
		}
		player.getPacketSender().sendMessage("Attempting to join channel...");
		if (clan.getMembers().size() >= 100) {
			player.getPacketSender().sendMessage("This clan channel is currently full.");
			return;
		}
		if(clan.isBanned(player.getUsername())) {
			player.getPacketSender().sendMessage("You're currently banned from using this channel. Bans expire after 30 minutes.");
			return;
		}
		ClanChatRank rank = clan.getRank(player);

		//Give or Remove friends rank if needed.
		if(rank == null) {
			if(clan.getOwner() != null && clan.getOwner().getRelations().isFriendWith(player.getUsername())) {
				clan.giveRank(player, ClanChatRank.FRIEND);
			}
		} else {
			if(rank == ClanChatRank.FRIEND && clan.getOwner() != null && 
					!clan.getOwner().getRelations().isFriendWith(player.getUsername())) {
				clan.giveRank(player, null);
			}
		}

		//Give or Remove staff rank if needed.
		/*if (Player.getRights().isStaff()) {
			if(rank == null || rank != ClanChatRank.OWNER) {
				rank = ClanChatRank.STAFF;
				clan.giveRank(player, ClanChatRank.STAFF);
			}
		} else {
			if(rank != null && rank == ClanChatRank.STAFF) {
				clan.giveRank(player, null);
			}
		}*/

		if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER] != null) {
			if (rank == null || clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER].ordinal() > rank.ordinal()) {
				player.getPacketSender().sendMessage("Your rank is not high enough to enter this channel.");
				return;
			}
		}
		player.setCurrentClanChat(clan);
		player.setClanChatName(clan.getName());
		String clanName = Misc.capitalizeWords(clan.getName());
		clan.addMember(player);
		player.getPacketSender().sendString(37139, "Talking in: @whi@" + clanName);
		player.getPacketSender().sendString(37140, "Owner: " + Misc.capitalizeWords(clan.getOwnerName()));
		player.getPacketSender().sendString(37135, "Leave Chat");

		//	player.getPacketSender().sendString(29454, "Lootshare: "+getLootshareStatus(clan));
		player.getPacketSender().sendMessage("Now talking in "+clan.getOwnerName()+"'s channel.");
		player.getPacketSender().sendMessage("To talk start each line of chat with the / symbol.");
		updateList(clan);
	}

	public static void delete(Player player) {
		ClanChat clan = getClanChatChannel(player);
		File file = new File(FILE_DIRECTORY + clan.getName());
		for (Player member : clan.getMembers()) {
			if (member != null) {
				leave(member, true);
			}
		}
		if(player.getClanChatName().equalsIgnoreCase(clan.getName())) {
			player.setClanChatName("");
		}
		player.getPacketSender().sendMessage("Your clanchat channel was successfully deleted.");
		clans[clan.getIndex()] = null;
		file.delete();
	}

	public static void setName(Player player, String newName) {
		final ClanChat clan = getClanChatChannel(player);
		if (clan == null) {
			player.getPacketSender().sendMessage("You need to have a clan channel to do this.");
			return;
		}
		if(newName.length() == 0)
			return;
		if (newName.length() > 12)
			newName = newName.substring(0, 11);
		if(new File(FILE_DIRECTORY + newName).exists()) {
			player.getPacketSender().sendMessage("That clanchat name is already taken.");
			return;
		}
		if(clan.getLastAction().elapsed(5000) || player.getRights().isOrInherits(Right.MODERATOR)) {
			new File(FILE_DIRECTORY + clan.getName()).delete();
			clan.setName(Misc.capitalizeWords(newName));
			for(Player member : clan.getMembers()) {
				if(member == null)
					continue;
				member.setClanChatName(clan.getName());
				member.getPacketSender().sendString(37139, "Talking in: @whi@" + clan.getName());
			}
			sendClanSettingsInterface(player, clan);
			writeFile(clan);
			clan.getLastAction().reset();
		} else {
			player.getPacketSender().sendMessage("You need to wait a few seconds between every clanchat action.");
		}
	}

	public static void kick(Player player, Player target) {
		ClanChat clan = player.getCurrentClanChat();
		if (clan == null) {
			player.getPacketSender().sendMessage("You're not in a clan channel.");
			return;
		}
		final ClanChatRank rank = clan.getRank(player);
		if (rank == null || rank != ClanChatRank.STAFF && clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK] != null && rank.ordinal() < clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK].ordinal()) {
			player.getPacketSender().sendMessage("You do not have the required rank to kick this player.");
			return;
		}
		for (Player member : clan.getMembers()) {
			if (member != null && member.equals(target)) {
				ClanChatRank memberRank = clan.getRank(member);
				if (memberRank != null) {
					if(memberRank == ClanChatRank.STAFF) {
						player.getPacketSender().sendMessage("That player cannot be kicked.");
						break;
					}
					if(rank.ordinal() < memberRank.ordinal()) {
						player.getPacketSender().sendMessage("You cannot kick a player who has a higher rank than you!");
						break;
					}
				}
				leave(member, true);
				sendMessage(player.getCurrentClanChat(), "@bla@[@blu@"+player.getCurrentClanChat().getName() +"@bla@]<col=3300CC> "+member.getUsername()+" has been kicked from the channel by "+player.getUsername()+".");
				break;
			}
		}
	}
	
	public static void ban(Player player, Player target) {
		ClanChat clan = player.getCurrentClanChat();
		if (clan == null) {
			player.getPacketSender().sendMessage("You're not in a clan channel.");
			return;
		}
		final ClanChatRank rank = clan.getRank(player);
		if (rank == null || rank != ClanChatRank.STAFF && clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK] != null && rank.ordinal() < clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK].ordinal()) {
			player.getPacketSender().sendMessage("You do not have the required rank to kick this player.");
			return;
		}
		for (Player member : clan.getMembers()) {
			if (member != null && member.equals(target)) {
				ClanChatRank memberRank = clan.getRank(member);
				if (memberRank != null) {
					if(memberRank == ClanChatRank.STAFF) {
						player.getPacketSender().sendMessage("That player cannot be kicked.");
						break;
					}
					if(rank.ordinal() < memberRank.ordinal()) {
						player.getPacketSender().sendMessage("You cannot kick a player who has a higher rank than you!");
						break;
					}
				}
				clan.addBannedName(member.getUsername());
				leave(member, true);
				sendMessage(player.getCurrentClanChat(), "@bla@[@blu@"+player.getCurrentClanChat().getName() +"@bla@]<col=3300CC> "+member.getUsername()+" has been kicked from the channel by "+player.getUsername()+".");
				break;
			}
		}
	}

	public static boolean handleButton(Player player, int button, int menuId) {

		//Member options..

		if(player.isDebug()) {
			System.out.println("ClanChatManager: " + button + " - " + menuId);
		}
		
		if (handleClanChatSetupButton(player, button, menuId))
			return true;
		
		if(button >= 37144 && button < 37246) {
			final int index = button - 37144;

			if(index > 0) {
				if ((player.getCurrentClanChat() == null || !player.getCurrentClanChat().getOwnerName().equals(player.getUsername())) && menuId != 1) {
					player.getPacketSender().sendMessage("Only the clanchat owner can do that.");
					return true;
				}
				Player target = getPlayer(index, player.getCurrentClanChat());
				if(target == null || target.equals(player)) {
					return true;
				}

				switch(menuId) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
						ClanChatRank rank = ClanChatRank.forMenuId(menuId);
						ClanChatRank targetRank = player.getCurrentClanChat().getRank(target);
						if(targetRank != null) {
							if(targetRank == rank) {
								player.getPacketSender().sendMessage("That player already has that rank.");
								return true;
							}
							if(targetRank == ClanChatRank.STAFF) {
								player.getPacketSender().sendMessage("That player cannot be promoted or demoted.");
								return true;
							}
						}
						if(player.getCurrentClanChat().getLastAction().elapsed(5000) || player.getRights().isOrInherits(Right.MODERATOR)) {
							player.getCurrentClanChat().giveRank(target, rank);
							updateList(player.getCurrentClanChat());
							sendMessage(player.getCurrentClanChat(), "@bla@[@blu@"+player.getCurrentClanChat().getName() +"@bla@] <col=3300CC>"+target.getUsername()+" has been given the rank: "+Misc.formatText(rank.name().toLowerCase())+".");
							player.getCurrentClanChat().getLastAction().reset();
						} else {
							player.getPacketSender().sendMessage("You need to wait a few seconds between every clanchat action.");
						}
						break;
					case 6:
						targetRank = player.getCurrentClanChat().getRank(target);
						if(targetRank == null) {
							player.getPacketSender().sendMessage("That player has no rank.");
							return true;
						}
						if(targetRank == ClanChatRank.STAFF) {
							player.getPacketSender().sendMessage("That player cannot be promoted or demoted.");
							return true;
						}
						if(player.getCurrentClanChat().getLastAction().elapsed(5000) || player.getRights().isOrInherits(Right.MODERATOR)) {
							player.getCurrentClanChat().getRankedNames().remove(target.getUsername());
							updateList(player.getCurrentClanChat());
							sendMessage(player.getCurrentClanChat(), "@bla@[@blu@"+player.getCurrentClanChat().getName() +"@bla@]<col=3300CC> "+player.getUsername()+" has "+(player.getCurrentClanChat().getLootShare() ? "enabled" : "disabled")+" Coinshare.");
							player.getCurrentClanChat().getLastAction().reset();
						} else {
							player.getPacketSender().sendMessage("You need to wait a few seconds between every clanchat action.");
						}
						break;
					case 7:
						kick(player, target);
						break;
					case 8:
						ban(player, target);
						break;
				}
			}
			return true;
		}

		ClanChat clan = ClanChatManager.getClanChatChannel(player);
		//Other buttons..
		switch(button) {
			case 37132: //CC Setup
				sendClanSettingsInterface(player, clan);
				return true;
			case 37129: // Join / Leave clan
				if(player.getCurrentClanChat() == null) {
					player.setEnterSyntax(new JoinClanChat());
					player.getPacketSender().sendEnterInputPrompt("Which channel would you like to join?");
				} else {
					leave(player, false);
					player.setClanChatName("");
				}
				return true;
			case 37250:
				toggleLootShare(player);
				return true;
		}
		
		//Owner Specific Buttons
		if (clan != null) {
			switch (button) {
				case 45007:
					player.setEnterSyntax(new NameClanChat());
					player.getPacketSender().sendEnterInputPrompt("Enter a name for your Clan Chat");
					return true;
				case 45034:
					toggleCoinShare(player);
					return true;
			}
		} else {
			if (button == 45007) {
				player.setEnterSyntax(new NameClanChat());
				player.getPacketSender().sendEnterInputPrompt("Enter a name for your Clan Chat");
				return true;
			}
		}
		return false;
	}
	
	static int SETTINGS_CLAN_NAME = 45011,
			SETTINGS_RANK_TO_ENTER = 45016,
			SETTINGS_RANK_TO_TALK = 45021,
			SETTINGS_RANK_TO_KICK = 45026,
			SETTINGS_RANK_TO_LOOTSHARE = 45031,
			SETTINGS_COIN_SHARE = 45033;
	
	public static void sendClanSettingsInterface(Player player, ClanChat clan) {
		player.getPacketSender().sendInterfaceRemoval();
		if (clan == null) {
			player.getPacketSender().sendString(SETTINGS_CLAN_NAME, "No Clan");
			player.getPacketSender().sendString(SETTINGS_RANK_TO_ENTER, "");
			player.getPacketSender().sendString(SETTINGS_RANK_TO_TALK, "");
			player.getPacketSender().sendString(SETTINGS_RANK_TO_KICK, "");
			player.getPacketSender().sendString(SETTINGS_RANK_TO_LOOTSHARE, "");
		} else {
			player.getPacketSender().sendString(SETTINGS_CLAN_NAME, clan.getName());
			player.getPacketSender().sendConfigByte(1500, (clan.getCoinShare() ? 1 : 0));
			
			if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER] == null) {
				player.getPacketSender().sendString(SETTINGS_RANK_TO_ENTER, "Anyone");
			} else {
				player.getPacketSender().sendString(SETTINGS_RANK_TO_ENTER, Misc.formatText(clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER].name().toLowerCase())+"+");
			}
	
			if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK] == null) {
				player.getPacketSender().sendString(SETTINGS_RANK_TO_TALK, "Anyone");
			} else {
				player.getPacketSender().sendString(SETTINGS_RANK_TO_TALK, Misc.formatText(clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK].name().toLowerCase())+"+");
			}
	
			if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK] == null) {
				player.getPacketSender().sendString(SETTINGS_RANK_TO_KICK, "Only me");
			} else {
				player.getPacketSender().sendString(SETTINGS_RANK_TO_KICK, Misc.formatText(clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK].name().toLowerCase())+"+");
			}
	
			if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_LOOTSHARE] == null) {
				player.getPacketSender().sendString(SETTINGS_RANK_TO_LOOTSHARE, "Only me");
			} else {
				player.getPacketSender().sendString(SETTINGS_RANK_TO_LOOTSHARE, Misc.formatText(clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_LOOTSHARE].name().toLowerCase())+"+");
			}
		}
		
		player.getPacketSender().sendString(SETTINGS_COIN_SHARE, "Coin Share");
		
		int startName = 46005;
		int startRank = 46006;

		if (clan != null) {
			for (Player member : clan.getMembers()) {
				player.getPacketSender().sendString(startName, member.getUsername());
				ClanChatRank ccr = clan.getRank(member);
				if (ccr != null)
					player.getPacketSender().sendString(startRank, Misc.capitalizeWords(clan.getRank(member).name().toLowerCase()));
				else
					player.getPacketSender().sendString(startRank, "No Rank");
				startName += 2;
				startRank += 2;
			}
		}
		int endName = 46105;
		for (int i = startName; i <= endName; i++) {
			player.getPacketSender().sendString(startName, "");
			player.getPacketSender().sendString(startRank, "");
			startName += 2;
			startRank += 2;
		}
		
		player.getPacketSender().sendInterface(45000);
	}

	public static boolean dropShareLoot(Player player, NPC npc, List<Item> list) {
		for (Item i : list)
			dropShareLoot(player, npc, i);
		return true;
	}
	
	public static boolean dropShareLoot(Player player, NPC npc, Item itemDropped) {
		ClanChat clan = player.getCurrentClanChat();
		if (clan != null) {
			boolean received = false;
			List<Player> tmpPlayers = getPlayersWithinPosition(clan, npc.getPosition());
			List<Player> players = new ArrayList<Player>();
			for (int i = 0; i < tmpPlayers.size(); i++) {
				Player member = tmpPlayers.get(i);
				if (member != null) {
					if (member.getMode().isEqualOrHigher(PlayerModes.IRON_MAN)) {
						member.getPacketSender().sendMessage("@dre@Because there are Normal accounts in the clan chat, you miss");
						member.getPacketSender().sendMessage("@dre@out on loot from loot share.");
					} else {
						players.add(member);
					}
				}
			}
			String green = "@gr2@";
			if (clan.getLootShare() && itemDropped.getId() != 995) {
				Player rewarded = players.size() > 0 ? players.get(Misc.random(players.size()-1)) : null;
				if (rewarded != null) {
					rewarded.getPacketSender().sendMessage(green + "You have received " + itemDropped.getAmount() + "x " + itemDropped.getDefinition().getName() + ".");
					ItemOnGroundManager.register(rewarded, itemDropped, npc.getPosition());
					received = true;
				}
			}
			if (clan.getCoinShare() && itemDropped.getId() == 995) {
				if ((itemDropped.getDefinition().getValue() * itemDropped.getAmount()) < 50000) {
					ItemOnGroundManager.register(player, itemDropped, npc.getPosition());
					return true;
				} else {
					int amount = (int) (ItemDefinition.forId(itemDropped.getId()).getValue() / players.size());
					Item split = new Item(995, amount);
					for (Player member : players) {
						ItemOnGroundManager.register(member, split, npc.getPosition());
						member.getPacketSender().sendMessage(green + "You have received " + amount + "x " + split.getDefinition().getName() + " as part of a split drop.");
					}
					return true;
				}
			} else if(!clan.getLootShare() && !clan.getCoinShare() || !received)
				return false;
		}
		return false;
	}

	private static List<Player> getPlayersWithinPosition(ClanChat clan, Position position) {
		List<Player> players = new ArrayList<Player>();
		for (Player player : clan.getMembers()) {
			if (player.getPosition().isWithinDistance(position)) {
				players.add(player);
			}
		}
		return players;
	}

	public static void toggleCoinShare(Player player) {
		final ClanChat clan = player.getCurrentClanChat();
		if (clan == null) {
			player.getPacketSender().sendMessage("You're not in a clan channel.");
			return;
		}
		if (!clan.getOwnerName().equalsIgnoreCase(player.getUsername())) {
			player.getPacketSender().sendMessage("Only the Owner of the Clan Chat can toggle Coinshare.");
			return;
		}
		if(clan.getLastAction().elapsed(5000)) {
			clan.setCoinShare(!clan.getCoinShare());
			sendMessage(player.getCurrentClanChat(), "@bla@[@blu@"+player.getCurrentClanChat().getName() +"@bla@] <col=3300CC>"+player.getUsername()+" has "+(player.getCurrentClanChat().getCoinShare() ? "@gr1@enabled" : "@red@disabled")+"<col=3300CC> Coinshare.");
			boolean normalPlayerCC = false;
			for (Player member : clan.getMembers()) {
				if (member != null) {
					member.getPacketSender().sendConfigByte(1500, (clan.getCoinShare() ? 1 : 0));
					if (!member.getMode().isEqualOrHigher(PlayerModes.IRON_MAN) && !normalPlayerCC) {
						normalPlayerCC = true;
					}
				}
			}
			if (clan.getLootShare()) {
				for (Player ironman : clan.getMembers()) {
					if (normalPlayerCC && ironman.getMode().isEqualOrHigher(PlayerModes.IRON_MAN)) {
						ironman.getPacketSender().sendMessage("@dre@Because there are Non-Ironman accounts in the clan chat no Ironman accounts will");
						ironman.getPacketSender().sendMessage("@dre@not receive coin share.");
					}
				}
			}
			clan.getLastAction().reset();
		} else {
			player.getPacketSender().sendMessage("You need to wait a few seconds between toggling Coin Share.");
		}
	}

	public static void toggleLootShare(Player player) {
		final ClanChat clan = player.getCurrentClanChat();
		if (clan == null) {
			player.getPacketSender().sendMessage("You're not in a clan channel.");
			return;
		}
		ClanChatRank rank = clan.getRank(player);
		if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_LOOTSHARE] != null) {
			if (rank == null || clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_LOOTSHARE].ordinal() > rank.ordinal()) {
				player.getPacketSender().sendMessage("Your rank is not high enough to toggle Loot Share.");
				return;
			}
		} else if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_LOOTSHARE] == null && !clan.getOwnerName().equalsIgnoreCase(player.getUsername())) {
			player.getPacketSender().sendMessage("Your rank is not high enough to toggle Loot Share.");
			return;
		}
		if(clan.getLastAction().elapsed(5000)) {
			clan.setLootShare(!clan.getLootShare());
			sendMessage(player.getCurrentClanChat(), "@bla@[@blu@"+player.getCurrentClanChat().getName() +"@bla@] <col=3300CC>"+player.getUsername()+" has "+(player.getCurrentClanChat().getLootShare() ? "enabled" : "disabled")+" Lootshare.");
			boolean normalPlayerCC = false;
			for (Player member : clan.getMembers()) {
				if (member != null) {
					member.getPacketSender().sendConfigByte(1499, getLootshareStatus(clan));
					if (!member.getMode().isEqualOrHigher(PlayerModes.IRON_MAN) && !normalPlayerCC) {
						normalPlayerCC = true;
					}
				}
			}
			if (clan.getLootShare()) {
				for (Player ironman : clan.getMembers()) {
					if (normalPlayerCC && ironman.getMode().isEqualOrHigher(PlayerModes.IRON_MAN)) {
						ironman.getPacketSender().sendMessage("@dre@Because there are Non-Ironman accounts in the clan chat no Ironman accounts will");
						ironman.getPacketSender().sendMessage("@dre@not receive loot share.");
					}
				}
			}
			clan.getLastAction().reset();
		} else {
			player.getPacketSender().sendMessage("You need to wait a few seconds between toggling Loot Share.");
		}
	}
	
	private static int getLootshareStatus(ClanChat clan) {
		return clan.getLootShare() ? 1 : 0;
	}

	private static int getIndex() {
		for (int i = 0; i < clans.length; i++) {
			if (clans[i] == null) {
				return i;
			}
		}
		return -1;
	}

	public static boolean handleClanChatSetupButton(Player player, int button, int id) {
		if(player.getInterfaceId() == 45000) {
			if(player.isDebug()) {
				System.out.println("ClanChatManager: " + button + " - " + id);
			}
			final ClanChat clan = getClanChatChannel(player);
			if (clan == null) {
				return false;
			}
			switch(button) {
				case 45012:
					clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_ENTER, id == 0 ? null : ClanChatRank.forId(id-1));
					player.getPacketSender().sendMessage("You have changed your clanchat channel's settings.");
					if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER] != null) {
						for(Player member : clan.getMembers()) {
							if(member == null)
								continue;
							ClanChatRank rank = clan.getRank(member);
							if (rank == null || clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER].ordinal() > rank.ordinal() && rank != ClanChatRank.STAFF) {
								member.getPacketSender().sendMessage("Your rank is not high enough to be in this channel.");
								leave(member, false);
								player.getPacketSender().sendMessage("@red@Warning! Changing that setting kicked the player "+member.getUsername()+" from the chat because").sendMessage("@red@ they do not have the required rank to be in the chat.");
							}
						}
					}
					sendClanSettingsInterface(player, clan);
					writeFile(clan);
					return true;
				case 45017:
					clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_TALK, id == 0 ? null : ClanChatRank.forId(id-1));
					player.getPacketSender().sendMessage("You have changed your clanchat channel's settings.");
					sendClanSettingsInterface(player, clan);
					writeFile(clan);
					return true;
				case 45022:
					clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_KICK, id == 0 ? null : ClanChatRank.forId(id-1));
					player.getPacketSender().sendMessage("You have changed your clanchat channel's settings.");
					sendClanSettingsInterface(player, clan);
					writeFile(clan);
					return true;
				case 45027:
					clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_LOOTSHARE, id == 0 ? null : ClanChatRank.forId(id-1));
					player.getPacketSender().sendMessage("You have changed your clanchat channel's settings.");
					sendClanSettingsInterface(player, clan);
					writeFile(clan);
					return true;
			}
		}
		return false;
	}

	public static void onLogin(Player player) {
		resetInterface(player);
		if(player.getClanChatName() != null && !player.getClanChatName().isEmpty()) {
			ClanChatManager.join(player, player.getClanChatName());
			ClanChat cc = player.getCurrentClanChat();
			if (cc != null)
				player.getPacketSender().sendConfigByte(1499, (player.getCurrentClanChat().getCoinShare() ? 1 : 0));
		}
	}

	public static void resetInterface(Player player) {
		player.getPacketSender().sendString(37139, "Talking in: N/A");
		player.getPacketSender().sendString(37140, "Owner: N/A");
		player.getPacketSender().sendString(37135, "Join Chat");
		for (int i = 37144; i < 37244; i++) {
			player.getPacketSender().sendString(i, "");
		}
	}

	public static Player getPlayer(int index, ClanChat clan) {
		int clanIndex = 0;
		for (Player members : clan.getMembers()) {
			if (members != null) {
				if(clanIndex == index) {
					return members;
				}
				clanIndex++;
			}
		}
		return null;
	}
}
