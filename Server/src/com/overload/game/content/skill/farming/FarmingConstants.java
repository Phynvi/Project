package com.overload.game.content.skill.farming;

import java.util.concurrent.TimeUnit;

import org.joda.time.Duration;

import com.overload.game.model.Item;
import com.overload.game.model.Position;

public class FarmingConstants {

	public static final int WATERING_CAN_ANIM = 2293;
	public static final int RAKING_ANIM = 2273;
	public static final int SPADE_ANIM = 830;
	public static final int SEED_DIBBING = 2291;
	public static final int PICKING_VEGETABLE_ANIM = 2282;
	public static final int PICKING_HERB_ANIM = 2279;
	public static final int PUTTING_COMPOST = 2283;
	public static final int CURING_ANIM = 2288;
	public static final int FILLING_POT_ANIM = 2287;
	public static final int PLANTING_POT_ANIM = 2272;
	public static final int PRUNING_ANIM = 2275;
	public static final int RAKE = 5341;
	public static final int SEED_DIBBER = 5343;
	public static final int SPADE = 952;
	public static final int TROWEL = 5325;
	public static final int SECATEURS = 5329;
	public static final int MAGIC_SECATEURS = 7409;

	public static final int[] WATERED_SAPPLING = { 5364, 5365, 5366, 5367, 5368, 5369, 5488, 5489, 5490, 5491, 5492, 5493, 5494, 5495 };
	
	public static final Item ITEM_PLANT_CURE = new Item(6036, 1);
	public static final int COMPOST_BIN = 7837;

	public static boolean inRangeArea(Position base, Position top, Position point) {
		return point.getX() >= base.getX() && point.getY() >= base.getY() && point.getX() <= top.getX() && point.getY() <= top.getY();
	}

	public static boolean inRangeArea(Position base, Position top, int x, int y) {
		return x >= base.getX() && y >= base.getY() && x <= top.getX() && y <= top.getY();
	}

	public static boolean isWateringCan(int id) {
		if (id == 5340 || id == 5339 || id == 5338 || id == 5337 || id == 5336 || id == 5335 || id == 5334 || id == 5333)
			return true;
		return false;
	}
	
	public static Duration setTime(int mins) {
		return Duration.millis(TimeUnit.MINUTES.toMillis(75));
	}
}