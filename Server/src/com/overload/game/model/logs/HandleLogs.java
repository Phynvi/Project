package com.overload.game.model.logs;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.overload.game.entity.impl.player.Player;
import com.overload.util.Misc;

public class HandleLogs {

	public enum LogType {
        CHAT, COMMAND, DROP,PICKUP,PRIVATEMESSAGE,TRADE;
    }
	
	public static Map<String, Logs> LOG_MAP = new TreeMap<>();

	public static boolean executeLog(Player player, String message, LogType logtype) {
		if(!player.VPSEnvironment()) {
			return false;
		}
		try {
			String path = "com.overload.game.model.logs.impl." + Misc.capitalize(logtype.toString());
			//System.out.println(Misc.capitalize(logtype.toString()));
			if (!LOG_MAP.containsKey(path)) {
				initialize(path);
			}
			LOG_MAP.get(path).write(player, message);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		} catch (Exception e) {
			player.getPacketSender().sendMessage("Error while writing the following log: " + Misc.capitalize(logtype.toString()) + " Username: " + player.getUsername());
			e.printStackTrace();
			return true;
		}
		
	}

	private static void initialize(String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<?> commandClass = Class.forName(path);
		Object instance = commandClass.newInstance();
		if (instance instanceof Logs) {
			Logs command = (Logs) instance;
			LOG_MAP.putIfAbsent(path, command);
		}
	}

	public static void initializeCommands() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		ClassPath classPath = ClassPath.from(HandleLogs.class.getClassLoader());
		String[] packages = {
				"com.overload.game.model.logs.impl"
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
	
}
