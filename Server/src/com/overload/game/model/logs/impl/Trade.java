package com.overload.game.model.logs.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.logs.Logs;

public class Trade extends Logs {

	@Override
	public void write(Player player, String message) throws IOException {
		final File dir = new File(location(player));
		if(!dir.exists()) {
			dir.mkdirs();
		}
		final File file = new File(location(player) + name() + ".txt");
		file.createNewFile();
		final BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		SimpleDateFormat date = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
		writer.write("[ " + date.format(new Date()) + " ] " + "[" + player.getUsername() + ", " + player.getRights().getPrimary().name() + "]: " + message);
		writer.newLine();
		writer.close();
		
	}
	
	@Override
	public String location(Player player) {
		return "./data/logs/" + player.getUsername() + "/";
	}
	
	@Override
	public String name() {
		return "PrivateMessage";
	}



}
