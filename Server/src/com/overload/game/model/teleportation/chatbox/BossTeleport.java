package com.overload.game.model.teleportation.chatbox;

import com.overload.game.model.Position;

public enum BossTeleport implements Teleport {
	
	CORPOREAL_BEAST(new Position(2970, 4382, 2), null),
	
	GOD_WARS_DUNGEON(new Position(2881, 5296, 2), null),
	
	DAGANNOTH_KINGS(new Position(2900, 4449), null),

	KING_BLACK_DRAGON(new Position(2257, 4683), null),

	GIANT_MOLE(new Position(1752, 5235), null),

    BARRELCHEST(new Position(2759, 10064), null),
	
	;

	private BossTeleport(Position position,
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
