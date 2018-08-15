package com.overload.net.discord;

import net.dv8tion.jda.core.entities.TextChannel;

public class DiscordConstants {
	
	public static final String Prefex = "::";
	
	public static final String DISCORD_BOT_MESSAGE = "Project | type ::help";
	
	
	public static boolean DEBUG = false;
	
	public static int Timer = 40;
	
	public static String FOOTER_ICON = "https://cdn.pbrd.co/images/H3Zg1tp.png";//need image
	
	public static final String Token = "NDc2ODY5NTg1OTg4MDkxOTI0.DlV9LQ.FZa51Vctyr0rNNl74ONZWyFBgbg";

	public static String getToken() {
		return Token;
	}

	public static String getPrefex() {
		return Prefex;
	}
	
	public static final String[] PERMS = {"Member", "Server Support", "Moderator", "Forum Developer", "Graphics Designer"};

	public static final String[] FULLPERMS = {"Founder", "Developer","Community Manager","Administrator"};
	
	public static TextChannel getMainChannel() {
		return mainChannel;
	}

	public static void setMainChannel(TextChannel mainChannelid) {
		mainChannel = mainChannelid;
	}
	
	public static TextChannel mainChannel;

}
