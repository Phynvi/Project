package com.overload.net.packet.impl;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.overload.game.content.clan.ClanChatManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.logs.HandleLogs;
import com.overload.game.model.rights.Right;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;
import com.overload.net.packet.impl.commands.Command;
import com.overload.util.Misc;

/**
 * This packet listener manages commands a player uses by using the command
 * console prompted by using the "`" char.
 *
 * ORIGINAL
 * @author Gabriel Hannason
 * 
 * HEAVILY MODIFIED BY
 * @author Adam Maxwell (Jowcey)
 */
public class CommandPacketListener implements PacketListener {

    public static final int OP_CODE = 103;


	public static Map<String, Command> COMMAND_MAP = new TreeMap<>();

	public static boolean executeCommand(Player c, String playerCommand, String commandPackage) {
		String commandName = Misc.findCommand(playerCommand);
		String commandInput = Misc.findInput(playerCommand);
		String className;
		
		if (commandName.length() <= 0) {
			return true;
		} else if (commandName.length() == 1) {
			className = commandName.toUpperCase();
		} else {
			className = Character.toUpperCase(commandName.charAt(0)) + commandName.substring(1).toLowerCase();
		}
		try {
			String path = "com.overload.net.packet.impl.commands." + commandPackage + "." + className;

			if (!COMMAND_MAP.containsKey(path)) {
				initialize(path);
			}
			COMMAND_MAP.get(path).execute(c, commandInput);
			HandleLogs.executeLog(c, commandName + " " + commandInput, HandleLogs.LogType.COMMAND);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		} catch (Exception e) {
			c.getPacketSender().sendMessage("Error while executing the following command: " + playerCommand);
			e.printStackTrace();
			return true;
		}
	}

	private static void initialize(String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<?> commandClass = Class.forName(path);
		Object instance = commandClass.newInstance();
		if (instance instanceof Command) {
			Command command = (Command) instance;
			COMMAND_MAP.putIfAbsent(path, command);
		}
	}

	public static void initializeCommands() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		ClassPath classPath = ClassPath.from(CommandPacketListener.class.getClassLoader());
		String[] packages = {
				"com.overload.net.packet.impl.commands.all",
				"com.overload.net.packet.impl.commands.donator",
				"com.overload.net.packet.impl.commands.support",
				"com.overload.net.packet.impl.commands.moderator",
				"com.overload.net.packet.impl.commands.admin",
				"com.overload.net.packet.impl.commands.developer",
				"com.overload.net.packet.impl.commands.owner"
		};

		for (String pack : packages) {
			for (ClassInfo classInfo : classPath.getTopLevelClasses(pack)) {
				try {
					initialize(classInfo.getName());
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
    
    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getHitpoints() <= 0) {
            return;
        }
        
        String command = packet.readString();
		
		final String[] restrictions = { "@", "img=" };
		if (command.startsWith("/")) {
			for (String string : restrictions) {
				if (command.toLowerCase().contains(string)) {
					player.getPacketSender().sendMessage("Your message contained illegal characters!");
					return;
				}
			}
			ClanChatManager.sendMessage(player, command.substring(1, command.length()));
			return;
		}
		
		for (String string : restrictions) {
			if (command.toLowerCase().contains(string)) {
				player.getPacketSender().sendMessage("Your message contained illegal characters!");
				return;
			}
		}
		if (player.getRights().isOrInherits(Right.OWNER) && executeCommand(player, command, "owner")) {
			return;
		} else if (player.getRights().isOrInherits(Right.GAME_DEVELOPER) && executeCommand(player, command, "developer")) {
			return;
		} else if (player.getRights().isOrInherits(Right.ADMINISTRATOR) && executeCommand(player, command, "admin")) {
			return;
		} else if (player.getRights().isOrInherits(Right.MODERATOR) && executeCommand(player, command, "moderator")) {
			return;
		} else if (player.getRights().isOrInherits(Right.SERVER_SUPPORT) && executeCommand(player, command, "support")) {
			return;
		} else if (player.getRights().isOrInherits(Right.BRONZE_DONATOR) && executeCommand(player, command, "donator")) {
			return;
		} else if (executeCommand(player, command, "all")) {
			return;
		}
    }
}
