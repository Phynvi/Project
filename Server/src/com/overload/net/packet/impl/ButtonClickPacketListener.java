package com.overload.net.packet.impl;

import com.overload.game.content.Emotes;
import com.overload.game.content.ItemsKeptOnDeath;
import com.overload.game.content.PrayerHandler;
import com.overload.game.content.clan.ClanChatManager;
import com.overload.game.content.combat.WeaponInterfaces;
import com.overload.game.content.combat.magic.Autocasting;
import com.overload.game.content.combat.magic.EffectSpells;
import com.overload.game.content.combat.magic.spells.BoltEnchanting;
import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.presets.Presetables;
import com.overload.game.content.skill.smithing.Smithing;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.AccountSettings;
import com.overload.game.model.container.impl.Bank;
import com.overload.game.model.equipment.BonusManager;
import com.overload.game.model.teleportation.TeleportHandler;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;

/**
 * This packet listener manages a button that the player has clicked upon.
 *
 * @author Gabriel Hannason
 */

public class ButtonClickPacketListener implements PacketListener {

	// Dialogues
	public static final int FIRST_DIALOGUE_OPTION_OF_FIVE = 2494;
	public static final int SECOND_DIALOGUE_OPTION_OF_FIVE = 2495;
	public static final int THIRD_DIALOGUE_OPTION_OF_FIVE = 2496;
	public static final int FOURTH_DIALOGUE_OPTION_OF_FIVE = 2497;
	public static final int FIFTH_DIALOGUE_OPTION_OF_FIVE = 2498;
	public static final int FIRST_DIALOGUE_OPTION_OF_FOUR = 2482;
	public static final int SECOND_DIALOGUE_OPTION_OF_FOUR = 2483;
	public static final int THIRD_DIALOGUE_OPTION_OF_FOUR = 2484;
	public static final int FOURTH_DIALOGUE_OPTION_OF_FOUR = 2485;
	public static final int FIRST_DIALOGUE_OPTION_OF_THREE = 2471;
	public static final int SECOND_DIALOGUE_OPTION_OF_THREE = 2472;
	public static final int THIRD_DIALOGUE_OPTION_OF_THREE = 2473;
	public static final int FIRST_DIALOGUE_OPTION_OF_TWO = 2461;
	public static final int SECOND_DIALOGUE_OPTION_OF_TWO = 2462;
	private static final int LOGOUT = 2458;
	private static final int TOGGLE_RUN_ENERGY_ORB = 1050;
	private static final int TOGGLE_RUN_ENERGY_SETTINGS = 42507;
	private static final int OPEN_EQUIPMENT_SCREEN = 27653;
	private static final int OPEN_PRICE_CHECKER = 27651;
	private static final int OPEN_ITEMS_KEPT_ON_DEATH_SCREEN = 27654;
    private static final int TOGGLE_AUTO_RETALIATE_328 = 24115;
    private static final int TOGGLE_AUTO_RETALIATE_425 = 24041;
    private static final int TOGGLE_AUTO_RETALIATE_3796 = 24033;
    private static final int TOGGLE_AUTO_RETALIATE_776 = 24048;
    private static final int TOGGLE_AUTO_RETALIATE_1698 = 24017;
    private static final int TOGGLE_AUTO_RETALIATE_1764 = 24010;
    private static final int TOGGLE_AUTO_RETALIATE_2276 = 22845;
    private static final int TOGGLE_AUTO_RETALIATE_5570 = 24025;
	private static final int DESTROY_ITEM = 14175;
	private static final int CANCEL_DESTROY_ITEM = 14176;
	private static final int PRICE_CHECKER_WITHDRAW_ALL = 18255;
	private static final int PRICE_CHECKER_DEPOSIT_ALL = 18252;
	private static final int TOGGLE_EXP_LOCK = 476;
	private static final int OPEN_WORLD_MAP = 156;
	
	// Autocast buttons
	private static final int AUTOCAST_BUTTON_1 = 349;
	private static final int AUTOCAST_BUTTON_2 = 24111;
	// Trade buttons
	private static final int TRADE_ACCEPT_BUTTON_1 = 3420;
	private static final int TRADE_ACCEPT_BUTTON_2 = 3546;
	// Duel buttons
	private static final int DUEL_ACCEPT_BUTTON_1 = 6674;
	private static final int DUEL_ACCEPT_BUTTON_2 = 6520;
	// Close buttons
	private static final int CLOSE_BUTTON_1 = 18247;
	private static final int CLOSE_BUTTON_2 = 38117;
	// Presets
	private static final int OPEN_PRESETS = 31015;
	// Settings tab
	private static final int OPEN_ADVANCED_OPTIONS = 42524;
	private static final int OPEN_KEY_BINDINGS = 42552;

