package com.overload.game.model.teleportation;

import com.overload.game.model.Position;

public enum Teleportable {
	
//	EDGEVILLE_DITCH(TeleportButton.WILDERNESS, 0, 0, new Position(3088, 3520)),
//	
//	DUEL_ARENA(TeleportButton.MINIGAME, 1, 0, new Position(3370, 3270)),
//	BARROWS(TeleportButton.MINIGAME, 1, 1, new Position(3565, 3313)),
//	FIGHT_CAVES(TeleportButton.MINIGAME, 1, 2, new Position(2439, 5171)),
	
	;
	
	private final TeleportButton teleportButton;
	private final int type;
	private final int index;
	private final Position position;
	
	private Teleportable(TeleportButton teleportButton, int type, int index, Position position) {
		this.teleportButton = teleportButton;
		this.type = type;
		this.index = index;
		this.position = position;
	}

	public TeleportButton getTeleportButton() {
		return teleportButton;
	}

	public int getType() {
		return type;
	}

	public int getIndex() {
		return index;
	}

	public Position getPosition() {
		return position;
	}
}
