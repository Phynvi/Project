package com.overload.game.content.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.overload.game.content.combat.WeaponInterfaces.WeaponInterface;
import com.overload.game.content.combat.hit.HitDamageCache;
import com.overload.game.content.combat.hit.HitQueue;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.magic.CombatSpell;
import com.overload.game.content.combat.method.CombatMethod;
import com.overload.game.content.combat.method.impl.specials.GraniteMaulCombatMethod;
import com.overload.game.content.combat.ranged.RangedData.Ammunition;
import com.overload.game.content.combat.ranged.RangedData.RangedWeapon;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.SecondsTimer;
import com.overload.net.packet.impl.commands.developer.Npc;
import com.overload.util.Stopwatch;
import com.overload.util.timers.TimerKey;

/**
 * My entity-based combat system. The main class of the system.
 *
 * @author Swiffy
 */

public class Combat {

	// The user's damage map
	private final Map<Player, HitDamageCache> damageMap = new HashMap<>();
	// The timer of the last attack which occured
	private final Stopwatch lastAttack = new Stopwatch();
	// Timers
	private final SecondsTimer poisonImmunityTimer = new SecondsTimer();
	private final SecondsTimer fireImmunityTimer = new SecondsTimer();
	private final SecondsTimer teleblockTimer = new SecondsTimer();
	private final SecondsTimer prayerBlockTimer = new SecondsTimer();
	// Ranged data
	public RangedWeapon rangedWeapon;
	public Ammunition rangeAmmoData;
	// The user's HitQueue
	private HitQueue hitQueue;
	// The character
	private Character character;
	// The character's current target
	private Character target;
	// The last person who attacked the character this instance belongs to.
	private Character attacker;
	// The last combat method used
	private CombatMethod method;
	// Fight type
	private FightType fightType = FightType.UNARMED_KICK;
	// WeaponInterface
	private WeaponInterface weapon;
	// Autoretaliate
	private boolean autoRetaliate;
	// Magic data
	private CombatSpell castSpell;
	private CombatSpell autoCastSpell;
	private CombatSpell previousCast;

	public Combat(Character character) {
		this.character = character;
		this.hitQueue = new HitQueue();
	}

	/**
	 * Attacks an entity by updating our current target.
	 *
	 * @param target
	 *            The target to attack.
	 */
	public void attack(Character target) {
		// Update the target
		setTarget(target);

		// Start facing the target
		character.setEntityInteraction(target);

		// Start following the target
        if (target.isNpc()) {
            if (target.getAsNpc().getCombatMethod() != null) {
                if (!target.getAsNpc().getCombatMethod().canMove()) {
                    return;
                }
            }
        }
		character.getMovementQueue().follow(target);
	}

	/**
	 * Processes combat.
	 */
	public void process() {
		// Process the hit queue
		hitQueue.process(character);

		// Handle attacking
		performNewAttack(false);

		// Reset attacker if we haven't been attacked in 6 seconds.
		if (lastAttack.elapsed(6000)) {
			setUnderAttack(null);
		}
	}

	/**
	 * Attempts to attack the target.
	 */
	public void performNewAttack(boolean disregardDelay) {
		if (target != null) {

			// Fetch the combat method the character will be attacking with
			method = CombatFactory.getMethod(character);

			// Follow target
			character.getMovementQueue().follow(target);

			// Check if the character can reach the target before attempting attack
			if (CombatFactory.canReach(character, method, target)) {

				// Granite maul special attack, make sure we disregard delay
				// and that we do not reset the attack timer.
				boolean graniteMaulSpecial = (method instanceof GraniteMaulCombatMethod);
				if (graniteMaulSpecial) {
					disregardDelay = true;
				}

				// Make sure attack timer is <= 0
				if (!character.getTimers().has(TimerKey.COMBAT_ATTACK) || disregardDelay) {

					// Check if the character can perform the attack
					if (CombatFactory.canAttack(character, method, target)) {

						// Face target
						character.setEntityInteraction(target);

						// Do animation
						method.startAnimation(character);

						// Perform the abstract method "preQueueAdd" before adding the hit for the
						// target
						method.prepareAttack(character, target);

						// Create a new {PendingHit}[] using the player's combat method
						// (melee/range/magic)
						PendingHit[] hits = method.getHits(character, target);
						if (hits == null)
							return;

						// Put all of the {PendingHit} in the target's HitQueue
						// And also do other things, such as reward attacker experience
						// If they're a player.
						for (PendingHit hit : hits) {
							CombatFactory.addPendingHit(hit);
						}

						// Let the method know we finished the attack
						// And perform final actions.
						// Example: After attack for magic, reset spell if player is not autocasting.
						method.finished(character, target);

						// Reset attack timer
						if (!graniteMaulSpecial) {
							character.getTimers().register(TimerKey.COMBAT_ATTACK, method.getAttackSpeed(character));
						}
						disregardDelay = false;
					}
				}
			}
		}
	}

	/**
	 * Resets combat for the {@link Character}.
	 */
	public void reset() {
		target = null;
		character.getMovementQueue().resetFollowing();
		character.setEntityInteraction(null);
	}

