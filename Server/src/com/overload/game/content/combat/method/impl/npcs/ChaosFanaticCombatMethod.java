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

public class ChaosFanaticCombatMethod implements CombatMethod {

	private static final String[] QUOTES = { 
			"Burn!",
			"WEUGH!",
			"Develish Oxen Roll!",
			"All your wilderness are belong to them!",
			"AhehHeheuhHhahueHuUEehEahAH",
			"I shall call him squidgy and he shall be my squidgy!",
	};

	private static enum Attack {
		SPECIAL_ATTACK, DEFAULT_MAGIC_ATTACK;
	}

	private Attack attack = Attack.DEFAULT_MAGIC_ATTACK;
	private static final Graphic ATTACK_END_GFX = new Graphic(305, GraphicHeight.HIGH);
	private static final Graphic EXPLOSION_END_GFX = new Graphic(157, GraphicHeight.MIDDLE);
	private static final Animation MAGIC_ATTACK_ANIM = new Animation(811);

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
		return new PendingHit[] { new PendingHit(character, target, this, true, 2) };
	}

	@Override
	public void prepareAttack(Character character, Character target) {
		if (!character.isNpc() || !target.isPlayer())
			return;

		attack = Attack.DEFAULT_MAGIC_ATTACK;

		if (Misc.getRandom(9) < 3) {
			attack = Attack.SPECIAL_ATTACK;
		}
		
		character.forceChat(QUOTES[Misc.getRandom(QUOTES.length - 1)]);

		if (attack == Attack.DEFAULT_MAGIC_ATTACK) {
			new Projectile(character, target, 554, 62, 80, 31, 43, 0).sendProjectile();
			if (Misc.getRandom(1) == 0) {
				TaskManager.submit(new Task(3, target, false) {
					@Override
					public void execute() {
						target.performGraphic(ATTACK_END_GFX);
						stop();
					}
				});
			}
		} else if (attack == Attack.SPECIAL_ATTACK) {
			Position targetPos = target.getPosition();
			List<Position> attackPositions = new ArrayList<>();
			attackPositions.add(targetPos);
			for (int i = 0; i < 3; i++) {
				attackPositions.add(new Position((targetPos.getX() - 1) + Misc.getRandom(3),
						(targetPos.getY() - 1) + Misc.getRandom(3)));
			}
			for (Position pos : attackPositions) {
				character.executeProjectile(new Projectile(character.getPosition(), pos, 0, 551, 40, 80, 31, 43, 0));
			}
			TaskManager.submit(new Task(4) {
				@Override
				public void execute() {
					for (Position pos : attackPositions) {
						target.getAsPlayer().getPacketSender().sendGlobalGraphic(EXPLOSION_END_GFX, pos);
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
		}
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
		character.performAnimation(MAGIC_ATTACK_ANIM);
	}

	@Override
	public void finished(Character character, Character target) {
		if (Misc.getRandom(10) == 1) {
			if (target.isPlayer()) {
				ChaosElementalCombatMethod.disarmAttack(target.getAsPlayer());
			}
		}
	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}
