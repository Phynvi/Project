package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.World;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.Misc;
import com.overload.util.timers.TimerKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CerberusCombatMethod implements CombatMethod {

    private  enum Attack {
        MELEE, RANGE, MAGIC, LAVA, GHOSTS
    }
    private Attack attack = Attack.RANGE;

    protected Random random = new Random();

    private List<NPC> ghosts = new ArrayList<>();
    private static final Animation HOWL_ANIMATION = new Animation(4485 , Priority.MEDIUM);
    private static final Animation RANGED_ANIMATION = new Animation(4490 , Priority.MEDIUM);
    private static final Graphic RANGED_GRAPHIC = new Graphic(1244, GraphicHeight.HIGH);
    private static final Animation MAGIC_ANIMATION = new Animation(4489 , Priority.MEDIUM);
    private static final Graphic MAGIC_GRAPHIC = new Graphic(1243, GraphicHeight.HIGH);
    private static final Graphic LAVA_GRAPHIC = new Graphic(1246);
    private static final Graphic LAVA_GRAPHIC_END = new Graphic(1247);
    private static List<Position> attackPositions = new ArrayList<>();
    boolean ghosts_spawned = false;

	private static final String GHOST_SPAWN_TEXT = "Aaarrrooooooo";
    private static final String LAVA_SPAWN_TEXT = "Grrrrrrrrrrrrrr";

    public static boolean isInLavaLocation(Player player) {
        for (Position p : attackPositions) {
            if (player.getX() == p.getX() && player.getY() == p.getY())
                return true;
        }
        return false;
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
        if (!character.isNpc() || !target.isPlayer())
            return;

        attack = Attack.RANGE;

        if (target.getPosition().getDistance(character.getPosition()) < 2 && Misc.getRandom(1) == 0) {
            attack = Attack.MELEE;
        }

        if (Misc.getRandom(10) < 3 && character.getAsNpc().getHitpoints() <= 200) {
            attack = Attack.LAVA;
        }

        if (Misc.getRandom(10) < 3 && character.getAsNpc().getHitpoints() <= 400 && character.getAsNpc().getHitpoints() > 200) {
            attack = Attack.GHOSTS;
        }

        if (attack == Attack.MELEE) {
            character.performAnimation(new Animation(4491));
        }

        if (attack == Attack.GHOSTS) {
            if (!ghosts_spawned) {
                character.performAnimation(HOWL_ANIMATION);
                character.forceChat(GHOST_SPAWN_TEXT);
                TaskManager.submit(new Task(1, character, false) {
                    int Stage = 0;

                    @Override
                    public void execute() {
                        if (Stage == 0) {
                            ghosts_spawned = true;
                            NPC ghost1 = new NPC(5867, new Position(1239, 1256, target.getPosition().getZ()));
                            NPC ghost2 = new NPC(5868, new Position(1240, 1256, target.getPosition().getZ()));
                            NPC ghost3 = new NPC(5869, new Position(1241, 1256, target.getPosition().getZ()));

                            ghost1.getMovementQueue().setBlockMovement(true);
                            ghost2.getMovementQueue().setBlockMovement(true);
                            ghost3.getMovementQueue().setBlockMovement(true);

                            ghosts.add(ghost1);
                            ghosts.add(ghost2);
                            ghosts.add(ghost3);
                        }
                        if (Stage == 1) {
                            for (NPC n : ghosts) {

                                World.getNpcs().add(n);
                            }
                        }
                        if (Stage >= 15) {
                            for (NPC n : ghosts) {
                                World.getNpcs().remove(n);
                            }
                            ghosts_spawned = false;
                            ghosts.clear();
                            stop();
                        }
                        Stage++;
                    }
                });
            }
        }
        if (attack == Attack.MAGIC) {
            character.performAnimation(MAGIC_ANIMATION);
            character.executeProjectile(new Projectile(character, target, 1242, 45, 70, 43, 35, 0));
            TaskManager.submit(new Task(3, target, false) {
                @Override
                protected void execute() {
                    target.performGraphic(MAGIC_GRAPHIC);
                    stop();
                }
            });
        }
        if (attack == Attack.RANGE) {
            character.performAnimation(RANGED_ANIMATION);
            character.executeProjectile(new Projectile(character, target, 1245, 45, 70, 43, 35, 0));
            TaskManager.submit(new Task(3, target, false) {
                @Override
                public void execute() {
                    target.performGraphic(RANGED_GRAPHIC);
                    stop();
                }
            });
        } else if (attack == Attack.LAVA) {
            character.performAnimation(HOWL_ANIMATION);
            character.forceChat(LAVA_SPAWN_TEXT);
            Position targetPos = target.getPosition();
            attackPositions.add(targetPos);

            for (int i = 0; i < 10; i++) {
                attackPositions.add(new Position((targetPos.getX() - 1) + Misc.getRandom(10), (targetPos.getY() - 1) + Misc.getRandom(10)));
                TaskManager.submit(new Task(1, target, true) {
                    @Override
                    public void execute() {
                        for (Position pos : attackPositions) {
                            target.getAsPlayer().getPacketSender().sendGlobalGraphic(LAVA_GRAPHIC, pos);
                            Task t = new Task(1, target, true) {
                                int tick = 0;
                                @Override
                                public void execute() {
                                    if (tick == 0 || tick == 8) {
                                        target.getAsPlayer().getPacketSender().sendGlobalGraphic(LAVA_GRAPHIC, pos);
                                    }
                                    if (tick >= 16) {
                                        target.getAsPlayer().getPacketSender().sendGlobalGraphic(LAVA_GRAPHIC_END, pos);
                                        attackPositions.clear();
                                        stop();
                                    }
                                    tick++;
                                }
                            };
                            TaskManager.submit(t);
                            //target.getAsPlayer().getPacketSender().sendGraphic(LAVA_GRAPHIC, pos);

//                            for (Player player : character.getAsNpc().getNearbyPlayers(3)) {
//                                if (player.getPosition().equals(pos)) {
//                                    player.getCombat().getHitQueue().addPendingDamage(new HitDamage(Misc.getRandom(5), HitMask.RED));
//                                }
//                            }
                        }
                        finished(character, target);
                        stop();
                    }
                });
                character.getTimers().register(TimerKey.COMBAT_ATTACK, 5);
            }
        }
    }


    @Override
    public int getAttackSpeed(Character character) {
        return 6;
    }

    @Override
    public int getAttackDistance(Character character) {
        return 6;
    }

    @Override
    public CombatType getCombatType() {
        if (attack == Attack.MELEE) {
            return CombatType.MELEE;
        }
        if (attack == Attack.MAGIC) {
            return CombatType.MAGIC;
        }
        return CombatType.RANGED;
    }


    @Override
    public PendingHit[] getHits(Character character, Character target) {
        if (attack == Attack.LAVA || attack == Attack.GHOSTS) {
            return null;
        }
        int delay = 2;
        if (attack == Attack.RANGE || attack == Attack.MELEE || attack == Attack.MAGIC) {
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
