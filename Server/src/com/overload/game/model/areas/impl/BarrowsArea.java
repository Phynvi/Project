package com.overload.game.model.areas.impl;

import com.overload.game.content.minigames.barrows.Barrows;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Boundary;
import com.overload.game.model.areas.Area;

import java.util.Arrays;
import java.util.Optional;

public class BarrowsArea extends Area {

    public BarrowsArea() {
        super(Arrays.asList(new Boundary(3521, 3582, 9662, 9724), new Boundary(3545, 3583, 3265, 3306, true), new Boundary(3525, 9675, 3586, 9724, 3)));
    }

    @Override
    public void enter(Character character) {
        if (character.isPlayer()) {
            Player player = character.getAsPlayer();
            player.getPacketSender().sendWalkableInterface(Barrows.KILLCOUNTER_INTERFACE_ID);
            Barrows.updateInterface(player);
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
            Barrows.brotherDeath(player, character.getAsNpc());
        }
    }

    @Override
    public boolean handleObjectClick(Player player, int objectId, int type) {
        return Barrows.handleObject(player, objectId);
    }

	@Override
	public boolean isWilderness() {
		return false;
	}
}
