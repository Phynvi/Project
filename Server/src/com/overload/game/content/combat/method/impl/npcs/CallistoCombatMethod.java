package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.task.TaskManager;
import com.overload.game.task.impl.ForceMovementTask;
import com.overload.util.Misc;
import com.overload.util.timers.TimerKey;

/**
 * Handles Callisto's combat.
 *
 * @author Professor Oak
 */
public class CallistoCombatMethod implements CombatMethod {

	private static final Animation MELEE_ATTACK_ANIMATION = new Animation(4925);
	private static final Graphic END_PROJECTILE_GRAPHIC = new Graphic(359, GraphicHeight.HIGH);

	private SecondsTimer comboTimer = new SecondsTimer();
	private CombatType currentAttackType = CombatType.MELEE;

	@Override
	public CombatType getCombatType() {
		return currentAttackType;
	}

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
	public boolean canAttack(Character character, Character target) {
		return true;
	}

	@Override
	public PendingHit[] getHits(Character character, Character target) {
		return new PendingHit[] { new PendingHit(character, target, this, true, 2) };
	}

	@Override
	public void prepareAttack(Character character, Character target) {
		if (currentAttackType == CombatType.MAGIC) {
			character.executeProjectile(new Projectile(character, target, 395, 40, 60, 31, 43, 0));
		}
	}

	@Override
	public int getAttackSpeed(Character character) {
		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		return 4;
	}

	@Override
	public void startAnimation(Character character) {
		character.performAnimation(MELEE_ATTACK_ANIMATION);
	}

	@Override
	public void finished(Character character, Character target) {

		currentAttackType = CombatType.MELEE;

		// Switch attack to magic randomly
		if (comboTimer.finished()) {
			if (Misc.getRandom(10) <= 2) {
				comboTimer.start(5);
				currentAttackType = CombatType.MAGIC;
				character.getCombat().performNewAttack(true);
			}
		}
	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {
		if (hit.getTarget() == null || !hit.getTarget().isPlayer()) {
			return;
		}

		final Player player = hit.getTarget().getAsPlayer();

		if (currentAttackType == CombatType.MAGIC) {
			player.performGraphic(END_PROJECTILE_GRAPHIC);
		}

		// Stun player 15% chance
		if (!player.getTimers().has(TimerKey.STUN) && Misc.getRandom(100) <= 10) {
			player.performAnimation(new Animation(3131));
			final Position toKnock = new Position(player.getPosition().getX() > 3325 ? -3 : 1 + Misc.getRandom(2),
					player.getPosition().getY() > 3834 && player.getPosition().getY() < 3843 ? 3 : -3);
			TaskManager.submit(new ForceMovementTask(player, 3,
					new ForceMovement(player.getPosition().clone(), toKnock, 0, 15, 0, 0)));
			CombatFactory.stun(player, 4, false);
		}
	}
}