	/**
	 * Adds damage to the damage map, as long as the argued amount of damage is
	 * above 0 and the argued entity is a player.
	 *
	 * @param entity
	 *            the entity to add damage for.
	 * @param amount
	 *            the amount of damage to add for the argued entity.
	 */
	public void addDamage(Character entity, int amount) {

		if (amount <= 0 || entity.isNpc()) {
			return;
		}

		Player player = (Player) entity;
		if (damageMap.containsKey(player)) {
			damageMap.get(player).incrementDamage(amount);
			return;
		}

		damageMap.put(player, new HitDamageCache(amount));
	}
	
	public boolean wasSolo() {
		if (damageMap.size() == 1) {
			return true;
		}
		return true;
	}

	/**
	 * Performs a search on the <code>damageMap</code> to find which {@link Player}
	 * dealt the most damage on this controller.
	 *
	 * @param clearMap
	 *            <code>true</code> if the map should be discarded once the killer
	 *            is found, <code>false</code> if no data in the map should be
	 *            modified.
	 * @return the player who killed this entity, or <code>null</code> if an npc or
	 *         something else killed this entity.
	 */
	public Optional<Player> getKiller(boolean clearMap) {

		// Return null if no players killed this entity.
		if (damageMap.size() == 0) {
			return Optional.empty();
		}

		// The damage and killer placeholders.
		int damage = 0;
		Optional<Player> killer = Optional.empty();

		for (Entry<Player, HitDamageCache> entry : damageMap.entrySet()) {

			// Check if this entry is valid.
			if (entry == null) {
				continue;
			}

			// Check if the cached time is valid.
			long timeout = entry.getValue().getStopwatch().elapsed();
			if (timeout > CombatConstants.DAMAGE_CACHE_TIMEOUT) {
				continue;
			}

			// Check if the key for this entry has logged out.
			Player player = entry.getKey();
			if (!player.isRegistered()) {
				continue;
			}

			// If their damage is above the placeholder value, they become the
			// new 'placeholder'.
			if (entry.getValue().getDamage() > damage) {
				damage = entry.getValue().getDamage();
				killer = Optional.of(entry.getKey());
			}
		}

		// Clear the damage map if needed.
		if (clearMap)
			damageMap.clear();

		// Return the killer placeholder.
		return killer;
	}
	
	public void clearDamage() {
		damageMap.clear();
	}
	
	public void removePlayerDamage(Player player) {
		if (damageMapContains(player)) {
			damageMap.remove(player);
		}
	}

	public boolean damageMapContains(Player player) {
		return damageMap.containsKey(player);
	}

	/**
	 * Getters and setters
	 **/

	public Character getCharacter() {
		return character;
	}

	public Character getTarget() {
		return target;
	}

	public void setTarget(Character target) {
		this.target = target;
	}

	public HitQueue getHitQueue() {
		return hitQueue;
	}

	public Character getAttacker() {
		return attacker;
	}

	public void setUnderAttack(Character attacker) {
		this.attacker = attacker;
		this.lastAttack.reset();
	}

	public CombatSpell getCastSpell() {
		return castSpell;
	}

	public void setCastSpell(CombatSpell castSpell) {
		this.castSpell = castSpell;
	}

	public CombatSpell getAutocastSpell() {
		return autoCastSpell;
	}

	public void setAutocastSpell(CombatSpell autoCastSpell) {
		this.autoCastSpell = autoCastSpell;
	}

	public CombatSpell getPreviousCast() {
		return previousCast;
	}

	public void setPreviousCast(CombatSpell previousCast) {
		this.previousCast = previousCast;
	}

	public RangedWeapon getRangedWeapon() {
		return rangedWeapon;
	}

	public void setRangedWeapon(RangedWeapon rangedWeapon) {
		this.rangedWeapon = rangedWeapon;
	}

	public Ammunition getAmmunition() {
		return rangeAmmoData;
	}

	public void setAmmunition(Ammunition rangeAmmoData) {
		this.rangeAmmoData = rangeAmmoData;
	}

	public WeaponInterface getWeapon() {
		return weapon;
	}

	public void setWeapon(WeaponInterface weapon) {
		this.weapon = weapon;
	}

	public FightType getFightType() {
		return fightType;
	}

	public void setFightType(FightType fightType) {
		this.fightType = fightType;
	}

	public boolean autoRetaliate() {
		return autoRetaliate;
	}

	public void setAutoRetaliate(boolean autoRetaliate) {
		this.autoRetaliate = autoRetaliate;
	}

	public SecondsTimer getPoisonImmunityTimer() {
		return poisonImmunityTimer;
	}

	public SecondsTimer getFireImmunityTimer() {
		return fireImmunityTimer;
	}

	public SecondsTimer getTeleBlockTimer() {
		return teleblockTimer;
	}

	public SecondsTimer getPrayerBlockTimer() {
		return prayerBlockTimer;
	}

	public Stopwatch getLastAttack() {
		return lastAttack;
	}
}
