package com.overload.cache.graphics.widget.impl;

import com.overload.cache.graphics.GameFont;
import com.overload.cache.graphics.widget.Widget;

public class MagicBooks extends Widget {

	/*Click a spell*/
	public static final int DEFAULT = 0;
	
	/*Use a spell on only ground items*/
	public static final int GROUND_ITEM = 1;
	
	/*Use a spell on only npcs*/
	public static final int NPC = 2;
	
	/*Use a spell on only objects*/
	public static final int OBJECT = 4;
	
	/*Use a spell on only players*/
	public static final int PLAYER = 8;
	
	/*Use a spell on players or npcs, combat related*/
	public static final int COMBAT = 10;
	
	/*Use a spell on only items*/
	public static final int ITEM = 16;
	
	/*Action type for a spell, will create a new tooltip for using the spell on objects, npcs, players, etc*/
	public static final int USE_SPELL_ON = 2;
	
	/*Action type for a spell, action is performed when the spell is clicked*/
	public static final int CLICK_SPELL = 5;
	
	/** SpellDataEnum	Format: 
	*	NAME(
			LEVEL, SPELL_TYPE, (USE_ON | CLICK), SPELL_NAME, SPELL_DESCRIPTION,
	*			RUNE_IDS,
	*			(POSITON | X | Y));
	*
	*	Any combination runes MUST come after the default elemental rune. 
	*
	*	Rune index format:
	*	0 		Main rune
	*	1-3 	Combination rune
	*	4		Catalytic / Elemental (If enabled)
	*	4+ 		Staffs (5+ if the above is enabled)
	*	
	*	If you want to add the elemental and catalytic runes from RS3, create a new enum value in the Item enum with their Id
	*		**Catalytic - non-elemental
	*
	*	Now go add the catalytic after each instance of the rune type it is,
	*	and for elemental ADD IT AFTER the combination runes but before the staffs.
	*	
	*	Go to addSpellButton | addSpellConfigString and increase "item > 4" to "item > 5"
	*
	*/
		
	public enum Item {
		
		FIRE(554), WATER(555), AIR(556), EARTH(557),
		
		MIND(558), BODY(559), COSMIC(564), NATURE(561), LAW(563),
		
		CHAOS(562), DEATH(560), BLOOD(565),
		
		SOUL(566), ASTRAL(9075),
		
		STEAM(4694), MIST(4695), DUST(4696), SMOKE(4697), MUD(4698), LAVA(4699),
		
		STAFF_OF_AIR(1381), 	STAFF_OF_WATER(1383), STAFF_OF_EARTH(1385), STAFF_OF_FIRE(1387),
		
		AIR_BATTLESTAFF(1397), 	WATER_BATTLESTAFF(1395), 	EARTH_BATTLESTAFF(1399), 	FIRE_BATTLESTAFF(1393),
		LAVA_BATTLESTAFF(3053), STEAM_BATTLESTAFF(11787), 	SMOKE_BATTLESTAFF(11998), 
		MIST_BATTLESTAFF(1397), DUST_BATTLESTAFF(1399), 	MUD_BATTLESTAFF(6562),
		
		MYSTIC_AIR_STAFF(1405),  MYSTIC_WATER_STAFF(1403), 	MYSTIC_EARTH_STAFF(1407),	 MYSTIC_FIRE_STAFF(1401), 
		MYSTIC_LAVA_STAFF(3054), MYSTIC_STEAM_STAFF(11789), MYSTIC_SMOKE_STAFF(12000), 
		MYSTIC_MIST_STAFF(1397), MYSTIC_DUST_STAFF(1399),	MYSTIC_MUD_STAFF(6563),
		
		SLAYER_STAFF(4170), 	IBAN_STAFF(1409), 
		SARADOMIN_STAFF(2415), 	GUTHIX_STAFF(2416), ZAMORAK_STAFF(2417), VOID_KNIGHT_MACE(8841), 
		
		BANANA(1963),
		UNPOWERED_ORB(567);
		
		private final int id;
		Item(int id) {
			this.id = (id);
		}
		
		public int getId() {
			return id;
		}
	}
	
