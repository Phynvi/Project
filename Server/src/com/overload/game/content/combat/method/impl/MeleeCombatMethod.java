package com.overload.game.content.combat.method.impl;

import com.overload.game.content.Dueling.DuelRule;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.WeaponInterfaces.WeaponInterface;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.dialogue.DialogueManager;

/**
 * Represents the combat method for melee attacks.
 *
 * @author Professor Oak
 */
public class MeleeCombatMethod implements CombatMethod {

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

    @Override
    public boolean canMove() {
        return true;
    }

	@Override
	public PendingHit[] getHits(Character character, Character target) {
		return new PendingHit[] { new PendingHit(character, target, this, true, 0) };
	}

	@Override
	public boolean canAttack(Character character, Character target) {

		// Duel, disabled melee?
		if (character.isPlayer()) {
			Player player = character.getAsPlayer();
			if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_MELEE.ordinal()]) {
				DialogueManager.sendStatement(player, "Melee has been disabled in this duel!");
				player.getCombat().reset();
				return false;
			}
		}

		return true;
	}

	@Override
	public void prepareAttack(Character character, Character target) {

	}

	@Override
	public int getAttackSpeed(Character character) {
		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		if (character.getCombat().getWeapon() == WeaponInterface.HALBERD) {
			return 2;
		}
		return 1;
	}

	@Override
	public void startAnimation(Character character) {
		int animation = character.getAttackAnim();

		if (animation != -1) {
			character.performAnimation(new Animation(animation));
		}
	}

	@Override
	public void finished(Character character, Character target) {

	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {

	}

}
