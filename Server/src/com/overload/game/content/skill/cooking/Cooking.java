package com.overload.game.content.skill.cooking;

import com.overload.game.content.skill.cooking.Cookable.Fish;
import com.overload.game.content.skill.cooking.Cookable.Pizza;
import com.overload.game.content.skill.cooking.Cookable.Pie;
import com.overload.game.content.skill.skillable.impl.ItemCreationSkillable;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.object.ObjectManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.util.Misc;
import com.overload.util.ObjectIdentifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents the Cooking skill.
 *
 * @author Professor Oak
 */
public class Cooking extends ItemCreationSkillable {

    /**
     * The animation the player will perform
     */
    public static final Animation ANIMATION = new Animation(896);

    /**
     * The object we're cooking on.
     */
    private final GameObject object;

    /**
     * The {@link Cookable} we're going to cook.
     */
    private final Cookable cookable;

    public Cooking(GameObject object, Cookable cookable, int amount) {
    	super(
			getAllSecondaries(cookable),
			cookable.getResult(),
			amount,
			Optional.of(new AnimationLoop(ANIMATION, 4)), 
			cookable.getLevel(),
			cookable.getExperience(),
			Skill.COOKING
		);
        this.object = object;
        this.cookable = cookable;
    }
    
//    public Cooking(GameObject object, Fish cookable, int amount) {
//    	super(
//    		Arrays.asList(
//    			new RequiredItem(new Item(cookable.getRawItem()), true)
//    		),
//			new Item(cookable.getCookedItem()),
//			amount,
//			Optional.of(new AnimationLoop(ANIMATION, 4)), 
//			cookable.getLevelReq(),
//			cookable.getXp(),
//			Skill.COOKING
//		);
//        this.object = object;
//        this.cookable = cookable;
//    }

    /**
     * Get's the rate for burning or successfully cooking food.
     *
     * @param player Player cooking.
     * @param burnBonus   Consumables's enum.
     * @return Successfully cook food.
     */
    public static boolean success(Player player, int burnBonus, int levelReq, int stopBurn, boolean canBurn) {
        if (player.getSkillManager().getCurrentLevel(Skill.COOKING) >= stopBurn || !canBurn) {
            return true;
        }
        double burn_chance = (45.0 - burnBonus);
        double cook_level = player.getSkillManager().getCurrentLevel(Skill.COOKING);
        double lev_needed = (double) levelReq;
        double burn_stop = (double) stopBurn;
        double multi_a = (burn_stop - lev_needed);
        double burn_dec = (burn_chance / multi_a);
        double multi_b = (cook_level - lev_needed);
        burn_chance -= (multi_b * burn_dec);
        double randNum = Misc.getRandomDouble() * 100.0;
        return burn_chance <= randNum;
    }

    @Override
    public void finishedCycle(Player player) {
        //Decrement amount left to cook..
    	
        decrementAmount();

        Fish fish = null;
        Pizza pizza = null;
        Pie pie = null;
        
        if (cookable.isFish()) {
        	fish = ((Cookable.Fish) cookable.get());
        }
        
        if (cookable.isPizza()) {
        	pizza = ((Cookable.Pizza) cookable.get());
        }
        
        if (cookable.isPie()) {
        	pie = ((Cookable.Pie) cookable.get());
        }
        
        if (fish != null) {
        	//Delete raw food..
	        player.getInventory().delete(fish.getRawItem(), 1);
	
	        //Add burnt or cooked item..
	        if (success(player, 3, fish.getLevelReq(), fish.getStopBurn(), true)) {
	            player.getInventory().add(fish.getCookedItem(), 1);
	            player.getPacketSender().sendMessage("You cook the " + ItemDefinition.forId(fish.getRawItem()).getName() + ".");
	            player.getSkillManager().addExperience(Skill.COOKING, fish.getXp());
	        } else {
	            player.getInventory().add(fish.getBurntItem(), 1);
	            player.getPacketSender().sendMessage("You burn the " + ItemDefinition.forId(fish.getRawItem()).getName() + ".");
	        }
        } else if (pizza != null) {
        	//Delete raw food..
	        player.getInventory().delete(pizza.getPrimary());
	        player.getInventory().delete(pizza.getIngredients());
	
	        //Add burnt or cooked item..
	        if (success(player, 3, pizza.getLevel(), pizza.getStopBurn(), pizza.needsFire())) {
	            player.getInventory().add(pizza.getResult());
	            player.getPacketSender().sendMessage("You cook the " + ItemDefinition.forId(pizza.getPrimary().getId()).getName() + ".");
	            player.getSkillManager().addExperience(Skill.COOKING, pizza.getExperience());
	        } else {
	            player.getInventory().add(pizza.getBurntItem());
	            player.getPacketSender().sendMessage("You burn the " + ItemDefinition.forId(pizza.getPrimary().getId()).getName() + ".");
	        }
        } else if (pie != null) {
        	//Delete raw food..
	        player.getInventory().delete(pie.getPrimary());
	        player.getInventory().delete(pie.getIngredients());
	
	        //Add burnt or cooked item..
	        if (success(player, 3, pie.getLevel(), pie.getStopBurn(), true)) {
	            player.getInventory().add(pie.getResult());
	            player.getPacketSender().sendMessage("You cook the " + ItemDefinition.forId(pie.getResult().getId()).getName() + ".");
	            player.getSkillManager().addExperience(Skill.COOKING, pie.getExperience());
	        } else {
	            player.getInventory().add(pie.getBurntItem());
	            player.getPacketSender().sendMessage("You burn the " + ItemDefinition.forId(pie.getResult().getId()).getName() + ".");
	        }
        }
    }

    @Override
    public boolean hasRequirements(Player player) {
        //If we're using a fire, make sure to check it's still there.
    	if (object != null)
	        if (object.getId() == ObjectIdentifiers.FIRE_5 && !ObjectManager.exists(ObjectIdentifiers.FIRE_5, object.getPosition())) {
	            return false;
	        }
        return super.hasRequirements(player);
        
//        if (cookable.isFish()) {
//        	return cookable.hasRequirements(player, true);
//        }
//        
//        if (cookable.isPizza()) {
//        	return cookable.hasRequirements(player, true);
//        }
//        
//        
//        
//        return false;
    }
    
    private static List<RequiredItem> getAllSecondaries(Cookable cook) {
		List<RequiredItem> list = new ArrayList<RequiredItem>();
    	if (cook.isPizza()) {
    		Pizza p = cook.getAsPizza();
			list.add(new RequiredItem(p.getPrimary(), true));
			for (Item ingredient : p.getIngredients()) {
				list.add(new RequiredItem(ingredient, true));
			}
    	} else if (cook.isFish()) {
    		Fish p = cook.getAsFish();
			list.add(new RequiredItem(new Item(p.getRawItem()), true));
    	}
		return list;
	}
}
