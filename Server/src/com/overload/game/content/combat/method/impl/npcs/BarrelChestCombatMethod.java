package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.model.Animation;
import com.overload.game.model.Projectile;
import com.overload.util.Misc;

public class BarrelChestCombatMethod implements CombatMethod {

    private enum Attack {
        ZIP, DEFAULT_MAGIC_ATTACK, DEFAULT_MELEE_ATTACK
    }

    private Attack attack = Attack.DEFAULT_MELEE_ATTACK;
    private static final Animation MELEE_ATTACK_ANIM = new Animation(5894);
    private static final Animation DEFAULT_MAGIC_ATTACK_ANIM = new Animation(5895);
    private static final Animation ZIP_ATTACK_ANIM = new Animation(5896);

    @Override
    public void startAnimation(Character character) {
    }

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public void prepareAttack(Character character, Character target) {
        if (!character.isNpc() || !target.isPlayer())
            return;

        attack = Attack.DEFAULT_MELEE_ATTACK;

        if (Misc.getRandom(10) < 3) {
            attack = Attack.ZIP;
        }
        if (Misc.getRandom(30) < 3) {
            attack = Attack.DEFAULT_MAGIC_ATTACK;
        }
        if (attack == Attack.DEFAULT_MELEE_ATTACK) {
            character.performAnimation(MELEE_ATTACK_ANIM);
        }
        if (attack == Attack.ZIP) {
            character.performAnimation(ZIP_ATTACK_ANIM);
            character.executeProjectile(new Projectile(character, target, 258, 45, 70, 54, 32, 0));
        }
        if (attack == Attack.DEFAULT_MAGIC_ATTACK) {
            character.performAnimation(DEFAULT_MAGIC_ATTACK_ANIM);
            }
        }

    @Override
    public void finished(Character character, Character target) {


    }

    @Override
    public PendingHit[] getHits(Character character, Character target) {
        if (attack == Attack.ZIP) {
            return null;
        }
        int delay = 2;
        if (attack == Attack.DEFAULT_MELEE_ATTACK || attack == Attack.DEFAULT_MAGIC_ATTACK) {
            delay = 0;
        }
        return new PendingHit[] { new PendingHit(character, target, this, true, delay) };
    }

    @Override
    public int getAttackSpeed(Character character) {
        return character.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Character character) {
        return 5;
    }

    @Override
    public CombatType getCombatType() {
        if (attack == Attack.DEFAULT_MAGIC_ATTACK || attack == Attack.ZIP) {
            return CombatType.MAGIC;
        }
        return CombatType.MELEE;
    }

    @Override
    public boolean canAttack(Character character, Character target) {
        return target.isPlayer();
    }

    @Override
    public void handleAfterHitEffects(PendingHit hit) {

    }
}
