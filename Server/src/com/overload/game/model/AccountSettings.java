package com.overload.game.model;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.syntax.impl.SetEmail;
import com.overload.game.model.syntax.impl.SetPassword;

public class AccountSettings {
	
	public static String currentName = "Discord";

	public static String getCurrentName() {
		return currentName;
	}

	public static void setCurrentName(String currentName) {
		AccountSettings.currentName = currentName;
	}

	public static void open(Player player) {
		player.getPacketSender().sendInterface(25000);
		updateInterface(player);
	}
	
	public static void updateInterface(Player player) {
		if(getCurrentName() == "Discord") {
			String state = "";
			if(player.getDiscordUID() == "") {
				state = "@red@Not Linked";
			} else {
				state = "@gre@Linked";
			}
			player.getPacketSender().sendString(25012,"Setup Discord (Linked State: " + state + " )");
		} else if (getCurrentName() == "Password") {
			player.setEnterSyntax(new SetPassword());
            player.getPacketSender().sendEnterInputPrompt("Enter a new Password");
		} else if (getCurrentName() == "Username") {
			
		} else if (getCurrentName() == "Email") {
			player.setEnterSyntax(new SetPassword());
            player.getPacketSender().sendEnterInputPrompt("Enter a Email");
		}
	}
	
	public static void buttonClick(Player player, int Buttonid) {
		switch(Buttonid) {
			case 25002:
				player.getPacketSender().sendInterface(25000);
				setCurrentName("Discord");
			break;
			case 25003:
				player.setEnterSyntax(new SetPassword());
	            player.getPacketSender().sendEnterInputPrompt("Enter a new Password");
			break;
			case 25004:
				player.getPacketSender().sendMessage("@red@ Not Ready Yet");
			break;
			case 25005:
				player.setEnterSyntax(new SetEmail());
	            player.getPacketSender().sendEnterInputPrompt("Enter a Email");
			break;
		}
		updateInterface(player);
	}

}
