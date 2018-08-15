package com.overload.game.content.combat.method.impl;

import com.overload.game.content.Dueling.DuelRule;
import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.content.combat.ranged.RangedData.Ammunition;
import com.overload.game.content.combat.ranged.RangedData.RangedWeapon;
import com.overload.game.content.combat.ranged.RangedData.RangedWeaponType;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Projectile;
import com.overload.game.model.dialogue.DialogueManager;

/**
 * Represents the combat method for ranged attacks.
 *
 * @author Professor Oak
 */
public class RangedCombatMethod implements CombatMethod {

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

        // Darkbow is double hits.
        // Ballista is 1 tick slower
        if (character.getCombat().getRangedWeapon() != null) {
            if (character.getCombat().getRangedWeapon() == RangedWeapon.DARK_BOW) {
                return new PendingHit[]{new PendingHit(character, target, this, true, 2),
                        new PendingHit(character, target, this, true, 3)};
            } else if (character.getCombat().getRangedWeapon() == RangedWeapon.BALLISTA) {
                return new PendingHit[]{new PendingHit(character, target, this, true, 3)};
            }
        }

        return new PendingHit[]{new PendingHit(character, target, this, true, 2)};
    }

    @Override
    public boolean canAttack(Character character, Character target) {

        if (character.isNpc()) {
            return true;
        }

        Player p = character.getAsPlayer();

        // Duel, disabled ranged?
        if (p.getDueling().inDuel() && p.getDueling().getRules()[DuelRule.NO_RANGED.ordinal()]) {
            DialogueManager.sendStatement(p, "Ranged has been disabled in this duel!");
            p.getCombat().reset();
            return false;
        }

        int ammoRequired = 1;

        // Check for weapons that require more arrows than 1 to fire..
        if (p.getCombat().getRangedWeapon() == RangedWeapon.DARK_BOW) {
            ammoRequired = 2;
        }

        // Check that we have the ammo required
        if (!CombatFactory.checkAmmo(p, ammoRequired)) {
            return false;
        }

        return true;
    }

    @Override
    public void prepareAttack(Character character, Character target) {

        final Ammunition ammo = character.getCombat().getAmmunition();
        final RangedWeapon rangedWeapon = character.getCombat().getRangedWeapon();

        if (ammo == null || rangedWeapon == null) {
            return;
        }

        int projectileId = ammo.getProjectileId();
        int delay = 40;
        int speed = 60;
        int heightEnd = 31;
        int heightStart = 43;
        int curve = 0;

        if (rangedWeapon.getType() == RangedWeaponType.CROSSBOW) {
            delay = 46;
            speed = 62;
            heightStart = 44;
            heightEnd = 35;
            curve = 3;
        } else if (rangedWeapon.getType() == RangedWeaponType.LONGBOW) {
            speed = 70;
        } else if (rangedWeapon.getType() == RangedWeaponType.BLOWPIPE) {
            speed = 60;
            heightStart = 40;
            heightEnd = 35;
            curve = 5;
        }
        if (ammo == Ammunition.TOKTZ_XIL_UL) {
            delay = 30;
            speed = 55;
        }

        // Fire projectile
        new Projectile(character, target, projectileId, delay, speed, heightStart, heightEnd, curve).sendProjectile();

        // Dark bow sends two arrows, so send another projectile and delete another
        // arrow.
        if (rangedWeapon == RangedWeapon.DARK_BOW) {
            new Projectile(character, target, ammo.getProjectileId(), delay - 7, speed + 4, heightStart + 5, heightEnd,
                    curve).sendProjectile();

            // Decrement 2 ammo if d bow
            if (character.isPlayer()) {
                CombatFactory.decrementAmmo(character.getAsPlayer(), target.getPosition(), 2);
            }

        } else {

            // Decrement 1 ammo
            if (character.isPlayer()) {
                CombatFactory.decrementAmmo(character.getAsPlayer(), target.getPosition(), 1);
            }
        }
    }

    @Override
    public int getAttackSpeed(Character character) {
        return character.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Character character) {
    	RangedWeapon weapon = character.getCombat().getRangedWeapon();
		if (weapon != null) {

			// Long range fight type has longer attack distance than other types
			if (character.getCombat().getFightType() == weapon.getType().getLongRangeFightType()) {
				return weapon.getType().getLongRangeDistance();
			}

			return weapon.getType().getDefaultDistance();
		}
		return 6;
    }

    @Override
    public void startAnimation(Character character) {
        int animation = character.getAttackAnim();

        if (animation != -1) {
            character.performAnimation(new Animation(animation));
        }

        final Ammunition ammo = character.getCombat().getAmmunition();
        if (ammo != null && ammo.getStartGraphic() != null) {

            // Check toxic blowpipe, it shouldn't have any start gfx.
            if (character.getCombat().getRangedWeapon() != null) {
                if (character.getCombat().getRangedWeapon() == RangedWeapon.TOXIC_BLOWPIPE) {
                    return;
                }
            }

            // Perform start gfx for ammo
            character.performGraphic(ammo.getStartGraphic());
        }
    }

    @Override
    public void finished(Character character, Character target) {

    }

    @Override
    public void handleAfterHitEffects(PendingHit hit) {

    }
}
