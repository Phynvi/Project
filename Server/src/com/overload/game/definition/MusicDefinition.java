package com.overload.game.definition;

import java.util.HashMap;
import java.util.Map;

public class MusicDefinition {

    public static final Map<Integer, MusicDefinition> definitions = new HashMap<Integer, MusicDefinition>();

    public static final MusicDefinition DEFAULT = new MusicDefinition();
	
	private int[] regions;
	private String name;
	private int song;
	private int frame;
	private int button;

    public static MusicDefinition forRegion(int regionId) {
    	for (MusicDefinition music : definitions.values()) {
    		for (int i = 0; i < music.regions.length; i++) {
        		if (music.regions[i] == regionId) {
        	        return definitions.getOrDefault(regionId, DEFAULT);
        		}
    		}
    	}
    	return DEFAULT;
    }

    public static MusicDefinition forFrame(int frame) {
        return definitions.getOrDefault(frame, DEFAULT);
    }
	
	public String getName() {
		return name;
	}
	
	public int getSong() {
		return song;
	}
	
	public int getFrame() {
		return frame;
	}

	public int getButton() {
		return button;
	}
}
