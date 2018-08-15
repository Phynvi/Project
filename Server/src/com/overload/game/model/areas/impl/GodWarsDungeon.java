package com.overload.game.model.areas.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.overload.game.content.minigames.godwars.GodWarsData;
import com.overload.game.content.minigames.godwars.GodWarsData.Allegiance;
import com.overload.game.content.minigames.godwars.GodWarsData.GodWarsNpc;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Boundary;
import com.overload.game.model.Position;
import com.overload.game.model.Skill;
import com.overload.game.model.areas.Area;
import com.overload.game.model.container.impl.Equipment;

public class GodWarsDungeon extends Area {

    public GodWarsDungeon(List<Boundary> boundaries) {
        super(boundaries);
    }

    @Override
    public boolean isWilderness() {
        return false;
    }

    public GodWarsDungeon() {
        super(Arrays.asList(new Boundary(2806, 5243, 2953, 5381, 0), new Boundary(2806, 5243, 2953, 5381, 1), new Boundary(2806, 5243, 2953, 5381, 2), new Boundary(2806, 5243, 2953, 5381, 3)));
    }

    @Override
    public void enter(Character character) {
        if (character.isPlayer()) {
            Player player = character.getAsPlayer();
            player.getPacketSender().sendWalkableInterface(65000);
            player.getGodwars().drawInterface();
        }
    }

    @Override
    public void leave(Character character) {
        if (character.isPlayer()) {
            character.getAsPlayer().getPacketSender().sendWalkableInterface(-1);
        }
    }

    @Override
    public void process(Character character) {
    }

    @Override
    public boolean canTeleport(Player player) {
        return true;
    }

    @Override
    public boolean canAttack(Character attacker, Character target) {
        if (attacker.isPlayer() && target.isPlayer()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canTrade(Player player, Player target) {
        return true;
    }

    @Override
    public boolean isMulti(Character character) {
        return true;
    }

    @Override
    public boolean canEat(Player player, int itemId) {
        return true;
    }

    @Override
    public boolean canDrink(Player player, int itemId) {
        return true;
    }

    @Override
    public boolean dropItemsOnDeath(Player player, Optional<Player> killer) {
        return true;
    }

    @Override
    public boolean handleDeath(Player player, Optional<Player> killer) {
        return false;
    }

    @Override
    public void onPlayerRightClick(Player player, Player rightClicked, int option) {
    }

    @Override
    public void defeated(Player player, Character character) {
        if (character.isNpc()) {
        	int id = character.getAsNpc().getId();
        	GodWarsNpc godNpc = GodWarsData.forId(id);
        	Allegiance god = godNpc.getAllegiance();
        	player.getGodwars().increaseKillcount(god);
        }
    }

    @Override
    public boolean handleObjectClick(Player player, int objectId, int type) {
    	if (objectId == 26518) {//Zamorak Ice Bridge
    		if (player.getSkillManager().getCurrentLevel(Skill.HITPOINTS) >= 70) {
	    		if (player.getPosition().getY() > 5337) {
	    			player.moveTo(new Position(2885, 5332, 2));
	    		} else {
	    			player.moveTo(new Position(2885, 5345, 2));
	    		}
    		} else {
    			player.getPacketSender().sendMessage("You need a hitpoints level of atleast 70 to do this.");
    		}
    		return true;
    	} else if (objectId == 26380) {//Armadyl Grapple Pillar
    		if (player.getSkillManager().getCurrentLevel(Skill.RANGED) >= 70) {
	    		if (player.getEquipment().get(Equipment.AMMUNITION_SLOT) != null && player.getEquipment().get(Equipment.AMMUNITION_SLOT).getDefinition() != null && player.getEquipment().get(Equipment.AMMUNITION_SLOT).getDefinition().getId() == 9419 &&
					player.getEquipment().get(Equipment.WEAPON_SLOT) 	 != null && player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition() 	 != null && player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition().getName() != null && player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition().getName().toLowerCase().contains("crossbow")) {
			    		if (player.getPosition().getY() > 5275) {
			    			player.moveTo(new Position(2872, 5269, 2));
			    		} else {
			    			player.moveTo(new Position(2872, 5279, 2));
			    		}
	    		} else {
	    			player.getPacketSender().sendMessage("You need a crossbow and a mithril grapple to get across there.");
	    		}
    		} else {
    			player.getPacketSender().sendMessage("You need a ranged level of atleast 70 to do this.");
    		}
    		return true;
    	} else if (objectId == 26561 || objectId == 26562) {
    		if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) >= 70) {
    			if (player.getInventory().contains(954)) {
		    		if (player.getPosition().getZ() == 2) {
		    			player.moveTo(new Position(2914, 5300, 1));
		    		} else if (player.getPosition().getZ() == 1) {
		    			player.moveTo(new Position(2920, 5274, 0));
		    		}
    			} else {
        			player.getPacketSender().sendMessage("You can't climb down there... i need a rope.");
    			}
    		} else {
    			player.getPacketSender().sendMessage("You need an agility level of atleast 70 to do this.");
    		}
    		return true;
    	}
    	return false;
    }
}

