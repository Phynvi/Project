package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.WeaponInterfaces;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.model.equipment.BonusManager;
import com.overload.util.Misc;

/**
 * Handles the Chaos Elemental's combat.
 *
 * @author Professor Oak
 */
public class ChaosElementalCombatMethod implements CombatMethod {

    private static final Graphic MELEE_COMBAT_GFX = new Graphic(869);
    private static final Graphic RANGED_COMBAT_GFX = new Graphic(867);
    private static final Graphic MAGIC_COMBAT_GFX = new Graphic(868);
    private ChaosElementalAttackType currentAttack = ChaosElementalAttackType.DEFAULT;
    private CombatType combatType = CombatType.MELEE;

    @Override
    public CombatType getCombatType() {
        return combatType;
    }

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public boolean canAttack(Character character, Character target) {
        return true;
    }

    @Override
    public PendingHit[] getHits(Character character, Character target) {
        return new PendingHit[]{new PendingHit(character, target, this, true, 2)};
    }

    @Override
    public void prepareAttack(Character character, Character target) {
        //Fire projectile
        character.executeProjectile(new Projectile(character, target, currentAttack.projectileId, 40, 70, 31, 43, 0));
    }

    @Override
    public int getAttackSpeed(Character character) {
        return character.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Character character) {
        return 8;
    }

    @Override
    public void startAnimation(Character character) {
        int animation = character.getAttackAnim();

        if (animation != -1) {
            character.performAnimation(new Animation(animation));
        }
    }

    @Override
    public void finished(Character character, Character target) {

        /**
         * Handles special attacks
         */
        currentAttack = ChaosElementalAttackType.DEFAULT;

        if (Misc.getRandom(100) <= 10) {
            currentAttack = ChaosElementalAttackType.DISARM;
        } else if (Misc.getRandom(100) <= 10) {
            currentAttack = ChaosElementalAttackType.TELEPORT;
        }

        /**
         * Always switch to random combat type
         */
        int randomAtackType = Misc.getRandom(CombatType.values().length - 1);
        combatType = CombatType.values()[randomAtackType];
    }

    @Override
    public void handleAfterHitEffects(PendingHit hit) {
        if (hit.getTarget() != null) {

            switch (combatType) {
                case MELEE:
                    hit.getTarget().performGraphic(MELEE_COMBAT_GFX);
                    break;
                case RANGED:
                    hit.getTarget().performGraphic(RANGED_COMBAT_GFX);
                    break;
                case MAGIC:
                    hit.getTarget().performGraphic(MAGIC_COMBAT_GFX);
                    break;
            }

            if (hit.getTarget().isPlayer()) {
                if (Misc.getRandom(100) <= 20) {
                    Player player = hit.getTarget().getAsPlayer();

                    //DISARMING
                    if (currentAttack == ChaosElementalAttackType.DISARM) {
                    	disarmAttack(player);
                    }
                    //TELEPORTING
                    else if (currentAttack == ChaosElementalAttackType.TELEPORT) {
                        player.moveTo(player.getPosition().add(Misc.getRandom(4), Misc.getRandom(4)));
                        player.getPacketSender().sendMessage("The Chaos elemental has teleported you.");
                    }
                }
            }
        }
    }
    
    public static void disarmAttack(Player player) {
        if (!player.getInventory().isFull()) {
            final int randomSlot = Misc.getRandom(player.getEquipment().capacity() - 1);
            final Item toDisarm = player.getEquipment().getItems()[randomSlot];
            if (toDisarm.isValid()) {
                player.getEquipment().set(randomSlot, new Item(-1, 0));
                player.getInventory().add(toDisarm.clone());
                player.getPacketSender().sendMessage("You have been disarmed!");
                WeaponInterfaces.assign(player);
                BonusManager.update(player);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
            }
        }
    }

    private static enum ChaosElementalAttackType {
        DEFAULT(558),
        DISARM(551),
        TELEPORT(554);

        private final int projectileId;

        ChaosElementalAttackType(final int projectileId) {
            this.projectileId = projectileId;
        }
    }
}
