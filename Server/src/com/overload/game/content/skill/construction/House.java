package com.overload.game.content.skill.construction;

import com.overload.game.GameConstants;
import com.overload.game.Palette;
import com.overload.game.Palette.PaletteTile;
import com.overload.game.content.skill.construction.rooms.*;
import com.overload.game.content.skill.construction.rooms.Room;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Position;

public class House {

	Player player;
	
	Room[][][] rooms;
	
	public House(Player player) {
		this.player = player;
	}
	
	public void create() {
		rooms = new Room[13][13][4];
        for (int z = 0; z < 4; z++) {
            for (int y = 0; y < 13; y++) {
            	for (int x = 0; x < 13; x++) {
            		rooms[x][y][z] = new Default();
            	}
            }
        }
        rooms[7][7][0] = new Portal();
	}
	
	public void enter(Player owner, Player player, boolean buildMode) {
		if (player != owner)
			buildMode = false;

		Palette palette = new Palette();

        for (int z = 0; z < 4; z++) {
            for (int y = 0; y < 13; y++) {
            	for (int x = 0; x < 13; x++) {
            		Room room = rooms[x][y][z];
            		PaletteTile pt = new PaletteTile(room.getX(), room.getY(), 0);
                	palette.setTile(x,  y,  z, pt);
                }
            }
        }
        
        player.moveTo(new Position(1207, 7543, player.getIndex() * 4));
        player.getPacketSender().sendConstructMapRegion(palette);
        player.moveTo(new Position(1211, 7546, player.getIndex() * 4));
        if(player.isDebug()) {
        	player.sendMessage("Rachel Loves Big Black Penis but it has to be fat to fill her.");
        }
        
        
	}
	
	public void leave(Player player, House house) {
		player.moveTo(GameConstants.DEFAULT_POSITION);
	}
	
	public void load() {
		if (player == null)
			return;
		player.sendMessage("Loading...");
	}
	
	public void save() {
		if (player == null)
			return;
		player.sendMessage("Saving...");
	}
	
	public void sendInterface() {
		
	}
	
}
