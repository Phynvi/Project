package com.overload.game.model.areas.impl;

import java.util.Arrays;
import java.util.Optional;

import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Boundary;
import com.overload.game.model.areas.Area;

public class NightmareZoneLobby extends Area {

	public NightmareZoneLobby() {
	    super(Arrays.asList(new Boundary(2600, 2607, 3112, 3119)));
	}

	@Override
	public void enter(Character character) {//Show interface if player is in area
		if (character.isPlayer())
			character.getAsPlayer().getPacketSender().sendWalkableInterface(21119);
	}
	
	@Override
	public void leave(Character character) {//Remove interface if player leaves area
		character.getAsPlayer().getPacketSender().sendInterfaceRemoval();
	}
	
	@Override
	public void process(Character character) {//On every game tick if player is in area
	    
	}
	
	@Override
	public boolean canTeleport(Player player) {//you get the idea... ok so
	    return true;
	}
	
	@Override
	public boolean canAttack(Character attacker, Character target) {
	    return false;
	}
	
	@Override
	public void defeated(Player player, Character character) {
		
	}
	
	@Override
	public boolean canTrade(Player player, Player target) {
	    return true;
	}
	
	@Override
	public boolean isMulti(Character character) {
	    return false;
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
	    return false;
	}
	
	@Override
	public boolean handleDeath(Player player, Optional<Player> killer) {
	    return false;
	}
	
	@Override
	public void onPlayerRightClick(Player player, Player rightClicked, int option) {
	
	}
	
	@Override
	public boolean handleObjectClick(Player player, int objectId, int type) {
	    switch (objectId) {
	        default:
	        	return false;
	    }
	}
	
	@Override
	public boolean isWilderness() {
		return false;
	}

}
