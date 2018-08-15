package com.overload.net.packet.impl;

import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.container.impl.Bank;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketListener;
import com.overload.util.Misc;

public class ExamineItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int itemId = packet.readShort();
		int interfaceId = packet.readInt();

		// Coins
		if (itemId == 995 || itemId == 13307) {
			if (player.isDebug())
				player.sendMessage("Interface: " + interfaceId);
			int amount = player.getInventory().getAmount(itemId);
			if (interfaceId >= Bank.CONTAINER_START && interfaceId < Bank.CONTAINER_START + Bank.TOTAL_BANK_TABS) {
				amount = player.getBank(player.getCurrentBankTab()).getAmount(itemId);
			}
			player.getPacketSender().sendMessage("@red@"
					+ Misc.insertCommasToNumber("" + amount + "") + "x " + (itemId == 995 ? "coins" : "blood money") + ".");
			return;
		}

		ItemDefinition itemDef = ItemDefinition.forId(itemId);
		if (itemDef != null) {
			player.getPacketSender().sendMessage(itemDef.getExamine());
		}
	}

}
