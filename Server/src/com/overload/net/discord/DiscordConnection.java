package com.overload.net.discord;


import javax.security.auth.login.LoginException;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.discord.commands.Command;
import com.overload.net.discord.commands.CommandHandler;
import com.overload.net.discord.commands.impl.all.Players;
import com.overload.net.discord.listeners.CommandListener;
import com.overload.net.discord.listeners.ReadyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.MessageChannel;

public class DiscordConnection {
	
	public static MessageChannel channel;
	
	public static Command command;
	
	public static JDA builder;
	
	public static JDA getBuilder() {
		return builder;
	}

	public static void init() {
		if(Player.VPSEnvironment1()) {
			try {
				builder = new JDABuilder(AccountType.BOT).setToken(DiscordConstants.getToken()).buildAsync();
				getBuilder().setAutoReconnect(true);
				getBuilder().getPresence().setStatus(OnlineStatus.ONLINE);
				getBuilder().getPresence().setGame(Game.of(GameType.DEFAULT, DiscordConstants.DISCORD_BOT_MESSAGE));
				addCommands();
				addListeners();
			} catch (LoginException e) {
				e.printStackTrace();
			}
		}
		
	}

	
	public static void addCommands() {
		CommandHandler.COMMAND_MAP.put("player", new Players());
	}

	
	public static void addListeners() {
		builder.addEventListener(new CommandListener());
		builder.addEventListener(new ReadyListener());
	}

	
	public void setDefualt() {
		DiscordConstants.setMainChannel(getBuilder().getTextChannelById("476884942505115660"));
	}

}
