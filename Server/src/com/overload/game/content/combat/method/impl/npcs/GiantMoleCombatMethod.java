package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Graphic;
import com.overload.game.model.Position;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class GiantMoleCombatMethod implements CombatMethod {

    private enum Attack {
        DIG, DEFAULT_MELEE_ATTACK
    }

    private Attack attack = Attack.DEFAULT_MELEE_ATTACK;

    private List<Position> positions = new ArrayList<Position>();

    public GiantMoleCombatMethod() {
        positions.add(new Position(1778, 5237));
        positions.add(new Position(1761, 5186));
        positions.add(new Position(1737, 5709));
        positions.add(new Position(1737, 5227));
    }

    private Position random() {
        return Misc.randomElement(positions);
    }

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
        Player player = target.getAsPlayer();
        NPC npc = character.getAsNpc();

        attack = Attack.DEFAULT_MELEE_ATTACK;

        if (attack == Attack.DEFAULT_MELEE_ATTACK) {
            character.performAnimation(new Animation(character.getAttackAnim()));
        }
        if (Misc.getRandom(9) < 3) {
            attack = Attack.DIG;
            player.sendMessage("The Giant Mole Starts Digging Down!");
            character.performAnimation(new Animation(3314, 2));
            character.performGraphic(new Graphic(571, 2));
            character.setUntargetable(true);
            TaskManager.submit(new Task(2, player, false) {
                @Override
                protected void execute() {
                    npc.performGraphic(new Graphic(572, 2));
                    npc.performAnimation(new Animation(3315));
                    npc.moveTo(random());
                    player.sendMessage("The Giant Mole has appeared in" + npc.getPosition().clone() + "!");
                    npc.setUntargetable(false);
                    npc.getCombat().reset();
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
        if (attack == Attack.DEFAULT_MELEE_ATTACK) {
            return CombatType.MELEE;
        }
        return CombatType.MELEE;
    }

    @Override
    public PendingHit[] getHits(Character character, Character target) {
        int hitDelay = (attack == Attack.DEFAULT_MELEE_ATTACK ? 1 : 3);
        return new PendingHit[] { new PendingHit(character, target, this, true, hitDelay) };
    }
    @Override
    public void finished(Character character, Character target) {

    }

    @Override
    public void handleAfterHitEffects(PendingHit hit) {

    }
}
