package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.World;
import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.object.ObjectManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.game.task.impl.TimedObjectSpawnTask;
import com.overload.game.task.impl.CombatPoisonEffect.PoisonType;
import com.overload.util.Misc;
import com.overload.util.timers.TimerKey;

import java.util.*;

public class ZulrahCombatMethod implements CombatMethod {

    private enum Attack {
        MELEE, MAGIC, RANGE, JAD
    }
    private enum Stage {
        POISON, COMBAT, SNAKELINGS, TRANSFORM
    }
    private long lastHazard = 0L;

    private Attack attack = Attack.RANGE;
    private Stage stage = Stage.COMBAT;

    public List<Position> positions = new ArrayList<Position>();
    public Map<Attack, Integer> attackNpcId = new HashMap<Attack, Integer>();
    public List<Position> HAZARD_POSITIONS = new ArrayList<Position>();

    private Position meleePosition = null;
    
    private int tickCounter = 0;

    private int currentStage = 0;
    private int MAX_STAGE = 11;

    public ZulrahCombatMethod() {
        attackNpcId.put(Attack.RANGE, 2042);
        attackNpcId.put(Attack.MELEE, 2043);
        attackNpcId.put(Attack.MAGIC, 2044);

        lastHazard = System.currentTimeMillis();
    }

    @Override
    public boolean canMove() {
        return true;
    }

    public void setup(Player player) {
        int index = player.getIndex() * 4;

        if (positions.isEmpty()) {
	        positions.add(new Position(2268, 3073, index));//Center
	        positions.add(new Position(2258, 3071, index));//Left
	        positions.add(new Position(2277, 3074, index));//Right
	        positions.add(new Position(2268, 3065, index));//Bottom
        }

        if (HAZARD_POSITIONS.isEmpty()) {
	        HAZARD_POSITIONS.add(new Position(2263, 3076, index));// < top left
	        HAZARD_POSITIONS.add(new Position(2263, 3074, index));// < middle
	        HAZARD_POSITIONS.add(new Position(2263, 3072, index));// < bottom left
	        HAZARD_POSITIONS.add(new Position(2264, 3070, index));// v left
	        HAZARD_POSITIONS.add(new Position(2266, 3069, index));// v left
	        HAZARD_POSITIONS.add(new Position(2268, 3069, index));// v middle
	        HAZARD_POSITIONS.add(new Position(2270, 3069, index));// v right
	        HAZARD_POSITIONS.add(new Position(2272, 3070, index));// v right
	        HAZARD_POSITIONS.add(new Position(2273, 3072, index));// > bottom
	        HAZARD_POSITIONS.add(new Position(2273, 3074, index));// > middle
	        HAZARD_POSITIONS.add(new Position(2273, 3076, index));// > top
        }

    	System.out.println("POSITIONS");
        for (Position pos : positions) {
        	System.out.println(pos.toString());
        }
    	System.out.println("");
    	System.out.println("HAZARD_POSITIONS");
        for (Position pos : HAZARD_POSITIONS) {
        	System.out.println(pos.toString());
        }
    }

