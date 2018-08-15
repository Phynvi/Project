package com.overload.game.content.combat.method.impl.specials;

import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.combat.CombatSpecial;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.content.combat.ranged.RangedData.Ammunition;
import com.overload.game.content.combat.ranged.RangedData.RangedWeapon;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;

public class DarkBowCombatMethod implements CombatMethod {

	private static final Animation ANIMATION = new Animation(426, Priority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(1100, GraphicHeight.HIGH, Priority.HIGH);

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
	public PendingHit[] getHits(Character character, Character target) {
		return new PendingHit[] { new PendingHit(character, target, this, true, 3),
				new PendingHit(character, target, this, true, 2) };
	}

	@Override
	public boolean canAttack(Character character, Character target) {
		Player player = character.getAsPlayer();

		// Check if current player's ranged weapon data is dark bow.
		if (!(player.getCombat().getRangedWeapon() != null
				&& player.getCombat().getRangedWeapon() == RangedWeapon.DARK_BOW)) {
			return false;
		}

		// Check if player has enough ammunition to fire.
		if (!CombatFactory.checkAmmo(player, 2)) {
			return false;
		}

		return true;
	}

	@Override
	public void prepareAttack(Character character, Character target) {
		final Player player = character.getAsPlayer();

		CombatSpecial.drain(player, CombatSpecial.DARK_BOW.getDrainAmount());

		// Get proper projectile id
		int projectileId = 1099;
		if (player.getCombat().getAmmunition() != Ammunition.DRAGON_ARROW) {
			projectileId = 1101;
		}

		// Send 2 arrow projectiles
		new Projectile(player, target, projectileId, 40, 70, 43, 31, 0).sendProjectile();
		new Projectile(character, target, projectileId, 33, 74, 48, 31, 0).sendProjectile();

		// Decrement 2 arrows from ammunition
		CombatFactory.decrementAmmo(player, target.getPosition(), 2);
	}

	@Override
	public int getAttackSpeed(Character character) {
		return character.getBaseAttackSpeed() + 1;
	}

	@Override
	public int getAttackDistance(Character character) {
		return CombatFactory.RANGED_COMBAT.getAttackDistance(character);
	}

	@Override
	public void startAnimation(Character character) {
		character.performAnimation(ANIMATION);
	}

	@Override
	public void finished(Character character, Character target) {

	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {
		hit.getTarget().performGraphic(GRAPHIC);
	}
}