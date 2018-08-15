package com.overload.game.model.areas.impl;

import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Boundary;

import java.util.Arrays;
import java.util.Optional;

public class FightCavesArea extends PrivateArea {

    public static final Boundary BOUNDARY = new Boundary(2368, 5056, 2431, 5119, true);

    public FightCavesArea() {
        super(Arrays.asList(BOUNDARY));
    }

    @Override
    public void process(Character character) {
    }

    @Override
    public boolean canTeleport(Player player) {
		player.getPacketSender().sendMessage("Teleport spells are blocked in these caves. Maybe theres an Exit!");
        return false;
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
        return false;
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
        return false;
    }

    @Override
    public boolean handleDeath(Player player, Optional<Player> killer) {
		player.getFightCave().handleDeath();
        return true;
    }

    @Override
    public void onPlayerRightClick(Player player, Player rightClicked, int option) {
    }

    @Override
    public void defeated(Player player, Character character) {
		player.getFightCave().leaveGame();
    }

    @Override
    public boolean handleObjectClick(Player player, int objectId, int type) {
        return false;
    }

	@Override
	public boolean isWilderness() {
		return false;
	}
}
