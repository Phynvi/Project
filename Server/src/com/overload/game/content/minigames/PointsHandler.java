package com.overload.game.content.minigames;

import com.overload.game.entity.impl.player.Player;

public class PointsHandler {

    @SuppressWarnings("unused")
    private Player p;

    public PointsHandler(Player p) {
        this.p = p;
    }

    public void reset() {
        commendations = (int) (slayerPoints = 0);
    }

    @SuppressWarnings("unused")
    private int slayerPoints;
    private int commendations;


    public int getCommendations() {
        return this.commendations;
    }

    public void setCommendations(int commendations, boolean add) {
        if(add)
            this.commendations += commendations;
        else
            this.commendations = commendations;
    }
}
