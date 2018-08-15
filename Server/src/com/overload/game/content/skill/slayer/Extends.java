package com.overload.game.content.skill.slayer;

public enum Extends {
	NEED_MORE_DARKNESS(100),
	ANKOU_VERY_MUCH(100),
	SUQ_A_NOTHER_ONE(100),
	FIRE_AND_DARKNESS(50),
	PEDAL_TO_THE_METAL(100),
	I_REALLY_MITH_YOU(120),
	ADAMIND_SOME_MORE(100),
	RUUUUUNE(100),
	SPIRITUAL_FERVOUR(100),
	BIRDS_OF_A_FEATHER(100),
	GREATER_CHALLENGE(100),
	ITS_DARK_IN_HERE(100),
	BLEED_ME_DRY(75),
	SMELL_YA_LATER(100),
	HORRORIFIC(100),
	TO_DUST_YOU_SHALL_RETURN(100),
	WYVER_NOTHER_ONE(100),
	GET_SMASHED(100),
	NECHS_PLEASE(100),
	AUGMENT_MY_ABBIES(100),
	KRACK_ON(100),
	GET_SCABARIGHT_ON_IT(50),
	WYVER_NOTHER_TWO(100);
	
	private int points;
	
	Extends(int points) {
		this.points = points;
	}
	
	public int getPoints() {
		return points;
	}
}
