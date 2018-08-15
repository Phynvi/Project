package com.overload.game.model.areas;

import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Boundary;
import com.overload.game.model.Position;
import com.overload.game.model.areas.impl.*;
import com.overload.game.model.areas.impl.ScorpiaArea;
import com.overload.game.model.areas.impl.VorkathArea;
import com.overload.game.model.region.InstancedRegion;

import java.util.ArrayList;
import java.util.List;

public class AreaManager {

    public static List<Area> areas = new ArrayList<>();
    public static List<InstancedRegion> instances = new ArrayList<>();

    static {
        areas.add(new BarrowsArea());
        areas.add(new DuelArenaArea());
        areas.add(new WildernessArea());
        areas.add(new PestControlBoatArea());
        areas.add(new PestControlGameArea());
        areas.add(new ZulrahArea());
        areas.add(new WinterTodtArea());
        areas.add(new CorpArea());
        areas.add(new BandosArea());
        areas.add(new ZamarokArea());
        areas.add(new SaradominArea());
        areas.add(new ArmaArea());
        areas.add(new DagganothArea());
        areas.add(new GodWarsDungeon());
        areas.add(new VorkathArea());
        areas.add(new ScorpiaArea());
    }

    /**
     * Processes areas for the given character.
     *
     * @param c
     */
    public static void process(Character c) {
        Position position = c.getPosition();
        Area area = c.getArea();

        if (area != null) {
            if (!inside(position, area)) {
                area.leave(c);
                area = null;
            }
        }

        if (area == null) {
            area = get(position);
            if (area != null) {
                area.enter(c);
            }
        }

        // Handle processing..
        if (area != null) {
            area.process(c);
        }

        // Handle multiicon update..
        if (c.isPlayer()) {
            Player player = c.getAsPlayer();

            int multiIcon = 0;

            if (area != null) {
                multiIcon = area.isMulti(player) ? 1 : 0;
            }

            if (player.getMultiIcon() != multiIcon) {
                player.getPacketSender().sendMultiIcon(multiIcon);
            }
        }

        // Update area..
        c.setArea(area);
    }

    /**
     * Checks if a {@link Character} is in multi.
     *
     * @param c
     * @return
     */
    public static boolean inMulti(Character c) {
    	boolean ret = false;
        if (c.getArea() != null)
        	ret = c.getArea().isMulti(c);
        if (!ret) {
	        if (c.getInstancedRegion().isPresent()) {
	        	if (c.getInstancedRegion().get().getAreas().isPresent()) {
	        		for (Area area : c.getInstancedRegion().get().getAreas().get()) {
	        			ret = area.isMulti(c);
	        			if (ret)
	        				break;
	        		}
	        	}
	        }
        }
        return ret;
    }

    /**
     * Checks if a {@link Character} can attack another one.
     *
     * @param attacker
     * @param target
     * @return
     */
    public static boolean canAttack(Character attacker, Character target) {
        if (attacker.getArea() != null) {
            return attacker.getArea().canAttack(attacker, target);
        }

        // Don't allow PvP by default
        if (attacker.isPlayer() && target.isPlayer()) {
            return false;
        }

        return true;
    }

    /**
     * Gets a {@link Area} based on a given {@link Position}.
     *
     * @param position
     * @return
     */
    public static Area get(Position position) {
        for (Area area : areas) {
            if (inside(position, area)) {
                return area;
            }
        }
        return null;
    }

    /**
     * Checks if a position is inside of an area's boundaries.
     *
     * @param position
     * @return
     */
    public static boolean inside(Position position, Area area) {
        for (Boundary b : area.getBoundaries()) {
            if (b.inside(position)) {
                return true;
            }
        }
        return false;
    }
}
