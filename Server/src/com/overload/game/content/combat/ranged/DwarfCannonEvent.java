package com.overload.game.content.combat.ranged;

import com.overload.game.content.combat.hit.HitDamage;
import com.overload.game.content.combat.hit.HitMask;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.npc.NPCMovementCoordinator;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.object.ObjectManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.Misc;

public class DwarfCannonEvent {

    private static final int
            /**Start of cannon object ids**/
            CANNON_BASE = 7, CANNON_STAND = 8,
            CANNON_BARRELS = 9, CANNON = 6,
             /**Start of cannon item ids**/
            CANNONBALL = 2, CANNON_BASE_ID = 6,
            CANNON_STAND_ID = 8, CANNON_BARRELS_ID = 10,
            CANNON_FURNACE_ID = 12;

    public static void setupCannon(final Player c) {
        if(!canSetupCannon(c))
            return;
        c.getMovementQueue().reset();
        c.setSettingUpCannon(true);
        c.getSkillManager().stopSkillable();
        c.getMovementQueue().setBlockMovement(true);
        final GameObject object = new GameObject(CANNON_BASE, c.getPosition().clone());
        final GameObject object2 = new GameObject(CANNON_STAND, c.getPosition().clone());
        final GameObject object3 = new GameObject(CANNON_BARRELS, c.getPosition().clone());
        final GameObject object4 = new GameObject(CANNON, c.getPosition().clone());
        c.getInventory().delete(CANNON_FURNACE_ID, 1);
        c.getInventory().delete(CANNON_BARRELS_ID, 1);
        c.getInventory().delete(CANNON_STAND_ID, 1);
        c.getInventory().delete(CANNON_BASE_ID, 1);
        TaskManager.submit(new Task(2, c, true) {
            int setupTicks = 5;
            @Override
            public void execute() {
                switch(setupTicks) {

                    case 5:
                        c.performAnimation(new Animation(827));
                        ObjectManager.register(object, true);
                        break;

                    case 4:
                        c.performAnimation(new Animation(827));
                        ObjectManager.deregister(object, true);
                        ObjectManager.register(object2, true);
                        break;

                    case 3:
                        c.performAnimation(new Animation(827));
                        ObjectManager.deregister(object2, true);
                        ObjectManager.register(object3, true);
                        break;

                    case 2:
                        c.performAnimation(new Animation(827));
                        ObjectManager.deregister(object3, true);
                        DwarfCannon cannon = new DwarfCannon(c.getIndex(), object4);
                        c.setCannon(cannon);
                        ObjectManager.register(c.getCannon().getObject(), true);
                        break;

                    case 1:
                        setupTicks = 5;
                        //Achievements.finishAchievement(c, AchievementData.SET_UP_A_CANNON);
                        c.setSettingUpCannon(false);
                        stop();
                        break;
                }
                setupTicks--;
            }

            @Override
            public void stop() {
                c.getMovementQueue().setBlockMovement(false);
                setEventRunning(false);
            }
        });
    }

    public static boolean canSetupCannon(Player c) {
        if(c.getSkillManager().getCurrentLevel(Skill.RANGED) < 75) {
            c.getPacketSender().sendMessage("You need a Ranged level of atleast 75 to setup the dwarf-cannon.");
            return false;
        }
        if (!c.getInventory().contains(CANNON_BASE_ID)
                || !c.getInventory().contains(CANNON_STAND_ID)
                || !c.getInventory().contains(CANNON_BARRELS_ID)
                || !c.getInventory().contains(CANNON_FURNACE_ID)) {
            c.getPacketSender().sendMessage("You don't have the required items to setup the dwarf-cannon.");
            return false;
        }
        if(c.getCannon() != null) {
            c.getPacketSender().sendMessage("You can only have one dwarf-cannon setup at once.");
            return false;
        }
        if(!c.getMovementQueue().canWalk(3, 3) || ObjectManager.exists(c.getPosition().clone()) || c.getPosition().getZ() != 0) {
            c.getPacketSender().sendMessage("The dwarf-cannon cannot be setup here. Try moving around a bit.");
            return false;
        }
        if(c.isSettingUpCannon() || c.getHitpoints() <= 0 || c.getHitpoints() <= 0)
            return false;
        return true;
    }

