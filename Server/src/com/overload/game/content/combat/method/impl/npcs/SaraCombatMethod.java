package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Graphic;
import com.overload.game.model.Priority;
import com.overload.util.Misc;

import java.util.Random;

public class SaraCombatMethod implements CombatMethod {

    private CombatType currentAttackType = CombatType.MELEE;
    private static final Animation MELEE_ATTACK_ANIM = new Animation(6967, Priority.HIGH);
    private static final Animation RANGED_ATTACK_ANIM = new Animation(6970, Priority.HIGH);
    private static final Graphic RANGED_ATTACK_GRAPHIC = new Graphic(1221, Priority.HIGH);
    private static final Graphic MELEE_ATTACK_GRAPHIC = new Graphic(428, Priority.HIGH);


    private static final String[] MESSAGES = {"Death to the enemies of the light!",
            "Slay the evil ones!",
            "Saradomin lend me strength!",
            "By the power of Saradomin!",
            "May Saradomin be my sword.",
            "Good will always triumph!",
            "Forward! Our allies are with us!",
            "Saradomin is with us!",
            "In the name of Saradomin!",
            "Attack! Find the Godsword!",
            "All praise Saradomin!"};
    /**
     * The random number generator.
     */
    private final Random random = new Random();
    private long lastMessage;

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

        if (random.nextInt(3) == 2 && System.currentTimeMillis() - lastMessage > 3000) {
            ((NPC) character).forceChat(MESSAGES[random.nextInt(MESSAGES.length)]);
            lastMessage = System.currentTimeMillis();
        }

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
                character.performAnimation(RANGED_ATTACK_ANIM);
                target.performGraphic(new Graphic(1221, 60));
                break;
            case MELEE:
                character.performAnimation(MELEE_ATTACK_ANIM);
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
        return 1;
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
        if (hit.getTarget() == null || !hit.getTarget().isPlayer()) {
            return;
        }

        final Player player = hit.getTarget().getAsPlayer();

        if (currentAttackType == CombatType.MELEE) {
            player.performGraphic(MELEE_ATTACK_GRAPHIC);

            if (currentAttackType == CombatType.MAGIC) {
                player.performGraphic(RANGED_ATTACK_GRAPHIC);
            }
        }
    }
}