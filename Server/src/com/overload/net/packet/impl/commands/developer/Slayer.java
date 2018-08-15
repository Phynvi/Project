package com.overload.net.packet.impl.commands.developer;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.skill.slayer.SlayerTask;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Slayer extends Command {

	@Override
	public void execute(Player player, String input) {
		String[] args = input.split(" ");
		if (args[0].equalsIgnoreCase("get")) {
			if (player.getSlayer().getTask().isPresent()) {
				SlayerTask task = player.getSlayer().getTask().get();
				String taskName = task.getPrimaryName();
				DialogueHandler.npcChat(player, player.getSlayer().getMaster(), new String[] {
						"Your slayer assignment is to kill " + player.getSlayer().getTaskAmount() + " " + task.getPrimaryName() + (player.getSlayer().getTaskAmount() > 1 && (taskName.substring(0, taskName.length() - 1) == "s") ? "" : "s"),
						"You have completed: " + player.getSlayerTasksCompleted() + " and have " + player.getSlayer().getPoints() + " points.",
						"You have completed " + player.getSlayer().getConsecutiveTasks() + " consecutive tasks."
				});
			} else {
				DialogueHandler.npcChat(player, player.getSlayer().getMaster(), "You don't have a slayer assignment, come and speak to me to get one.");
			}
		} else if (args[0].equalsIgnoreCase("set")) {
			player.getSlayer().createNewTask(Integer.parseInt(args[1]));
		}
	}

}
