package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Graphic;
import com.overload.game.model.GraphicHeight;
import com.overload.game.model.Projectile;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;

public class PrimeCombatMethod implements CombatMethod {

    private CombatType currentAttackType = CombatType.MAGIC;

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
        NPC npc = character.getAsNpc();

        currentAttackType = CombatType.MAGIC;

            if (currentAttackType == CombatType.MAGIC) {
                    npc.performAnimation(new Animation(2854));
                    character.executeProjectile(new Projectile(character, target, 162, 40, 75, 70, 35, 1));
                    TaskManager.submit(new Task(3, player, false) {
                        @Override
                        public void execute() {
                            player.performGraphic(new Graphic(163 , GraphicHeight.HIGH));
                            this.stop();
                        }
                    });
            }
        }

    @Override
    public void finished(Character character, Character target) {

    }

    @Override
    public PendingHit[] getHits(Character character, Character target) {
        return new PendingHit[]{new PendingHit(character, target, this, true, 1)};
    }

    @Override
    public int getAttackSpeed(Character character) {
        return character.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Character character) {
        return 10;
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


