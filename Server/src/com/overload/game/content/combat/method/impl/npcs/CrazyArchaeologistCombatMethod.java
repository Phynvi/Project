package com.overload.game.content.combat.method.impl.npcs;

import java.util.ArrayList;
import java.util.List;

import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.HitDamage;
import com.overload.game.content.combat.hit.HitMask;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Graphic;
import com.overload.game.model.GraphicHeight;
import com.overload.game.model.Position;
import com.overload.game.model.Projectile;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.Misc;
import com.overload.util.timers.TimerKey;

public class CrazyArchaeologistCombatMethod implements CombatMethod {

	private static final String[] QUOTES = { 
			"I'm Bellock - respect me!",
			"Get off my site!",
			"No-one messes with Bellock's dig!",
			"These ruins are mine!",
			"Taste my knowledge!",
			"You belong in a museum!",
	};

	private static enum Attack {
		SPECIAL_ATTACK, DEFAULT_RANGED_ATTACK, DEFAULT_MELEE_ATTACK;
	}

	private Attack attack = Attack.DEFAULT_RANGED_ATTACK;
	private static final Graphic RANGED_END_GFX = new Graphic(305, GraphicHeight.HIGH);
	private static final Graphic MAKE_IT_RAIN_START_GFX = new Graphic(157, GraphicHeight.MIDDLE);
	private static final Animation MELEE_ATTACK_ANIM = new Animation(423);
	private static final Animation RANGED_ATTACK_ANIM = new Animation(3353);

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
		if (attack == Attack.DEFAULT_MELEE_ATTACK) {
			delay = 0;
		}
		return new PendingHit[] { new PendingHit(character, target, this, true, delay) };
	}

	@Override
	public void prepareAttack(Character character, Character target) {
		if (!character.isNpc() || !target.isPlayer())
			return;

		attack = Attack.DEFAULT_RANGED_ATTACK;

		if (target.getPosition().getDistance(character.getPosition()) < 2 && Misc.getRandom(1) == 0) {
			attack = Attack.DEFAULT_MELEE_ATTACK;
		}

		if (Misc.getRandom(10) < 3) {
			attack = Attack.SPECIAL_ATTACK;
		}
		
		character.forceChat(QUOTES[Misc.getRandom(QUOTES.length - 1)]);

		if (attack == Attack.DEFAULT_RANGED_ATTACK) {
			character.performAnimation(RANGED_ATTACK_ANIM);
			character.executeProjectile(new Projectile(character, target, 1259, 40, 65, 31, 43, 0));
			TaskManager.submit(new Task(3, target, false) {
				@Override
				public void execute() {
					target.performGraphic(RANGED_END_GFX);
					stop();
				}
			});
		} else if (attack == Attack.SPECIAL_ATTACK) {
			character.performAnimation(RANGED_ATTACK_ANIM);
			character.forceChat("Rain of Knowledge!");
			Position targetPos = target.getPosition();
			List<Position> attackPositions = new ArrayList<>();
			attackPositions.add(targetPos);
			for (int i = 0; i < 2; i++) {
				attackPositions.add(new Position((targetPos.getX() - 1) + Misc.getRandom(3),
						(targetPos.getY() - 1) + Misc.getRandom(3)));
			}
			for (Position pos : attackPositions) {
				new Projectile(character.getPosition(), pos, 0, 1260, 40, 80, 31, 43, 0).sendProjectile();
			}
			TaskManager.submit(new Task(4) {
				@Override
				public void execute() {
					for (Position pos : attackPositions) {
						target.getAsPlayer().getPacketSender().sendGlobalGraphic(MAKE_IT_RAIN_START_GFX, pos);
						for (Player player : character.getAsNpc().getNearbyPlayers(10)) {
							if (player.getPosition().equals(pos)) {
								player.getCombat().getHitQueue()
										.addPendingDamage(new HitDamage(Misc.getRandom(25), HitMask.RED));
							}
						}
					}
					finished(character, target);
					stop();
				}
			});
			character.getTimers().register(TimerKey.COMBAT_ATTACK, 5);
		} else if (attack == Attack.DEFAULT_MELEE_ATTACK) {
			character.performAnimation(MELEE_ATTACK_ANIM);
		}
	}

	@Override
	public int getAttackSpeed(Character character) {
		if (attack == Attack.DEFAULT_MELEE_ATTACK) {
			return 3;
		}
		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		if (attack == Attack.DEFAULT_MELEE_ATTACK) {
			return 1;
		}
		if (attack == Attack.SPECIAL_ATTACK) {
			return 8;
		}
		return 6;
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
		if (attack == Attack.DEFAULT_MELEE_ATTACK) {
			return CombatType.MELEE;
		}
		return CombatType.RANGED;
	}
}
