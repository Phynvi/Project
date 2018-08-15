package com.overload.game.model;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.container.impl.Equipment;
import com.overload.util.Misc;

public class Starter {
	
	private static final int[] ITEMS_INV = {995,555,557,554,563,558,303,307,1351,1265,1381,882,841,577,579,1011,590,230,313,380,1733,1734,954,301,250}; 
	
	private static final int[] NORMAL_ITEMS_AMMOUNT =  {500000,150,150,150,150,150,1,1,1,1,1,1000,1,1,1,1,1,1,500,100,1,200,1,1,50};

	private static final int[] IRONMAN_ITEMS_AMMOUNT = {250000,150,150,150,100,150,1,1,1,1,1,500,1,1,1,1,1,50,250,50,1,200,1,1,25};
	
	private static final int[] HARDCORE_ITEMS_AMMOUNT = {250000,150,150,150,100,150,1,1,1,1,1,500,1,1,1,1,1,50,250,50,1,200,1,1,25};
	
	private static final int[] ULIMITE_ITEMS_AMMOUNT =  {250000,50,50,50,25,50,1,1,1,1,1,25,1,1,1,1,1,25,150,100,1,25,1,1,10};
	
	public String Mode = "Normal";
	
	public String getMode() {
		return Mode;
	}

	public void setMode(String mode) {
		Mode = mode;
	}

	public void openStarter(Player player) {
		if(player.getStarter() == -1) {
			player.getPacketSender().sendInterface(11000);
			updateInfo(player);
		}
	}
	
