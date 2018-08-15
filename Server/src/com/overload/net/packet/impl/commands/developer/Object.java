package com.overload.net.packet.impl.commands.developer;

import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.object.ObjectManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.net.packet.impl.commands.Command;

public class Object extends Command {

	@Override
	public void execute(Player player, String input) {
		String[] args = input.split(" ");
		if (args.length > 1)
            ObjectManager.register(new GameObject(Integer.parseInt(args[0]), player.getPosition(), Integer.parseInt(args[1])), true);
		else
            ObjectManager.register(new GameObject(Integer.parseInt(args[0]), player.getPosition(), 10), true);
	}

}
