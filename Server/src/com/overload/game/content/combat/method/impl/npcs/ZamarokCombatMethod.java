package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.util.Misc;



public class ZamarokCombatMethod implements CombatMethod {

    private CombatType currentAttackType = CombatType.MELEE;

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
            currentAttackType = CombatType.MAGIC;
        }
        if (distance <= character.getSize()) {
            if (random == 1) {
                currentAttackType = CombatType.MELEE;
            }
        }

        switch (currentAttackType) {
            case MAGIC:
                character.performAnimation(new Animation(6950));
                character.performGraphic(new Graphic(1221, 60));
                character.executeProjectile(new Projectile(character, target, 1225, 45, 65, 43, 35, 0));
                break;
            case MELEE:
                character.performAnimation(new Animation(6948));
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
        return 3;
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
        if (!hit.isAccurate() || hit.getTarget() == null || !hit.getTarget().isPlayer()) {
            return;
        }
        final Player player = hit.getTarget().getAsPlayer();
        if (Misc.getRandom(50) <= 15) {
            player.getSkillManager().decreaseCurrentLevel(Skill.PRAYER, (int) (hit.getTotalDamage() * 0.35), 0);
            player.getPacketSender().sendMessage("K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained.");

            }
        }
    }
