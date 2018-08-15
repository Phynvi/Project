package com.overload.net.packet.impl.commands.developer;

import com.overload.Server;
import com.overload.game.World;
import com.overload.game.content.clan.ClanChatManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.net.packet.impl.commands.Command;

public class Update extends Command {

	@Override
	public void execute(Player player, String input) {
		String[] args = input.split(" ");
		
		int time = 15;
		if (args[0] != "")
			time = Integer.parseInt(args[0]);
        if (time < 15) {
        	time = 15;
        	player.getPacketSender().sendMessage("@dre@Update time set to 15 seconds...");
        	player.getPacketSender().sendMessage("@dre@We need time to stop and save all tasks.");
        }
		final int shutdownClients = !(args.length > 1) ? 0 : Integer.parseInt(args[1]) == 1 ? 1 : 0;
        
        Server.setUpdating(true);
        for (Player players : World.getPlayers()) {
            if (players == null) {
                continue;
            }
            players.getPacketSender().sendSystemUpdate(time);
        }
        TaskManager.submit(new Task(time) {
            @Override
            protected void execute() {
                for (Player player : World.getPlayers()) {
                    if (player != null) {
                    	if (shutdownClients == 1) {
                    		player.getPacketSender().sendExit();
                    	} else {
                    		player.requestLogout();
                    	}
                    }
                }
                ClanChatManager.save();
                Server.getLogger().info("Update task finished!");
                stop();
            }
        });
	}
	
}