	public enum Standard {
		//x, y = {start+widthOffset, start+heightOffset}
		LUMBRIDGE_HOME_TELEPORT(
			0, CLICK_SPELL, DEFAULT, "Lumbridge Home<br>Teleport", 
			"Requires no runes - recharge time<br>30 mins. Warning: This spell takes a<br>long time to cast and will be<br>interrupted by combat.", null,
			new int[] {14, 12}),
		WIND_STRIKE(
			1, USE_SPELL_ON, COMBAT, "Wind Strike", "A basic Air missile", new int[][] {
			{1, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.MIND.getId()}},
			new int[] {14+24, 12}),
		CONFUSE(
			3, USE_SPELL_ON, COMBAT, "Confuse", "Reduces your opponent's attack by<br>5%", new int[][] {
			{3, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{2, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{1, Item.BODY.getId()}},
			new int[] {14+48, 12}),
		ENCHANT_CROSSBOW_BOLT(
			4, CLICK_SPELL, DEFAULT, "Enchant Crossbow Bolt", "Minimum level 4 Magic, Multiple<br>other requirements", null,
			new int[] {14+72, 12}),
		WATER_STRIKE(
			5, USE_SPELL_ON, COMBAT, "Water Strike", "A basic Water missile", new int[][] {
			{1, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{1, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.MIND.getId()}},
			new int[] {14+96, 12}),
		ENCHANT_SAPPHIRE(
			7, USE_SPELL_ON, ITEM, "Lvl-1 Enchant", "For use on sapphire jewellery", new int[][] {
			{1, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{1, Item.COSMIC.getId()}},
			new int[] {14+120, 12}),
		EARTH_STRIKE(
			9, USE_SPELL_ON, COMBAT, "Earth Strike", "A basic Earth missile", new int[][] {
			{2, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{1, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.MIND.getId()}},
			new int[] {14+144, 12}),
		WEAKEN(
			11, USE_SPELL_ON, COMBAT, "Weaken", "Reduces your opponent's<br>strength by 5%", new int[][] {
			{3, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{2, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{1, Item.BODY.getId()}},
			new int[] {14, 12+24}),
		FIRE_STRIKE(
			13, USE_SPELL_ON, COMBAT, "Fire Strike", "A basic Fire missile", new int[][] {
			{3, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{2, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.MIND.getId()}},
			new int[] {14+24, 12+24}),
		BONES_TO_BANANAS(
			15, CLICK_SPELL, DEFAULT, "Bones to Bananas", "Changes all held bones into<br>bananas", new int[][] {
			{3, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{2, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{1, Item.NATURE.getId()}},
			new int[] {14+48, 12+24}),
		WIND_BOLT(
			17, USE_SPELL_ON, COMBAT, "Wind Bolt", "A low level Air missile", new int[][] {
			{2, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.CHAOS.getId()}},
			new int[] {14+72, 12+24}),
		CURSE(
			19, USE_SPELL_ON, COMBAT, "Curse", "Reduces your opponent's<br>defence by 5%", new int[][] {
			{2, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{3, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{1, Item.BODY.getId()}},
			new int[] {14+96, 12+24}),
		BIND(
			20, USE_SPELL_ON, COMBAT, "Bind", "Holds your opponent for 5 seconds", new int[][] {
			{3, Item.EARTH.getId(),
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{3, Item.WATER.getId()},
			{2, Item.NATURE.getId()}},
			new int[] {14+120, 12+24}),
		LOW_LEVEL_ALCHEMY(
			21, USE_SPELL_ON, ITEM, "Low Level Alchemy", "Converts an item into gold", new int[][] {
			{3, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{1, Item.NATURE.getId()}},
			new int[] {14+144, 12+24}),
		WATER_BOLT(
			23, USE_SPELL_ON, COMBAT, "Water Bolt", "A low level Water missile", new int[][] {
			{2, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{2, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.CHAOS.getId()}},
			new int[] {14, 12+48}),
		VARROCK_TELEPORT(
			25, CLICK_SPELL, DEFAULT, "Varrock Teleport", "Teleports you to Varrock", new int[][] {
			{1, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{3, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.LAW.getId()}},
			new int[] {14+24, 12+48}),
		ENCHANT_EMERALD(
			27, USE_SPELL_ON, ITEM, "Lvl-2 Enchant", "For use on emerald jewellery", new int[][] {
			{3, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.COSMIC.getId()}},
			new int[] {14+48, 12+48}),
		EARTH_BOLT(
			29, USE_SPELL_ON, COMBAT, "Earth Bolt", "A low level Earth missile", new int[][] {
			{3, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{2, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.CHAOS.getId()}},
			new int[] {14+72, 12+48}),
		LUMBRIDGE_TELEPORT(
			31, CLICK_SPELL, DEFAULT, "Lumbridge Teleport", "Teleports you to Lumbridge", new int[][] {
			{1, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{3, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.LAW.getId()}},
			new int[] {14+96, 12+48}),
		TELEKINETIC_GRAB(
			33, USE_SPELL_ON, GROUND_ITEM, "Telekinetic Grab", "Take an item you can see but can't<br>reach", new int[][] {
			{1, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.LAW.getId()}},
			new int[] {14+120, 12+48}),
		FIRE_BOLT(
			35, CLICK_SPELL, COMBAT, "Fire Bolt", "A low level Fire missile", new int[][] {
			{4, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{3, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.CHAOS.getId()}},
			new int[] {14+144, 12+48}),
		FALADOR_TELEPORT(
			37, CLICK_SPELL, DEFAULT, "Falador Teleport", "Teleports you to Falador", new int[][] {
			{1, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{3, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.LAW.getId()}},
			new int[] {14, 12+72}),
		CRUMBLE_UNDEAD(
			39, USE_SPELL_ON, COMBAT, "Crumble Undead", "Hits skeletons, ghosts, shades &<br>zombies hard", new int[][] {
			{2, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{2, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.CHAOS.getId()}},
			new int[] {14+24, 12+72}),
		TELEPORT_TO_HOUSE(
			40, CLICK_SPELL, DEFAULT, "Teleport to House", "Teleports you to your house", new int[][] {
			{1, Item.LAW.getId()},
			{1, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
			new int[] {14+48, 12+72}),
		WIND_BLAST(
			41, USE_SPELL_ON, COMBAT, "Wind Blast", "A medium level Air missile", new int[][] {
			{3, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.DEATH.getId()}},
			new int[] {14+72, 12+72}),
		SUPERHEAT_ITEM(
			43, USE_SPELL_ON, ITEM, "Superheat Item", "Smelt ore without a furnace", new int[][] {
			{4, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{1, Item.NATURE.getId()}},
			new int[] {14+96, 12+72}),
		CAMELOT_TELEPORT(
			45, CLICK_SPELL, DEFAULT, "Camelot Teleport", "Teleports you to Camelot", new int[][] {
			{5, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.LAW.getId()}},
			new int[] {14+120, 12+72}),
		WATER_BLAST(
			47, USE_SPELL_ON, COMBAT, "Water Blast", "A medium level Water missile", new int[][] {
			{3, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{3, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.DEATH.getId()}},
			new int[] {14+144, 12+72}),
		ENCHANT_RUBY(
			49, USE_SPELL_ON, ITEM, "Lvl-3 Enchant", "For use on ruby jewellery", new int[][] {
			{5, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{1, Item.COSMIC.getId()}},
			new int[] {14, 12+96}),
		IBAN_BLAST(
			50, USE_SPELL_ON, COMBAT, "Iban Blast", "Summons the wrath of Iban", new int[][] {
			{5, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{1, Item.DEATH.getId()},
			{1, Item.IBAN_STAFF.getId()}},
			new int[] {14+24, 12+96}),
		SNARE(
			50, USE_SPELL_ON, COMBAT, "Snare", "Holds your opponent for 10<br>seconds", new int[][] {
			{4, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{4, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{3, Item.NATURE.getId()}},
			new int[] {14+48, 12+96}),
		MAGIC_DART(
			50, USE_SPELL_ON, COMBAT, "Magic Dart", "A magic dart of slaying", new int[][] {
			{1, Item.SLAYER_STAFF.getId()},
			{1, Item.DEATH.getId()},
			{4, Item.MIND.getId()}},
			new int[] {14+72, 12+96}),
		ARDOUGNE_TELEPORT(
			51, CLICK_SPELL, DEFAULT, "Ardougne Teleport", "Teleports you to Ardougne", new int[][] {
			{2, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{2, Item.LAW.getId()}},
			new int[] {14+96, 12+96}),
		EARTH_BLAST(
			53, USE_SPELL_ON, COMBAT, "Earth Blast", "A medium level Earth missile", new int[][] {
			{4, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{3, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.DEATH.getId()}},
			new int[] {14+120, 12+96}),
		HIGH_LEVEL_ALCHEMY(
			55, USE_SPELL_ON, ITEM, "High Level Alchemy", "Converts an item into more gold", new int[][] {
			{5, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{1, Item.NATURE.getId()}},
			new int[] {14+144, 12+96}),
		CHARGE_WATER_ORB(
			56, USE_SPELL_ON, OBJECT, "Charge Water Orb", "Needs to be cast on a water<br>obelisk", new int[][] {
			{30,Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{3, Item.COSMIC.getId()},
			{1, Item.UNPOWERED_ORB.getId()}},
			new int[] {14, 12+120}),
		ENCHANT_DIAMOND(
			57, USE_SPELL_ON, ITEM, "Lvl-4 Enchant", "For use on diamond jewellery", new int[][] {
			{10,Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{1, Item.COSMIC.getId()}},
			new int[] {14+24, 12+120}),
		WATCHTOWER_TELEPORT(
			58, CLICK_SPELL, DEFAULT, "Watchtower Teleport", "Teleports you to the Watchtower", new int[][] {
			{2, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{2, Item.LAW.getId()}},
			new int[] {14+48, 12+120}),
		FIRE_BLAST(
			59, USE_SPELL_ON, COMBAT, "Fire Blast", "A medium level Fire missile", new int[][] {
			{5, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{4, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.DEATH.getId()}},
			new int[] {14+72, 12+120}),
		CHARGE_EARTH_ORB(
			60, USE_SPELL_ON, OBJECT, "Charge Earth Orb", "Needs to be cast on an earth<br>oblelisk", new int[][] {
			{30,Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{3, Item.COSMIC.getId()},
			{1, Item.UNPOWERED_ORB.getId()}},
			new int[] {14+96, 12+120}),
		BONES_TO_PEACHES(
			60, CLICK_SPELL, DEFAULT, "Bones to Peaches", "Turns Bones into Peaches", new int[][] {
			{2, Item.NATURE.getId()},
			{4, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{4, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
			new int[] {14+120, 12+120}),
		SARADOMIN_STRIKE(
			60, USE_SPELL_ON, COMBAT, "Saradomin Strike", "Summons the power of Saradomin", new int[][] {
			{2, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{2, Item.BLOOD.getId()},
			{4, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.SARADOMIN_STAFF.getId()}},
			new int[] {14+144, 12+120}),
		CLAWS_OF_GUTHIX(
			60, USE_SPELL_ON, COMBAT, "Claws of Guthix", "Summons the power of Guthix", new int[][] {
			{1, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{2, Item.BLOOD.getId()},
			{4, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.GUTHIX_STAFF.getId()}},
			new int[] {14, 12+144}),
		FLAMES_OF_ZAMORAK(
			60, USE_SPELL_ON, COMBAT, "Flames of Zamorak", "Summons the power of Zamorak", new int[][] {
			{4, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{2, Item.BLOOD.getId()},
			{1, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.ZAMORAK_STAFF.getId()}},
			new int[] {14+24, 12+144}),
		TROLLHEIM_TELEPORT(
			61, CLICK_SPELL, DEFAULT, "Trollheim Teleport", "Teleports you to Trollheim", new int[][] {
			{2, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{2, Item.LAW.getId()}},
			new int[] {14+48, 12+144}),
		WIND_WAVE(
			62, USE_SPELL_ON, COMBAT, "Wind Wave", "A high level Air missile", new int[][] {
			{5, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.BLOOD.getId()}},
			new int[] {14+72, 12+144}),
		CHARGE_FIRE_ORB(
			63, USE_SPELL_ON, OBJECT, "Charge Fire Orb", "Needs to be cast on a fire obelisk", new int[][] {
			{30,Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{3, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{1, Item.UNPOWERED_ORB.getId()}},
			new int[] {14+96, 12+144}),
		TELEPORT_TO_APE_ATOLL(
			64, CLICK_SPELL, DEFAULT, "Teleport to Ape Atoll", "Teleports you to Ape Atoll", new int[][] {
			{2, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{2, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{2, Item.LAW.getId()},
			{1, Item.BANANA.getId()}},
			new int[] {14+120, 12+144}),
		WATER_WAVE(
			65, USE_SPELL_ON, COMBAT, "Water Wave", "A high level Water missile", new int[][] {
			{7, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{5, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.BLOOD.getId()}},
			new int[] {14+144, 12+144}),
		CHARGE_AIR_ORB(
			66, USE_SPELL_ON, OBJECT, "Charge Air Orb", "Needs to be cast on an air obelisk", new int[][] {
			{30,Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{3, Item.COSMIC.getId()},
			{2, Item.UNPOWERED_ORB.getId()}},
			new int[] {14, 12+168}),
		VULNERABILITY(
			66, USE_SPELL_ON, COMBAT, "Vulnerability", "Reduces your opponent's defence<br>by 10%", new int[][] {
			{5, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{5, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{1, Item.SOUL.getId()}},
			new int[] {14+24, 12+168}),
		ENCHANT_DRAGONSTONE(
			68, USE_SPELL_ON, ITEM, "Lvl-5 Enchant", "For use on dragonstone jewellery", new int[][] {
			{15,Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{15,Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{1, Item.COSMIC.getId()}},
			new int[] {14+48, 12+168}),
		TELEPORT_TO_KOUREND(
			69, CLICK_SPELL, DEFAULT, "Teleport to Kourend", "Teleports you to Kourend", new int[][] {
			{2, Item.LAW.getId()},
			{2, Item.SOUL.getId()},
			{4, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{5, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
			new int[] {14+72, 12+168}),
		EARTH_WAVE(
			70, USE_SPELL_ON, COMBAT, "Earth Wave", "A high level Earth missile", new int[][] {
			{7, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{5, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.BLOOD.getId()}},
			new int[] {14+96, 12+168}),
		ENFEEBLE(
			73, USE_SPELL_ON, COMBAT, "Enfeeble", "Reduces your opponent's strength<br>by 10%", new int[][] {
			{8, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{8, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{1, Item.SOUL.getId()}},
			new int[] {14+120, 12+168}),
		TELEOTHER_LUMBRIDGE(
			74, USE_SPELL_ON, PLAYER, "Teleother Lumbridge", "Teleports target to Lumbridge", new int[][] {
			{1, Item.SOUL.getId()},
			{1, Item.LAW.getId()},
			{1, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
			new int[] {14+144, 12+168}),
		FIRE_WAVE(
			75, USE_SPELL_ON, COMBAT, "Fire Wave", "A high level Fire missile", new int[][] {
			{7, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{5, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
			{1, Item.BLOOD.getId()}},
			new int[] {14, 12+192}),
		ENTANGLE(
			79, USE_SPELL_ON, COMBAT, "Entangle", "Holds your opponent for 15<br>seconds", new int[][] {
			{5, Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{5, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{4, Item.NATURE.getId()}},
			new int[] {14+24, 12+192}),
		STUN(
			80, USE_SPELL_ON, COMBAT, "Stun", "Reduces your opponent's attack by<br>10%", new int[][] {
			{12,Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{12,Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{1, Item.SOUL.getId()}},
			new int[] {14+48, 12+192}),
		CHARGE(
			80, CLICK_SPELL, DEFAULT, "Charge", "Temporarily increases the power<br>of the three arena spells when<br>wearing Mage Arena capes", new int[][] {
			{3, Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{3, Item.BLOOD.getId()},
			{3, Item.AIR.getId(),
				Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
				Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()}},
			new int[] {14+72, 12+192}),
		TELEOTHER_FALADOR(
			82, USE_SPELL_ON, PLAYER, "Teleother Falador", "Teleports target to Falador", new int[][] {
			{1, Item.SOUL.getId()},
			{1, Item.WATER.getId(), 
				Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
				Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
				Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
			{1, Item.LAW.getId()}},
			new int[] {14+96, 12+192}),
		TELE_BLOCK(
			85, USE_SPELL_ON, PLAYER, "Tele Block", "Stops your target from teleporting", new int[][] {
			{1, Item.LAW.getId()},
			{1, Item.CHAOS.getId()},
			{1, Item.DEATH.getId()}},
			new int[] {14+120, 12+192}),
		TELEPORT_TO_BOUNTY_TARGET(
			85, CLICK_SPELL, DEFAULT, "Teleport to Bounty<br>Target", "Teleports you near your Bounty<br>Hunter target", new int[][] {
			{1, Item.LAW.getId()},
			{1, Item.DEATH.getId()},
			{1, Item.CHAOS.getId()}},
			new int[] {14+144, 12+192}),
		ENCHANT_ONYX(
			87, USE_SPELL_ON, ITEM, "Lvl-6 Enchant", "For use on onyx jewellery", new int[][] {
			{20,Item.EARTH.getId(), 
				Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
				Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
				Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
				Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{20,Item.FIRE.getId(), 
				Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
				Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
				Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
				Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
				Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
			{1, Item.COSMIC.getId()}},
			new int[] {14, 12+216}),
		TELEOTHER_CAMELOT(
			90, USE_SPELL_ON, PLAYER, "Teleother Camelot", "Teleports target to Camelot", new int[][] {
			{3, Item.SOUL.getId()},
			{1, Item.LAW.getId()}},
			new int[] {14+24, 12+216}),
		ENCHANT_ZENYTE(
			93, USE_SPELL_ON, ITEM, "Lvl-7 Enchant", "For use on zenyte jewellery", new int[][] {
			{20,Item.BLOOD.getId()},
			{20,Item.SOUL.getId()},
			{1, Item.COSMIC.getId()}},
			new int[] {14+48, 12+216});
			
		private final int[] position;
		private final int[][] itemList;
		private final int level, cast, option;
		private final String name, description;
		Standard(int level, int option, int cast, String name, String description, int[][] itemList, int[] position) {
			this.level = level;
			this.option = option;
			this.cast = cast;
			this.name = name;
			this.description = description;
			this.itemList = itemList;
			this.position = position;
		}
		public int getOption() {
			return option;
		}
		public int getCastType() {
			return cast;
		}
		public int getLevelRequirement() {
			return level;
		}
		public int getX() {
			return position[0];
		}
		public int getY() {
			return position[1];
		}
		public String getName() {
			return name;
		}
		public String getDescription() {
			return description;
		}
	}
	
	public enum Ancients {
		EDGEVILLE_HOME_TELEPORT(
			0, CLICK_SPELL, DEFAULT, "Edgeville Home<br>Teleport", 
				"Requires no runes - recharge time<br>30 mins. Warning: This spell takes a<br>long time to cast and will be<br>interrupted by combat.",
				null, 
				new int[] {15, 10}),
		SMOKE_RUSH(
			50, USE_SPELL_ON, COMBAT, "Smoke Rush", "A single target smoke attack", new int[][] {
				{2, Item.CHAOS.getId()},
				{2, Item.DEATH.getId()},
				{1, Item.FIRE.getId(), 
					Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
					Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
				{1, Item.AIR.getId(),
					Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
					Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()}}, 
				new int[] {15+45, 10}),
		SHADOW_RUSH(
			52, USE_SPELL_ON, COMBAT, "Shadow Rush", "A single target shadow attack", new int[][] {
				{2, Item.CHAOS.getId()},
				{2, Item.DEATH.getId()},
				{1, Item.AIR.getId(),
					Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
					Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
				{1, Item.SOUL.getId()}},
				new int[] {15+90, 10}),
		PADDEWWA_TELEPORT(
			54, CLICK_SPELL, DEFAULT, "Paddewwa Teleport", "A teleportation spell", new int[][] {
				{2, Item.LAW.getId()}, 
				{1, Item.FIRE.getId(), 
					Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
					Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
				{1, Item.AIR.getId(),
					Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
					Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()}}, 
				new int[] {15+135, 10}),
		BLOOD_RUSH(
			56, USE_SPELL_ON, COMBAT, "Blood Rush", "A single target blood attack", new int[][] {
				{2, Item.CHAOS.getId()},
				{2, Item.DEATH.getId()},
				{1, Item.BLOOD.getId()}}, 
				new int[] {15, 10+28}),
		ICE_RUSH(
			58, USE_SPELL_ON, COMBAT, "Ice Rush", "A single target ice attack", new int[][] {
				{2, Item.CHAOS.getId()},
				{2, Item.DEATH.getId()},
				{2, Item.WATER.getId(), 
					Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {15+45, 10+28}),
		SENNTISTEN_TELEPORT(
			60, CLICK_SPELL, DEFAULT, "Senntisten Teleport", "A teleportation spell", new int[][] {
				{2, Item.LAW.getId()}, 
				{1, Item.SOUL.getId()}}, 
				new int[] {15+90, 10+28}),
		SMOKE_BURST(
			62, USE_SPELL_ON, COMBAT, "Smoke Burst", "A multi-target smoke attack", new int[][] {
				{4, Item.CHAOS.getId()},
				{2, Item.DEATH.getId()},
				{2, Item.FIRE.getId(), 
					Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
					Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
				{2, Item.AIR.getId(),
					Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
					Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()}}, 
				new int[] {15+135, 10+28}),
		SHADOW_BURST(
			64, USE_SPELL_ON, COMBAT, "Shadow Burst", "A multi-target shadow attack", new int[][] {
				{4, Item.CHAOS.getId()},
				{2, Item.DEATH.getId()},
				{1, Item.AIR.getId(),
					Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
					Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
				{2, Item.SOUL.getId()}},
				new int[] {15, 10+56}),
		KHARYRLL_TELEPORT(
			66, CLICK_SPELL, DEFAULT, "Kharyrll Teleport", "A teleportation spell", new int[][] {
				{2, Item.LAW.getId()}, 
				{1, Item.BLOOD.getId()}}, 
				new int[] {15+45, 10+56}),
		BLOOD_BURST(
			68, USE_SPELL_ON, COMBAT, "Blood Burst", "A multi-target blood attack", new int[][] {
				{4, Item.CHAOS.getId()},
				{2, Item.DEATH.getId()},
				{2, Item.BLOOD.getId()}}, 
				new int[] {15+90, 10+56}),
		ICE_BURST(
			70, USE_SPELL_ON, COMBAT, "Ice Burst", "A multi-target ice attack", new int[][] {
				{4, Item.CHAOS.getId()},
				{2, Item.DEATH.getId()},
				{4, Item.WATER.getId(), 
					Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}}, 
				new int[] {15+135, 10+56}),
		LASSAR_TELEPORT(
			72, CLICK_SPELL, DEFAULT, "Lassar Teleport", "A teleportation spell", new int[][] {
				{2, Item.LAW.getId()},
				{4, Item.WATER.getId(), 
					Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {15, 10+84}),
		SMOKE_BLITZ(
			74, USE_SPELL_ON, COMBAT, "Smoke Blitz", "A single target strong smoke attack", new int[][] {
				{2, Item.DEATH.getId()},
				{2, Item.BLOOD.getId()},
				{2, Item.FIRE.getId(), 
					Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
					Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
				{2, Item.AIR.getId(),
					Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
					Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()}}, 
				new int[] {15+45, 10+84}),
		SHADOW_BLITZ(
			76, USE_SPELL_ON, COMBAT, "Shadow Blitz", "A single target strong shadow<br>attack", new int[][] {
				{2, Item.DEATH.getId()},
				{2, Item.BLOOD.getId()},
				{2, Item.AIR.getId(),
					Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
					Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
				{2, Item.SOUL.getId()}}, 
				new int[] {15+90, 10+84}),
		DAREEYAK_TELEPORT(
			78, CLICK_SPELL, DEFAULT, "Dareeyak Teleport", "A teleportation spell", new int[][] {
				{2, Item.LAW.getId()}, 
				{3, Item.FIRE.getId(), 
					Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
					Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
				{2, Item.AIR.getId(),
					Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
					Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()}}, 
				new int[] {15+135, 10+84}),
		BLOOD_BLITZ(
			80, USE_SPELL_ON, COMBAT, "Blood Blitz", "A single target strong blood attack", new int[][] {
				{2, Item.DEATH.getId()},
				{4, Item.BLOOD.getId()}}, 
				new int[] {15, 10+112}),
		ICE_BLITZ(
			82, USE_SPELL_ON, COMBAT, "Ice Blitz", "A single target strong ice attack", new int[][] {
				{2, Item.DEATH.getId()},
				{2, Item.BLOOD.getId()},
				{3, Item.WATER.getId(), 
					Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}}, 
				new int[] {15+45, 10+112}),
		CARRALLANGAR_TELEPORT(
			84, CLICK_SPELL, DEFAULT, "Carrallangar Teleport", "A teleportation spell", new int[][] {
				{2, Item.LAW.getId()}, 
				{2, Item.SOUL.getId()}},
				new int[] {15+90, 10+112}),
		TELEPORT_TO_BOUNTY_TARGET(
			85, CLICK_SPELL, DEFAULT, "Teleport to Bounty<br>Target", "Teleports you near your Bounty<br>Hunter target", new int[][] {
				{1, Item.LAW.getId()}, 
				{1, Item.DEATH.getId()}, 
				{1, Item.CHAOS.getId()}}, 
				new int[] {15+135, 10+112}),
		SMOKE_BARRAGE(
			86, USE_SPELL_ON, COMBAT, "Smoke Barrage", "A multi-target strong smoke attack", new int[][] {
				{4, Item.DEATH.getId()},
				{2, Item.BLOOD.getId()},
				{4, Item.FIRE.getId(), 
					Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
					Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
				{4, Item.AIR.getId(),
					Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
					Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()}}, 
				new int[] {15, 10+140}),
		SHADOW_BARRAGE(
			88, USE_SPELL_ON, COMBAT, "Shadow Barrage", "A multi-target strong shadow<br>attack", new int[][] {
				{4, Item.DEATH.getId()},
				{2, Item.BLOOD.getId()},
				{4, Item.AIR.getId(),
					Item.MIST.getId(), 				Item.SMOKE.getId(), 			Item.DUST.getId(), 
					Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()},
				{3, Item.SOUL.getId()}},
				new int[] {15+45, 10+140}),
		ANNAKARL_TELEPORT(
			90, CLICK_SPELL, DEFAULT, "Annakarl Teleport", "A teleportation spell", new int[][] {
				{2, Item.LAW.getId()}, 
				{2, Item.BLOOD.getId()}}, 
				new int[] {15+90, 10+140}),
		BLOOD_BARRAGE(
			92, USE_SPELL_ON, COMBAT, "Blood Barrage", "A multi-target strong blood attack", new int[][] {
				{4, Item.DEATH.getId()},
				{4, Item.BLOOD.getId()},
				{1, Item.SOUL.getId()}},
				new int[] {15+135, 10+140}),
		ICE_BARRAGE(
			94, USE_SPELL_ON, COMBAT, "Ice Barrage", "A multi-target strong ice attack", new int[][] {
				{4, Item.DEATH.getId()},
				{2, Item.BLOOD.getId()},
				{6, Item.WATER.getId(), 
					Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}}, 
				new int[] {15, 10+168}),
		GHORROCK_TELEPORT(
			96, CLICK_SPELL, DEFAULT, "Ghorrock Teleport", "A teleportation spell", new int[][] {
				{2, Item.LAW.getId()}, 
				{8, Item.WATER.getId(), 
					Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}}, 
				new int[] {15+45, 10+168});
				
		private final int[] position;
		private final int[][] itemList;
		private final int level, cast, option;
		private final String name, description;
		Ancients(int level, int option, int cast, String name, String description, int[][] itemList, int[] position) {
			this.level = level;
			this.option = option;
			this.cast = cast;
			this.name = name;
			this.description = description;
			this.itemList = itemList;
			this.position = position;
		}
		public int getOption() {
			return option;
		}
		public int getCastType() {
			return cast;
		}
		public int getLevelRequirement() {
			return level;
		}
		public int getX() {
			return position[0];
		}
		public int getY() {
			return position[1];
		}
		public String getName() {
			return name;
		}
		public String getDescription() {
			return description;
		}
	}
	
	public enum Lunar {
		LUNAR_HOME_TELEPORT(
			0, CLICK_SPELL, DEFAULT, "Lunar Home<br>Teleport", 
				"Requires no runes - recharge time<br>30 mins. Warning: This spell takes a<br>long time to cast and will be<br>interrupted by combat.",
				null,
				new int[] {10, 10}),
		BAKE_PIE(
			65, CLICK_SPELL, DEFAULT, "Bake Pies", "Bake pies without a stove", new int[][] {
				{1, Item.ASTRAL.getId()}, 
				{5, Item.FIRE.getId(), 
					Item.SMOKE.getId(), 			Item.STEAM.getId(), 			Item.LAVA.getId(), 
					Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}, 
				{4, Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10+30, 10}),							
		GEOMANCY(
			65, CLICK_SPELL, DEFAULT, "Geomancy", "Check the status of all farming<br>allotments", new int[][] {
				{3, Item.NATURE.getId()}, 
				{3, Item.ASTRAL.getId()}, 
				{8, Item.EARTH.getId(), 
					Item.DUST.getId(),				Item.MUD.getId(),				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {10+60, 10}),				
		CURE_PLANT(
			66, USE_SPELL_ON, OBJECT, "Cure Plant", "Cure disease on farming patch", new int[][] {
				{1, Item.ASTRAL.getId()}, 
				{8, Item.EARTH.getId(), 
					Item.DUST.getId(),				Item.MUD.getId(),				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {10+90, 10}), 
		MONSTER_EXAMINE(
			66, USE_SPELL_ON, NPC, "Monster Examine", "Detect the combat statistics of a<br>monster", new int[][] {
				{1, Item.ASTRAL.getId()}, 
				{1, Item.COSMIC.getId()}, 
				{1, Item.MIND.getId()}},
				new int[] {10+120, 10}),
		NPC_CONTACT(
			67, CLICK_SPELL, DEFAULT, "NPC Contact", "Speak with varied NPCs", new int[][] {
				{1, Item.ASTRAL.getId()}, 
				{1, Item.COSMIC.getId()}, 
				{2, Item.AIR.getId(),
					Item.MIST.getId(),				Item.SMOKE.getId(), 			Item.DUST.getId(), 
					Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()}},
				new int[] {10+150, 10}), 	
		CURE_OTHER(
			68, USE_SPELL_ON, PLAYER, "Cure Other", "Cure poisoned players", new int[][] {
				{1, Item.ASTRAL.getId()}, 
				{1, Item.LAW.getId()}, 
				{10,Item.EARTH.getId(), 
					Item.DUST.getId(),				Item.MUD.getId(),				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {10, 10+30}), 
		HUMIDIFY(
			68, CLICK_SPELL, DEFAULT, "Humidify", "Fills certain vessels with water", new int[][] {
				{1, Item.ASTRAL.getId()}, 
				{3, Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
				{1, Item.FIRE.getId(), 
					Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
					Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}, },
				new int[] {10+30, 10+30}), 	
		MOONCLAN_TELEPORT(
			69, CLICK_SPELL, DEFAULT, "Moonclan Teleport", "Teleports you to moonclan island", new int[][] {
				{2, Item.ASTRAL.getId()}, 
				{1, Item.LAW.getId()}, 
				{2, Item.EARTH.getId(), 
					Item.DUST.getId(),				Item.MUD.getId(),				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {10+60, 10+30}),
		TELE_GROUP_MOONCLAN(
			70, CLICK_SPELL, DEFAULT, "Tele Group Moonclan", "Teleports players to Moonclan<br>island", new int[][] {
				{2, Item.ASTRAL.getId()}, 
				{1, Item.LAW.getId()}, 
				{4, Item.EARTH.getId(), 
					Item.DUST.getId(),				Item.MUD.getId(),				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {10+90, 10+30}),
		CURE_ME(
			71, CLICK_SPELL, DEFAULT, "Cure Me", "Cures Poison", new int[][] {
				{2, Item.ASTRAL.getId()}, 
				{2, Item.LAW.getId()}, 
				{1, Item.COSMIC.getId()}},
				new int[] {10+120, 10+30}), 
		OURANIA_TELEPORT(
			71, CLICK_SPELL, DEFAULT, "Ourania Teleport", "Teleports you to ourania rune altar", new int[][] {
				{2, Item.ASTRAL.getId()}, 
				{1, Item.LAW.getId()}, 
				{6, Item.EARTH.getId(), 
					Item.DUST.getId(),				Item.MUD.getId(),				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {10+150, 10+30}), 	
		HUNTER_KIT(
			71, CLICK_SPELL, DEFAULT, "Hunter Kit", "Get a kit of hunting gear", new int[][] {
				{2, Item.ASTRAL.getId()},
				{3, Item.EARTH.getId(), 
					Item.DUST.getId(),				Item.MUD.getId(),				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {10, 10+60}), 			
		WATERBIRTH_TELEPORT(
			72, CLICK_SPELL, DEFAULT, "Waterbirth Teleport", "Teleports you to Waterbirth island", new int[][] {
				{2, Item.ASTRAL.getId()},
				{1, Item.LAW.getId()},
				{1, Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10+30, 10+60}),
		GROUP_WATERBIRTH_TELEPORT(
			73, CLICK_SPELL, DEFAULT, "Tele Group Waterbirth", "Teleports players to Waterbirth<br>island", new int[][] {
				{2, Item.ASTRAL.getId()},
				{1, Item.LAW.getId()},
				{5, Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10+60, 10+60}),
		CURE_GROUP(
			74, CLICK_SPELL, DEFAULT, "Cure Group", "Cures Poison on players", new int[][] {
				{2, Item.ASTRAL.getId()},
				{2, Item.COSMIC.getId()},
				{2, Item.LAW.getId()}},
				new int[] {10+90, 10+60}),		
		STAT_SPY(//all OF THESE BELOW NEED MODIFYING THE SPRITES IN THE CACHE FOR ORDER
			75, USE_SPELL_ON, PLAYER, "Stat Spy", "Cast on another player to see their<br>skill levels", new int[][] {
				{2, Item.ASTRAL.getId()}, 
				{2, Item.COSMIC.getId()}, 
				{5, Item.BODY.getId()}},
				new int[] {10+150, 10+60}),
		BARBARIAN_TELEPORT(
			75, CLICK_SPELL, DEFAULT, "Barbarian Teleport", "Teleports you to the Barbarian<br>outpost", new int[][] {
				{2, Item.ASTRAL.getId()},
				{2, Item.LAW.getId()},
				{3, Item.FIRE.getId(), 
					Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
					Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {10+120, 10+60}),  		
		TELE_GROUP_BARBARIAN(
			76, CLICK_SPELL, DEFAULT, "Tele Group Barbarian", "Teleports players to the Barbarian<br>outpost", new int[][] {
				{2, Item.ASTRAL.getId()},
				{2, Item.LAW.getId()},
				{6, Item.FIRE.getId(), 
					Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
					Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {10+30, 10+90}), 
		SPIN_FLAX(
			76, CLICK_SPELL, DEFAULT, "Spin Flax", "Spin up to 5 flax instantly", new int[][] {
				{1, Item.ASTRAL.getId()},
				{2, Item.NATURE.getId()},
				{5, Item.AIR.getId(),
					Item.MIST.getId(),				Item.SMOKE.getId(), 			Item.DUST.getId(), 
					Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()}},
				new int[] {10, 10+90}),
		SUPERGLASS_MAKE(
			77, CLICK_SPELL, DEFAULT, "Superglass Make", "Make glass without a furnace", new int[][] {
				{2, Item.ASTRAL.getId()},
				{6, Item.FIRE.getId(), 
					Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
					Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
				{10,Item.AIR.getId(),
					Item.MIST.getId(),				Item.SMOKE.getId(), 			Item.DUST.getId(), 
					Item.STAFF_OF_AIR.getId(), 		Item.AIR_BATTLESTAFF.getId(), 	Item.MYSTIC_AIR_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId()}},
				new int[] {10+60, 10+90}),
		KHAZARD_TELEPORT(
				78, CLICK_SPELL, DEFAULT, "Khazard Teleport", "Teleports you to Port khazard", new int[][] {
					{2, Item.ASTRAL.getId()},
					{2, Item.LAW.getId()},
					{4, Item.WATER.getId(), 
						Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
						Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
						Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
						Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
						Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
					new int[] {10+90, 10+90}), 
		TAN_LEATHER(
			78, CLICK_SPELL, DEFAULT, "Tan Leather", "Tan up to 5 hides", new int[][] {
				{2, Item.ASTRAL.getId()},
				{1, Item.NATURE.getId()},
				{5, Item.FIRE.getId(), 
					Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
					Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {10+120, 10+90}),
		TELE_GROUP_KHAZARD(
			79, CLICK_SPELL, DEFAULT, "Tele Group Khazard", "Teleports players to Port khazard", new int[][] {
				{2, Item.ASTRAL.getId()}, 
				{2, Item.LAW.getId()},
				{8, Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10+150, 10+90}), 
		DREAM(
			79, CLICK_SPELL, DEFAULT, "Dream", "Take a rest and restore hitpoints 3<br> times faster", new int[][] {
				{2, Item.ASTRAL.getId()},
				{1, Item.COSMIC.getId()},
				{5, Item.BODY.getId()}},
				new int[] {10, 10+120}),
		STRING_JEWELRY(
			80, CLICK_SPELL, DEFAULT, "String Jewellery", "String amulets without wool", new int[][] {
				{2, Item.ASTRAL.getId()}, 
				{10,Item.EARTH.getId(), 
					Item.DUST.getId(),				Item.MUD.getId(),				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
				{5, Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10+30, 10+120}), 
		STAT_RESTORE_POT_SHARE(
			81, USE_SPELL_ON, ITEM, "Stat Restore Pot<br>Share", "Share a potion with up to 4 nearby<br>players", new int[][] {
				{2, Item.ASTRAL.getId()}, 
				{10,Item.EARTH.getId(), 
					Item.DUST.getId(),				Item.MUD.getId(),				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
				{10,Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10+60, 10+120}), 
		MAGIC_IMBUE(
			82, CLICK_SPELL, DEFAULT, "Magic Imbue", "Combine runes without a talisman", new int[][] {
				{2, Item.ASTRAL.getId()},
				{7, Item.FIRE.getId(), 
					Item.SMOKE.getId(), 			Item.STEAM.getId(), Item.LAVA.getId(), 
					Item.STAFF_OF_FIRE.getId(), 	Item.FIRE_BATTLESTAFF.getId(), 	Item.MYSTIC_FIRE_STAFF.getId(), 
					Item.SMOKE_BATTLESTAFF.getId(), Item.MYSTIC_SMOKE_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
				{7, Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10+90, 10+120}), 
		FERTILE_SOIL(
			83, USE_SPELL_ON, OBJECT, "Fertile Soil", "Fertilise a farming patch with super<br>compost", new int[][] {
				{3, Item.ASTRAL.getId()},
				{2, Item.NATURE.getId()},
				{15,Item.EARTH.getId(), 
					Item.DUST.getId(),				Item.MUD.getId(),				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {10+120, 10+120}),
		BOOST_POTION_SHARE(
			84, USE_SPELL_ON, ITEM, "Boost Potion Share", "Shares a potion with up to 4 nearby<br>players", new int[][] {
				{3, Item.ASTRAL.getId()},
				{12,Item.EARTH.getId(), 
					Item.DUST.getId(),				Item.MUD.getId(),				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}, 
				{10,Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10+150, 10+120}), 
		FISHING_GUILD_TELEPORT(
			85, CLICK_SPELL, DEFAULT, "Fishing Guild Teleport", "Teleports you to the fishing guild", new int[][] {
				{3, Item.ASTRAL.getId()},
				{3, Item.LAW.getId()}, 
				{10,Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10, 10+150}),
		BOUNTY_TARGET_TELEPORT(
			85, CLICK_SPELL, DEFAULT, "Teleport to Bounty<br>Target", "Teleports you near your Bounty<br>Hunter target", new int[][] {
				{1, Item.LAW.getId()},
				{1, Item.DEATH.getId()}, 
				{1, Item.CHAOS.getId()}},
				new int[] {10+30, 10+150}),
		TELE_GROUP_FISHING_GUILD(
			86, CLICK_SPELL, DEFAULT, "Tele Group Fishing<br>Guild", "Teleports players to the Fishing<br>Guild", new int[][] {
				{3, Item.ASTRAL.getId()},
				{3, Item.LAW.getId()}, 
				{15,Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10+60, 10+150}), 
		PLANK_MAKE(
			86, USE_SPELL_ON, ITEM, "Plank Make", "Turn Logs into planks", new int[][] {
				{2, Item.ASTRAL.getId()}, 
				{15, Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()},
				{1, Item.NATURE.getId()}},
				new int[] {10+90, 10+150}), 
		CATHERBY_TELEPORT(
			87, CLICK_SPELL, DEFAULT, "Catherby Teleport", "Teleports you to Catherby", new int[][] {
				{3, Item.ASTRAL.getId()},
				{3, Item.LAW.getId()}, 
				{10,Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10+120, 10+150}), 
		TELE_GROUP_CATHERBY(
			89, CLICK_SPELL, DEFAULT, "Tele Group Catherby", "Teleports players to Catherby", new int[][] {
				{3, Item.ASTRAL.getId()},
				{3, Item.LAW.getId()},
				{15,Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10+150, 10+150}),
		RECHARGE_DRAGONSTONE(
			89, CLICK_SPELL, DEFAULT, "Recharge<br>Dragonstone", "Recharges all enchanted<br>Dragonstone jewellery", new int[][] {
				{1, Item.ASTRAL.getId()},
				{1, Item.SOUL.getId()},
				{4, Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10, 10+180}), 
		ICE_PLATEAU_TELEPORT(
			89, CLICK_SPELL, DEFAULT, "Ice Plateau Teleport", "Teleports you to Ice Plateau", new int[][] {
				{3, Item.ASTRAL.getId()},
				{3, Item.LAW.getId()},
				{8, Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10+30, 10+180}), 
		TELE_GROUP_ICE_PLATEAU(
			90, CLICK_SPELL, DEFAULT, "Tele Group Ice Plateau", "Teleports players to Ice Plateau", new int[][] {
				{3, Item.ASTRAL.getId()},
				{3, Item.LAW.getId()},
				{16,Item.WATER.getId(), 
					Item.MIST.getId(),				Item.MUD.getId(),				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {10+60, 10+180}), 
		ENERGY_TRANSFER(
			91, USE_SPELL_ON, PLAYER, "Energy Transfer", "Spend hitpoints and SA energy to<br>give another SA and run energy", new int[][] {
				{3, Item.ASTRAL.getId()},
				{2, Item.LAW.getId()},
				{1, Item.NATURE.getId()}},
				new int[] {10+90, 10+180}), 
		HEAL_OTHER(
			92, USE_SPELL_ON, PLAYER, "Heal Other", "Transfer up to 75% of hitpoints<br> to another player", new int[][] {
				{3, Item.ASTRAL.getId()},
				{3, Item.LAW.getId()},
				{1, Item.BLOOD.getId()}},
				new int[] {10+120, 10+180}), 
		VENGEANCE_OTHER(
			93, USE_SPELL_ON, PLAYER, "Vengeance Other", "Allows another player to rebound<br>damage to an opponent", new int[][] {
				{3, Item.ASTRAL.getId()},
				{2, Item.DEATH.getId()},
				{10,Item.EARTH.getId(), 
					Item.DUST.getId(),				Item.MUD.getId(),				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {10+150, 10+180}),	
		VENGEANCE(
			94, CLICK_SPELL, DEFAULT, "Vengeance", "Rebound damage to an opponent", new int[][] {
				{4, Item.ASTRAL.getId()},
				{2, Item.DEATH.getId()},
				{10,Item.EARTH.getId(), 
					Item.DUST.getId(),				Item.MUD.getId(), 				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {10, 10+210}),
		HEAL_GROUP(
			95, CLICK_SPELL, DEFAULT, "Heal Group", "Transfer up to 75% of hitpoints<br> to a group", new int[][] {
				{4, Item.ASTRAL.getId()}, 
				{3, Item.BLOOD.getId()}, 
				{6, Item.LAW.getId()}},
				new int[] {10+30, 10+210}),	
		SPELLBOOK_SWAP(
			96, CLICK_SPELL, DEFAULT, "Spellbook Swap", "Change to another spellbook for 1<br>spell cast", new int[][] {
				{3, Item.ASTRAL.getId()},
				{2, Item.COSMIC.getId()},
				{1, Item.LAW.getId()}},
				new int[] {10+60, 10+210});
				
		private final int[] position;
		private final int[][] itemList;
		private final int level, cast, option;
		private final String name, description;
		Lunar(int level, int option, int cast, String name, String description, int[][] itemList, int[] position) {
			this.level = level;
			this.option = option;
			this.cast = cast;
			this.name = name;
			this.description = description;
			this.itemList = itemList;
			this.position = position;
		}
		public int getOption() {
			return option;
		}
		public int getCastType() {
			return cast;
		}
		public int getLevelRequirement() {
			return level;
		}
		public int getX() {
			return position[0];
		}
		public int getY() {
			return position[1];
		}
		public String getName() {
			return name;
		}
		public String getDescription() {
			return description;
		}
	}
	
	public enum Arceuus {
		ARCEUUS_HOME_TELEPORT(
			0, CLICK_SPELL, DEFAULT, "Arceuus Home Teleport", 
				"Requires no runes - recharge time<br>30 mins. Warning: This spell takes a<br>long time to cast and will be<br>interrupted by combat.",
				null,
				new int[] {7, 10}),
		REANIMATE_GOBLIN(
			3, USE_SPELL_ON, ITEM, "Reanimate Goblin", "Summons a Reanimated goblin", new int[][] {
				{1, Item.NATURE.getId()},
				{2, Item.BODY.getId()}},
				new int[] {7+38, 10}), 
		LUMBRIDGE_GRAVEYARD_TELEPORT(
			6, CLICK_SPELL, DEFAULT, "Lumbridge Graveyard<br>Teleport", "Teleport to the Lumbridge cemetery", new int[][] {
				{1, Item.LAW.getId()}, 
				{2, Item.EARTH.getId(), 
					Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {7+76, 10}),  
		REANIMATE_MONKEY(
			7, USE_SPELL_ON, ITEM, "Reanimate Monkey", "Summons a Reanimated monkey", new int[][] {
				{1, Item.NATURE.getId()},
				{3, Item.BODY.getId()}},
				new int[] {7+114, 10}), 							
		REANIMATE_IMP(
			12, USE_SPELL_ON, ITEM, "Reanimate Imp", "Summons a Reanimated imp", new int[][] {
				{2, Item.NATURE.getId()},
				{3, Item.BODY.getId()}},
				new int[] {7+152, 10}), 
		REANIMATE_MINOTAUR(
			16, USE_SPELL_ON, ITEM, "Reanimate Minotaur", "Summons a Reanimated minotaur", new int[][] {
				{2, Item.NATURE.getId()},
				{4, Item.BODY.getId()}},
				new int[] {7, 24+10+10}), 							
		DRAYNOR_MANOR_TELEPORT(
			17, CLICK_SPELL, DEFAULT, "Draynor Manor<br>Teleport", "Teleport to the Draynor manor", new int[][] {
				{1, Item.LAW.getId()},
				{1, Item.EARTH.getId(), 
					Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()},
				{1, Item.WATER.getId(), 
					Item.MIST.getId(), 				Item.MUD.getId(), 				Item.STEAM.getId(), 
					Item.STAFF_OF_WATER.getId(), 	Item.WATER_BATTLESTAFF.getId(), Item.MYSTIC_WATER_STAFF.getId(), 
					Item.MIST_BATTLESTAFF.getId(), 	Item.MYSTIC_MIST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(), 	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.STEAM_BATTLESTAFF.getId(), Item.MYSTIC_STEAM_STAFF.getId()}},
				new int[] {7+38, 24+10+10}), 		
		REANIMATE_SCORPION(
			19, USE_SPELL_ON, ITEM, "Reanimate Scorpion", "Summons a Reanimated scorpion", new int[][] {
				{1, Item.SOUL.getId()},
				{1, Item.NATURE.getId()}},
				new int[] {7+76, 24+10+10}), 							
		REANIMATE_BEAR(
			21, USE_SPELL_ON, ITEM, "Reanimate Bear", "Summons a Reanimated bear", new int[][] {
				{1, Item.SOUL.getId()},
				{1, Item.NATURE.getId()},
				{1, Item.BODY.getId()}},
				new int[] {7+114, 24+10+10}), 	
		REANIMATE_UNICORN(
			22, USE_SPELL_ON, ITEM, "Reanimate Unicorn", "Summons a Reanimated unicorn", new int[][] {
				{1, Item.SOUL.getId()},
				{1, Item.NATURE.getId()},
				{2, Item.BODY.getId()}},
				new int[] {7+152, 24+10+10}), 	
		REANIMATE_DOG(
			26, USE_SPELL_ON, ITEM, "Reanimate Dog", "Summons a Reanimated dog", new int[][] {
				{1, Item.SOUL.getId()},
				{2, Item.NATURE.getId()},
				{2, Item.BODY.getId()}},
				new int[] {7, 48+10+20}), 	
		MIND_ALTAR_TELEPORT(
			28, CLICK_SPELL, DEFAULT, "Mind Altar Teleport", "Teleport to the Mind altar", new int[][] {
				{1, Item.LAW.getId()},
				{2, Item.MIND.getId()}},
				new int[] {7+38, 48+10+20}), 								
		REANIMATE_CHAOS_DRUID(
			30, USE_SPELL_ON, ITEM, "Reanimate Chaos<br>Druid", "Summons a Reanimated chaos druid", new int[][] {
				{1, Item.SOUL.getId()},
				{2, Item.NATURE.getId()},
				{3, Item.BODY.getId()}},
				new int[] {7+76, 48+10+20}), 	
		RESPAWN_TELEPORT(
			34, USE_SPELL_ON, ITEM, "Respawn Teleport", "Teleport to your respawn point", new int[][] {
				{1, Item.SOUL.getId()},
				{1, Item.LAW.getId()}},
				new int[] {7+114, 48+10+20}), 								
		REANIMATE_GIANT(
			37, USE_SPELL_ON, ITEM, "Reanimate Giant", "Summons a Reanimated giant", new int[][] {
				{1, Item.SOUL.getId()},
				{2, Item.NATURE.getId()},
				{4, Item.BODY.getId()}},
				new int[] {7+152, 48+10+20}), 	
		SALVE_GRAVEYARD_TELEPORT(
			40, CLICK_SPELL, DEFAULT, "Salve Graveyard<br>Teleport", "Teleport to the Salve graveyard", new int[][] {
				{2, Item.SOUL.getId()},
				{1, Item.LAW.getId()}},
				new int[] {7, 72+10+30}),				
		REANIMATE_OGRE(
			40, USE_SPELL_ON, ITEM, "Reanimate Ogre", "Summons a Reanimated ogre", new int[][] {
				{1, Item.SOUL.getId()},
				{3, Item.NATURE.getId()},
				{4, Item.BODY.getId()}},
				new int[] {7+38, 72+10+30}), 	
		REANIMATE_ELF(
			43, USE_SPELL_ON, ITEM, "Reanimate Elf", "Summons a Reanimated elf", new int[][] {
				{2, Item.SOUL.getId()},
				{2, Item.NATURE.getId()},
				{2, Item.BODY.getId()}},
				new int[] {7+76, 72+10+30}), 	
		REANIMATE_TROLL(
			46, USE_SPELL_ON, ITEM, "Reanimate Troll", "Summons a Reanimated troll", new int[][] {
				{1, Item.SOUL.getId()},
				{2, Item.NATURE.getId()},
				{3, Item.BODY.getId()}},
				new int[] {7+114, 72+10+30}), 	
		FENKENSTRAINS_CASTLE_TELEPORT(
			48, CLICK_SPELL, DEFAULT, "Fenkenstrain's<br>CastleTeleport", "Teleport to Fenkenstrain's castle", new int[][] {
				{1, Item.SOUL.getId()},
				{1, Item.LAW.getId()},
				{1, Item.EARTH.getId(), 
					Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {7+152, 72+10+30}), 	
		REANIMATE_HORROR(
			52, USE_SPELL_ON, ITEM, "Reanimate Horror", "Summons a Reanimated horror", new int[][] {
				{2, Item.SOUL.getId()},
				{2, Item.NATURE.getId()},
				{4, Item.BODY.getId()}},
				new int[] {7, 96+10+40}), 	
		REANIMATE_KALPHITE(
			57, USE_SPELL_ON, ITEM, "Reanimate Kalphite", "Summons a Reanimated kalphite", new int[][] {
				{2, Item.SOUL.getId()},
				{3, Item.NATURE.getId()},
				{4, Item.BODY.getId()}},
				new int[] {7+38, 96+10+40}), 	
		WEST_ARDOUGNE_TELEPORT(
			61, CLICK_SPELL, DEFAULT, "West Ardougne<br>Teleport", "Teleport to West Ardougne", new int[][] {
				{2, Item.SOUL.getId()},
				{2, Item.LAW.getId()}},
				new int[] {7+76, 96+10+40}), 								
		REANIMATE_DAGANNOTH(
			62, USE_SPELL_ON, ITEM, "Reanimate<br>Dagannoth", "Summons a Reanimated dagannoth", new int[][] {
				{3, Item.SOUL.getId()},
				{3, Item.NATURE.getId()},
				{4, Item.BODY.getId()}},
				new int[] {7+114, 96+10+40}), 	
		REANIMATE_BLOODVELD(
			65, USE_SPELL_ON, ITEM, "Reanimate Bloodveld", "Summons a Reanimated bloodveld", new int[][] {
				{2, Item.SOUL.getId()},
				{1, Item.BLOOD.getId()},
				{2, Item.NATURE.getId()}},
				new int[] {7+152, 96+10+40}), 
		HARMONY_ISLAND_TELEPORT(
			65, CLICK_SPELL, DEFAULT, "Harmony Island<br>Teleport", "Teleport to Harmony Island", new int[][] {
				{1, Item.SOUL.getId()},
				{1, Item.LAW.getId()},
				{1, Item.NATURE.getId()}},
				new int[] {7, 120+10+50}), 		
		REANIMATE_TZHAAR(
			69, USE_SPELL_ON, ITEM, "Reanimate TzHaar", "Summons a Reanimated TzHaar", new int[][] {
				{2, Item.SOUL.getId()},
				{1, Item.BLOOD.getId()},
				{3, Item.NATURE.getId()}},
				new int[] {7+38, 120+10+50}), 	
		CEMETERY_TELEPORT(
			71, CLICK_SPELL, DEFAULT, "Cemetery Teleport", "Teleport to the Cemetery", new int[][] {
				{1, Item.SOUL.getId()},
				{1, Item.BLOOD.getId()},
				{1, Item.LAW.getId()}},
				new int[] {7+76, 120+10+50}), 		
		REANIMATE_DEMON(
			72, USE_SPELL_ON, ITEM, "Reanimate Demon", "Summons a Reanimated demon", new int[][] {
				{2, Item.SOUL.getId()},
				{1, Item.BLOOD.getId()},
				{4, Item.NATURE.getId()}},
				new int[] {7+114, 120+10+50}),	
		REANIMATE_AVIANSIE(
			78, USE_SPELL_ON, ITEM, "Reanimate Aviansie", "Summons a Reanimated aviansie", new int[][] {
				{3, Item.SOUL.getId()},
				{1, Item.BLOOD.getId()},
				{4, Item.NATURE.getId()}},
				new int[] {7+152, 120+10+50}), 
		RESURRECT_CROPS(
			78, USE_SPELL_ON, OBJECT, "Resurrect Crops", "Chance to reanimate a<br>dead farming patch", new int[][] {
				{8, Item.SOUL.getId()},
				{8, Item.BLOOD.getId()},
				{12, Item.NATURE.getId()},
				{25, Item.EARTH.getId(), 
					Item.DUST.getId(), 				Item.MUD.getId(), 				Item.LAVA.getId(), 
					Item.STAFF_OF_EARTH.getId(), 	Item.EARTH_BATTLESTAFF.getId(), Item.MYSTIC_EARTH_STAFF.getId(), 
					Item.DUST_BATTLESTAFF.getId(), 	Item.MYSTIC_DUST_STAFF.getId(), 
					Item.MUD_BATTLESTAFF.getId(),	Item.MYSTIC_MUD_STAFF.getId(), 
					Item.LAVA_BATTLESTAFF.getId(), 	Item.MYSTIC_LAVA_STAFF.getId()}},
				new int[] {7, 144+10+60}), 
		BARROWS_TELEPORT(
			83, CLICK_SPELL, DEFAULT, "Barrows Teleport", "Teleport to the Barrows entrance", new int[][] {
				{2, Item.SOUL.getId()},
				{1, Item.BLOOD.getId()},
				{2, Item.LAW.getId()}},
				new int[] {7+38, 144+10+60}), 		
		REANIMATE_ABYSSAL_CREATURE(
			85, USE_SPELL_ON, ITEM, "Reanimate Abyssal<br>Creature", "Summons a Reanimated abyssal", new int[][] {
				{4, Item.SOUL.getId()},
				{1, Item.BLOOD.getId()},
				{4, Item.NATURE.getId()}},
				new int[] {7+76, 144+10+60}), 	
		APE_ATOLL_TELEPORT(
			90, CLICK_SPELL, DEFAULT, "Ape Atoll<br>Teleport", "Teleport to Ape Atoll", new int[][] {
				{2, Item.SOUL.getId()},
				{2, Item.BLOOD.getId()},
				{2, Item.LAW.getId()}},
				new int[] {7+114, 144+10+60}), 		
		REANIMATE_DRAGON(
			93, USE_SPELL_ON, ITEM, "Reanimate Dragon", "Summons a Reanimated dragon", new int[][] {
				{4, Item.SOUL.getId()},
				{2, Item.BLOOD.getId()},
				{4, Item.NATURE.getId()}},
				new int[] {7+152, 144+10+60});
		
		private final int[] position;
		private final int[][] itemList;
		private final int level, cast, option;
		private final String name, description;
		Arceuus(int level, int option, int cast, String name, String description, int[][] itemList, int[] position) {
			this.level = level;
			this.option = option;
			this.cast = cast;
			this.name = name;
			this.description = description;
			this.itemList = itemList;
			this.position = position;
		}
		public int getOption() {
			return option;
		}
		public int getCastType() {
			return cast;
		}
		public int getLevelRequirement() {
			return level;
		}
		public int getX() {
			return position[0];
		}
		public int getY() {
			return position[1];
		}
		public String getName() {
			return name;
		}
		public String getDescription() {
			return description;
		}
	}
	
	public static void buildStandardSpellbook(GameFont[] font) {
		int parent = 20000;
		Widget tab = addInterface(parent, 190, 261);
		setIndex(Standard.values().length * 2, tab);
		int child_Id = 0;
		int info_Child_Id = Standard.values().length;
		int id = 60000;//Ids used for the information hover box, may require changing
		int sprite_Id = 800;
		int bids = 0;
		int[] originalId = {
			//id layout is based on the interface layout, row | column to get spellId
			19210, 1152, 1153, 19207, 1154, 1155, 1156,
			1157, 1158, 1159, 1160, 1161, 1572, 1162,
			1163, 1164, 1165, 1166, 1167, 1168, 1169,
			1170, 1171, 19208, 1172, 1173, 1174, 1175,
			1176, 1539, 1582, 12037, 1540, 1177, 1178,
			1179, 1180, 1541, 1181, 1182, 15877, 1190,
			1191, 1192, 7455, 1183, 1184, 18470, 1185, 
			1186, 1542, 1187, 1188, 1543, 12425, 1189,
			1592, 1562, 1193, 12435, 12445, 6003, 12465,
			12475, 12455, 12485
		};
		for(Standard data : Standard.values()) {
			addSpellButton(font, originalId[bids], id, parent, "standard", sprite_Id, data.getName(), data.getDescription(), data.getOption(), data.getCastType(), data.itemList, data.getLevelRequirement());
			setChild(originalId[bids], data.getX(), data.getY(), child_Id, tab); child_Id++;
			setChild(id + 1, 4, data.getY() > 10+112 ? 5 : 250 - Widget.interfaceCache[id + 1].height, info_Child_Id, tab); info_Child_Id++;
			id += 13;
			bids += 1;
			sprite_Id += 2;
		}
	}
	
	public static void buildAncientSpellbook(GameFont[] font) {
		int parent = 21000;
		Widget tab = addInterface(parent, 190, 261);
		setIndex(Ancients.values().length * 2, tab);
		int child = 0;
		int info = Ancients.values().length;
		int id = 61000;
		int spriteId = 938;
		int bids = 0;
		int[] originalId = {
			//id layout is based on the interface layout, row | column to get spellId
			21741, 12939, 12987, 13035,
			12901, 12861, 13045, 12963,
			13011, 13053, 12919, 12881, 
			13061, 12951, 12999, 13069,
			12911, 12871, 13079, 13105,
			12975, 13023, 13087, 12929,
			12891, 13095
		};
		for(Ancients data : Ancients.values()) {
			addSpellButton(font, originalId[bids], id, parent, "ancient", spriteId, data.getName(), data.getDescription(), data.getOption(), data.getCastType(), data.itemList, data.getLevelRequirement());
			setChild(originalId[bids], data.getX(), data.getY(), child, tab);
			child++;
			setChild(id + 1, 4, data.getY() > 10+112 ? 5 : 250 - Widget.interfaceCache[id + 1].height, info, tab); info++;
			id += 13;
			bids += 1;
			spriteId += 2;
		}
	}
	
	public static void buildLunarSpellbook(GameFont[] font) {
		int parent = 22000;
		Widget tab = addInterface(parent, 190, 261);
		setIndex(Lunar.values().length * 2, tab);
		int button_child = 0;
		int box_child = Lunar.values().length;
		int id = 62000;
		int spriteId = 990;
		for(Lunar data : Lunar.values()) {
			addSpellButton(font, id, parent, "lunar", spriteId, data.getName(), data.getDescription(), data.getOption(), data.getCastType(), data.itemList, data.getLevelRequirement());
			setChild(id, data.getX(), data.getY(), button_child, tab); 
			button_child++;
			setChild(id + 1, 4, data.getY() > 10+112 ? 7 : 250 - Widget.interfaceCache[id + 1].height, box_child, tab);	
			box_child++;
				
			id += 13;
			spriteId += 2;
		}
	}
	
	public static void buildArceuusSpellbook(GameFont[] font) {
		int parent = 23000;
		Widget tab = addInterface(parent, 190, 261);
		setIndex(Arceuus.values().length * 2, tab);
		int child = 0;
		int info = Arceuus.values().length;
		int id = 63000;
		int spriteId = 1080;
		for(Arceuus data : Arceuus.values()) {
			addSpellButton(font, id, parent, "arceuus", spriteId, data.getName(), data.getDescription(), data.getOption(), data.getCastType(), data.itemList, data.getLevelRequirement());
			setChild(id, data.getX(), data.getY(), child, tab); child++;
			setChild(id + 1, 4, data.getY() > 10+112 ? 7 : 250 - Widget.interfaceCache[id + 1].height, info, tab); info++;
			id += 13;
			spriteId += 2;
		}
	}
}
