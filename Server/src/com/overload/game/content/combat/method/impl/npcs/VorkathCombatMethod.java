package com.overload.game.content.combat.method.impl.npcs;

import com.overload.game.World;
import com.overload.game.collision.Region;
import com.overload.game.collision.RegionManager;
import com.overload.game.content.Boundary;
import com.overload.game.content.PrayerHandler;
import com.overload.game.content.combat.CombatEquipment;
import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.HitDamage;
import com.overload.game.content.combat.hit.HitMask;
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
import com.overload.game.task.impl.CombatPoisonEffect;
import com.overload.game.task.impl.TimedObjectSpawnTask;
import com.overload.net.packet.impl.commands.all.Pos;
import com.overload.util.Misc;
import com.overload.util.NpcIdentifiers;

import java.util.*;

public class VorkathCombatMethod implements CombatMethod {

    private static final Graphic EXPLOSION = new Graphic(157);

    private enum Attack {
        ICEDRAGONFIRE, SPECIAL_DRAGON_FIRE, DRAGONFIRE, VENOM, PINK, FIREBALL, SPECIAL, SPIKE_BALL, POISON, SLEEPING
        ;
    	public static Attack getRandomNoSleep() {
    		return Attack.values()[Misc.random(Attack.values().length-1)];
    	}
   }

    private static final Position vorkathCenter = new Position(2272, 4065);

    public static final Boundary NO_POOL_AREA = new Boundary(2268, 4062, 2276, 4068);
    private Attack attack = Attack.SLEEPING;
    private List<Position> possibleLocations = new ArrayList<>();

    public List<Position> getPossibleLocations() {
        return possibleLocations;
    }

    private void generateLocations(Player player) {
        for (int x = 2261; x <= 2283; x++) {
            for (int y = 4054; y <= 4076; y++) {
                possibleLocations.add(new Position(x, y, player.getZ()));
            }
        }
    }

    private List<GameObject> poisonPools = new LinkedList<>();

    public List<GameObject> getPoisonPools() {
        return poisonPools;
    }


    private void spawnPools(Player player, NPC npc) {
        if (possibleLocations.size() <= 5)
            generateLocations(player);
        final Set<Position> spawns = new HashSet<>();
        while (spawns.size() < 50) {
            final Position location = Misc.randomElement(getPossibleLocations());
            if (spawns.contains(location) || location.equals(player.getPosition()) || Boundary.isInBounds(location, NO_POOL_AREA)) {
                continue;
            }
            spawns.add(location);
        }
        npc.performAnimation(7957);
        spawns.forEach(s -> {
            int clientSpeed = 130;
            int gfxDelay = 140;
            final GameObject object = new GameObject(32000, s, 10, Misc.random(3), Optional.of(player));
            object.setPosition(s);
            player.executeProjectile(new Projectile(vorkathCenter, s, 1483, gfxDelay, clientSpeed, 35, 35, 0));

            final int hitDelay = (gfxDelay / 20) - 1;

            TaskManager.submit(new Task(hitDelay, false) {
                @Override
                protected void execute() {
                    ObjectManager.register(object, true);
                    getPoisonPools().add(object);
                    TaskManager.submit(new Task(27, false) {
                        @Override
                        protected void execute() {
                            getPoisonPools().forEach(o -> ObjectManager.deregister(o, true));
                            getPoisonPools().clear();
                            poison = false;
                            super.stop();
                        }

                    });
                    super.stop();
                }
            });
        });
    }

    public static boolean isInToxicLocation(Player player) {
        return ObjectManager.exists(32000, player.getPosition());
    }

    public static boolean checkZombie(Player player) {
        return player.getLocalNpcs().stream().anyMatch(npc -> {
            if (npc.getId() == 8063)
                return true;
            return false;
        });
    }

