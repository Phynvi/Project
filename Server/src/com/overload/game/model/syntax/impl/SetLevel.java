package com.overload.game.model.syntax.impl;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Skill;
import com.overload.game.model.rights.Right;
import com.overload.game.model.syntax.EnterSyntax;

public class SetLevel implements EnterSyntax {

    private Skill skill;

    public SetLevel(Skill skill) {
        this.skill = skill;
    }

    @Override
    public void handleSyntax(Player player, String input) {

    }

    @Override
    public void handleSyntax(Player player, int input) {
        int max = 99;
		if (player.getRights().isOrInherits(Right.GAME_DEVELOPER)) {
            max = 9999;
        }
        if (input <= 0 || input > max) {
            player.getPacketSender().sendMessage("Invalid syntax. Please enter a level in the range of 1-99.");
            return;
        }
        player.getSkillManager().setLevel(skill, input);
    }
}
