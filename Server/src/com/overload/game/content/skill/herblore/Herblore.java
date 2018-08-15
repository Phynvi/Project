package com.overload.game.content.skill.herblore;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.AnimationLoop;
import com.overload.game.model.Item;
import com.overload.game.model.RequiredItem;
import com.overload.game.model.Skill;
import com.overload.game.model.menu.CreationMenu;
import com.overload.game.model.menu.CreationMenu.CreationMenuAction;
import com.overload.game.model.menu.impl.SingleItemCreationMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.overload.game.content.skill.herblore.PotionData.FinishedPotions;
import com.overload.game.content.skill.skillable.impl.ItemCreationSkillable;

public class Herblore {

	/**
	 * A {@link Set} of all {@link Herb} elements from it's respective enumeration.
	 */
	private static final Set<Herb> HERBS = Collections.unmodifiableSet(EnumSet.allOf(Herb.class));

	/**
	 * A {@link Set} of all {@link Potion} elements from it's respective enumeration.
	 */
	private static final Set<FinishedPotions> FINISHED = Collections.unmodifiableSet(EnumSet.allOf(PotionData.FinishedPotions.class));

	/**
	 * Cleans a single her
	 * 
	 * @param itemId the herb attempting to be cleaned
	 * @return 
	 */
	public static boolean clean(Player player, int itemId) {
		Optional<Herb> herb = HERBS.stream().filter(h -> h.getGrimy() == itemId).findFirst();
		if (herb.isPresent()) {
			herb.ifPresent(h -> {
		        if (h != null) {
		            if (player.getInventory().contains(h.getGrimy())) {
		                if (player.getSkillManager().getCurrentLevel(Skill.HERBLORE) < h.getLevel()) {
		                    player.getPacketSender().sendMessage("You need a Herblore level of at least " + h.getLevel() + " to clean this leaf.");
		                } else {
		                    if (player.getClickDelay().elapsed(150)) {
		                        player.getInventory().delete(h.getGrimy(), 1);
		                        player.getInventory().add(h.getClean(), 1);
		                        player.getSkillManager().addExperience(Skill.HERBLORE, h.getExperience());
		                        player.getPacketSender().sendMessage("You clean the dirt off the leaf.");
		                        player.getClickDelay().reset();
		                    }
		                }
		            }
		        }
			});
			return true;
		}
		return false;
	}

	public static boolean make(Player player, int primary) {
		Optional<FinishedPotions> potion = FINISHED.stream().filter(p -> p.getPrimary().getId() == primary && containsSecondaries(player, p)).findFirst();
		if (potion.isPresent()) {
			potion.ifPresent(p -> {
	            int result = p.getResult().getId();
	            CreationMenu menu = new SingleItemCreationMenu(player, result, "How many potions would you like to make?", new CreationMenuAction() {
	                @Override
	                public void execute(int item, int amount) {
	                    ItemCreationSkillable skillable = new ItemCreationSkillable(
                    		getAllSecondaries(player, p),
		                    new Item(result),
		                    amount,
		                    Optional.of(new AnimationLoop(HerbConstants.ANIMATION, 4)),
		                    p.getLevel(),
		                    p.getExperience(), Skill.HERBLORE
	                    );
	                    player.getSkillManager().startSkillable(skillable);
	                }
	            }).open();
	            player.setCreationMenu(Optional.of(menu));
			});
			return true;
		}
		return false;
	}
	
	/**
	 * Determines if the player has all of the ingredients required for the potion.
	 * 
	 * @param p the potion we're determining this for
	 * @return {@code true} if we have all of the ingredients, otherwise {@code false}
	 */
	private static boolean containsSecondaries(Player player, FinishedPotions p) {
		int required = p.getIngredients().length;
		for (Item ingredient : p.getIngredients()) {
			if (player.getInventory().contains(ingredient.getId(), ingredient.getAmount())) {
				required--;
			}
		}
		return required == 0;
	}
	
	private static List<RequiredItem> getAllSecondaries(Player player, FinishedPotions p) {
		List<RequiredItem> list = new ArrayList<RequiredItem>();

		list.add(new RequiredItem(p.getPrimary(), true));
		for (Item ingredient : p.getIngredients()) {
			if (player.getInventory().contains(ingredient.getId(), ingredient.getAmount())) {
				list.add(new RequiredItem(ingredient, true));
			}
		}
		return list;
	}

	public void crushItem(int itemid) {

	}
}