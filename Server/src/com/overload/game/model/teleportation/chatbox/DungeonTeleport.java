package com.overload.game.model.teleportation.chatbox;

import com.overload.game.model.Position;


public enum DungeonTeleport implements Teleport {
	
	BRIMHAVEN_DUNGEON(new Position(2710, 9466), null),
	
	TAVERLEY_DUNGEON(new Position(2884, 9798), null),
	
	DUNGEON_OF_ABYSS(new Position(3297, 9824), null),
	;
	
	private DungeonTeleport(Position position,
			String[] warning) {
		this.position = position;
		this.warning = warning;
	}
	
	private final Position position;
	
	private final String[] warning;
	
	@Override
	public String[] getWarning() {
		return warning;
	}

	@Override
	public Position getPosition() {
		return position;
	}
}
