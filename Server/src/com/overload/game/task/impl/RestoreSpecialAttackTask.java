package com.overload.game.task.impl;

import com.overload.game.content.combat.CombatSpecial;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.task.Task;

/**
 * A {@link Task} implementation which handles
 * the regeneration of special attack.
 *
 * @author Professor Oak
 */
public class RestoreSpecialAttackTask extends Task {

    private final Character character;

    public RestoreSpecialAttackTask(Character character) {
        super(20, character, false);
        this.character = character;
        character.setRecoveringSpecialAttack(true);
    }

    @Override
    public void execute() {
        if (character == null || !character.isRegistered() || character.getSpecialPercentage() >= 100 || !character.isRecoveringSpecialAttack()) {
            character.setRecoveringSpecialAttack(false);
            stop();
            return;
        }
        int amount = character.getSpecialPercentage() + 5;
        if (amount >= 100) {
            amount = 100;
            character.setRecoveringSpecialAttack(false);
            stop();
        }
        character.setSpecialPercentage(amount);

        if (character.isPlayer()) {
            Player player = character.getAsPlayer();
            CombatSpecial.updateBar(player);
            if (amount == 25 || amount == 50 || amount == 75 || amount == 100) {
                player.getPacketSender().sendMessage("Your special attack energy is now " + player.getSpecialPercentage() + "%.");
            }
        }
    }
}