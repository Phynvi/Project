package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.util.Misc;

public class BandosCombatMethod implements CombatMethod {

    private CombatType currentAttackType = CombatType.MELEE;
    private static final Animation MELEE_ATTACK_ANIM = new Animation(7018, Priority.HIGH);
    private static final Animation RANGED_ATTACK_ANIM = new Animation(7021, Priority.HIGH);
    private static final Graphic RANGED_ATTACK_GRAPHIC = new Graphic(1218, Priority.HIGH);
    private static final Graphic MELEE_ATTACK_GRAPHIC = new Graphic(428, Priority.HIGH);

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

        currentAttackType = CombatType.MELEE;
        int random = Misc.getRandom(1);
        if (random == 0) {
            currentAttackType = CombatType.RANGED;
        }
        if (distance <= character.getSize()) {
            if (random == 1) {
                currentAttackType = CombatType.MELEE;
            }
        }

        switch (currentAttackType) {
            case RANGED:
                character.performAnimation(RANGED_ATTACK_ANIM);
                character.getAsNpc().executeProjectile(new Projectile(character, target, 1217, 25, 100, 43, 33, 0));
                break;
            case MELEE:
                character.performAnimation(MELEE_ATTACK_ANIM);
                character.performGraphic(new Graphic(428));
                break;
			default:
				break;
        }
    }

    @Override
    public void finished(Character character, Character target) {

    }

    @Override
    public PendingHit[] getHits(Character character, Character target) {
        int hitDelay = (currentAttackType == CombatType.MELEE ? 1 : 3);
        return new PendingHit[]{new PendingHit(character, target, this, true, hitDelay)};
    }

    @Override
    public int getAttackSpeed(Character character) {
        return character.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Character character) {
        return 1;
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
        if (hit.getTarget() == null || !hit.getTarget().isPlayer()) {
            return;
        }

        final Player player = hit.getTarget().getAsPlayer();

        if (currentAttackType == CombatType.MELEE) {
            player.performGraphic(MELEE_ATTACK_GRAPHIC);

            if (currentAttackType == CombatType.RANGED) {
                player.performGraphic(RANGED_ATTACK_GRAPHIC);
            }
        }
    }
}
