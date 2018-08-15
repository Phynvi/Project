package com.overload.net.discord;

import java.awt.Color;
import java.time.OffsetDateTime;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DiscordMisc {

	public static void sendMessage(MessageReceivedEvent event, String message, Color color) {
		if(DiscordConstants.DEBUG) {
			System.out.println(event.getChannel().getId());
		}
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTimestamp(OffsetDateTime.parse("2018-01-21T19:00:33.855Z"));
		eb.setColor(color).setFooter("Overload", DiscordConstants.FOOTER_ICON);
		eb.setAuthor(event.getMessage().getAuthor().getName(), event.getMessage().getMember().getUser().getAvatarUrl(),event.getMessage().getMember().getUser().getAvatarUrl());
		eb.setDescription(message);
		event.getChannel().sendMessage(eb.build()).queue();
	}
	
	public static void sendAnnouncement(String message, Color color) {
		if(DiscordConnection.channel == null) {
			return;
		}
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTimestamp(OffsetDateTime.parse("2018-01-21T19:00:33.855Z"));
		eb.setColor(color).setFooter("Overload", DiscordConstants.FOOTER_ICON);
		eb.setAuthor("Overload Server", DiscordConstants.FOOTER_ICON,DiscordConstants.FOOTER_ICON);
		eb.setDescription(message);
		DiscordConstants.mainChannel.sendMessage(eb.build()).queue();
	}
	
}
