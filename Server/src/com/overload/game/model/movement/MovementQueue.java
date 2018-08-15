package com.overload.game.model.movement;

import java.util.ArrayDeque;
import java.util.Deque;

import com.overload.game.collision.RegionManager;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Direction;
import com.overload.game.model.Position;
import com.overload.game.model.Skill;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.game.task.impl.CharacterFollowTask;
import com.overload.util.timers.TimerKey;

/**
 * A queue of {@link Direction}s which a {@link Character} will follow.
 *
 * @author Graham Edgecombe Edited by Gabbe
 */
public final class MovementQueue {

	/**
	 * The maximum size of the queue. If any additional steps are added, they are
	 * discarded.
	 */
	private static final int MAXIMUM_SIZE = 100;

	/**
	 * The character whose walking queue this is.
	 */
	private final Character character;
	
	/**
	 * The {@link Task} which handles following.
	 */
	private CharacterFollowTask followingTask;

	/**
	 * The queue of directions.
	 */
	private final Deque<Point> points = new ArrayDeque<Point>();

	/**
	 * The current potato?
	 */
	private boolean blockMovement = false;

	/**
	 * Are we currently moving?
	 */
	private boolean isMoving = false;

	/**
	 * Creates a walking queue for the specified character.
	 *
	 * @param character
	 *            The character.
	 */
	public MovementQueue(Character character) {
		this.character = character;
	}

	/**
	 * Checks if we can walk from one position to another.
	 * 
	 * @param from
	 * @param to
	 * @param size
	 * @return
	 */
	public static boolean canWalk(Position from, Position to, int size) {
		return RegionManager.canMove(from, to, size, size);
	}

	/**
	 * Steps away from a Gamecharacter
	 *
	 * @param character
	 *            The gamecharacter to step away from
	 */
	public static void clippedStep(Character character) {
		if (character.getMovementQueue().canWalk(-1, 0))
			character.getMovementQueue().walkStep(-1, 0);
		else if (character.getMovementQueue().canWalk(1, 0))
			character.getMovementQueue().walkStep(1, 0);
		else if (character.getMovementQueue().canWalk(0, -1))
			character.getMovementQueue().walkStep(0, -1);
		else if (character.getMovementQueue().canWalk(0, 1))
			character.getMovementQueue().walkStep(0, 1);
	}

	/**
	 * Adds the first step to the queue, attempting to connect the server and client
	 * position by looking at the previous queue.
	 *
	 * @param clientConnectionPosition
	 *            The first step.
	 * @return {@code true} if the queues could be connected correctly,
	 *         {@code false} if not.
	 */
	public boolean addFirstStep(Position clientConnectionPosition) {
		reset();
		addStep(clientConnectionPosition);
		return true;
	}

	/**
	 * Adds a step to walk to the queue.
	 *
	 * @param x
	 *            X to walk to
	 * @param y
	 *            Y to walk to
	 */
	public void walkStep(int x, int y) {
		Position position = character.getPosition().clone();
		position.setX(position.getX() + x);
		position.setY(position.getY() + y);
		addStep(position);
	}

	/**
	 * Adds a step.
	 *
	 * @param x
	 *            The x coordinate of this step.
	 * @param y
	 *            The y coordinate of this step.
	 * @param heightLevel
	 */
	private void addStep(int x, int y, int heightLevel) {
		if (!canMove()) {
			return;
		}

		if (points.size() >= MAXIMUM_SIZE)
			return;

		final Point last = getLast();
		final int deltaX = x - last.position.getX();
		final int deltaY = y - last.position.getY();
		final Direction direction = Direction.fromDeltas(deltaX, deltaY);
		if (direction != Direction.NONE)
			points.add(new Point(new Position(x, y, heightLevel), direction));
	}

	/**
	 * Adds a step to the queue.
	 *
	 * @param step
	 *            The step to add.
	 * @oaram flag
	 */
	public void addStep(Position step) {
		if (!canMove()) {
			return;
		}

		final Point last = getLast();
		final int x = step.getX();
		final int y = step.getY();
		int deltaX = x - last.position.getX();
		int deltaY = y - last.position.getY();
		final int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
		for (int i = 0; i < max; i++) {
			if (deltaX < 0)
				deltaX++;
			else if (deltaX > 0)
				deltaX--;
			if (deltaY < 0)
				deltaY++;
			else if (deltaY > 0)
				deltaY--;
			addStep(x - deltaX, y - deltaY, step.getZ());
		}
	}

	public boolean canMove() {
		if (character.isNeedsPlacement()) {
			return false;
		}
		if (character.getTimers().has(TimerKey.FREEZE) || character.getTimers().has(TimerKey.STUN) || blockMovement) {
			return false;
		}
		return true;
	}

	public boolean canWalk(int deltaX, int deltaY) {
		if (!canMove()) {
			return false;
		}

		final Position to = new Position(character.getPosition().getX() + deltaX,
				character.getPosition().getY() + deltaY, character.getPosition().getZ());
		if (character.getPosition().getZ() == -1 && to.getZ() == -1)
			return true;
		return canWalk(character.getPosition(), to, character.getSize());
	}

	/**
	 * Gets the last point.
	 *
	 * @return The last point.
	 */
	private Point getLast() {
		final Point last = points.peekLast();
		if (last == null)
			return new Point(character.getPosition(), Direction.NONE);
		return last;
	}

