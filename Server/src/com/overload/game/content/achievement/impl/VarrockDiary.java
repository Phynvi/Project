package com.overload.game.content.achievement.impl;

import com.overload.game.content.Requirement;
import com.overload.game.content.achievement.Diary;
import com.overload.game.content.achievement.DiaryRegions;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.rights.Right;

public class VarrockDiary extends Diary {
	
	public VarrockDiary() {
		super(DiaryRegions.VARROCK);
	}

	@Override
	public void process(Player player) {
		for (Tasks task : Tasks.getByDiaryType(player.getLastClickDiaryType())) {
			if (player.getLastClickOptionType() != task.getOptionType())
				continue;

			boolean cont = false;
			for (int i = 0; i < task.getActionId().length; i++) {
                if (player.getLastClickActionId()[i] != task.getActionId()[i])
                    cont = true;
            }

            if (cont)
                continue;

			if (player.isDebug() && player.getRights().isOrInherits(Right.GAME_DEVELOPER)) {
				player.sendMessage("Diary Type: " + player.getLastClickDiaryType())
					  .sendMessage("Option Type: " + player.getLastClickOptionType())
					  .sendMessage(player.getLastClickDiaryType().name() + " ID: " + player.getLastClickActionId());
			}
			
			if (task.getItemReqs() != null) {
				if (!player.getInventory().contains(task.getItemReqs())) {
					return;
				}
				if (task.removeItems()) {
                    player.getInventory().delete(task.getItemReqs());
                }
			}
			
			if (task.getQuestReqs() != null) {
				//Check Quest Reqs in here when done....
			}
			
			if (task.getSkillReqs() != null) {
				for (Requirement req : task.getSkillReqs()) {
					if (player.getSkillManager().getCurrentLevel(req.getSkill()) < req.getLevel()) {
						return;
					}
				}
			}
			
			progress(player, task);
		}
		player.clearLastClickData();
	}

	@Override
	public void progress(Player player, Tasks task) {
    	if (player.getDiaryAmount(task.name()) < task.getAmount()) {
			player.addDiaryTask(task.name(), 1);
			checkCompleted(player, task);
    	}
	}
	
}
