package com.overload.game.task.impl;

import java.util.Optional;

import com.overload.game.collision.RegionManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Action;
import com.overload.game.model.Flag;
import com.overload.game.model.ForceMovement;
import com.overload.game.model.Position;
import com.overload.game.task.Task;

/**
 * A {@link Task} implementation that handles forced movement.
 * An example of forced movement is the Wilderness ditch.
 *
 * @author Professor Oak
 */
public class ForceMovementTask extends Task {

    private Player player;
    private Position end;
    private Position start;
    private Optional<Action> onComplete;

    public ForceMovementTask(Player player, int delay, ForceMovement forceM) {
        super(delay, player, (delay == 0 ? true : false));
        this.player = player;
        this.start = forceM.getStart().clone();
        this.end = forceM.getEnd().clone();
        this.onComplete = Optional.empty();

        //Reset combat
        player.getCombat().reset();

        //Reset movement queue
        player.getMovementQueue().reset();

        //Playerupdating
        player.setForceMovement(forceM);

        RegionManager.getClipping(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());

    }

    public ForceMovementTask(Player player, int delay, ForceMovement forceM, Optional<Action> onComplete) {
        super(delay, player, (delay == 0 ? true : false));
        this.player = player;
        this.start = forceM.getStart().clone();
        this.end = forceM.getEnd().clone();
        this.onComplete = onComplete;

        //Reset combat
        player.getCombat().reset();

        //Reset movement queue
        player.getMovementQueue().reset();

        //Playerupdating
        player.setForceMovement(forceM);

        RegionManager.getClipping(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());

    }

    @Override
    protected void execute() {
        for (int i = 0; i < 8; i++) {
            if (!RegionManager.canMove(player.getForceMovement().getEnd(), i)) {
                player.setForceMovement(null);
                stop();
                return;
            }
        }
        if (player.getForceMovement() != null)
            player.getUpdateFlag().flag(Flag.FORCED_MOVEMENT);
        int x = start.getX() + end.getX();
        int y = start.getY() + end.getY();
        player.moveTo(new Position(x, y, player.getPosition().getZ()));
        player.setForceMovement(null);
        if (onComplete.isPresent()) {
        	onComplete.get().execute();
        	onComplete.get().execute(player);
        }
        stop();
    }
}
