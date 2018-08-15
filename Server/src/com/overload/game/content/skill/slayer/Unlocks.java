package com.overload.game.content.skill.slayer;

public enum Unlocks {
	GARGOYLE_SMASHER(120),
	SLUG_SALTER(80),
	REPTILE_FREEZER(90),
	SHROOM_SPRAYER(110),
	BROADER_FLETCHING(300),
	MALEVOLENT_MASQUERADE(400),
	RING_BLING(300),
	SEEING_RED(50),
	I_HOPE_YOU_MITH_ME(80),
	WATCH_THE_BIRDIE(80),
	HOT_STUFF(100),
	REPTILE_GOT_RIPPED(75),
	LIKE_A_BOSS(200),
	KING_BLACK_BONNET(150),
	BIGGET_AND_BADDER(1000),
	KALPHITE_KHAT(1000),
	UNHOLY_HELMET(1000),
	DARK_MANTLE(1000),
	UNDEAD_HEAD(1000),
	DUTY_NOTED(200),
	STOP_THE_WYVERN(500);
	
	private int points;
	
	Unlocks(int points) {
		this.points = points;
	}
	
	public int getPoints() {
		return points;
	}
}
