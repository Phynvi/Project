package com.overload.game.content.combat.method.impl.specials;

import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.combat.CombatSpecial;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.content.combat.ranged.RangedData.RangedWeapon;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Priority;
import com.overload.game.model.Projectile;

public class ArmadylCrossbowCombatMethod implements CombatMethod {

    private static final Animation ANIMATION = new Animation(4230, Priority.HIGH);

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
        return new PendingHit[]{new PendingHit(character, target, this, true, 2)};
    }

    @Override
    public boolean canAttack(Character character, Character target) {
        Player player = character.getAsPlayer();

        //Check if current player's ranged weapon data is armadyl crossbow
        if (!(player.getCombat().getRangedWeapon() != null
                && player.getCombat().getRangedWeapon() == RangedWeapon.ARMADYL_CROSSBOW)) {
            return false;
        }

        //Check if player has enough ammunition to fire.
        if (!CombatFactory.checkAmmo(player, 1)) {
            return false;
        }

        return true;
    }

    @Override
    public void prepareAttack(Character character, Character target) {
        final Player player = character.getAsPlayer();

        CombatSpecial.drain(player, CombatSpecial.ARMADYL_CROSSBOW.getDrainAmount());

        //Fire projectile
        new Projectile(character, target, 301, 50, 70, 44, 35, 3).sendProjectile();

        //Decrement ammo by 1
        CombatFactory.decrementAmmo(player, target.getPosition(), 1);
    }

    @Override
    public int getAttackSpeed(Character character) {
        return character.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Character character) {
        return 6;
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

    }
}