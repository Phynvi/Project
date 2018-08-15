package com.overload.game.content.combat.magic.spells;

import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Item;
import com.overload.game.model.Skill;

import java.util.HashMap;

public class BoltEnchanting {

	public enum BoltData {
		OPAL(879, 9236, 4, new Item[] { new Item(564, 1), new Item(556, 2)}),
		SAPPHIRE(9337, 9240, 7, new Item[] { new Item(564, 1), new Item(555, 1), new Item(558, 1) }),
		JADE(9335, 9237, 14, new Item[] { new Item(564, 1), new Item(557, 2) }),
		PEARL(880, 9238, 24, new Item[] { new Item(564, 1), new Item(555, 2) }),
		EMERALD(9338, 9241, 27, new Item[] { new Item(564, 1), new Item(556, 3), new Item(561, 1) }),
		RED_TOPAZ(9336, 9239, 29, new Item[] { new Item(564, 1), new Item(554, 2) }),
		RUBY(9339, 9242, 49, new Item[] { new Item(564, 1), new Item(554, 5), new Item(565, 1) }),
		DIAMOND(9340, 9243, 57, new Item[] { new Item(564, 1), new Item(557, 10), new Item(563, 2) }),
		DRAGONSTONE(9341, 9244, 68, new Item[] { new Item(564, 1), new Item(557, 15), new Item(566, 1) }),
		ONYX(9342, 9245, 87, new Item[] { new Item(564, 1), new Item(554, 20), new Item(560, 1) });
	
	private final int bolt;
	private final int enchantedBolt;
	private final int levelRequired;
	private final Item[] runesRequired;

	  BoltData(int bolt, int enchantedBolt, int levelRequired, Item...runesRequired) {
		this.bolt = bolt;
		this.enchantedBolt = enchantedBolt;
		this.levelRequired = levelRequired;
		this.runesRequired = runesRequired;
	}
		
	public int getBolt() {
		return bolt;
	}
	
	public int getEnchanted() {
		return enchantedBolt;
	}
	
	public int getLevel() {
		return levelRequired;
	}
	
	public Item[] getRunes() {
		return runesRequired;
	}
	
	private static final HashMap<Integer, BoltData> boltMap = new HashMap<Integer, BoltData>();

	static {
		for (final BoltData bolts : BoltData.values()) {
			BoltData.boltMap.put(bolts.enchantedBolt, bolts);
			}
		}
	}
	
	public static void handle(Player player, int itemId) {
		BoltData data = BoltData.boltMap.get(itemId);

		if (data == null) {
			return;
		}
		
//		if (player.getInterfaceManager().main != 42750) {
//			player.send(new SendRemoveInterfaces());
//			return;
//		}
		
		String boltName = ItemDefinition.forId(data.getBolt()).getName();
		
		if (player.getSkillManager().getCurrentLevel(Skill.MAGIC) < data.getLevel()) {
			player.sendMessage("@red@You need a Magic level of " + data.getLevel() + " to enchant " + boltName + " bolts.");
			return;
		}
		
		if (!player.getInventory().contains(data.getBolt(), 10)) {
			player.sendMessage("@red@You need 10 " + boltName + " to do this!");
			return;
		}
		
		if (!player.getInventory().contains(data.getRunes())) {
			player.sendMessage("@red@You do not have the required runes to do this!");
			return;
		}
		
		for (int index = 0; index < data.getRunes().length; index++) {
			player.getInventory().delete(data.getRunes()[index]);
		}
		
		player.getInventory().delete(data.getBolt(), 10);
		player.getInventory().add(data.getEnchanted(), 10);
		player.sendMessage("@red@You have enchanted 10 " + boltName + (boltName.endsWith("bolts") ? "." : " bolts."));
		
		String color = "@red@";
		
		for (int i = 0; i < runes.length; i++) {
			if (!player.getInventory().contains(runes[i][0], runes[i][1])) {
				color = "@red@";
			} else {
				color = "@gre@";
			}
			int amount = player.getInventory().getAmount(runes[i][0]);
			
			player.getPacketSender().sendString(color + (amount >= runes[i][1] ? runes[i][1] + "" : amount) + "/" + runes[i][1], 42766 + i);
		}
		
		player.getSkillManager().addExperience(Skill.MAGIC, 25);
		
	}
	
	private static int[] levels = {4, 7, 14, 24, 27, 29, 49, 57, 68, 87 };
	
	private static int[][] runes = { { 564, 1 }, { 556, 2 }, { 564, 1 }, { 555, 1 },	{ 558, 1 }, { 564, 1 }, { 557, 2 }, { 564, 1 }, { 555, 2 }, { 564, 1 }, { 556, 3 },	{ 561, 1 }, { 564, 1 }, { 554, 2 }, { 564, 1 }, { 554, 5 }, { 565, 1 }, { 564, 1 }, { 557, 10 }, { 563, 2 }, { 564, 1 }, { 557, 15 }, { 566, 1 }, { 564, 1 }, { 554, 20 }, { 560, 1 }	};
	
	private static Item[] item = {
	        new Item(9236, 15),
            new Item(9240, 15),
            new Item(9237, 15),
            new Item(9238, 15),
            new Item(9241, 15),
            new Item(9239, 15),
            new Item(9242, 15),
            new Item(9243, 15),
            new Item(9244, 15),
            new Item(9245, 15)
	};

	public static void open(Player player) {
		String color = "@red@";
		for (int i = 0; i < item.length; i++) {
			player.getPacketSender().sendSmithingData(item[i].getId(), 0, 42753+i, item[i].getAmount());
		}
		for (int i = 0; i < levels.length; i++) {
			if (player.getSkillManager().getCurrentLevel(Skill.MAGIC) < levels[i]) {
				color = "@red@";
			} else {
				color = "@gr3@";
			}
			player.getPacketSender().sendString(color + "Magic " + levels[i], 42763 + i);
		}
		for (int i = 0; i < runes.length; i++) {
			if (!player.getInventory().contains(runes[i][0], runes[i][1])) {
				color = "@red@";
			} else {
				color = "@gre@";
			}
			int amount = player.getInventory().getAmount(runes[i][0]);
			
			player.getPacketSender().sendString(color + (amount >= runes[i][1] ? runes[i][1] + "" : amount) + "/" + runes[i][1], 42773 + i);
		}
		player.getPacketSender().sendInterface(42750);
	}

}
