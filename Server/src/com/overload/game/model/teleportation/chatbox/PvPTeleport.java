package com.overload.game.model.teleportation.chatbox;

import com.overload.game.model.Position;


public enum PvPTeleport implements Teleport {

	WEST_GREEN_DRAGONS(new Position(2977, 3594), null),
	
	EAST_GREEN_DRAGONS(new Position(3349, 3691), null),
	
	EDGEVILLE_DITCH(new Position(3088, 3518), null),
	
	MAGE_BANK(new Position(2537, 4717), null),
	;
	
	private PvPTeleport(Position position,
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
