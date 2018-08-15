package com.overload.game.content.skill.herblore;

import java.util.Arrays;
import java.util.Optional;

import com.overload.game.content.skill.skillable.impl.ItemCreationSkillable;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.AnimationLoop;
import com.overload.game.model.Item;
import com.overload.game.model.RequiredItem;
import com.overload.game.model.Skill;
import com.overload.game.model.menu.CreationMenu;
import com.overload.game.model.menu.CreationMenu.CreationMenuAction;
import com.overload.game.model.menu.impl.SingleItemCreationMenu;
import com.overload.util.ItemIdentifiers;

public class UnfCreator extends ItemIdentifiers {

	public enum UnfinishedPotions {
		GUAM_POTION(new Item(91), new Item(249), 1), //Attack potion
		MARRENTILL_POTION(new Item(93), new Item(251), 5), //Antipoison
		TARROMIN_POTION(new Item(95), new Item(253), 12), //Strength potion
		HARRALANDER_POTION(new Item(97), new Item(255), 22), //Restore potion, Energy potion & Combat potion
		RANARR_POTION(new Item(99), new Item(257), 30), //Prayer potion
		TOADFLAX_POTION(new Item(3002), new Item(2998), 34), //Agility potion & Saradomin brew
		IRIT_POTION(new Item(101), new Item(259), 45), //Super attack & Fishing potion
		AVANTOE_POTION(new Item(103), new Item(261), 50), //Super energy
		KWUARM_POTION(new Item(105), new Item(263), 55), //Super strength & Weapon poison
		SNAPDRAGON_POTION(new Item(3004), new Item(3000), 63), //Super restore
		CADANTINE_POTION(new Item(107), new Item(265), 66), //Super defence
		LANTADYME(new Item(2483), new Item(2481), 69), //Antifire, Magic potion
		DWARF_WEED_POTION(new Item(109), new Item(267), 72), //Ranging potion
		TORSTOL_POTION(new Item(111), new Item(269), 78); //Zamorak brew
		
		
		private final Item potion, ingredient;
		private int levelReq;
		
		private UnfinishedPotions(Item potion, Item ingredient, int levelReq) {
			this.potion = potion;
			this.ingredient = ingredient;
			this.levelReq = levelReq;
		}
		
		public Item getPotion() {
			return potion;
		}
		
		public Item getHerb() {
			return ingredient;
		}
		
		public int getLevelReq() {
			return levelReq;
		}
		
		public static UnfinishedPotions forId(int i) {
			for(UnfinishedPotions unf : UnfinishedPotions.values()) {
				if (unf.getHerb().getId() == i) {
					return unf;
				}
			}
			return null;
		}
	}
	
	public static boolean make(final Player player, int itemUsed, int usedWith) {
        if (itemUsed == VIAL_OF_WATER || usedWith == VIAL_OF_WATER) {
	            int herb = itemUsed == VIAL_OF_WATER ? usedWith : itemUsed;
			final UnfinishedPotions unf = UnfinishedPotions.forId(herb);
			if (unf != null) {
				CreationMenu menu = new SingleItemCreationMenu(player, unf.getPotion().getId(), "How many potions would you like to make?", new CreationMenuAction() {
	                @Override
	                public void execute(int item, int amount) {
	                    ItemCreationSkillable skillable = new ItemCreationSkillable(
	                    		Arrays.asList(new RequiredItem(
	                    				new Item(VIAL_OF_WATER), true),
	                    				new RequiredItem(unf.getHerb(), true)
	                    		),
	                            unf.getPotion(),
	                            amount,
	                            Optional.of(new AnimationLoop(HerbConstants.ANIMATION, 4)),
	                            unf.getLevelReq(), 10, Skill.HERBLORE);
	                    player.getSkillManager().startSkillable(skillable);
	                }
	            }).open();
	            player.setCreationMenu(Optional.of(menu));
	            return true;
			}
        }
		return false;
	}

}
