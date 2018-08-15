package com.overload.game.model.movement;

import com.overload.game.collision.RegionManager;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Action;
import com.overload.game.model.Direction;
import com.overload.game.model.Position;

import java.util.Optional;

/**
 * This class handles the execution of an action once we're close enough to the
 * given position. Used for things such as clicking on entities.
 */

public class WalkToAction {

	/**
	 * The associated game character.
	 */
	private final Player player;
	/**
	 * The action a player must execute upon reaching said destination.
	 */
	private final Action finalizedAction;
	private int objectWidth = 0;
	private int objectHeight = 0;
	private int distance = -1;

	/**
	 * The character we are walking towards.
	 */
	private Optional<Character> character = Optional.empty();
	/**
	 * The destination the game character will move to.
	 */
	private Position destination;

	/**
	 * The WalkToTask constructor.
	 *
	 * @param entity
	 *            The associated game character.
	 * @param destination
	 *            The destination the game character will move to.
	 * @param finalizedTask
	 *            The task a player must execute upon reaching said destination.
	 */
	public WalkToAction(Player entity, Position destination, int distance, Action finalizedTask) {
		this.player = entity;
		this.destination = destination;
		this.finalizedAction = finalizedTask;
		this.distance = distance;
	}

	public WalkToAction(Player entity, Position destination, int distance, int objectWidth, int objectHeight,
			Action finalizedTask) {
		this(entity, destination, distance, finalizedTask);
		this.objectWidth = objectWidth;
		this.objectHeight = objectHeight;
	}

	public WalkToAction(Player player, Character character, int distance, Action finalizedTask) {
		this(player, character.getPosition(), distance, finalizedTask);
		this.character = Optional.of(character);
	}

	/**
	 * Executes the action if distance is correct
	 */
	public void process() {
		if (player == null)
			return;
		if (!player.isRegistered()) {
			player.setWalkToTask(null);
			return;
		}
		if (player.busy() || destination == null || player.getMovementQueue().isMovementBlocked()) {
			player.setWalkToTask(null);
			return;
		}

		if (character.isPresent()) {
			destination = character.get().getPosition();
		}

		boolean blocked = false;//RegionManager.canMove(destination, player.rotationFactor);
		
		if (objectWidth > 0 && objectHeight > 0) {
			for (int x = destination.getX(); x < destination.getX() + objectWidth; x++) {
				for (int y = destination.getY(); y < destination.getY() + objectHeight; y++) {
					if (player.getPosition().getDistance(new Position(x, y)) <= 1 && !blocked) {
						execute();
						return;
					}
				}
			}
		}

		if (player.getPosition().getDistance(destination) <= distance && !blocked) {
			execute();
		}
	}

	private void execute() {
		finalizedAction.execute();
		player.setEntityInteraction(null);
		player.setWalkToTask(null);
	}
}