    public static void pickupCannon(Player c, DwarfCannon cannon, boolean force) {
        if(c.isSettingUpCannon())
            return;
        boolean deleted = false;
        boolean addCannonballs = cannon.getCannonballs() > 0;
        if(force) { //Logout
            if(c.getInventory().getFreeSlots() >= 5) {
                c.getInventory().add(CANNON_BASE_ID, 1);
                c.getInventory().add(CANNON_STAND_ID, 1);
                c.getInventory().add(CANNON_BARRELS_ID, 1);
                c.getInventory().add(CANNON_FURNACE_ID, 1);
                if(addCannonballs)
                    c.getInventory().add(CANNONBALL, cannon.getCannonballs());
                deleted = true;
            } else {
                c.getBank(c.getCurrentBankTab()).add(CANNON_BASE_ID, 1);
                c.getBank(c.getCurrentBankTab()).add(CANNON_STAND_ID, 1);
                c.getBank(c.getCurrentBankTab()).add(CANNON_BARRELS_ID, 1);
                c.getBank(c.getCurrentBankTab()).add(CANNON_FURNACE_ID, 1);
                if(addCannonballs)
                    c.getBank(c.getCurrentBankTab()).add(CANNONBALL, cannon.getCannonballs());
                deleted = true;
            }
        } else {
            if(c.getInventory().getFreeSlots() >= 5) {
                c.getInventory().add(CANNON_BASE_ID, 1);
                c.getInventory().add(CANNON_STAND_ID, 1);
                c.getInventory().add(CANNON_BARRELS_ID, 1);
                c.getInventory().add(CANNON_FURNACE_ID, 1);
                if(addCannonballs)
                    c.getInventory().add(CANNONBALL, cannon.getCannonballs());
                deleted = true;
            } else {
                c.getPacketSender().sendMessage("You don't have enough free inventory space.");
                deleted = false;
            }
        }
        if(deleted) {
            cannon.setCannonballs(0);
            cannon.setCannonFiring(false);
            cannon.setRotations(0);
            ObjectManager.deregister(c.getCannon().getObject(), true);
            c.setCannon(null).setSettingUpCannon(false);
        }
    }

    public static void startFiringCannon(Player c, DwarfCannon cannon) {
        if(cannon.cannonFiring() && cannon.getCannonballs() > 15) {
            c.getPacketSender().sendMessage("Your cannon is already firing.");
            return;
        }
        if(cannon.getCannonballs() <= 15) {
            int playerCannonballs = c.getInventory().getAmount(2) > 30 ? 30: c.getInventory().getAmount(2);
            int cannonballsToAdd = playerCannonballs - cannon.getCannonballs();
            if(playerCannonballs < 1) {
                c.getPacketSender().sendMessage("You do not have any cannonballs in your inventory to fire the cannon with.");
                return;
            }
            c.getInventory().delete(CANNONBALL, cannonballsToAdd);
            cannon.setCannonballs(cannonballsToAdd);
            if(!cannon.cannonFiring())
                fireCannon(c, cannon);
        }
    }

    public static void fireCannon(final Player c, final DwarfCannon cannon) {
        if(cannon.cannonFiring())
            return;
        TaskManager.submit(new Task(1, c, true) {
            @Override
            public void execute() {
                if(c.getCannon() == null) {
                    this.stop();
                    return;
                }
                if(cannon.getCannonballs() > 0) {
                    rotateCannon(c, cannon);
                    cannon.setCannonFiring(true);
                    attack(c);
                } else {
                    c.getPacketSender().sendMessage("Your cannon has run out of cannonballs.");
                    cannon.setCannonballs(0);
                    cannon.setCannonFiring(false);
                    cannon.setRotations(0);
                    this.stop();
                }
            }

        });
    }

