package com.overload.game.content.minigames.wintertodt;

public enum BrazierStates {

    UNLIT(0, 29312),
    BROKEN(1, 29313),
    LIT(2, 29314);

    private final int iconId;
    private final int objectId;

     BrazierStates(int iconId, int objectId) {
        this.iconId = iconId;
        this.objectId = objectId;
    }

    public int getId() {
        return iconId;
    }

    public int getObjectId() {
        return objectId;
    }

}