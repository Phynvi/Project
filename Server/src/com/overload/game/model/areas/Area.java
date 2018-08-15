package com.overload.game.model.areas;

import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Boundary;
import com.overload.game.model.Position;

import java.util.List;
import java.util.Optional;

public abstract class Area {

    private final List<Boundary> boundaries;

    public Area(List<Boundary> boundaries) {
        this.boundaries = boundaries;
    }
    
    public Area(List<Boundary> boundaries, boolean privateArea) {
        this.boundaries = boundaries;
    }

    public abstract void enter(Character character);

    public abstract void leave(Character character);

    public abstract void process(Character character);

    public abstract boolean canTeleport(Player player);

    public abstract boolean canAttack(Character attacker, Character target);

    public abstract void defeated(Player player, Character character);

    public abstract boolean canTrade(Player player, Player target);

    public abstract boolean isMulti(Character character);

    public abstract boolean canEat(Player player, int itemId);

    public abstract boolean canDrink(Player player, int itemId);

    public abstract boolean dropItemsOnDeath(Player player, Optional<Player> killer);

    public abstract boolean handleDeath(Player character, Optional<Player> killer);

    public abstract void onPlayerRightClick(Player player, Player rightClicked, int option);

    public abstract boolean handleObjectClick(Player player, int objectId, int type);

    public List<Boundary> getBoundaries() {
        return boundaries;
    }

	public abstract boolean isWilderness();

	public Position getLootDropPosition() { return null; }
}
