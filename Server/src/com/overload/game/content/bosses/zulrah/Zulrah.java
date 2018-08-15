package com.overload.game.content.bosses.zulrah;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Position;
//import com.overload.game.model.areas.impl.instance.ZulrahInstance;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;

public class Zulrah {
	public static void Enter(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		TaskManager.submit(new Task(1, player, false) {
            int tick = 0;
            @Override
            protected void execute() {

                if (tick == 0) {
            		player.getPacketSender().sendScreenFade("Welcome to Zulrah's shrine", 1, 2);
                }
                if (tick == 3) {
                    player.moveTo(new Position(2268, 3069, player.getIndex() * 4));
            		player.getPacketSender().sendScreenFade("Welcome to Zulrah's shrine", -1, 2);
            		stop();
                }
                tick++;
            }

            @Override
            public void stop() {
            	setEventRunning(false);
            }
        });
	}
}
