package com.overload.game.model.syntax.impl;

import com.overload.game.content.skill.smithing.Smithing.Smelting;
import com.overload.game.content.skill.smithing.SmithingBarData;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.syntax.EnterSyntax;

public class SmeltBarX implements EnterSyntax {

    private final SmithingBarData.Bar bar;

    public SmeltBarX(SmithingBarData.Bar bar) {
        this.bar = bar;
    }

    @Override
    public void handleSyntax(Player player, String input) {
    }

    @Override
    public void handleSyntax(Player player, int input) {
        if (input <= 0 || input > Integer.MAX_VALUE) {
            return;
        }
        player.getSkillManager().startSkillable(new Smelting(bar, input));
    }
}
