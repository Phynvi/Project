package com.overload.net.packet.impl;

import com.overload.game.GameConstants;
import com.overload.game.content.*;
import com.overload.game.content.PrayerHandler.PrayerData;
import com.overload.game.content.achievement.Diary.DiaryType;
import com.overload.game.content.achievement.Diary.OptionType;
import com.overload.game.content.combat.CombatSpecial;
import com.overload.game.content.combat.ranged.DwarfCannonEvent;
import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.interactable.ZulrahItems;
import com.overload.game.content.interactable.ZulrahItems.ZulrahItemType;
import com.overload.game.content.minigames.barrows.Barrows;
import com.overload.game.content.skill.herblore.Herblore;
import com.overload.game.content.skill.hunter.*;
import com.overload.game.content.skill.prayer.Prayer;
import com.overload.game.content.skill.runecrafting.Runecrafting;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.BarrowsSet;
import com.overload.game.model.Item;
import com.overload.game.model.Position;
import com.overload.game.model.areas.impl.WildernessArea;
import com.overload.game.model.dialogue.DialogueManager;
import com.overload.game.model.dialogue.DialogueOptions;
import com.overload.game.model.teleportation.TeleportHandler;
import com.overload.game.model.teleportation.TeleportTablets;
import com.overload.game.model.teleportation.TeleportType;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketConstants;
import com.overload.net.packet.PacketListener;
import com.overload.util.ItemIdentifiers;
import com.overload.util.Misc;

public class ItemActionPacketListener extends ItemIdentifiers implements PacketListener {

