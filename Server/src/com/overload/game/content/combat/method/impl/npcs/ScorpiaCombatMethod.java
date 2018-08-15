package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.World;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Position;
import com.overload.game.model.Projectile;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;

import java.util.concurrent.CopyOnWriteArrayList;

public class ScorpiaCombatMethod implements CombatMethod {

    private static CopyOnWriteArrayList<NPC> npcList = new CopyOnWriteArrayList<>();

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
        NPC npc = character.getAsNpc();
        Player player = target.getAsPlayer();
        if(npc.getHitpoints() <= 50) {
            TaskManager.submit(new Task() {
                @Override
                protected void execute() {
                    npcList.add(spawnnpc(6617, new Position(npc.getX() + 3, npc.getY(), npc.getZ()), 175));
                    npcList.add(spawnnpc(6617, new Position(npc.getX() + 3, npc.getY(), npc.getZ()), 175));
                    stop();
                }
            });
        }
        if(npc.getDefinition().getId() == 6617) {
            npc.executeProjectile(new Projectile(npc, character, 168, 45, 65, 35, 35, 0));
            TaskManager.submit(new Task() {
                int tick = 0;
                @Override
                protected void execute() {
                    if(tick == 2)
                    character.heal(10);
                    if (tick == 20) {
                        World.getRemoveNPCQueue().add(npc);
                    }
                    tick++;
                    stop();
                }
            });
        }

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
        return CombatType.MELEE;
    }

    @Override
    public PendingHit[] getHits(Character character, Character target) {
        return new PendingHit[] { new PendingHit(character, target, this, true, 1) };
    }

    @Override
    public void finished(Character character, Character target) {

    }

    @Override
    public void handleAfterHitEffects(PendingHit hit) {

    }

    public static NPC spawnnpc(int id, Position pos, int constitution) {
        NPC np = new NPC(id, pos);
        np.setHitpoints(constitution);
        np.getDefinition().setAggressive(false);
        World.getAddNPCQueue().add(np);
        return np;
    }
}
