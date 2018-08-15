package com.overload.game.model.logs;

import java.io.IOException;


import com.overload.game.entity.impl.player.Player;


public abstract class Logs {
	
	public abstract void write(Player player, String message) throws IOException;
	
	public abstract String location(Player player);
	
	public abstract String name();
	
}
