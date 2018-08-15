package com.overload.game.content.achievement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.overload.game.collision.Region;
import com.overload.game.collision.RegionManager;
import com.overload.game.content.achievement.impl.*;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Position;

public class DiaryManager {

    public static List<Diary> diaries = new ArrayList<>();
    
    Player p;
    
    public DiaryManager(Player p) {
    	this.p = p;
    	diaries.add(new VarrockDiary());
    	diaries.add(new FaladorDiary());
    }

    public void process() {
        Diary diary = get(p.getPosition());
        if (diary != null) {
        	diary.process(p);
        }
    }

    public static Diary get(Position position) {
        for (Diary diary : diaries) {
            if (inside(position, diary)) {
                return diary;
            }
        }
        return null;
    }
    
    public List<Diary> getDiaries() {
    	return diaries;
    }

    public static boolean inside(Position position, Diary diary) {
    	Optional<Region> r = RegionManager.getRegion(position.getX(), position.getY());
    	if (r.isPresent()) {
	        return diary.getRegions().getRegions().contains(r.get().getRegionId());
    	}
        return false;
    }
}
