package com.overload.game.content.combat.method.impl.specials;

import com.overload.game.content.combat.CombatSpecial;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.model.Animation;
import com.overload.game.model.Graphic;
import com.overload.game.model.Priority;

public class SaradominSwordCombatMethod implements CombatMethod {

    private static final Graphic ENMEMY_GRAPHIC = new Graphic(1196, Priority.HIGH);
    private static final Animation ANIMATION = new Animation(1132, Priority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(1213, Priority.HIGH);

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
        PendingHit qHit = new PendingHit(character, target, this, true, 2, 0);

		/* SARA SWORD SPECIAL FORMULA */

        if (!qHit.isAccurate()) {
            qHit.getHits()[1].setDamage(0);
        } else {
            qHit.getHits()[1].setDamage(qHit.getHits()[0].getDamage() + 16);
        }

        return new PendingHit[]{qHit};
    }

    @Override
    public boolean canAttack(Character character, Character target) {
        return true;
    }

    @Override
    public void prepareAttack(Character character, Character target) {
        CombatSpecial.drain(character, CombatSpecial.SARADOMIN_SWORD.getDrainAmount());
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
    public void startAnimation(Character character) {
        character.performAnimation(ANIMATION);
        character.performGraphic(GRAPHIC);
    }

    @Override
    public void finished(Character character, Character target) {

    }

    @Override
    public void handleAfterHitEffects(PendingHit hit) {
        hit.getTarget().performGraphic(ENMEMY_GRAPHIC);
    }
}