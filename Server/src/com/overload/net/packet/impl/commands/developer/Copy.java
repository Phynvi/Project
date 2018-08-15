package com.overload.net.packet.impl.commands.developer;

import java.util.Optional;

import com.overload.game.World;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Item;
import com.overload.game.model.container.impl.Bank;
import com.overload.net.packet.impl.commands.Command;

public class Copy extends Command {

	@Override
	public void execute(Player player, String input) {
		String args[] = input.split(" ");
		String type = args[0];
		if (type.toLowerCase() == "bank") {
			String player2 = args[1];
			Optional<Player> plr = World.getPlayerByName(player2);
			if(plr.isPresent()) {
				for(int i = 0; i < Bank.TOTAL_BANK_TABS; i++) {
					if(player.getBank(i) != null) {
						player.getBank(i).resetItems();
					}
				}
				for(int i = 0; i < Bank.TOTAL_BANK_TABS; i++) {
					if(plr.get().getBank(i) != null) {
						for(Item item : plr.get().getBank(i).getValidItems()) {
							player.getBank(i).add(item, false);
						}
					}
				}
			}
		} else if (type.toLowerCase() == "inv") {
			String player2 = args[1];
			Optional<Player> plr = World.getPlayerByName(player2);
			if(plr.isPresent()) {
				player.getInventory().resetItems().refreshItems();
				player.getInventory().addItems(plr.get().getInventory().getItems(), true);
				player.getInventory().refreshItems();
			}
		} else if (type.toLowerCase() == "equip") {
			String player2 = args[1];
			Optional<Player> plr = World.getPlayerByName(player2);
			if(plr.isPresent()) {
				player.getEquipment().resetItems().refreshItems();
				player.getEquipment().addItems(plr.get().getEquipment().getItems(), true);
				player.getEquipment().refreshItems();
			}
		}
	}

}