	public void updateInfo(Player player) {
		if(getMode() == "Normal") {
			player.getPacketSender().sendString(11020, "                          " + Misc.formatName(player.getUsername()));
			for (int index = 0; index < 25; index++) {
				player.getPacketSender().sendItemOnInterface(11021 + index, ITEMS_INV[index], NORMAL_ITEMS_AMMOUNT[index]);
			}
			if(player.getStarter() == -1) {
				player.getEquipment().setItem(Equipment.HEAD_SLOT, new Item(1153));
				player.getEquipment().setItem(Equipment.CAPE_SLOT, new Item(-1));
				player.getEquipment().setItem(Equipment.AMULET_SLOT, new Item(-1));
				player.getEquipment().setItem(Equipment.WEAPON_SLOT, new Item(1323));
				player.getEquipment().setItem(Equipment.BODY_SLOT, new Item(1115));
				player.getEquipment().setItem(Equipment.SHIELD_SLOT, new Item(1191));
				player.getEquipment().setItem(Equipment.LEG_SLOT, new Item(1067));
				player.getEquipment().setItem(Equipment.HANDS_SLOT, new Item(2902));
				player.getEquipment().setItem(Equipment.FEET_SLOT, new Item(4121));
				player.getEquipment().setItem(Equipment.RING_SLOT, new Item(-1));
			}
			player.getPacketSender().sendString(11046, "No Restrictions:");
			player.getPacketSender().sendString(11047, "@gre@ Can @whi@ Trade with Players");
			player.getPacketSender().sendString(11048, "@gre@ Can @whi@ Use Shops");
			player.getPacketSender().sendString(11049, "@gre@ Can @whi@ Use GE");
			player.getPacketSender().sendString(11050, "@gre@ Can @whi@ Play All Content");
			player.getPacketSender().sendString(11051, "");
			player.getPacketSender().sendString(11052, "");
			player.getPacketSender().sendString(11053, "");
			player.getPacketSender().sendString(11054, "");
			player.getPacketSender().sendString(11055, "");
			player.getPacketSender().sendString(11056, "");
			
		} if(getMode() == "Ironman") {
			player.getPacketSender().sendString(11020, "<img=502>               " + Misc.formatName(player.getUsername()));
			for (int index = 0; index < 25; index++) {
				player.getPacketSender().sendItemOnInterface(11021 + index, ITEMS_INV[index], IRONMAN_ITEMS_AMMOUNT[index]);
			}
			if(player.getStarter() == -1) {
				player.getEquipment().setItem(Equipment.HEAD_SLOT, new Item(12810));
				player.getEquipment().setItem(Equipment.CAPE_SLOT, new Item(-1));
				player.getEquipment().setItem(Equipment.AMULET_SLOT, new Item(-1));
				player.getEquipment().setItem(Equipment.WEAPON_SLOT, new Item(1323));
				player.getEquipment().setItem(Equipment.BODY_SLOT, new Item(12811));
				player.getEquipment().setItem(Equipment.SHIELD_SLOT, new Item(1191));
				player.getEquipment().setItem(Equipment.LEG_SLOT, new Item(12812));
				player.getEquipment().setItem(Equipment.HANDS_SLOT, new Item(2902));
				player.getEquipment().setItem(Equipment.FEET_SLOT, new Item(4121));
				player.getEquipment().setItem(Equipment.RING_SLOT, new Item(-1));
			}
			
			player.getPacketSender().sendString(11046, "Restrictions:");
			player.getPacketSender().sendString(11047, "@red@ Can't @whi@ Trade with Players");
			player.getPacketSender().sendString(11048, "@red@ Can't @whi@ Buy player Stock in Shops ");
			player.getPacketSender().sendString(11049, "@red@ Can't @whi@ Use GE");
			player.getPacketSender().sendString(11050, "@red@ Can't @whi@ Pick up items on floor that aint yours");
			player.getPacketSender().sendString(11051, "");
			player.getPacketSender().sendString(11052, "");
			player.getPacketSender().sendString(11053, "");
			player.getPacketSender().sendString(11054, "");
			player.getPacketSender().sendString(11055, "");
			player.getPacketSender().sendString(11056, "");
			
		} if(getMode() == "Hardcore") {
			player.getPacketSender().sendString(11020, "<img=504>               " + Misc.formatName(player.getUsername()));
			for (int index = 0; index < 25; index++) {
				player.getPacketSender().sendItemOnInterface(11021 + index, ITEMS_INV[index], HARDCORE_ITEMS_AMMOUNT[index]);
			}
			if(player.getStarter() == -1) {
				player.getEquipment().setItem(Equipment.HEAD_SLOT, new Item(20792));
				player.getEquipment().setItem(Equipment.CAPE_SLOT, new Item(-1));
				player.getEquipment().setItem(Equipment.AMULET_SLOT, new Item(-1));
				player.getEquipment().setItem(Equipment.WEAPON_SLOT, new Item(1323));
				player.getEquipment().setItem(Equipment.BODY_SLOT, new Item(20794));
				player.getEquipment().setItem(Equipment.SHIELD_SLOT, new Item(1191));
				player.getEquipment().setItem(Equipment.LEG_SLOT, new Item(20796));
				player.getEquipment().setItem(Equipment.HANDS_SLOT, new Item(2902));
				player.getEquipment().setItem(Equipment.FEET_SLOT, new Item(4121));
				player.getEquipment().setItem(Equipment.RING_SLOT, new Item(-1));
			}
			player.getPacketSender().sendString(11046, "Restrictions:");
			player.getPacketSender().sendString(11047, "@red@ Can't @whi@ Trade with Players");
			player.getPacketSender().sendString(11048, "@red@ Can't @whi@ Buy player Stock in Shops ");
			player.getPacketSender().sendString(11049, "@red@ Can't @whi@ Use GE");
			player.getPacketSender().sendString(11050, "@red@ Can't @whi@ Pick up items on floor that aint yours");
			player.getPacketSender().sendString(11051, "@red@ ONLY ONE LIFE");
			player.getPacketSender().sendString(11052, "");
			player.getPacketSender().sendString(11053, "");
			player.getPacketSender().sendString(11054, "");
			player.getPacketSender().sendString(11055, "");
			player.getPacketSender().sendString(11056, "");
			
		} if(getMode() == "Ultimiate") {
			player.getPacketSender().sendString(11020, "<img=503>               " + Misc.formatName(player.getUsername()));
			for (int index = 0; index < 25; index++) {
				player.getPacketSender().sendItemOnInterface(11021 + index, ITEMS_INV[index], ULIMITE_ITEMS_AMMOUNT[index]);
			}
			if(player.getStarter() == -1) {
				player.getEquipment().setItem(Equipment.HEAD_SLOT, new Item(12813));
				player.getEquipment().setItem(Equipment.CAPE_SLOT, new Item(-1));
				player.getEquipment().setItem(Equipment.AMULET_SLOT, new Item(-1));
				player.getEquipment().setItem(Equipment.WEAPON_SLOT, new Item(1323));
				player.getEquipment().setItem(Equipment.BODY_SLOT, new Item(12814));
				player.getEquipment().setItem(Equipment.SHIELD_SLOT, new Item(1191));
				player.getEquipment().setItem(Equipment.LEG_SLOT, new Item(12815));
				player.getEquipment().setItem(Equipment.HANDS_SLOT, new Item(2902));
				player.getEquipment().setItem(Equipment.FEET_SLOT, new Item(4121));
				player.getEquipment().setItem(Equipment.RING_SLOT, new Item(-1));
			}
			
			
			player.getPacketSender().sendString(11046, "Restrictions:");
			player.getPacketSender().sendString(11047, "@red@ Can't @whi@ Trade with Players");
			player.getPacketSender().sendString(11048, "@red@ Can't @whi@ Buy player Stock in Shops ");
			player.getPacketSender().sendString(11049, "@red@ Can't @whi@ Use GE");
			player.getPacketSender().sendString(11050, "@red@ Can't @whi@ Pick up items on floor that aint yours");
			player.getPacketSender().sendString(11051, "@red@ ONLY ONE LIFE");
			player.getPacketSender().sendString(11052, "@red@ Can't @whi@ Use Banks");
			player.getPacketSender().sendString(11053, "");
			player.getPacketSender().sendString(11054, "");
			player.getPacketSender().sendString(11055, "");
			player.getPacketSender().sendString(11056, "");
		}
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		player.getEquipment().refreshItems();
		
	}
	
