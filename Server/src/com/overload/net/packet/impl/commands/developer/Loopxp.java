package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Skill;
import com.overload.net.packet.impl.commands.Command;

public class Loopxp extends Command {

	@Override
	public void execute(Player player, String input) {

		for (int i = 0; i < Integer.parseInt(input); i++) {
			for (Skill s : Skill.values()) {
				player.getSkillManager().addExperience(s, 1000000);
			}
		}
	}

}
