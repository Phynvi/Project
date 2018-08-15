package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Graphic;
import com.overload.game.model.Priority;
import com.overload.game.model.Projectile;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.Misc;

/**
 * Handles Jad's combat.
 *
 * @author Professor Oak
 */
public class JadCombatMethod implements CombatMethod {

	private static final Animation MAGIC_ATTACK_ANIM = new Animation(2656, Priority.MEDIUM);
	private static final Animation RANGED_ATTACK_ANIM = new Animation(2652, Priority.MEDIUM);
	private static final Animation MELEE_ATTACK_ANIM = new Animation(2655, Priority.MEDIUM);
	private static final int MAGIC_ATTACK_PROJECTILE = 448;
	private static final Graphic RANGED_ATTACK_GRAPHIC = new Graphic(451, Priority.MEDIUM);
	private CombatType currentAttackType = CombatType.RANGED;

	@Override
	public void startAnimation(Character character) {
	}

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
	public void prepareAttack(Character character, Character target) {
		Player player = target.getAsPlayer();

		// Select attack type..
		int distance = character.getPosition().getDistance(player.getPosition());
		
		currentAttackType = CombatType.RANGED;
		int random = Misc.getRandom(1);
		if (random == 0) {
			currentAttackType = CombatType.MAGIC;
		}
		if (distance <= character.getSize()) {
			if (random == 1) {
				currentAttackType = CombatType.MELEE;
			}
		}

		switch (currentAttackType) {
		case MAGIC:
			character.performAnimation(MAGIC_ATTACK_ANIM);
			character.executeProjectile(new Projectile(character, target, MAGIC_ATTACK_PROJECTILE, 25, 100, 110, 33, 1));
			break;
		case RANGED:
			character.performAnimation(RANGED_ATTACK_ANIM);
			break;
		case MELEE:
			character.performAnimation(MELEE_ATTACK_ANIM);
			break;
		}
	}

	@Override
	public void finished(Character character, Character target) {
		if (currentAttackType == CombatType.RANGED) {
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					target.performGraphic(RANGED_ATTACK_GRAPHIC);
					stop();
				}
			});
		}
	}

	@Override
	public PendingHit[] getHits(Character character, Character target) {
		int hitDelay = (currentAttackType == CombatType.MELEE ? 1 : 3);
		return new PendingHit[] { new PendingHit(character, target, this, true, hitDelay) };
	}

	@Override
	public int getAttackSpeed(Character character) {
		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		return 40;
	}

	@Override
	public CombatType getCombatType() {
		return currentAttackType;
	}

	@Override
	public boolean canAttack(Character character, Character target) {
		return target.isPlayer();
	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {
	}
}
