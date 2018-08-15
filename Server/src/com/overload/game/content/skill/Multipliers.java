package com.overload.game.content.skill;

public enum Multipliers {
	ATTACK(			175),
    DEFENCE(		175),
    STRENGTH(		175),
    HITPOINTS(		175),
    RANGED(			175),
    PRAYER(			175),
    MAGIC(			1  ),
    COOKING(		150),
    WOODCUTTING(	150),
    FLETCHING(		150),
    FISHING(		150),
    FIREMAKING(		150),
    CRAFTING(		150),
    SMITHING(		150),
    MINING(			150),
    HERBLORE(		150),
    AGILITY(		150),
    THIEVING(		150),
    SLAYER(			150),
    FARMING(		150),
    RUNECRAFTING(	150),
    CONSTRUCTION(	150),
    HUNTER(			150);
	
	int multiplier;
	
	Multipliers(int multiplier) {
		this.multiplier = multiplier;
	}

	public int getMultiplier() {
		return multiplier;
	}
}
