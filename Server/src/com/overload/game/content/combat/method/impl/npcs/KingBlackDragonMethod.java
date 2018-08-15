package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.content.PrayerHandler;
import com.overload.game.content.combat.CombatEquipment;
import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Projectile;
import com.overload.game.task.impl.CombatPoisonEffect.PoisonType;
import com.overload.util.Misc;

public class KingBlackDragonMethod implements CombatMethod {

    private CombatType currentAttackType = CombatType.MAGIC;
    private Breath currentBreath = Breath.DRAGON;

    @Override
    public boolean canAttack(Character character, Character target) {
        return true;
    }

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public void prepareAttack(Character character, Character target) {
        if (currentAttackType == CombatType.MAGIC) {
            switch (currentBreath) {
                case DRAGON:
                    character.executeProjectile(new Projectile(character, target, 393, 40, 55, 31, 43, 0));
                    break;
                case ICE:
                    character.executeProjectile(new Projectile(character, target, 396, 40, 55, 31, 43, 0));
                    break;
                case POISON:
                    character.executeProjectile(new Projectile(character, target, 394, 40, 55, 31, 43, 0));
                    break;
                case SHOCK:
                    character.executeProjectile(new Projectile(character, target, 395, 40, 55, 31, 43, 0));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getAttackSpeed(Character character) {
        return currentAttackType == CombatType.MAGIC ? 6 : 4;
    }

    @Override
    public int getAttackDistance(Character character) {
        return 8;
    }

    @Override
    public void startAnimation(Character character) {
        if (currentAttackType == CombatType.MAGIC) {
            character.performAnimation(new Animation(84));
        } else {
            character.performAnimation(new Animation(91));
        }
    }

    @Override
    public CombatType getCombatType() {
        return currentAttackType;
    }

    @Override
    public PendingHit[] getHits(Character character, Character target) {
        PendingHit hit = new PendingHit(character, target, this, true, 1);
        if (target.isPlayer()) {
        	Player p = target.getAsPlayer();
            if (currentAttackType == CombatType.MAGIC && currentBreath == Breath.DRAGON) {
                if (PrayerHandler.isActivated(p, PrayerHandler.PROTECT_FROM_MAGIC) && CombatEquipment.hasDragonProtectionGear(p) && !p.getAsPlayer().getCombat().getFireImmunityTimer().finished()) {
                    target.getAsPlayer().getPacketSender().sendMessage("You're protected against the dragonfire breath.");
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
                p.getAsPlayer().getPacketSender().sendMessage("The dragonfire burns you.");
                hit.getHits()[0].incrementDamage(extendedHit);
            }
            if (currentAttackType == CombatType.MAGIC) {
                switch (currentBreath) {
                    case ICE:
                        CombatFactory.freeze(hit.getTarget().getAsPlayer(), 5);
                        break;
                    case POISON:
                        CombatFactory.poisonEntity(hit.getTarget().getAsPlayer(), PoisonType.SUPER);
                        break;
                    default:
                        break;

                }
            }
        }
        return new PendingHit[]{hit};
    }

    @Override
    public void finished(Character character, Character target) {
        if (character.getPosition().getDistance(target.getPosition()) <= 3) {
            if (Misc.randomInclusive(0, 2) == 0) {
                currentAttackType = CombatType.MAGIC;
            } else {
                currentAttackType = CombatType.MELEE;
            }
        } else {
            currentAttackType = CombatType.MAGIC;
        }

        if (currentAttackType == CombatType.MAGIC) {
            int random = Misc.randomInclusive(0, 10);
            if (random >= 0 && random <= 3) {
                currentBreath = Breath.DRAGON;
            } else if (random >= 4 && random <= 6) {
                currentBreath = Breath.SHOCK;
            } else if (random >= 7 && random <= 9) {
                currentBreath = Breath.POISON;
            } else {
                currentBreath = Breath.ICE;
            }
        }
    }

    @Override
    public void handleAfterHitEffects(PendingHit hit) {

    }

    private enum Breath {
        ICE,
        POISON,
        SHOCK,
        DRAGON;
    }

}