	/**
	 * @return true if the character is moving.
	 */
	public boolean isMoving() {
		return isMoving; // !points.isEmpty();
	}

	/**
	 * Processes the movement queue.
	 * 
	 * Polls through the queue of steps and handles them.
	 * 
	 */
	public void process() {
			
		// Make sure movement isnt restricted..
		if (!canMove()) {
			reset();
			return;
		}
		
		// Poll through the actual movement queue and
		// begin moving.
		Point walkPoint = null;
		Point runPoint = null;

		walkPoint = points.poll();

		if (isRunToggled()) {
			runPoint = points.poll();
		}

		Position oldPosition = character.getPosition();
		boolean moved = false;

		if (walkPoint != null && walkPoint.direction != Direction.NONE) {
			Position next = walkPoint.position;
			if (validateStep(next)) {
				character.setPosition(next);
				character.setWalkingDirection(walkPoint.direction);
				moved = true;
			} else {
				reset();
				return;
			}
		}

		if (runPoint != null && runPoint.direction != Direction.NONE) {
			Position next = runPoint.position;
			oldPosition = next;
			if (validateStep(next)) {
				character.setPosition(next);
				character.setRunningDirection(runPoint.direction);
				moved = true;
			} else {
				reset();
				return;
			}
		}

		// Handle movement-related events such as
		// region change and energy drainage.
		if (character.isPlayer()) {
			if (moved) {
				handleRegionChange();
				drainRunEnergy();
				character.getAsPlayer().setOldPosition(oldPosition);
			}
		}

		isMoving = moved;
	}
	
	private boolean validateStep(Position next) {
		if (followingTask != null && next.equals(followingTask.getFollow().getPosition())) {
			return false;
		}
		
		/*if (character.isNpc()) {
			for (NPC npc : World.getNpcs()) {
				if (npc == null)
					continue;
				if (npc.getPosition().equals(next)) {
					return false;
				}
			}
		}*/
		
		// RegionManager.canMove(character.getPosition(), next, 1, 1)
		
		return true;
	}

	public void handleRegionChange() {
		Player player = ((Player) character);
		final int diffX = character.getPosition().getX() - character.getLastKnownRegion().getRegionX() * 8;
		final int diffY = character.getPosition().getY() - character.getLastKnownRegion().getRegionY() * 8;
		boolean regionChanged = false;
		if (diffX < 16)
			regionChanged = true;
		else if (diffX >= 88)
			regionChanged = true;
		if (diffY < 16)
			regionChanged = true;
		else if (diffY >= 88)
			regionChanged = true;
		if (regionChanged || player.getRegionHeight() != player.getPosition().getZ()) {
			player.getPacketSender().sendMapRegion();
			player.setRegionHeight(player.getPosition().getZ());
		}
	}

	private void drainRunEnergy() {
		Player player = ((Player) character);
		if (player.isRunning()) {
			player.setRunEnergy(player.getRunEnergy() - 1);
			if (player.getRunEnergy() <= 0) {
				player.setRunEnergy(0);
				player.setRunning(false);
				player.getPacketSender().sendRunStatus();
			}
			player.getPacketSender().sendRunEnergy();
		}
	}

	/**
	 * The rate of which we restore one run energy point
	 * 
	 * @return
	 */
	public static int runEnergyRestoreDelay(Player p) {
		int delay = 1500 - (p.getSkillManager().getCurrentLevel(Skill.AGILITY) * 10);
		// if (isResting()) {
		// delay *= 0.45;
		// }
		return delay;
	}

	/**
	 * Stops the movement.
	 */
	public MovementQueue reset() {
		points.clear();
		isMoving = false;
		return this;
	}
	
	/**
	 * Starts a new {@link CharacterFollowTask} which starts
	 * following the given {@link Character}.
	 * @param follow
	 */
	public void follow(Character follow) {
		if (follow == null) {
			resetFollowing();
			return;
		}
		
		if (followingTask == null || !followingTask.isRunning()) {
			TaskManager.submit((followingTask = new CharacterFollowTask(character, follow)));
		} else {
			followingTask.setFollowing(follow);
		}
	}
	
	/**
	 * Checks if we're currently following the given {@link Character}.
	 * @param character
	 * @return
	 */
	public boolean isFollowing(Character character) {
		if (followingTask != null) {
			return followingTask.getFollow().equals(character);
		}
		return false;
	}
	
	/**
	 * Stops any following which might be active.
	 */
	public void resetFollowing() {
		if (followingTask != null) {
			followingTask.stop();
		}
		followingTask = null;
	}

	/**
	 * Gets the size of the queue.
	 *
	 * @return The size of the queue.
	 */
	public int size() {
		return points.size();
	}

	public boolean isRunToggled() {
		return character.isPlayer() && ((Player) character).isRunning();
	}

	public MovementQueue setBlockMovement(boolean blockMovement) {
		this.blockMovement = blockMovement;
		return this;
	}

	public boolean isMovementBlocked() {
		return blockMovement;
	}

	/**
	 * Represents a single point in the queue.
	 *
	 * @author Graham Edgecombe
	 */
	public static final class Point {

		private final Position position;
		private final Direction direction;

		public Point(Position position, Direction direction) {
			this.position = position;
			this.direction = direction;
		}

		@Override
		public String toString() {
			return Point.class.getName() + " [direction=" + direction + ", position=" + position + "]";
		}

	}
}