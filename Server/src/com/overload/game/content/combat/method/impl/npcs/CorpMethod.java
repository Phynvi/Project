package com.overload.game.content.combat.method.impl.npcs;

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
import com.overload.util.Misc;
import com.overload.util.timers.TimerKey;

import java.util.ArrayList;
import java.util.List;


public class CorpMethod implements CombatMethod {

    private enum Attack {
        MELEE_ATTACK, DEFAULT_MAGIC_ATTACK, SPECIAL_ATTACK;
    }

    private Attack attack = Attack.MELEE_ATTACK;
    private static final Graphic MAGE_END_GFX = new Graphic(317, GraphicHeight.HIGH);
    private static final Animation MELEE_ATTACK_ANIM = new Animation(1683);
    private static final Animation MAGE_ATTACK_ANIM = new Animation(1680);



    @Override
    public boolean canAttack(Character character, Character target) {
        return true;
    }

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public PendingHit[] getHits(Character character, Character target) {
        if (attack == Attack.SPECIAL_ATTACK) {
            return null;
        }
        int delay = 2;
        if (attack == Attack.MELEE_ATTACK) {
            delay = 0;
        }
        return new PendingHit[] { new PendingHit(character, target, this, true, delay) };
    }

    @Override
    public void prepareAttack(Character character, Character target) {
        if (!character.isNpc() || !target.isPlayer())
            return;

        attack = Attack.MELEE_ATTACK;


        if (Misc.getRandom(16) < 4) {
        	if (Misc.getRandom(20) > 14) {
        		attack = Attack.SPECIAL_ATTACK;//WILL BE DARK CORE
        	} else {
        		attack = Attack.DEFAULT_MAGIC_ATTACK;
        	}
        }

        if (attack == Attack.DEFAULT_MAGIC_ATTACK) {
            character.performAnimation(MAGE_ATTACK_ANIM);
            character.executeProjectile(new Projectile(character, target, 316, 0, 75, 43, 35, 0));

            List<Position> attackPositions = new ArrayList<>();
            TaskManager.submit(new Task(3, target, false) {
                @Override
                public void execute() {
                    target.performGraphic(MAGE_END_GFX);
                    for (int i = 0; i < 5; i++) {
                        attackPositions.add(new Position((target.getX() - 1) + Misc.getRandom(5), (target.getY() - 1) + Misc.getRandom(5)
                    		)
                        );
                    }
                    stop();
                }
                
                @Override
                public void stop() {
                	this.setEventRunning(false);
                	 TaskManager.submit(new Task(4, target, true) {
                     	int splashTick = 0;
                         @Override
                         public void execute() {
                             if (splashTick == 0) {
                                 for (Position pos : attackPositions) {
                                     character.executeProjectile(new Projectile(target.getPosition(), pos, 0, 315, 75, 25, 43, 35, 0));
                                 }
                             } 

                             if (splashTick == 1) {
         	                     for (Position pos : attackPositions) {
         	                         target.getAsPlayer().getPacketSender().sendGlobalGraphic(MAGE_END_GFX, pos);
         	                         for (Player player : character.getAsNpc().getNearbyPlayers(10)) {
         	                             if (player.getPosition().equals(pos)) {
         	                                 player.getCombat().getHitQueue()
         	                                        .addPendingDamage(new HitDamage(Misc.getRandom(25), HitMask.RED));
         	                             }
         	                         }
         	                     }
                            	 splashTick++;
         	                     finished(character, target);
	                           	 attack = Attack.MELEE_ATTACK;
	                           	 this.setEventRunning(false);
         	                     stop();
                             }
                             splashTick++;
                         }
                         
                         @Override
                         public void stop() {
                        	 attack = Attack.MELEE_ATTACK;
                        	 this.setEventRunning(false);
                         }
                     });
                     character.getTimers().register(TimerKey.COMBAT_ATTACK, 5);
                }
            });
        } else if (attack == Attack.SPECIAL_ATTACK) {
           
        } else if (attack == Attack.MELEE_ATTACK) {
            character.performAnimation(MELEE_ATTACK_ANIM);
        }
    }

    @Override
    public int getAttackSpeed(Character character) {
        return character.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Character character) {
        if (attack == Attack.MELEE_ATTACK) {
            return 3;
        }
        return 7;
    }

    @Override
    public void startAnimation(Character character) {
    }

    @Override
    public void finished(Character character, Character target) {
    }

    @Override
    public void handleAfterHitEffects(PendingHit hit) {
    }

    @Override
    public CombatType getCombatType() {
        if (attack == Attack.MELEE_ATTACK) {
            return CombatType.MELEE;
        }
        return CombatType.MAGIC;
    }
}

