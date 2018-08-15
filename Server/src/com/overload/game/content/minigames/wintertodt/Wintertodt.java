package com.overload.game.content.minigames.wintertodt;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Position;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Wintertodt {

    public static final int RESPAWN_DURATION = (int) TimeUnit.SECONDS.toMillis(30);

    private static final int MAX_HEALTH = 70000;

    public static final Position HOWLING_STORM_LOCATION = new Position(1630, 4007, 0);

    private WintertodtPyroMancer[] pyromancers = new WintertodtPyroMancer[4];

    private HashMap<Player, Integer> playerPoints = new HashMap<>();

    private int health = MAX_HEALTH;

    private boolean areaAttackActive = false;

    private boolean started = false;

    private static final Position[] BRAZIER_LOCATIONS = new Position[] {
            new Position(1620, 3997),
            new Position(1620, 4015),
            new Position(1638, 4015),
            new Position(1638, 3997)
    };

    private static final Position[] BRAZIER_ATTACK_LOCATIONS = new Position[] {
            new Position(1622, 3999),
            new Position(1622, 4015),
            new Position(1638, 4015),
            new Position(1638, 3999)
    };

    private static final Position[][] BRAZIER_OPTION_LOCATIONS = new Position[][] {
            { new Position(1621, 3996),	new Position(1619, 3998) },
            { new Position(1621, 4018),	new Position(1619, 4016) },
            { new Position(1639, 4018),	new Position(1641, 4016) },
            { new Position(1641, 3998),	new Position(1639, 3996) }
    };

    private BrazierStates[] brazierStates = new BrazierStates[] {
            BrazierStates.UNLIT,
            BrazierStates.UNLIT,
            BrazierStates.UNLIT,
            BrazierStates.UNLIT
    };
}
