package com.overload.game.content.achievement.impl;

import com.overload.game.content.achievement.Diary;
import com.overload.game.content.achievement.DiaryRegions;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.rights.Right;

public class FaladorDiary extends Diary {
	
	public FaladorDiary() {
		super(DiaryRegions.FALADOR);
	}

	@Override
	public void process(Player player) {
		for (Tasks task : Tasks.getByDiaryType(player.getLastClickDiaryType())) {
			if (player.getLastClickOptionType() != task.getOptionType())
				continue;
			if (player.getLastClickActionId() != task.getActionId())
				continue;
			if (player.isDebug() && player.getRights().isOrInherits(Right.GAME_DEVELOPER)) {
				player.sendMessage("Diary Type: " + player.getLastClickDiaryType())
					  .sendMessage("Option Type: " + player.getLastClickOptionType())
					  .sendMessage(player.getLastClickDiaryType().name() + " ID: " + player.getLastClickActionId());
			}
		}
		player.clearLastClickData();
	}

	@Override
	public void progress(Player player, Tasks task) {
		
	}
	
}
