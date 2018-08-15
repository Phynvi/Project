package com.overload.game.content.achievement;

import java.util.ArrayList;
import java.util.List;

public enum DiaryRegions {
	VARROCK(10571, 12596, 12597, 12598, 12852, 12853, 12854, 13108, 13109, 13110, 12341),
	FALADOR(11571, 11572, 11827, 11828, 12083, 12084),
	LUMBRIDGE(12593, 12594, 12849, 12850),
	;
	
    private int[] regions;
	
	DiaryRegions(int... regions) {
		this.regions = regions;
	}
	
	public List<Integer> getRegions() {
		List<Integer> list = new ArrayList<>();
		for (Integer i : regions)
			list.add(i);
		return list;
	}
}
