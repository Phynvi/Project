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
import com.overload.game.model.Position;
import com.overload.game.model.Projectile;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.Misc;
import com.overload.util.timers.TimerKey;

public class VetionCombatMethod implements CombatMethod {

	private CombatType attack = CombatType.MELEE;
	private static final Graphic MAGIC_END_GFX = new Graphic(281);

	@Override
	public boolean canAttack(Character character, Character target) {
		return true;
	}

	@Override
	public PendingHit[] getHits(Character character, Character target) {
		if (attack == CombatType.MAGIC) {
			return null;
		}
		return new PendingHit[] { new PendingHit(character, target, this, true, 2) };
	}

	@Override
	public void prepareAttack(Character character, Character target) {
		if (!character.isNpc() || !target.isPlayer())
			return;

		if (target.getPosition().getDistance(character.getPosition()) < 2 && Misc.getRandom(1) == 0) {
			attack = CombatType.MELEE;
		} else {
			attack = CombatType.MAGIC;
		}

		if (attack == CombatType.MAGIC) {
			Position targetPos = target.getPosition();
			List<Position> attackPositions = new ArrayList<>();
			attackPositions.add(targetPos);
			for (int i = 0; i < 2; i++) {
				attackPositions.add(new Position((targetPos.getX() - 1) + Misc.getRandom(3),
						(targetPos.getY() - 1) + Misc.getRandom(3)));
			}
			for (Position pos : attackPositions) {
				character.executeProjectile(new Projectile(character.getPosition(), pos, 0, 280, 40, 80, 31, 43, 0));
			}
			TaskManager.submit(new Task(4) {
				@Override
				public void execute() {
					for (Position pos : attackPositions) {
						target.getAsPlayer().getPacketSender().sendGlobalGraphic(MAGIC_END_GFX, pos);
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
		if (attack == CombatType.MELEE) {
			return 2;
		}
		return 8;
	}

	@Override
	public void startAnimation(Character character) {
		character.performAnimation(new Animation(character.getAttackAnim()));
	}

	@Override
	public void finished(Character character, Character target) {
	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {
	}

	@Override
	public CombatType getCombatType() {
		return attack;
	}

    @Override
    public boolean canMove() {
        return true;
    }

}
