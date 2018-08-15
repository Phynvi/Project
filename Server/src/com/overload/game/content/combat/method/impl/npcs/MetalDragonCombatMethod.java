package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.content.PrayerHandler;
import com.overload.game.content.combat.CombatEquipment;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Priority;
import com.overload.game.model.Projectile;
import com.overload.util.Misc;

public class MetalDragonCombatMethod implements CombatMethod {


    private CombatType currentAttackType = CombatType.MELEE;
    private static final Animation MELEE_ATTACK_ANIM = new Animation(80, Priority.HIGH);
    private static final Animation RANGED_ATTACK_ANIM = new Animation(81, Priority.HIGH);

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
        int random = Misc.getRandom(5);
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
                character.performAnimation(RANGED_ATTACK_ANIM);
                character.executeProjectile(new Projectile(character, target, 54, 45, 75, 43, 35, 0));
                break;
            case MELEE:
                character.performAnimation(MELEE_ATTACK_ANIM);
                if (Misc.getRandom(2) < 1)
                    character.performAnimation(new Animation(91));
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
        PendingHit hit = new PendingHit(character, target, this, true, 1);
        if (target.isPlayer()) {
            Player p = target.getAsPlayer();
            if (currentAttackType == CombatType.MAGIC) {
                if (PrayerHandler.isActivated(p, PrayerHandler.PROTECT_FROM_MAGIC)
                        && CombatEquipment.hasDragonProtectionGear(p) && !p.getAsPlayer().getCombat().getFireImmunityTimer().finished()) {
                    target.getAsPlayer().getPacketSender().sendMessage("@or2@You're protected against the dragonfire breath.");
                    return new PendingHit[]{hit};
                }
                int extendedHit = 25;
                if (PrayerHandler.isActivated(p, PrayerHandler.PROTECT_FROM_MAGIC)) {
                    extendedHit -= 5;
                }
                if (!p.getAsPlayer().getCombat().getFireImmunityTimer().finished()) {
                    extendedHit -= 10;
                }
                if (CombatEquipment.hasDragonProtectionGear(p)) {
                    extendedHit -= 10;
                }
                p.getAsPlayer().getPacketSender().sendMessage("@or2@The dragonfire burns you.");
                hit.getHits()[0].incrementDamage(extendedHit);
            }
        }
        return new PendingHit[]{hit};
    }
    @Override
    public int getAttackSpeed(Character character) {
        return character.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Character character) {
        return 4;
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
            }
        }



