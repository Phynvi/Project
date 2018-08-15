package com.overload.game.model.teleportation.chatbox;

import com.overload.game.model.Position;

public enum TrainingTeleport implements Teleport {
	
	ROCK_CRABS(new Position(2677, 3720), null),
	
	YAKS(new Position(2324, 3794), null),
	
	DESERT_BANDITS(new Position(3174, 2980), null),
	
	GORILLAS(new Position(2786, 2786), null),
	
	EXPERIMENTS(new Position(3559, 9946), null),
	
	FIRE_GIANTS(new Position(2577, 9888), null),
	
	;
	
	private TrainingTeleport(Position position,
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
