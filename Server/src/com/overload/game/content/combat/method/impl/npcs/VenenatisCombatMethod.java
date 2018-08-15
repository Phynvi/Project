package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.util.Misc;

/**
 * Handles Venenatis' combat.
 *
 * @author Professor Oak
 */
public class VenenatisCombatMethod implements CombatMethod {

	private static final Animation MELEE_ATTACK_ANIMATION = new Animation(5319);
	private static final Animation MAGIC_ATTACK_ANIMATION = new Animation(5322);
	private static final Graphic DRAIN_PRAYER_GRAPHIC = new Graphic(172, GraphicHeight.MIDDLE);

	private CombatType currentAttackType = CombatType.MELEE;

	@Override
	public CombatType getCombatType() {
		return currentAttackType;
	}

	@Override
	public boolean canAttack(Character character, Character target) {
		return true;
	}

	@Override
	public PendingHit[] getHits(Character character, Character target) {
		return new PendingHit[] { new PendingHit(character, target, this, true, 1) };
	}

	@Override
	public void prepareAttack(Character character, Character target) {
		if (currentAttackType == CombatType.MAGIC) {
			character.executeProjectile(new Projectile(character, target, 165, 40, 55, 31, 43, 0));
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
		if (currentAttackType == CombatType.MELEE) {
			character.performAnimation(MELEE_ATTACK_ANIMATION);
		} else {
			character.performAnimation(MAGIC_ATTACK_ANIMATION);
		}
	}

	@Override
	public void finished(Character character, Character target) {
		// Switch attack type after each attack
		if (currentAttackType == CombatType.MAGIC) {
			currentAttackType = CombatType.MELEE;
		} else {
			currentAttackType = CombatType.MAGIC;

			// Have a chance of comboing with magic by reseting combat delay.
			if (Misc.getRandom(10) <= 3) {
				character.getCombat().performNewAttack(true);
			}
		}
	}

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
	public void handleAfterHitEffects(PendingHit hit) {
		if (!hit.isAccurate() || hit.getTarget() == null || !hit.getTarget().isPlayer()) {
			return;
		}

		// Drain prayer randomly 15% chance
		if (Misc.getRandom(100) <= 15) {
			Player player = hit.getTarget().getAsPlayer();
			hit.getTarget().performGraphic(DRAIN_PRAYER_GRAPHIC);
			player.getSkillManager().decreaseCurrentLevel(Skill.PRAYER, (int) (hit.getTotalDamage() * 0.35), 0);
			player.getPacketSender().sendMessage("Venenatis drained your prayer!");
		}
	}
}
