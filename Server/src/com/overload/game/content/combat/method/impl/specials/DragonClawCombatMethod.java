package com.overload.game.content.combat.method.impl.specials;

import com.overload.game.content.combat.CombatSpecial;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.model.Animation;
import com.overload.game.model.Graphic;
import com.overload.game.model.Priority;

public class DragonClawCombatMethod implements CombatMethod {

    private static final Animation ANIMATION = new Animation(7527, Priority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(1171, Priority.HIGH);

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
        PendingHit qHit = new PendingHit(character, target, this, true, 4, 0);

		/* DRAGON CLAWS SPECIAL FORMULA */

        int first = qHit.getHits()[0].getDamage();
        int second = first <= 0 ? qHit.getHits()[1].getDamage() : (first / 2);
        int third = second <= 0 ? second : (second / 2);
        int fourth = second <= 0 ? second : (second / 2);

        qHit.getHits()[0].setDamage(first);
        qHit.getHits()[1].setDamage(second);
        qHit.getHits()[2].setDamage(third);
        qHit.getHits()[3].setDamage(fourth);
        qHit.updateTotalDamage();

        return new PendingHit[]{qHit};
    }

    @Override
    public boolean canAttack(Character character, Character target) {
        return true;
    }

    @Override
    public void prepareAttack(Character character, Character target) {
        CombatSpecial.drain(character, CombatSpecial.DRAGON_CLAWS.getDrainAmount());
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

    }
}