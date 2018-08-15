package com.overload.game.model.areas.impl;

import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Boundary;
import com.overload.game.model.areas.Area;

import java.util.Arrays;
import java.util.Optional;

public class CorpArea extends Area {

    public static final Boundary BOUNDARY = new Boundary(2972, 4370, 2999, 4397, 2);

    public CorpArea() {
        super(Arrays.asList(BOUNDARY));
    }


    @Override
    public void enter(Character character) {

    }

    @Override
    public void leave(Character character) {

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
        return true;
    }

    @Override
    public void defeated(Player player, Character character) {
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
    public boolean handleObjectClick(Player player, int objectId, int type) {
        return false;
    }

    @Override
    public boolean isWilderness() {
        return false;
    }
}
