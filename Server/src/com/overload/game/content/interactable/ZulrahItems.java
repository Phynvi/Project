package com.overload.game.content.interactable;

import java.text.DecimalFormat;

import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Item;
import com.overload.util.ItemIdentifiers;
import com.overload.util.Misc;

public class ZulrahItems extends ItemIdentifiers {
	
	public enum ZulrahItemType {
		BLOWPIPE
	};

	public final static int MAX_SCALES_BLOWPIPE = 16383;
	
	private final static int SCALES_FOR_DISMANTLE = 20000;
	
	public static void charge(Player player, Item used, Item usedWith, ZulrahItemType type) {
		if (type == ZulrahItemType.BLOWPIPE) {
			int reload = used.getId() == TOXIC_BLOWPIPE ? usedWith.getId() : used.getId() == TOXIC_BLOWPIPE_EMPTY ? usedWith.getId() : used.getId();
			int blowpipeType = used.getId() == TOXIC_BLOWPIPE ? used.getId() : used.getId() == TOXIC_BLOWPIPE_EMPTY ? used.getId() : usedWith.getId();
	        if (reload == ZULRAHS_SCALES) {
	            int amountScales = player.getInventory().getAmount(ZULRAHS_SCALES);
	            if (player.getBlowpipeScales() + amountScales > MAX_SCALES_BLOWPIPE) {
	            	amountScales = MAX_SCALES_BLOWPIPE - player.getBlowpipeScales();
	            }
	            if (amountScales <= 0) {
	            	player.getPacketSender().sendMessage("Your blowpipe is already 100% charged.");
	            	return;
	            }
	            player.setBlowpipeScales(amountScales);
	            player.getInventory().delete(ZULRAHS_SCALES, amountScales);
	            if (blowpipeType == TOXIC_BLOWPIPE_EMPTY) {
		            player.getInventory().delete(TOXIC_BLOWPIPE_EMPTY, 1);
		            player.getInventory().add(TOXIC_BLOWPIPE, 1);
	            }
	            String ammoName = player.getBlowpipeAmmoType() == 0 ? "None" : ItemDefinition.forId(player.getBlowpipeAmmoType()).getName();
	            player.getPacketSender().sendMessage("Darts: " + (ammoName == "None" ? "@red@" : "<col=00B400>") + "" + ammoName + " x " + Misc.format(player.getBlowpipeAmmoAmount()) + "@bla@. Scales: " + percentString(player));
	        } else if (ItemDefinition.forId(reload).getName().toLowerCase().contains(" dart") && !ItemDefinition.forId(reload).getName().toLowerCase().contains("(p")) {
	            if (blowpipeType == TOXIC_BLOWPIPE_EMPTY) {
	            	player.sendMessage("I should probably charge this first.");
	            	return;
	            }
	        	
	            int amountDarts = player.getInventory().getAmount(reload);
	            
	            if (player.getBlowpipeAmmoType() != reload && player.getBlowpipeAmmoType() != 0) {
	            	player.getPacketSender().sendMessage("Your blowpipe is currently using " + ItemDefinition.forId(player.getBlowpipeAmmoType()).getName());
	            	if (player.isDebug()) {
	            		player.getPacketSender().sendMessage("Current Ammo: " + player.getBlowpipeAmmoType());
	            	}
	            	return;
	            }
	            player.setBlowpipeAmmoAmount(player.getBlowpipeAmmoAmount() + amountDarts);
	            player.setBlowpipeAmmoType(reload);
	            player.getInventory().delete(reload, amountDarts);
	            String ammoName = player.getBlowpipeAmmoType() == 0 ? "None" : ItemDefinition.forId(player.getBlowpipeAmmoType()).getName();
	            player.getPacketSender().sendMessage("Darts: " + (ammoName == "None" ? "@red@" : "<col=00B400>") + "" + ammoName + " x " + Misc.format(player.getBlowpipeAmmoAmount()) + "@bla@. Scales: " + percentString(player));
	        } else {
	            player.getPacketSender().sendMessage("You cannot load the blowpipe with that!");
	        }
		}
	}

	public static void uncharge(Player player, int slot, ZulrahItemType type) {
		if (type == ZulrahItemType.BLOWPIPE) {
			Item delete = new Item(TOXIC_BLOWPIPE, 1);
			player.getInventory().delete(delete, slot, true);
			player.getInventory().add(new Item(TOXIC_BLOWPIPE_EMPTY, 1));
			player.getInventory().add(ZULRAHS_SCALES, player.getBlowpipeScales());
			player.getInventory().add(player.getBlowpipeAmmoType(), player.getBlowpipeAmmoAmount());
			player.sendMessage("You uncharge your Toxic Blowpipe and recieve " + Misc.format(player.getBlowpipeScales()) + " Zulrah Scales.");
		}
	}

	public static void dismantle(Player player, int slot, ZulrahItemType type) {
		if (type == ZulrahItemType.BLOWPIPE) {
			Item delete = new Item(TOXIC_BLOWPIPE_EMPTY, 1);
			player.getInventory().delete(delete, slot, true);
			player.getInventory().add(ZULRAHS_SCALES, SCALES_FOR_DISMANTLE);
			player.sendMessage("You dismantle the Toxic Blowpipe and recieve " + Misc.format(SCALES_FOR_DISMANTLE) + " Zulrah Scales.");
		}
	}
	
	public static String percentString(Player player) {
        Float percent = (float) ((player.getBlowpipeScales() / ZulrahItems.MAX_SCALES_BLOWPIPE) * 100);
        int colour = (int) ((player.getBlowpipeScales() / ZulrahItems.MAX_SCALES_BLOWPIPE) * 255);
        int green = colour;
        int red   = (255 - green);
        if (green >= 255) {
        	green = 255;
        	green -= 75;
        }
        String r = String.format("%X", red);
        String g = String.format("%X", green);
        String b = "00";

        if (green < 10) {
            g = "0" + String.format("%X", green);
        }
        if (red < 10) {
            r = "0" + String.format("%X", red);
        }
        
        if (player.isDebug()) {
        	player.sendMessage("Color HEX: R: " + r + " G: " + g + " B: " + b);
        }
        DecimalFormat df = new DecimalFormat("#.#");
        return "<col=" + r + g + b + ">" + df.format(percent) + "%";
	}
}