	public static boolean handlers(Player player, int button) {
		if (PrayerHandler.togglePrayer(player, button)) {
			return true;
		}
		if (Autocasting.toggleAutocast(player, button)) {
			return true;
		}
		if (TeleportHandler.handleButton(player, button, 0)) {
			return true;
		}
		if (WeaponInterfaces.changeCombatSettings(player, button)) {
			BonusManager.update(player);
			return true;
		}
		if (EffectSpells.handleSpell(player, button)) {
			return true;
		}
		if (Bank.handleButton(player, button, 0)) {
			return true;
		}
		if (Emotes.doEmote(player, button)) {
			return true;
		}
		if (ClanChatManager.handleButton(player, button, 0)) {
			return true;
		}
		if (player.getSkillManager().pressedSkill(button)) {
			return true;
		}
		if (player.getQuickPrayers().handleButton(button)) {
			return true;
		}
		if (player.getDueling().checkRule(button)) {
			return true;
		}
		if (player.getCreationMenu().isPresent() && player.getCreationMenu().get().handleButton(button)) {
			return true;
		}
		if (Smithing.handleButton(player, button)) {
			return true;
		}
		if (Presetables.handleButton(player, button)) {
			return true;
		}
		if (player.getSlayer().handleInterface(button)) {
			return true;
		}
		return false;
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		int button = packet.readInt();

		if (player.getHitpoints() <= 0 || player.isTeleporting()) {
			return;
		}

		if (player.isDebug()) {
			player.getPacketSender().sendMessage("Button clicked: " + Integer.toString(button) + ".");
		}

		if (handlers(player, button)) {
			return;
		}
		AccountSettings.buttonClick(player, button);
		player.starterclass.handleButtonClick(button, player);
		switch (button) {

		case OPEN_PRESETS:
			if (player.busy()) {
				player.getPacketSender().sendInterfaceRemoval();
			}
			Presetables.open(player);
			break;

		case OPEN_WORLD_MAP:
			if (player.busy()) {
				player.getPacketSender().sendInterfaceRemoval();
			}
			player.getPacketSender().sendInterface(54000);
			break;
			
		case 35004: //First Button (GRAPICS)
    	case 37004:
    	case 13004:
    		player.getPacketSender().sendInterface(34000);
    	break;
    	case 19207:
            BoltEnchanting.open(player);
    	break;
    	case 35005: //Second Button (UI)
    	case 37005:
    	case 13005:
    		player.getPacketSender().sendInterface(37000);
    	break;
    	
    	case 35006: //Third Button (MISC)
    	case 37006:
    	case 39006:
    		player.getPacketSender().sendInterface(13000);
    	break;
    	
    	case 35003: //Close
    	case 37003:
    	case 13003:
    		player.getPacketSender().sendInterfaceRemoval();
    	break;

		case LOGOUT:
			if (player.canLogout()) {
				player.requestLogout();
			} else {
				player.getPacketSender().sendMessage("You cannot log out at the moment.");
			}
			break;

		case TOGGLE_RUN_ENERGY_ORB:
		case TOGGLE_RUN_ENERGY_SETTINGS:
			if (player.busy()) {
				player.getPacketSender().sendInterfaceRemoval();
			}
			if (player.getRunEnergy() > 0) {
				player.setRunning(!player.isRunning());
			} else {
				player.setRunning(false);
			}
			player.getPacketSender().sendRunStatus();
			break;

		case OPEN_ADVANCED_OPTIONS:
			if (player.busy()) {
				player.getPacketSender().sendInterfaceRemoval();
			}
			player.getPacketSender().sendInterface(35000);
			break;

		case OPEN_KEY_BINDINGS:
			if (player.busy()) {
				player.getPacketSender().sendInterfaceRemoval();
			}
			player.getPacketSender().sendInterface(53000);
			break;

		case OPEN_EQUIPMENT_SCREEN:
			if (player.busy()) {
				player.getPacketSender().sendInterfaceRemoval();
			}
			BonusManager.open(player);
			break;

		case OPEN_PRICE_CHECKER:
			if (player.busy()) {
				player.getPacketSender().sendInterfaceRemoval();
			}
			player.getPriceChecker().open();
			break;

		case OPEN_ITEMS_KEPT_ON_DEATH_SCREEN:
			if (player.busy()) {
				player.getPacketSender().sendInterfaceRemoval();
			}
			ItemsKeptOnDeath.open(player);
			break;

		case PRICE_CHECKER_WITHDRAW_ALL:
			player.getPriceChecker().withdrawAll();
			break;

		case PRICE_CHECKER_DEPOSIT_ALL:
			player.getPriceChecker().depositAll();
			break;

		case TRADE_ACCEPT_BUTTON_1:
		case TRADE_ACCEPT_BUTTON_2:
			player.getTrading().acceptTrade();
			break;

		case DUEL_ACCEPT_BUTTON_1:
		case DUEL_ACCEPT_BUTTON_2:
			player.getDueling().acceptDuel();
			break;

		case TOGGLE_AUTO_RETALIATE_328:
        case TOGGLE_AUTO_RETALIATE_425:
        case TOGGLE_AUTO_RETALIATE_3796:
        case TOGGLE_AUTO_RETALIATE_776:
        case TOGGLE_AUTO_RETALIATE_1764:
        case TOGGLE_AUTO_RETALIATE_2276:
        case TOGGLE_AUTO_RETALIATE_5570:
        case TOGGLE_AUTO_RETALIATE_1698:
            player.getCombat().setAutoRetaliate(!player.getCombat().autoRetaliate());
            break;

		case DESTROY_ITEM:
			final int item = player.getDestroyItem();
			player.getPacketSender().sendInterfaceRemoval();
			if (item != -1) {
				player.getInventory().delete(item, player.getInventory().getAmount(item));
			}
			break;

		case CANCEL_DESTROY_ITEM:
			player.getPacketSender().sendInterfaceRemoval();
			break;

		case AUTOCAST_BUTTON_1:
		case AUTOCAST_BUTTON_2:
			player.getPacketSender()
					.sendMessage("A spell can be autocast by simply right-clicking on it in your Magic spellbook and ")
					.sendMessage("selecting the \"Autocast\" option.");
			break;

		case TOGGLE_EXP_LOCK:
			player.setExperienceLocked(!player.experienceLocked());
			if (player.experienceLocked()) {
				player.getPacketSender().sendMessage("Your experience is now @red@locked.");
			} else {
				player.getPacketSender().sendMessage("Your experience is now @red@unlocked.");
			}
			break;

		case CLOSE_BUTTON_1:
		case CLOSE_BUTTON_2:
			player.getPacketSender().sendInterfaceRemoval();
			break;

		case FIRST_DIALOGUE_OPTION_OF_FIVE:
	        player.setDialogueAction(4, 1);
            DialogueHandler.proceed(player);
			break;
		case SECOND_DIALOGUE_OPTION_OF_FIVE:
	        player.setDialogueAction(4, 2);
            DialogueHandler.proceed(player);
			break;
		case THIRD_DIALOGUE_OPTION_OF_FIVE:
	        player.setDialogueAction(4, 3);
            DialogueHandler.proceed(player);
			break;
		case FOURTH_DIALOGUE_OPTION_OF_FIVE:
	        player.setDialogueAction(4, 4);
            DialogueHandler.proceed(player);
			break;
		case FIFTH_DIALOGUE_OPTION_OF_FIVE:
	        player.setDialogueAction(4, 5);
            DialogueHandler.proceed(player);
			break;

			
			
			
			
		case FIRST_DIALOGUE_OPTION_OF_FOUR:
            player.setDialogueAction(3, 1);
            DialogueHandler.proceed(player);
			break;
		case SECOND_DIALOGUE_OPTION_OF_FOUR:
            player.setDialogueAction(3, 2);
            DialogueHandler.proceed(player);
			break;
		case THIRD_DIALOGUE_OPTION_OF_FOUR:
            player.setDialogueAction(3, 3);
            DialogueHandler.proceed(player);
			break;
		case FOURTH_DIALOGUE_OPTION_OF_FOUR:
            player.setDialogueAction(3, 4);
            DialogueHandler.proceed(player);
			break;
			
			
			
			
		case FIRST_DIALOGUE_OPTION_OF_THREE:
            player.setDialogueAction(2, 1);
            DialogueHandler.proceed(player);
			break;
		case SECOND_DIALOGUE_OPTION_OF_THREE:
            player.setDialogueAction(2, 2);
            DialogueHandler.proceed(player);
			break;
        case THIRD_DIALOGUE_OPTION_OF_THREE:
            player.setDialogueAction(2, 3);
            DialogueHandler.proceed(player);
			break;

			
			
		case FIRST_DIALOGUE_OPTION_OF_TWO:
            player.setDialogueAction(1, 1);
            DialogueHandler.proceed(player);
            break;
		case SECOND_DIALOGUE_OPTION_OF_TWO:
            player.setDialogueAction(1, 2);
            DialogueHandler.proceed(player);
            break;
            
		default:
			// player.getPacketSender().sendMessage("Player "+player.getUsername()+", click
			// button: "+button);
			break;
		}
	}
}
