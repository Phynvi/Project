package com.overload.net.packet.impl.commands.developer;

import com.overload.game.content.combat.WeaponInterfaces;
import com.overload.game.content.skill.SkillManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Skill;
import com.overload.net.packet.impl.commands.Command;

public class Setlevel extends Command {

	@Override
	public void execute(Player player, String input) {
		String[] parts = input.split(" ");
		if (parts.length > 2) {
			Skill skill = Skill.values()[Integer.parseInt(parts[0])];
			int level = Integer.parseInt(parts[1]);
			player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill, SkillManager.getExperienceForLevel(level));
			WeaponInterfaces.assign(player);
		} else {
			int level = Integer.parseInt(parts[2]);
			for (int i = Integer.parseInt(parts[0]); i < Integer.parseInt(parts[1]); i++) {
				Skill skill = Skill.values()[i];
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill, SkillManager.getExperienceForLevel(level));
				WeaponInterfaces.assign(player);
			}
		}
	}

}
