package com.overload.game.content.combat.method.impl.specials;

import com.overload.game.content.combat.CombatSpecial;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Graphic;
import com.overload.game.model.GraphicHeight;
import com.overload.game.model.Priority;

public class AbyssalWhipCombatMethod implements CombatMethod {

    private static final Animation ANIMATION = new Animation(1658, Priority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(341, GraphicHeight.HIGH, Priority.HIGH);

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
        return new PendingHit[]{new PendingHit(character, target, this, true, 0)};
    }

    @Override
    public boolean canAttack(Character character, Character target) {
        return true;
    }

    @Override
    public void prepareAttack(Character character, Character target) {
        CombatSpecial.drain(character, CombatSpecial.ABYSSAL_WHIP.getDrainAmount());
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
    }

    @Override
    public void finished(Character character, Character target) {

    }

    @Override
    public void handleAfterHitEffects(PendingHit hit) {
        Character target = hit.getTarget();

        if (target.getHitpoints() <= 0) {
            return;
        }

        target.performGraphic(GRAPHIC);
        if (target.isPlayer()) {
            Player p = (Player) target;
            int totalRunEnergy = p.getRunEnergy() - 25;
            if (totalRunEnergy < 0) {
                totalRunEnergy = 0;
            }
            p.setRunEnergy(totalRunEnergy);
            p.getPacketSender().sendRunEnergy();
            if (totalRunEnergy == 0) {
                p.setRunning(false);
                p.getPacketSender().sendRunStatus();
            }
        }
    }
}