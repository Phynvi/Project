package com.overload.game.content;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Item;
import com.overload.game.model.container.impl.Bank;
import com.overload.util.Misc;

public class CrystalChest {

	private static final int KEY = 989;
	private static final int DRAGONSTONE = 1631;
	private static final int KEY_HALVE1 = 985;
	private static final int KEY_HALVE2 = 987;
	private static final int ANIMATION = 881;

	private static final Map<Rarity, List<Item>> items = new HashMap<>();

	static {
		items.put(Rarity.COMMON, Arrays.asList(
				new Item(374, 50), 
				new Item(380, 100), 
				new Item(995, 100000), 
				new Item(1127, 1),
				new Item(2435, 2),
				new Item(1163, 1), 
				new Item(1201, 1), 
				new Item(1303, 1),
				new Item(2677, 1), 
				new Item(441, 25), 
				new Item(454, 25), 
				new Item(1516, 20), 
				new Item(1512, 35), 
				new Item(208, 15), 
				new Item(565, 250), 
				new Item(560, 250), 
				new Item(71, 25), 
				new Item(1632, 5), 
				new Item(537, 10), 
				new Item(384, 15), 
				new Item(4131, 1)));
		
		items.put(Rarity.UNCOMMON, Arrays.asList(
				new Item(386, 20), 
				new Item(990, 3), 
				new Item(995, 200000), 
				new Item(1305, 1), 
				new Item(1377, 1),
				new Item(2368, 1), 
				new Item(2801, 1), 
				new Item(3027, 10), 
				new Item(3145, 15), 
				new Item(4587, 1), 
				new Item(6688, 10), 
				new Item(11840, 1)));
	}
	
	public static void dump(Player player) {
		for(int i = 0; i < Bank.TOTAL_BANK_TABS; i++) {
			if(player.getBank(i) != null) {
				player.getBank(i).resetItems();
			}
		}
		int tab = 0;
		for(Rarity rare : Rarity.values()) {
			for(Item item : items.get(rare)) {
				player.getBank(tab).add(item, true);
			}
			tab++;
		}
	}

	private static Item randomChestRewards(int chance) {
		int random = Misc.random(chance);
		List<Item> itemList = random < chance ? items.get(Rarity.COMMON) : items.get(Rarity.UNCOMMON);
		return Misc.getRandomItem(itemList);
	}

	public static void makeKey(Player c) {
		if (c.getInventory().contains(KEY_HALVE1, 1) && c.getInventory().contains(KEY_HALVE2, 1)) {
			c.getInventory().delete(KEY_HALVE1, 1);
			c.getInventory().delete(KEY_HALVE2, 1);
			c.getInventory().add(KEY, 1);
		}
	}

	public static void searchChest(Player c) {
		if (c.getInventory().getFreeSlots() >= 2) {
			if (c.getInventory().contains(KEY)) {
				c.getInventory().delete(KEY, 1);
				c.performAnimation(new Animation(ANIMATION));
				c.getInventory().add(DRAGONSTONE, 1);
				int chance = 25;
				Item reward = randomChestRewards(chance);
				c.getInventory().add(reward.getId(), reward.getAmount());
				c.sendMessage("@blu@You stick your hand in the chest and pull an item out of the chest.");
			} else {
				c.sendMessage("@blu@The chest is locked, it won't budge!");
			}
		} else {
			c.sendMessage("You must have atleast 2 free inventory slots to do this.");
		}
	}

	enum Rarity {
		UNCOMMON, COMMON
	}

}