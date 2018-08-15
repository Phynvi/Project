package com.overload.game.content.combat.method.impl;

import com.overload.game.content.Dueling.DuelRule;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.magic.CombatSpell;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Graphic;
import com.overload.game.model.GraphicHeight;
import com.overload.game.model.dialogue.DialogueManager;

/**
 * Represents the combat method for magic attacks.
 *
 * @author Professor Oak
 */
public class MagicCombatMethod implements CombatMethod {

	public static final Graphic SPLASH_GRAPHIC = new Graphic(85, GraphicHeight.MIDDLE);

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
	public PendingHit[] getHits(Character character, Character target) {
		return new PendingHit[] { new PendingHit(character, target, this, true, 3) };
	}

	@Override
	public boolean canAttack(Character character, Character target) {

		if (character.isNpc()) {
			return true;
		}

		// Duel, disabled magic?
		if (character.isPlayer()) {
			Player player = character.getAsPlayer();
			if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_MAGIC.ordinal()]) {
				DialogueManager.sendStatement(player, "Magic has been disabled in this duel!");
				player.getCombat().reset();
				return false;
			}
		}

		// Set the current spell to the autocast spell if it's null.
		if (character.getCombat().getCastSpell() == null) {
			character.getCombat().setCastSpell(character.getCombat().getAutocastSpell());
		}

		// Character didn't have autocast spell either.
		if (character.getCombat().getCastSpell() == null) {
			return false;
		}

		return character.getCombat().getCastSpell().canCast(character.getAsPlayer(), true);
	}

	@Override
	public void prepareAttack(Character character, Character target) {
		CombatSpell spell = character.getCombat().getCastSpell();
		
		if (spell == null)
			spell = character.getCombat().getAutocastSpell();

		if (spell != null) {
			spell.startCast(character, target);
		}
	}

	@Override
	public int getAttackSpeed(Character character) {

		if (character.getCombat().getPreviousCast() != null) {
			return character.getCombat().getPreviousCast().getAttackSpeed();
		}

		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		return 10;
	}

	@Override
	public void startAnimation(Character character) {
	}

	@Override
	public void finished(Character character, Character target) {

		// Reset the castSpell to autocastSpell
		// Update previousCastSpell so effects can be handled.

		final CombatSpell current = character.getCombat().getCastSpell();

		character.getCombat().setCastSpell(null);

		if (character.getCombat().getAutocastSpell() == null) {
			character.getCombat().reset();
		}

		character.getCombat().setPreviousCast(current);
	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {
		Character attacker = hit.getAttacker();
		Character target = hit.getTarget();
		boolean accurate = hit.isAccurate();
		int damage = hit.getTotalDamage();

		if (attacker.getHitpoints() <= 0 || target.getHitpoints() <= 0) {
			return;
		}

		CombatSpell previousSpell = attacker.getCombat().getPreviousCast();

		if (previousSpell != null) {

			if (accurate) {

				// Send proper end graphics for the spell because it was accurate
				previousSpell.endGraphic().ifPresent(target::performGraphic);

			} else {

				// Send splash graphics for the spell because it wasn't accurate
				target.performGraphic(SPLASH_GRAPHIC);
			}

			previousSpell.finishCast(attacker, target, accurate, damage);

		}
	}
}