    public static boolean isInToxicLocation(Player player) {
        for (int x = player.getX() - 1; x < player.getX() + 1; x++) {
            for (int y = player.getY() - 1; y < player.getY() + 1; y++) {
                if (ObjectManager.exists(11700, new Position(x, y, player.getZ()))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canAttack(Character character, Character target) {
        return true;
    }

    @Override
    public void startAnimation(Character character) { }

    @Override
    public void prepareAttack(Character character, Character target) {

    	if (HAZARD_POSITIONS.isEmpty() || positions.isEmpty()) {
			this.setup(character.isPlayer() ? character.getAsPlayer() : target.getAsPlayer());
    	}
    	
        if (stage == Stage.POISON || stage == Stage.SNAKELINGS)
            return;

        Player player = target.getAsPlayer();
        NPC npc = character.getAsNpc();
        npc.getMovementQueue().reset();

        if (attack == Attack.MELEE)
        	tickCounter += 3;
        else
        	tickCounter++;

        if ((System.currentTimeMillis() - lastHazard) > 25000) {
            if (Misc.random(2) == 1)
                stage = Stage.SNAKELINGS;
            else
                stage = Stage.POISON;
        } else {
            if (tickCounter >= 5) {
                tickCounter = 0;
                currentStage++;
                if (currentStage > MAX_STAGE)
                    currentStage = 0;
                if (currentStage == 1) {
                    stage = Stage.POISON;
                    tickCounter = Misc.random(5);
                } else {
                    switch (currentStage) {
                        case 3:
                        case 5:
                        case 8:
                            stage = Stage.TRANSFORM;
                            attack = Attack.MAGIC;
                            break;
                        case 2:
                        case 6:
                        case 10:
                            stage = Stage.TRANSFORM;
                            attack = Attack.MELEE;
                            break;
                        case 4:
                        case 11:
                            stage = Stage.TRANSFORM;
                            attack = Attack.RANGE;
                            break;
                        case 9:
                            stage = Stage.TRANSFORM;
                            attack = Attack.JAD;
                        	break;

                    }
                }
            }
        }

        handleZulrahTransform(npc, player);
    }

    public void handleZulrahTransform(NPC npc, Player player) {
        List<Position> pos = new ArrayList<Position>();
        if (stage == Stage.SNAKELINGS || stage == Stage.POISON) {
            int ps = 0;
            while (ps < 4) {
                Position p = Misc.randomElement(this.HAZARD_POSITIONS);
                if (pos.contains(p))
                    continue;
                ps++;
                pos.add(p);
            }
        }
        switch (stage) {
            case TRANSFORM:
                npc.getTimers().register(TimerKey.ATTACK_IMMUNITY, 5);
                player.getCombat().reset();
                npc.getCombat().reset();
                npc.performAnimation(new Animation(5072));
                stage = Stage.COMBAT;
                TaskManager.submit(new Task(1, player, false) {
                    int tick = 0;
                    @Override
                    protected void execute() {
                        if (tick == 2) {
                            npc.setNpcTransformationId(6709);
                        } else if (tick == 3) {
                        	Position p = null;
                        	if (attack == Attack.MELEE)
                        		p = positions.get(0);
                        	else
                        		p = Misc.randomElement(positions);
                            npc.moveTo(p);
                        } else if (tick >= 5) {
                            npc.setNpcTransformationId(attackNpcId.get(attack));
                            npc.performAnimation(new Animation(5071));
                            npc.setPositionToFace(player.getPosition());
                            stop();
                        }
                        tick++;
                    }

                    @Override
                    public void stop() {
                        npc.getCombat().attack(player);
                        npc.getCombat().setTarget(player);
                        super.stop();
                    }
                });
                break;
            case POISON:
                npc.getCombat().reset();
                npc.getDefinition().setAggressive(false);
                player.setUntargetable(true);
                TaskManager.submit(new Task(1, player, false) {
                    int tick = 0;
                    int loops = 0;
                    Position p = null;
                    @Override
                    protected void execute() {

                        if (p == null) {
                            p = pos.get(Misc.random(pos.size()-1));
                            pos.remove(p);
                        }

                        npc.setPositionToFace(p);
                        if (tick == 1) {
                            npc.performAnimation(new Animation(5069));
                            npc.getUpdateFlag().flag(Flag.ANIMATION);
                            player.executeProjectile(new Projectile(npc, p, 1044, 40, 50, 80, 0, 10));
                            //new Projectile(npcPos, p, 1044, 40, 100, 85, 0, 0).sendProjectile();
                        }

                        if (tick == 4) {
                            TaskManager.submit(new TimedObjectSpawnTask(new GameObject(11700, p), 40, Optional.empty()));
                            tick = -1;
                            loops++;
                            if (loops != 4) {
                                p = null;
                            }
                        }

                        tick++;

                        if (loops == 4)
                            stop();
                    }

                    @Override
                    public void stop() {
                        npc.getDefinition().setAggressive(true);
                        player.setUntargetable(false);
                        npc.getCombat().attack(player);
                        npc.getCombat().setTarget(player);
                        stage = Stage.COMBAT;
                        lastHazard = System.currentTimeMillis();
                        setEventRunning(false);
                    }
                });
                break;
            case SNAKELINGS:
                npc.getCombat().reset();
                npc.getDefinition().setAggressive(false);

                TaskManager.submit(new Task(1, player, false) {
                    int tick = 0;
                    int loops = 0;
                    Position p = null;
                    @Override
                    protected void execute() {

                        if (p == null) {
                            p = pos.get(Misc.random(pos.size()-1));
                            pos.remove(p);
                        }

                        npc.setPositionToFace(p);

                        if (tick == 1) {
                            npc.performAnimation(new Animation(5069));
                            npc.getUpdateFlag().flag(Flag.ANIMATION);
                            player.executeProjectile(new Projectile(npc.getPosition(), p, 1047, 40, 50, 80, 0, 10));
                            //new Projectile(npcPos, p, 1047, 40, 100, 85, 0, 0).sendProjectile();
                        }

                        if (tick == 4) {
                        	NPC n = new NPC(2045, p);
                            World.getAddNPCQueue().add(n);
                            tick = -1;
                            loops++;
                            if (loops != 4) {
                                p = null;
                            }
                        }

                        tick++;

                        if (loops == 4)
                            stop();
                    }

                    @Override
                    public void stop() {
                        stage = Stage.COMBAT;
                        npc.getDefinition().setAggressive(true);
                        npc.getCombat().attack(player);
                        npc.getCombat().setTarget(player);
                        lastHazard = System.currentTimeMillis();
                        setEventRunning(false);
                    }
                });
                break;
            case COMBAT:
                handleZulrahCombat(npc, player);
                break;
        }
    }

    public void handleZulrahCombat(NPC npc, Player player) {
        switch (attack) {
	        case MELEE:
	            if (Misc.random(2) == 1) {
	                npc.performAnimation(new Animation(5806));
	            } else {
	                npc.performAnimation(new Animation(5807));
	            }
                meleePosition = player.getPosition().clone();
	            break;
	        case JAD:
	            if (Misc.random(2) == 1) {
	                npc.performAnimation(new Animation(5069));
	                player.executeProjectile(new Projectile(npc, player, 1044, 40, 55, 85, 40, 10));
	            } else {
	                npc.performAnimation(new Animation(5069));
	                player.executeProjectile(new Projectile(npc, player, 1046, 40, 55, 85, 40, 10));
	            }
	            break;
            case RANGE:
                npc.performAnimation(new Animation(5069));
                player.executeProjectile(new Projectile(npc, player, 1044, 40, 55, 85, 40, 10));
                break;
            case MAGIC:
                npc.performAnimation(new Animation(5069));
                player.executeProjectile(new Projectile(npc, player, 1046, 40, 55, 85, 40, 10));
                break;
        }
    }

    @Override
    public int getAttackSpeed(Character character) {
        if (attack == Attack.MELEE) {
            return 14;
        } else if (attack == Attack.MAGIC) {
            return 5;
        } else {
            return 5;
        }
    }
 
    @Override
    public int getAttackDistance(Character character) {
        return 25;
    }

    @Override
    public CombatType getCombatType() {
        if (attack == Attack.MAGIC) {
            return CombatType.MAGIC;
        } else if (attack == Attack.MELEE) {
            return CombatType.MELEE;
        }
        return CombatType.RANGED;
    }

    @Override
    public PendingHit[] getHits(Character character, Character target) {
    	if (target != null && character != null) {
	        if (target.isPlayer()) {
	        	if (attack == Attack.RANGE) {
	                CombatFactory.poisonEntity(target.getAsPlayer(), PoisonType.VENOM);
	        	}
	        	if (attack == Attack.MELEE) {
	        		if (target.getAsPlayer().getPosition() != meleePosition) {
	        			return null;
	        		}
	        	}
	        }
	        return new PendingHit[] {
	            new PendingHit(
	                character,
	                target,
	                this,
	                true,
	                1
	            )
	        };
    	}
    	return null;
    }

    @Override
    public void finished(Character character, Character target) {
    	
    }

    @Override
    public void handleAfterHitEffects(PendingHit hit) {

    }
}
