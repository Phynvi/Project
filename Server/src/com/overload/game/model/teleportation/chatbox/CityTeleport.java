package com.overload.game.model.teleportation.chatbox;

import com.overload.game.model.Position;

public enum CityTeleport implements Teleport {
	
	AL_KHARID(new Position(3277, 3161), null),
	
	ARDOUGNE(new Position(2662, 3305), null),
	
	CAMELOT(new Position(2757, 3479), null),
	
	DRAYNOR(new Position(3081, 3251), null),
	
	EDGEVILLE(new Position(3087, 3503), null),
	
	FALADOR(new Position(2963, 3382), null),
	
	KARAMJA(new Position(2916, 3170), null),
	
	LUMBRIDGE(new Position(3222, 3218), null),
	
	POLLNIVNEACH(new Position(3357, 2968), null),
	
	RELLEKA(new Position(2662, 3645), null),
	
	RIMMINGTON(new Position(2956, 3211), null),
	
	TAVERLEY(new Position(2884, 3458), null),
	
	VARROCK(new Position(3212, 3429), null),
	
	YANILLE(new Position(2602, 3088), null),

	;
	
	private CityTeleport(Position position,
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
