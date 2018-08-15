package com.overload.game.model.syntax.impl;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.syntax.EnterSyntax;

public class BankPin implements EnterSyntax {

    @Override
    public void handleSyntax(Player player, String input) {
        if(player.getPin() == "") {
        	player.setPin(input);
        } else {
        	if(player.getPin() == input) {
            	player.setEnteredPin(true);
        	} else {
        		player.getPacketSender().sendMessage("@red@ Incorrect Bank pin.");
        	}
        }
    }

    @Override
    public void handleSyntax(Player player, int input) {

    }

}
