package com.overload.game.content.minigames;

public class MinigameAttributes {

    private final PestControlAttributes pestControlAttributes = new PestControlAttributes();

    public class PestControlAttributes {

        public PestControlAttributes() {

        }

        private int damageDealt;

        public int getDamageDealt() {
            return damageDealt;
        }

        public void setDamageDealt(int damageDealt) {
            this.damageDealt = damageDealt;
        }

        public void incrementDamageDealt(int damageDealt) {
            this.damageDealt += damageDealt;
        }
    }


    public PestControlAttributes getPestControlAttributes() {
        return pestControlAttributes;
    }


    public MinigameAttributes() {}

}
