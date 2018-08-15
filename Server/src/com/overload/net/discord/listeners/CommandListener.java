package com.overload.net.discord.listeners;

import com.overload.net.discord.CommandParser;
import com.overload.net.discord.DiscordConnection;
import com.overload.net.discord.DiscordConstants;
import com.overload.net.discord.commands.CommandHandler;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

	public void onMessageReceived(MessageReceivedEvent event) {
		DiscordConnection.channel = event.getChannel();
		if (event.getMessage().getContentRaw().startsWith(DiscordConstants.getPrefex()) && event.getMessage().getAuthor().getId() != event.getJDA() .getSelfUser().getId()) {
			CommandHandler.handleCommand(CommandParser.parser(event.getMessage().getContentRaw(), event));
			event.getTextChannel().deleteMessageById(event.getMessageId()).queue();
		}
	}
	
}
