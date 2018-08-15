package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.HitDamage;
import com.overload.game.content.combat.hit.HitMask;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.game.task.impl.CombatPoisonEffect;
import com.overload.util.Misc;
import com.overload.util.timers.TimerKey;

import java.util.ArrayList;
import java.util.List;

public class LizardShamanMethod implements CombatMethod {

    private static enum Attack {
       JUMP_ATTACK, DEFAULT_RANGED_ATTACK, DEFAULT_MELEE_ATTACK, DEFAULT_MAGE_ATTACK, ACID_ATTACK;
    }

    private Attack attack = Attack.DEFAULT_RANGED_ATTACK;
    private static final Animation JUMP_HIDE = new Animation(7152);
    private static final Animation JUMP_DOWN = new Animation(6946);


    @Override
    public boolean canAttack(Character character, Character target) {
        return true;
    }

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public void startAnimation(Character character) {

    }

    @Override
    public void prepareAttack(Character character, Character target) {

        attack = Attack.DEFAULT_RANGED_ATTACK;

        if (target.getPosition().getDistance(character.getPosition()) < 2 && Misc.getRandom(1) == 0) {
            attack = Attack.DEFAULT_MELEE_ATTACK;
        }

        if (Misc.getRandom(49) < 7) {
            attack = Attack.JUMP_ATTACK;
        }

        if (Misc.getRandom(9) < 3) {
            attack = Attack.ACID_ATTACK;
            character.performAnimation(new Animation(7193));
            character.executeProjectile(new Projectile(character, target, 1293, 40, 65, 31, 43, 0));
            CombatFactory.poisonEntity(target, CombatPoisonEffect.PoisonType.EXTRA);
        }

        if (attack == Attack.DEFAULT_MELEE_ATTACK) {
            character.performAnimation(new Animation(7192));
        }

        if (attack == Attack.DEFAULT_RANGED_ATTACK) {
            character.performAnimation(new Animation(character.getAttackAnim()));
            character.executeProjectile(new Projectile(character, target, 1291, 40, 65, 31, 43, 0));
            TaskManager.submit(new Task(3, target, false) {
                @Override
                public void execute() {
                    stop();
                }
            });
        } else if (attack == Attack.JUMP_ATTACK) {
            Position targetPos = target.getPosition();
            List<Position> attackPositions = new ArrayList<>();
            attackPositions.add(targetPos);
            character.performAnimation(JUMP_HIDE);
            TaskManager.submit(new Task(4) {
                @Override
                public void execute() {
                    for (Position pos : attackPositions) {
                        for (Player player : character.getAsNpc().getNearbyPlayers(10)) {
                            character.performAnimation(JUMP_DOWN);
                            character.moveTo(target.getPosition().clone());
                            if (player.getPosition().equals(pos)) {
                                player.getCombat().getHitQueue().addPendingDamage(new HitDamage(Misc.getRandom(25), HitMask.RED));
                            }
                        }
                    }
                    finished(character, target);
                    stop();
                }
            });
            character.getTimers().register(TimerKey.COMBAT_ATTACK, 5);
        } else if (attack == Attack.DEFAULT_MELEE_ATTACK) {
            character.performAnimation(new Animation(7192));
        }
    }

    @Override
    public int getAttackSpeed(Character character) {
        if (attack == Attack.DEFAULT_MELEE_ATTACK) {
            return 5;
        }
        return character.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Character character) {
        return 3;
    }

    @Override
    public CombatType getCombatType() {
        if (attack == Attack.DEFAULT_MELEE_ATTACK) {
            return CombatType.MELEE;
        }
        if (attack == Attack.DEFAULT_MAGE_ATTACK) {
            return CombatType.MAGIC;
        }
        return CombatType.RANGED;
    }

    @Override
    public PendingHit[] getHits(Character character, Character target) {
        if (attack == Attack.JUMP_ATTACK) {
            return null;
        }
        int delay = 5;
        if (attack ==  Attack.DEFAULT_RANGED_ATTACK) {
            delay = 0;
        }
        return new PendingHit[] { new PendingHit(character, target, this, true, delay) };
    }

    @Override
    public void finished(Character character, Character target) {

    }

    @Override
    public void handleAfterHitEffects(PendingHit hit) {

    }
}