    @Override
    public boolean canAttack(Character character, Character target) {
        return true;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public void startAnimation(Character character) {
    	
    }

    private boolean poison = false;
    
    public void awake(Player player, NPC vorkath) {
        player.performAnimation(2286);
        vorkath.performAnimation(new Animation(7950, 8));
        vorkath.setNpcTransformationId(NpcIdentifiers.VORKATH_AWAKE);
    	TaskManager.submit(new Task(8, player, false) {
			@Override
			protected void execute() {
				attack = Attack.DRAGONFIRE;
				vorkath.getCombat().setTarget(player);
				super.stop();
			}
		});
    }
    
    @Override
    public void prepareAttack(Character character, Character target) {
    	
    	if (attack == Attack.SLEEPING)
        	return;
    	
        final Position selectedLocation = target.getPosition();

        if (poison) {
        	handleRapidFire(character, target);
        	return;
        }
        
        if(Misc.random(100) <= 100 && !poison) {
            attack = Attack.POISON;
            spawnPools(target.getAsPlayer(), character.getAsNpc());
            poison = true;
        } else if (Misc.getRandom(25) < 5 && !checkZombie(target.getAsPlayer())) {
            attack = Attack.ICEDRAGONFIRE;
            character.performAnimation(new Animation(7952));
            character.executeProjectile(new Projectile(character, target, 1479, 45, 65, 35, 35, 0));
            target.performGraphic(new Graphic(369, 2));
            Position p = target.getPosition().clone();
            int xAdd = Misc.random(8);
            p.add(xAdd, (8 - xAdd), 0);
            TaskManager.submit(new Task(2, target, false) {
                @Override
                protected void execute() {
                    spawnnpc(8063, p, 38);
                    stop();
                }
            });
        } else if (Misc.random(10) < 2) {
            attack = Attack.PINK;
            character.performAnimation(new Animation(7952));
            character.executeProjectile(new Projectile(character, target, 1471, 45, 65, 35, 35, 0));
            target.performGraphic(new Graphic(1473, GraphicHeight.HIGH));
        } else if (Misc.random(12) < 2) {
            attack = Attack.SPIKE_BALL;
            character.performAnimation(new Animation(7952));
            character.executeProjectile(new Projectile(character, target, 1477, 45, 65, 35, 35, 0));
            target.performGraphic(new Graphic(1478, GraphicHeight.HIGH));
        } else if (Misc.random(20) < 2) {
            attack = Attack.FIREBALL;
	        character.performAnimation(new Animation(7952));
	        character.executeProjectile(new Projectile(character, target, 1481, 45, 65, 35,  35, 0));
	        List<Position> attackPositions = new ArrayList<>();
	        TaskManager.submit(new Task(3, false) {
	            @Override
	            protected void execute() {
	                target.performGraphic(EXPLOSION);
	                for (int i = 0; i < 1; i++) {
	                    attackPositions.add(new Position((target.getX() - 1) + Misc.getRandom(3), (target.getY() - 1) + Misc.getRandom(3)
	                            )
	                    );
	                }
	                stop();
	            }
	            @Override
	            public void stop(){
	                this.setEventRunning(false);
	                TaskManager.submit(new Task(3, target, false) {
	                    int splashTick = 0;
	                    @Override
	                    protected void execute() {
	                        if (splashTick == 0) {
	                            for (Position pos : attackPositions) {
	                                character.executeProjectile(new Projectile(target.getPosition(), pos, 0, 1481, 75, 25, 35, 35, 0));
	                            }
	                            if(splashTick == 1) {
	                                for(Position pos : attackPositions) {
	                                    for (Player player : character.getAsNpc().getNearbyPlayers(10)) {
	                                        if(player.equals(pos)) {
	                                            player.getCombat().getHitQueue().addPendingDamage(new HitDamage(63, HitMask.RED));
	                                        }
	                                    }
	                                }
	                                splashTick++;
	                                finished(character, target);
	                                this.setEventRunning(false);
	                                stop();
	                            }
	                            splashTick++;
	                        }
	                    }
	                });
	            }
	        });
        }
    }

    private int hits = 0;
    
    private void handleRapidFire(Character character, Character target) {
    	int clientSpeed = 70;
        int gfxDelay = 80;
        final int hitDelay = (gfxDelay / 20) - 1;
        
        NPC vorkath = character.getAsNpc();
        Player player = target.getAsPlayer();

        player.executeProjectile(new Projectile(vorkathCenter, player.getPosition(), 1482, gfxDelay, clientSpeed, 35, 35, 0));
        vorkath.getDefinition().setAttackSpeed(1);
        TaskManager.submit(new Task(hitDelay, false) {
			@Override
			protected void execute() {
				final Position selectedLocation = player.getPosition();
                target.getAsPlayer().getPacketSender().sendGlobalGraphic(new Graphic(1466), selectedLocation);
                super.stop();
			};
        });
        if (attack != Attack.SPECIAL_DRAGON_FIRE)
        	attack = Attack.SPECIAL_DRAGON_FIRE;
        if (++hits >= 25) {
            hits = 0;
            vorkath.performAnimation(new Animation(-1));
            attack = Attack.getRandomNoSleep();
            vorkath.getDefinition().setAttackSpeed(4);
        }
	}

	@Override
    public int getAttackSpeed(Character character) {
        return character.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Character character) {
        return 15;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MAGIC;
    }

    @Override
    public PendingHit[] getHits(Character character, Character target) {
    	if (attack == Attack.SLEEPING)
        	return null;
    	
        PendingHit hit = new PendingHit(character, target, this, true, 1);
        if (target.isPlayer()) {
            Player p = target.getAsPlayer();
            
            if (attack == Attack.SPECIAL_DRAGON_FIRE && p.getMovementQueue().isMoving()) {
            	return null;
            }
            
            if (attack == Attack.DRAGONFIRE && attack == Attack.SPECIAL_DRAGON_FIRE) {
                if (PrayerHandler.isActivated(p, PrayerHandler.PROTECT_FROM_MAGIC) && CombatEquipment.hasDragonProtectionGear(p) && !p.getAsPlayer().getCombat().getFireImmunityTimer().finished()) {
                    target.getAsPlayer().getPacketSender().sendMessage("You're protected against the dragonfire breath.");
                    return new PendingHit[]{hit};
                }
                int extendedHit = 25;
                if (PrayerHandler.isActivated(p, PrayerHandler.PROTECT_FROM_MAGIC)) {
                    extendedHit -= 5;
                }
                if (!p.getAsPlayer().getCombat().getFireImmunityTimer().finished()) {
                    extendedHit -= 10;
                }
                if (CombatEquipment.hasDragonProtectionGear(p)) {
                    extendedHit -= 10;
                }
                p.getAsPlayer().getPacketSender().sendMessage("Vorkath burns you.");
                hit.getHits()[0].incrementDamage(extendedHit);
            }
        }
        return new PendingHit[]{hit};
    }

    @Override
    public void finished(Character character, Character target) {

    }

    @Override
    public void handleAfterHitEffects(PendingHit hit) {
        Player player = hit.getTarget().getAsPlayer();
        if(attack == attack.ICEDRAGONFIRE) {
            CombatFactory.freeze(hit.getTarget().getAsPlayer(), 5);
        }
        if(attack == attack.PINK) {
            player.getSkillManager().decreaseCurrentLevel(Skill.PRAYER, (int) (hit.getTotalDamage() * 0.35), 0);
            player.getPacketSender().sendMessage("Vorkath drained your prayer!");
        }
    }
    public static NPC spawnnpc(int id, Position pos, int constitution) {
        NPC np = new NPC(id, pos);
        np.setHitpoints(constitution);
        World.getAddNPCQueue().add(np);
        return np;
    }

}