package com.overload.net.discord;

import net.dv8tion.jda.core.entities.TextChannel;

public class DiscordConstants {
	
	public static final String Prefex = "::";
	
	public static final String DISCORD_BOT_MESSAGE = "Overload | type ::help";
	
	
	public static boolean DEBUG = false;
	
	public static int Timer = 40;
	
	public static String FOOTER_ICON = "https://cdn.pbrd.co/images/H3Zg1tp.png";
	
	public static final String Token = "NDIyNTI3MTM0OTcyNTEwMjE4.DYdHPQ.8o8qGTTaLHKBJ0-2qTsWm_6WniQ";

	public static String getToken() {
		return Token;
	}

	public static String getPrefex() {
		return Prefex;
	}
	
	public static final String[] PERMS = {"Overload Member", "Server Support", "Moderator", "Forum Developer", "Graphics Designer"};

	public static final String[] FULLPERMS = {"Founder", "Developer","Community Manager","Administrator"};
	
	public static TextChannel getMainChannel() {
		return mainChannel;
	}

	public static void setMainChannel(TextChannel mainChannelid) {
		mainChannel = mainChannelid;
	}
	
	public static TextChannel mainChannel;

}