    private static void rotateCannon(final Player c, DwarfCannon cannon) {
        final GameObject object = c.getCannon().getObject();
        cannon.addRotation(1);
        switch(cannon.getRotations()) {
            case 1: // north
                object.performAnimation(new Animation(516));
                break;
            case 2: // north-east
                object.performAnimation(new Animation(517));
                break;
            case 3: // east
                object.performAnimation(new Animation(518));
                break;
            case 4: // south-east
                object.performAnimation(new Animation(519));
                break;
            case 5: // south
                object.performAnimation(new Animation(520));
                break;
            case 6: // south-west
                object.performAnimation(new Animation(521));
                break;
            case 7: // west
                object.performAnimation(new Animation(514));
                break;
            case 8: // north-west
                object.performAnimation(new Animation(515));
                cannon.setRotations(0);
                break;
        }
     //   c.getPacketSender().sendSound(c, Sound.ROTATING_CANNON);
    }

    public static void attack(Player player) {
        DwarfCannon cannon = player.getCannon();
        if(cannon == null)
            return;
        NPC n = getTarget(player, cannon);
        if(n == null)
            return;
        HitDamage dmg = new HitDamage(Misc.getRandom(25), HitMask.RED);
        new Projectile(cannon.getObject(), n, 53, 30, 75, 43, 31, 0).sendProjectile();
        n.decrementHealth(dmg);
        n.getLastCombat().reset();
        n.performAnimation(new Animation(n.getDefinition().getDefenceAnim()));
        //n.getCombat().addDamage(player, dmg.getDamage());
        n.getCombat().getHitQueue().addPendingDamage(dmg);
        player.getSkillManager().addExperience(Skill.RANGED, (int)(dmg.getDamage() * .5));
        player.getSkillManager().addExperience(Skill.HITPOINTS, (int)(dmg.getDamage() * .5));

        if(n.getMovementCoordinator().getCoordinateState() == NPCMovementCoordinator.CoordinateState.HOME)
            n.getCombat().attack(player);

        cannon.setCannonballs(cannon.getCannonballs() - 1);
      //  Sounds.sendGlobalSound(player, Sound.FIRING_CANNON);
    }

    private static NPC getTarget(Player p, DwarfCannon cannon) {
        for(NPC n : p.getLocalNpcs()) {
            if(n == null)
                continue;
            int cannonX = cannon.getObject().getPosition().getX();
            int cannonY = cannon.getObject().getPosition().getY();
            int npcX = n.getPosition().getX();
            int npcY = n.getPosition().getY();
            if (n.getDefinition().isAttackable() && n.getHitpoints() > 0 && Position.goodDistance(cannon.getObject().getPosition(), n.getPosition(), 8)) {
//                if(!AreaManager.inMulti(n)) {
//                    continue;
//                }
                switch (cannon.getRotations()) {
                    case 7: // north
                        if (npcY > cannonY && npcX >= cannonX - 1
                                && npcX <= cannonX + 1)
                            return n;
                        break;
                    case 8: // north-east
                        if (npcX >= cannonX + 1 && npcY >= cannonY + 1)
                            return n;
                        break;
                    case 1: // east
                        if (npcX > cannonX && npcY >= cannonY - 1
                                && npcY <= cannonY + 1)
                            return n;
                        break;
                    case 2: // south-east
                        if (npcY <= cannonY - 1 && npcX >= cannonX + 1)
                            return n;
                        break;
                    case 3: // south
                        if (npcY < cannonY && npcX >= cannonX - 1
                                && npcX <= cannonX + 1)
                            return n;
                        break;
                    case 4: // south-west
                        if (npcX <= cannonX - 1 && npcY <= cannonY - 1)
                            return n;
                        break;
                    case 5: // west
                        if (npcX < cannonX && npcY >= cannonY - 1
                                && npcY <= cannonY + 1)
                            return n;
                        break;
                    case 6: // north-west
                        if (npcX <= cannonX - 1 && npcY >= cannonY + 1)
                            return n;
                        break;
                }
            }
        }
        return null;
    }
}
