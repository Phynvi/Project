package com.overload.game.task.impl;

import com.overload.game.GameConstants;
import com.overload.game.content.ItemsKeptOnDeath;
import com.overload.game.content.PrayerHandler;
import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.combat.bountyhunter.Emblem;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.grounditem.ItemOnGroundManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.model.areas.Area;
import com.overload.game.model.rights.Right;
import com.overload.game.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a player's death task, through which the process of dying is
 * handled, the animation, dropping items, etc.
 *
 * @author Professor Oak
 */

public class PlayerDeathTask extends Task {

	/**
	 * The {@link Player} which has died.
	 */
	private final Player player;
	/**
	 * The {@link Player} which killed us.
	 */
	private final Optional<Player> killer;
	/**
	 * Should the player drop their items?
	 */
	private boolean loseItems = true;
	/**
	 * The {@link List} which holds the items the {@link Player} will keep.
	 */
	private Optional<List<Item>> itemsToKeep = Optional.empty();
	/**
	 * The amount of ticks this task will execute.
	 */
	private int ticks = 2;

	/**
	 * The PlayerDeathTask constructor.
	 *
	 * @param player
	 *            The player setting off the task.
	 */
	public PlayerDeathTask(Player player) {
		super(1, player, false);
		this.player = player;
		this.killer = player.getCombat().getKiller(true);
	}

	@Override
	public void execute() {
		if (player == null) {
			stop();
			return;
		}
		try {
			switch (ticks) {
			case 2:
				// Reset combat..
				player.getCombat().reset();

				// Reset movement queue and disable it..
				player.getMovementQueue().setBlockMovement(true).reset();

				// Mark us as untargetable..
				player.setUntargetable(true);

				// Close all open interfaces..
				player.getPacketSender().sendInterfaceRemoval();

				// Send death message..
				player.getPacketSender().sendMessage("Oh dear, you are dead!");

				// Perform death animation..
				player.performAnimation(new Animation(836, Priority.HIGH));

				// Handle retribution prayer effect on our killer, if present..
				if (PrayerHandler.isActivated(player, PrayerHandler.RETRIBUTION)) {
					if (killer.isPresent()) {
						CombatFactory.handleRetribution(player, killer.get());
					}
				}
				break;
			case 0:

				if (player.getArea() != null) {
					loseItems = player.getArea().dropItemsOnDeath(player, killer);
				}

				final List<Item> droppedItems = new ArrayList<Item>();

				// Handle the loss of items..
				if (loseItems) {
					
					// Get items to keep
					itemsToKeep = Optional.of(ItemsKeptOnDeath.getItemsToKeep(player));

					// Fetch player's items
					final List<Item> playerItems = new ArrayList<Item>();
					playerItems.addAll(player.getInventory().getValidItems());

					// The position the items will be dropped at
					final Position position = player.getPosition();

					// Go through player items, drop them to killer
					boolean dropped = false;

					for (Item item : playerItems) {

						// Keep tradeable items
						if (!item.getDefinition().isTradeable() || itemsToKeep.get().contains(item)) {
							if (!itemsToKeep.get().contains(item)) {
								itemsToKeep.get().add(item);
							}
							continue;
						}

						// Don't drop items if we're owner or dev

						if (player.getRights().isOrInherits(Right.GAME_DEVELOPER)) {
							break;
						}

						// Drop emblems but downgrade them a tier.
						if (item.getId() == Emblem.MYSTERIOUS_EMBLEM_1.id
								|| item.getId() == Emblem.MYSTERIOUS_EMBLEM_2.id
								|| item.getId() == Emblem.MYSTERIOUS_EMBLEM_3.id
								|| item.getId() == Emblem.MYSTERIOUS_EMBLEM_4.id
								|| item.getId() == Emblem.MYSTERIOUS_EMBLEM_5.id
								|| item.getId() == Emblem.MYSTERIOUS_EMBLEM_6.id
								|| item.getId() == Emblem.MYSTERIOUS_EMBLEM_7.id
								|| item.getId() == Emblem.MYSTERIOUS_EMBLEM_8.id
								|| item.getId() == Emblem.MYSTERIOUS_EMBLEM_9.id
								|| item.getId() == Emblem.MYSTERIOUS_EMBLEM_10.id) {

							// Tier 1 shouldnt be dropped cause it cant be downgraded
							if (item.getId() == Emblem.MYSTERIOUS_EMBLEM_1.id) {
								continue;
							}

							if (killer.isPresent() && killer.get().isPlayer()) {
								final int lowerEmblem = item.getId() == Emblem.MYSTERIOUS_EMBLEM_2.id ? item.getId() - 2
										: item.getId() - 1;
								ItemOnGroundManager.registerNonGlobal(killer.get(), new Item(lowerEmblem), position);
								killer.get().getPacketSender().sendMessage("@red@" + player.getUsername()
										+ " dropped a " + ItemDefinition.forId(lowerEmblem).getName() + "!");
								dropped = true;
							}

							continue;
						}

						droppedItems.add(item);

						// Drop item
						if (killer.isPresent() && killer.get().isPlayer())
							ItemOnGroundManager.register(killer.get(), item, position);
						else
							ItemOnGroundManager.register(player, item, position);
							
					}

					// Handle defeat..
					if (killer.isPresent() && killer.get().isPlayer()) {
						Player k = killer.get();
						if (k.getArea() != null) {
							k.getArea().defeated(k, player);
						}
						if (!dropped) {
							killer.get().getPacketSender()
									.sendMessage("" + player.getUsername() + " had no valuable items to be dropped.");
						}
					}

					// Reset items
					player.getInventory().resetItems().refreshItems();
				}

				// Restore the player's default attributes (such as stats)..
				player.resetAttributes();

				// If the player lost items..
				if (loseItems) {
					// Handle items kept on death..
					if (itemsToKeep.isPresent()) {
						for (Item it : itemsToKeep.get()) {
							int id = it.getId();

							// Handle item breaking..
							BrokenItem brokenItem = BrokenItem.get(id);
							if (brokenItem != null) {
								id = brokenItem.getBrokenItem();
								player.getPacketSender()
										.sendMessage("Your " + ItemDefinition.forId(it.getId()).getName()
												+ " has been broken. You can fix it by talking to Perdu.");
							}

							player.getInventory().add(new Item(id, it.getAmount()));
						}
						itemsToKeep.get().clear();
						player.getEquipment().resetItems().refreshItems();
					}
				}

				boolean handledDeath = false;

				if (player.getArea() != null) {
					handledDeath = player.getArea().handleDeath(player, killer);
				}
				if (player.getInstancedRegion() != null) {
					if (player.getInstancedRegion().isPresent()) {
						if(player.getInstancedRegion().get().getAreas().isPresent()) {
							for(Area area : player.getInstancedRegion().get().getAreas().get()) {
								handledDeath = area.handleDeath(player, killer);
							}
						}
					}
				}

				if (!handledDeath) {
					player.moveTo(GameConstants.DEFAULT_POSITION.clone());
				}

				// Stop the event..
				stop();
				break;
			}
			ticks--;
		} catch (Exception e) {
			setEventRunning(false);
			e.printStackTrace();
			player.resetAttributes();
			player.moveTo(GameConstants.DEFAULT_POSITION.clone());
		}
	}
}