	public void handleButtonClick(int click, Player player) {
		switch(click) {
			case 11006:
				setMode("Normal");
			break;
			case 11009:
				setMode("Ironman");
			break;
			case 11012:
				setMode("Hardcore");
			break;
			case 11015:
				setMode("Ultimiate");
			break;
			case 11003:
				player.getPacketSender().sendInterfaceRemoval();
				player.setStarter(1);
				if(getMode() == "Normal") {
					player.setMode(PlayerModes.NORMAL);
					for (int index = 0; index < 25; index++) {
						player.getInventory().add(ITEMS_INV[index], NORMAL_ITEMS_AMMOUNT[index]);
					}
				} if(getMode() == "Ironman") {
					player.setMode(PlayerModes.IRON_MAN);
					for (int index = 0; index < 25; index++) {
						player.getInventory().add(ITEMS_INV[index], IRONMAN_ITEMS_AMMOUNT[index]);
					}
				} if(getMode() == "Hardcore") {
					player.setMode(PlayerModes.HARD_CORE_IRONMAN);
					for (int index = 0; index < 25; index++) {
						player.getInventory().add(ITEMS_INV[index], HARDCORE_ITEMS_AMMOUNT[index]);
					}
				} if(getMode() == "Ultimiate") {
					player.setMode(PlayerModes.ULTIMATE);
					for (int index = 0; index < 25; index++) {
						player.getInventory().add(ITEMS_INV[index], ULIMITE_ITEMS_AMMOUNT[index]);
					}
				}
			break;
		}	
		updateInfo(player);
		player.getEquipment().refreshItems();
	}

}
