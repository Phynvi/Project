package com.overload.game.task.impl;

import com.overload.game.content.combat.CombatFactory;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.model.Position;
import com.overload.game.model.movement.path.PathGenerator;
import com.overload.game.model.movement.path.RS317PathFinder;
import com.overload.game.task.Task;
import com.overload.util.NpcIdentifiers;

/**
 * A {@link Task} implementation which handles the following of a
 * {@link Character}
 *
 * @author Professor Oak
 */
public class CharacterFollowTask extends Task {

	private final Character character;
	private Character following;

	public CharacterFollowTask(Character character, Character following) {
		super(1, character, true);
		this.character = character;
		this.following = following;
	}

	@Override
	protected void execute() {
		// Update interaction
		character.setEntityInteraction(following);
		
		// Block if our movement is locked.
		if (!character.getMovementQueue().canMove()) {
			return;
		}

		// Are we in range of the target?
		boolean inRange = true;

		// Are we following in combat?
		boolean combatFollow = CombatFactory.isAttacking(character) && character.getCombat().getTarget().equals(following);

		final Position targetPos = following.getPosition();/*
														 * (target.getCachedPosition() == null) ? target.getPosition() :
														 * target.getCachedPosition();
														 */

		// If we're way too far away from eachother, simply reset following completely.
		if (!character.getPosition().isViewableFrom(targetPos) || !following.isRegistered()) {

			boolean reset = true;

			// Handle pets, they should teleport to their owner
			// when they're too far away.
			if (character.isNpc()) {
				NPC npc = character.getAsNpc();
				if (npc.isPet()) {
					npc.moveTo(npc.getOwner().getPosition().clone().add(0, 1));
					return;
				}

				switch (npc.getId()) {
				case NpcIdentifiers.TZTOK_JAD:
					reset = false;
					break;
				}
			}

			if (reset) {
				if (character.isPlayer() && following.isPlayer()) {
					character.getAsPlayer().getPacketSender()
							.sendMessage("Unable to find " + following.getAsPlayer().getUsername() + ".");
				}
				if (combatFollow) {
					character.getCombat().reset();
				}
				character.getMovementQueue().resetFollowing();
				character.getMovementQueue().reset();
				character.setEntityInteraction(null);
				stop();
				return;
			}
		}

		// The destination we'll be walking to.
		Position destination = null;

		// If we are combat following, make sure to only reset movement once we are in
		// striking range.
		if (combatFollow) {
			if (!CombatFactory.canReach(character, CombatFactory.getMethod(character), following)) {
				inRange = false;
			}
		} else {
			// If two players are following eachother, make them "dance" with
			// each other by marking the destination as their old position.
			if (character.isPlayer() && following.isPlayer()) {
				if (following.getMovementQueue().isFollowing(character)) {
					destination = following.getAsPlayer().getOldPosition();
					inRange = false;
				}
			}
			
			// If we aren't close to the target or we are in a diagonal block, flag as not
			// in range..
			// This will cause our character to continue following the target.
			if (inRange) {
				if (!character.getPosition().isWithinDistance(targetPos, following.getSize())
						|| character.getPosition().equals(targetPos) || (character.getSize() == 1 && following.getSize() == 1
						&& RS317PathFinder.isInDiagonalBlock(character.getPosition(), targetPos))) {
					inRange = false;
				}
			}
		}

		if (inRange) {
			character.getMovementQueue().reset();
			return;
		}

		if (destination == null) {
			if (!combatFollow/* || character.isNpc()*/) {
				destination = PathGenerator.getBasicPath(character, targetPos);
			} else {
				destination = PathGenerator.getCombatPath(character, targetPos, following.getSize());
			}
		}

		if (destination != null) {
			RS317PathFinder.findPath(character, destination.getX(), destination.getY(), false, 16, 16);
		}
	}

	public CharacterFollowTask setFollowing(Character following) {
		this.following = following;
		return this;
	}

	public Character getFollow() {
		return following;
	}
}