	private static void firstAction(final Player player, Packet packet) {
		@SuppressWarnings("unused")
		int interfaceId = packet.readUnsignedShort();
		int itemId = packet.readShort();
		int slot = packet.readShort();

		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;

		if (player.isTeleporting() || player.getHitpoints() <= 0)
			return;
		
		if (player.isDebug()) {
			player.sendMessage("Item Option 1: Item ID - " + itemId + " | Slot: " + slot);
		}

		player.getPacketSender().sendInterfaceRemoval();
		
		player.setLastClickData(DiaryType.ITEM, OptionType.FIRST, itemId);

		// Herblore
		if (Herblore.clean(player, itemId)) {
			return;
		}

		// Prayer
		if (Prayer.buryBone(player, itemId)) {
			return;
		}

		// Eating food..
		if (Food.consume(player, itemId, slot)) {
			return;
		}

		// Drinking potions..
		if (PotionConsumable.drink(player, itemId, slot)) {
			return;
		}

		// Runecrafting pouches..
		if (Runecrafting.handlePouch(player, itemId, 1)) {
			return;
		}

		// Teleport tablets..
		if (TeleportTablets.init(player, itemId)) {
			return;
		}

		switch (itemId) {
		case ItemIdentifiers.ROTTEN_POTATO:
			DialogueHandler.sendOptions(player, "Mod Panel (Eat)", new String[] {
					"Max Stats",
					"Clear Inventory",
					"Setup POH",
					"Teleport to Player",
					"Spawn aggressive NPC"
			});
			player.setDialogueState(1);
			player.setNpcTalking(2147000000);
			break;
		case ItemIdentifiers.SPADE:
			player.performAnimation(new Animation(830));
			TaskManager.submit(new Task(1, player, false) {
				@Override
				protected void execute() {
					if (!player.isTeleporting()) {
						Barrows.dig(player);
					}
					stop();
				}
			});
			break;
		case Gambling.MITHRIL_SEEDS:
			Gambling.plantFlower(player);
			break;
		case 9520:
			if (!(player.getArea() instanceof WildernessArea)) {
				if (player.getSpecialPercentage() < 100) {
					player.getPacketSender().sendInterfaceRemoval();
					player.performAnimation(new Animation(829));
					player.getInventory().delete(9520, 1);
					player.setSpecialPercentage(100);
					CombatSpecial.updateBar(player);
					player.getPacketSender().sendMessage("You now have 100% special attack energy.");
				} else {
					player.getPacketSender().sendMessage("You already have full special attack energy!");
				}
			} else {
				player.getPacketSender().sendMessage("You cannot use this in the Wilderness!");
			}
			break;
            case 10006:
                Hunter.layTrap(player, new SnareTrap(new GameObject(9345, new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ())), Trap.TrapState.SET, 200, player));
                break;
            case 10008:
                Hunter.layTrap(player, new BoxTrap(new GameObject(9380, new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ())), Trap.TrapState.SET, 200, player));
                break;
		case 8013:
			if (TeleportHandler.checkReqs(player, GameConstants.DEFAULT_POSITION)) {
				TeleportHandler.teleport(player, GameConstants.DEFAULT_POSITION, TeleportType.TELE_TAB, false);
				player.getInventory().delete(8013, 1);
			}
			break;
		case 2542:
		case 2543:
		case 2544:
			if (player.busy()) {
				player.getPacketSender().sendMessage("You cannot do that right now.");
				return;
			}
			if (itemId == 2542 && player.isPreserveUnlocked() || itemId == 2543 && player.isRigourUnlocked()
					|| itemId == 2544 && player.isAuguryUnlocked()) {
				player.getPacketSender().sendMessage("You have already unlocked that prayer.");
				return;
			}
			DialogueManager.start(player, 9);
			player.setDialogueOptions(new DialogueOptions() {
				@Override
				public void handleOption(Player player, int option) {
					if (option == 1) {
						player.getInventory().delete(itemId, 1);

						if (itemId == 2542)
							player.setPreserveUnlocked(true);
						else if (itemId == 2543)
							player.setRigourUnlocked(true);
						else if (itemId == 2544)
							player.setAuguryUnlocked(true);
						player.getPacketSender().sendConfigByte(709,
								PrayerHandler.canUse(player, PrayerData.PRESERVE, false) ? 1 : 0);
						player.getPacketSender().sendConfigByte(711,
								PrayerHandler.canUse(player, PrayerData.RIGOUR, false) ? 1 : 0);
						player.getPacketSender().sendConfigByte(713,
								PrayerHandler.canUse(player, PrayerData.AUGURY, false) ? 1 : 0);
						player.getPacketSender().sendMessage("You have unlocked a new prayer.");
					}
					player.getPacketSender().sendInterfaceRemoval();
				}
			});
			break;
            case 6:
                DwarfCannonEvent.setupCannon(player);
                break;
		case 2545:
			if (player.busy()) {
				player.getPacketSender().sendMessage("You cannot do that right now.");
				return;
			}
			if (player.isTargetTeleportUnlocked()) {
				player.getPacketSender().sendMessage("You have already unlocked that teleport.");
				return;
			}
			DialogueManager.start(player, 12);
			player.setDialogueOptions(new DialogueOptions() {
				@Override
				public void handleOption(Player player, int option) {
					if (option == 1) {
						player.getInventory().delete(itemId, 1);
						player.setTargetTeleportUnlocked(true);
						player.getPacketSender().sendMessage("You have unlocked a new teleport.");
					}
					player.getPacketSender().sendInterfaceRemoval();
				}
			});
			break;
		case 12873:
		case 12875:
		case 12879:
		case 12881:
		case 12883:
		case 12877:
			BarrowsSet set = BarrowsSet.get(itemId);
			if (set != null) {
				if (!player.getInventory().contains(set.getSetId())) {
					return;
				}
				if ((player.getInventory().getFreeSlots() - 1) < set.getItems().length) {
					player.getPacketSender().sendMessage(
							"You need at least " + set.getItems().length + " free inventory slots to do that.");
					return;
				}
				player.getInventory().delete(set.getSetId(), 1);
				for (int item : set.getItems()) {
					player.getInventory().add(item, 1);
				}
				player.getPacketSender()
						.sendMessage("You've opened your " + ItemDefinition.forId(itemId).getName() + ".");
			}
			break;
		}
	}

	public static void secondAction(Player player, Packet packet) {
		@SuppressWarnings("unused")
		int interfaceId = packet.readLEShortA();
		int slot = packet.readLEShort();
		int itemId = packet.readShortA();
		if (slot < 0 || slot >= player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;
		
		if (player.isDebug()) {
			player.sendMessage("Item Option 2: Item ID - " + itemId + " | Slot: " + slot);
		}

		if (Runecrafting.handleTalisman(player, itemId)) {
			return;
		}
		if (Runecrafting.handlePouch(player, itemId, 2)) {
			return;
		}
		player.setLastClickData(DiaryType.ITEM, OptionType.SECOND, itemId);

		switch (itemId) {
			case ROTTEN_POTATO:
				DialogueHandler.sendOptions(player, "Mod Panel (Mash)", new String[] {
						"Keep me logged in",
						"Kick me out",
						"Kill me",
						"Transmorgrify me..."
				});
				player.setDialogueState(4);
				player.setNpcTalking(2147000000);
				break;
			case TOXIC_BLOWPIPE_EMPTY:
				ZulrahItems.dismantle(player, slot, ZulrahItemType.BLOWPIPE);
				break;
			case 2550:
				player.setDialogueOptions(new DialogueOptions() {
					@Override
					public void handleOption(Player player, int option) {
						player.getPacketSender().sendInterfaceRemoval();
						if (option == 1) {
							if (player.getInventory().contains(2550)) {
								player.getInventory().delete(2550, 1);
								player.setRecoilDamage(0);
								player.getPacketSender().sendMessage("Your Ring of recoil has degraded.");
							}
						}
					}
				});
				player.setDialogue(DialogueManager.getDialogues().get(10)); // Yes / no option
				DialogueManager.sendStatement(player,
						"You still have " + (40 - player.getRecoilDamage()) + " damage before it breaks. Continue?");
				break;
		}
	}
	
	public void thirdClickAction(Player player, Packet packet) {
		int itemId = packet.readShortA();
		int slot = packet.readLEShortA();
		@SuppressWarnings("unused")
		int interfaceId = packet.readLEShortA();
		if (slot < 0 || slot >= player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;
		
		if (player.isDebug()) {
			player.sendMessage("Item Option 3: Item ID - " + itemId + " | Slot: " + slot);
		}

		player.setLastClickData(DiaryType.ITEM, OptionType.THIRD, itemId);
		
		if (BarrowsSet.pack(player, itemId)) {
			return;
		}
        if(JarData.forJar(itemId) != null) {
            PuroPuro.lootJar(player, new Item(itemId, 1), JarData.forJar(itemId));
            return;
        }
		if (Runecrafting.handlePouch(player, itemId, 3)) {
			return;
		}

		switch (itemId) {
		case ROTTEN_POTATO:
			DialogueHandler.sendOptions(player, "Mod Panel (Peel)", new String[] {
					"Bank Menu",
					"Anti Macro Event All Near",
					"Teleport to RARE!",
					"Spawn RARE!"
			});
			player.setDialogueState(3);
			player.setNpcTalking(2147000000);
			break;
		case TOXIC_BLOWPIPE:
            String ammoName = player.getBlowpipeAmmoType() == 0 ? "None" : ItemDefinition.forId(player.getBlowpipeAmmoType()).getName();
            String percent = ZulrahItems.percentString(player);
            player.getPacketSender().sendMessage("Darts: " + (ammoName == "None" ? "@red@" : "<col=00B400>") + "" + ammoName + " x " + Misc.format(player.getBlowpipeAmmoAmount()) + "@bla@. Scales: " + percent);
			break;
		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player == null || player.getHitpoints() <= 0)
			return;
		switch (packet.getOpcode()) {
		case PacketConstants.FIRST_ITEM_ACTION_OPCODE:
			firstAction(player, packet);
			break;
		case PacketConstants.SECOND_ITEM_ACTION_OPCODE:
			secondAction(player, packet);
			break;
		case PacketConstants.THIRD_ITEM_ACTION_OPCODE:
			thirdClickAction(player, packet);
			break;
		}
	}
}