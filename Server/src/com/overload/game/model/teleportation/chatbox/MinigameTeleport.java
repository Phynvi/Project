package com.overload.game.model.teleportation.chatbox;

import com.overload.game.model.Position;

public enum MinigameTeleport implements Teleport {
	
	BARROWS(new Position(3565, 3311), null),
	
	CLAN_WARS(new Position(3273, 3681), null),
		
	DUEL_ARENA(new Position(3364, 3267), null),
	
	FIGHT_CAVE(new Position(2443, 5168), null),
	
	PEST_CONTROL(new Position(2658, 2660), null),

	WARRIORS_GUILD(new Position(2883, 3546), null),

	;

	private MinigameTeleport(Position position,
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
