package com.overload.game.content.skill.runecrafting;

import com.overload.game.content.PetHandler;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Graphic;
import com.overload.game.model.Skill;
import com.overload.game.model.teleportation.TeleportHandler;
import com.overload.util.ItemIdentifiers;
import com.overload.util.Misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Runecrafting {

	private static final Graphic CRAFT_RUNES_GRAPHIC = new Graphic(186);

	private static final Animation CRAFT_RUNES_ANIMATION = new Animation(791);

	public static boolean initialize(Player player, int objectId) {
		Optional<RuneData.Rune> rune = RuneData.Rune.forId(objectId);
		if (rune.isPresent()) {
			if (player.getSkillManager().getCurrentLevel(Skill.RUNECRAFTING) < rune.get().getLevelRequirement()) {
				player.getPacketSender().sendMessage("You need a Runecrafting level of at least "
						+ rune.get().getLevelRequirement() + " to craft this.");
				return false;
			}
			int essence;
			if (rune.get().isPureRequired()) {
				if (!player.getInventory().contains(ItemIdentifiers.PURE_ESSENCE)) {
					player.getPacketSender().sendMessage("You need Pure essence to craft runes using this altar.");
					return true;
				}
				essence = ItemIdentifiers.PURE_ESSENCE;
			} else {
				if (player.getInventory().contains(ItemIdentifiers.RUNE_ESSENCE)) {
					essence = ItemIdentifiers.RUNE_ESSENCE;
				} else if (player.getInventory().contains(ItemIdentifiers.PURE_ESSENCE)) {
					essence = ItemIdentifiers.PURE_ESSENCE;
				} else {
					player.getPacketSender().sendMessage("You don't have any essence in your inventory.");
					return true;
				}
			}
			player.performGraphic(CRAFT_RUNES_GRAPHIC);
			player.performAnimation(CRAFT_RUNES_ANIMATION);
			int craftAmount = craftAmount(rune.get(), player);
			int xpGain = 0;
			for (int i = 0; i < 28; i++) {
				if (!player.getInventory().contains(essence)) {
					break;
				}
				player.getInventory().delete(essence, 1);
				player.getInventory().add(rune.get().getRuneID(), craftAmount);
				xpGain += rune.get().getXP();
			}

			// Finally add the total experience they gained..
			player.getSkillManager().addExperience(Skill.RUNECRAFTING, xpGain);

			// Pets..
			PetHandler.onSkill(player, Skill.RUNECRAFTING);
		}
		return false;
	}

	public static boolean handleTalisman(Player player, int itemId) {
		Optional<TalismanData.Talisman> talisman = TalismanData.Talisman.forId(itemId);
		if (talisman.isPresent()) {
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) < talisman.get().getLevelRequirement()) {
				player.getPacketSender().sendMessage("You need a Runecrafting level of at least "
						+ talisman.get().getLevelRequirement() + " to use this Talisman's teleport function.");
			} else {
				if (TeleportHandler.checkReqs(player, talisman.get().getPosition())) {
					TeleportHandler.teleport(player, talisman.get().getPosition(),
							player.getSpellbook().getTeleportType(), true);
				}
			}
			return true;
		}
		return false;
	}

	public static boolean handlePouch(Player player, int itemId, int actionType) {
		Optional<Pouch> pouch = Pouch.forItemId(itemId);
		if (pouch.isPresent()) {
			// PouchContainer container =
			// player.getPouchContainers()[pouch.get().ordinal()];
			Optional<PouchContainer> container = Optional.empty();
			for (PouchContainer pC : player.getPouches()) {
				if (pC.getPouch() == pouch.get()) {
					container = Optional.of(pC);
					break;
				}
			}
			if (container.isPresent()) {
				switch (actionType) {
				case 1:
					container.get().store(player);
					break;
				case 2:
					container.get().check(player);
					break;
				case 3:
					container.get().withdraw(player);
					break;
				}
				return true;
			}
		}
		return false;
	}

	private static int craftAmount(RuneData.Rune rune, Player player) {
		int amount = 1;
		switch (rune) {
		case AIR_RUNE:
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 11)
				amount = 2;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 22)
				amount = 3;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 33)
				amount = 4;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 44)
				amount = 5;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 55)
				amount = 6;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 66)
				amount = 7;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 77)
				amount = 8;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 88)
				amount = 9;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 99)
				amount = 10;
			break;
		case ASTRAL_RUNE:
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 82)
				amount = 2;
			break;
		case BLOOD_RUNE:
			break;
		case BODY_RUNE:
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 46)
				amount = 2;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 92)
				amount = 3;
			break;
		case CHAOS_RUNE:
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 74)
				amount = 2;
			break;
		case COSMIC_RUNE:
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 59)
				amount = 2;
			break;
		case DEATH_RUNE:
			break;
		case EARTH_RUNE:
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 26)
				amount = 2;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 52)
				amount = 3;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 78)
				amount = 4;
			break;
		case FIRE_RUNE:
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 35)
				amount = 2;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 70)
				amount = 3;
			break;
		case LAW_RUNE:
			break;
		case MIND_RUNE:
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 14)
				amount = 2;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 28)
				amount = 3;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 42)
				amount = 4;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 56)
				amount = 5;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 70)
				amount = 6;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 84)
				amount = 7;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 98)
				amount = 8;
			break;
		case NATURE_RUNE:
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 91)
				amount = 2;
			break;
		case WATER_RUNE:
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 19)
				amount = 2;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 38)
				amount = 3;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 57)
				amount = 4;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 76)
				amount = 5;
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) >= 95)
				amount = 6;
			break;
		default:
			break;
		}
		return amount;
	}

	/**
	 * Represents a pouch used for the Runecrafting skill to hold essence for the
	 * player.
	 *
	 * @author Professor Oak
	 */
	public enum Pouch {
		SMALL_POUCH(5509, 1, 3, -1), MEDIUM_POUCH(5510, 25, 6, 45), LARGE_POUCH(5512, 50, 9, 29), GIANT_POUCH(5514, 75,
				12, 10),;

		private static final Map<Integer, Pouch> pouches = new HashMap<Integer, Pouch>();

		static {
			for (Pouch p : Pouch.values()) {
				pouches.put(p.getItemId(), p);
			}
		}

		/**
		 * The pouch's item identifier.
		 */
		private final int itemId;
		/**
		 * The level required to use this pouch.
		 */
		private final int requiredLevel;
		/**
		 * The pouch's capacity.
		 */
		private final int capacity;
		/**
		 * The pouch's decay chance.
		 */
		private final int decayChance;

		Pouch(int itemId, int requiredLevel, int capacity, int decayChance) {
			this.itemId = itemId;
			this.requiredLevel = requiredLevel;
			this.capacity = capacity;
			this.decayChance = decayChance;
		}

		public static Optional<Pouch> forItemId(int itemId) {
			Pouch pouch = pouches.get(itemId);
			if (pouch != null) {
				return Optional.of(pouch);
			}
			return Optional.empty();
		}

		public int getItemId() {
			return itemId;
		}

		public int getRequiredLevel() {
			return requiredLevel;
		}

		public int getCapacity() {
			return capacity;
		}

		public int getDecayChance() {
			return decayChance;
		}
	}

	/**
	 * Represents a container for a {@link Pouch}.
	 *
	 * @author Professor Oak
	 */
	public static class PouchContainer {
		/**
		 * The pouch which belongs to this container.
		 */
		private final Pouch pouch;

		/**
		 * The amount of regular Rune essence stored in this container.
		 */
		private int runeEssenceAmt;

		/**
		 * The amount of Pure essence stored in this container.
		 */
		private int pureEssenceAmt;

		/**
		 * Creates this container.
		 * @param pouch
		 */
		public PouchContainer(Pouch pouch) {
			this.pouch = pouch;
		}

		/**
		 * Creates a PouchContainer with the given essence amounts. [Neytorokx]
		 *
		 * @param pouch
		 * @param runeEssence
		 * @param pureEssence
		 */
		public PouchContainer(Pouch pouch, int runeEssence, int pureEssence) {
			this.pouch = pouch;
			this.runeEssenceAmt = runeEssence;
			this.pureEssenceAmt = pureEssence;
		}

		/**
		 * Attempts to store essence into the container.
		 */
		public void store(Player player) {
			if (getStoredAmount() >= pouch.getCapacity()) {
				player.getPacketSender().sendMessage("Your pouch is already full.");
				return;
			}
			if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) < pouch.getRequiredLevel()) {
				player.getPacketSender().sendMessage("You need a Runecrafting level of at least "
						+ Integer.toString(pouch.getRequiredLevel()) + " to use this.");
				return;
			}
			for (int i = getStoredAmount(); i < pouch.getCapacity(); i++) {
				if (player.getInventory().contains(ItemIdentifiers.PURE_ESSENCE)) {
					player.getInventory().delete(ItemIdentifiers.PURE_ESSENCE, 1);
					pureEssenceAmt++;
				} else if (player.getInventory().contains(ItemIdentifiers.RUNE_ESSENCE)) {
					player.getInventory().delete(ItemIdentifiers.RUNE_ESSENCE, 1);
					runeEssenceAmt++;
				} else {
					player.getPacketSender().sendMessage("You don't have any more essence to store.");
					break;
				}
			}
		}

		/**
		 * Attempts to withdraw essence from the container.
		 */
		public void withdraw(Player player) {
			int total = getStoredAmount();
			if (total == 0) {
				player.getPacketSender().sendMessage("Your pouch is already empty.");
				return;
			}
			for (int i = 0; i < total; i++) {
				if (player.getInventory().isFull()) {
					player.getInventory().full();
					break;
				}
				if (pureEssenceAmt > 0) {
					player.getInventory().add(ItemIdentifiers.PURE_ESSENCE, 1);
					pureEssenceAmt--;
				} else if (runeEssenceAmt > 0) {
					player.getInventory().add(ItemIdentifiers.RUNE_ESSENCE, 1);
					runeEssenceAmt--;
				} else {
					player.getPacketSender().sendMessage("You don't have any more essence to withdraw.");
					break;
				}
			}
		}

		/**
		 * Checks the amount of essence in the container.
		 */
		public void check(Player player) {
			player.getPacketSender()
					.sendMessage("Your " + Misc.capitalize(pouch.toString().toLowerCase().replace("_", " "))
							+ " contains " + Integer.toString(runeEssenceAmt) + " Rune essence and "
							+ Integer.toString(pureEssenceAmt) + " Pure essence.");
		}

		/**
		 * Returns the total stored essence.
		 *
		 * @return
		 */
		public int getStoredAmount() {
			return runeEssenceAmt + pureEssenceAmt;
		}

		/**
		 * Returns the total rune essence stored.
		 *
		 * @return
		 */
		public int getStoredRuneEssence() {
			return runeEssenceAmt;
		}

		/**
		 * Returns the total pure essence stored.
		 *
		 * @return
		 */
		public int getStoredPureEssence() {
			return pureEssenceAmt;
		}

		/**
		 * Returns the pouch.
		 *
		 * @return
		 */
		public Pouch getPouch() {
			return pouch;
		}
	}
}
