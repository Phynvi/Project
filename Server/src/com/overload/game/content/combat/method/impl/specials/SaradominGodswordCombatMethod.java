package com.overload.game.content.combat.method.impl.specials;

import com.overload.game.content.combat.CombatSpecial;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Graphic;
import com.overload.game.model.Priority;
import com.overload.game.model.Skill;

public class SaradominGodswordCombatMethod implements CombatMethod {

    private static final Animation ANIMATION = new Animation(7640, Priority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(1209, Priority.HIGH);

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
        CombatSpecial.drain(character, CombatSpecial.SARADOMIN_GODSWORD.getDrainAmount());
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
        Player player = hit.getAttacker().getAsPlayer();
        int damage = hit.getTotalDamage();
        int damageHeal = (int) (damage * 0.5);
        int damagePrayerHeal = (int) (damage * 0.25);
        if (player.getSkillManager().getCurrentLevel(Skill.HITPOINTS) < player.getSkillManager().getMaxLevel(Skill.HITPOINTS)) {
            int level = player.getSkillManager().getCurrentLevel(Skill.HITPOINTS) + damageHeal > player.getSkillManager().getMaxLevel(Skill.HITPOINTS) ? player.getSkillManager().getMaxLevel(Skill.HITPOINTS) : player.getSkillManager().getCurrentLevel(Skill.HITPOINTS) + damageHeal;
            player.getSkillManager().setCurrentLevel(Skill.HITPOINTS, level);
        }
        if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager().getMaxLevel(Skill.PRAYER)) {
            int level = player.getSkillManager().getCurrentLevel(Skill.PRAYER) + damagePrayerHeal > player.getSkillManager().getMaxLevel(Skill.PRAYER) ? player.getSkillManager().getMaxLevel(Skill.PRAYER) : player.getSkillManager().getCurrentLevel(Skill.PRAYER) + damagePrayerHeal;
            player.getSkillManager().setCurrentLevel(Skill.PRAYER, level);
        }
    }
}