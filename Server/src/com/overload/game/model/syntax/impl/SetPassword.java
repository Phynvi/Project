package com.overload.game.model.syntax.impl;


import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.syntax.EnterSyntax;

public class SetPassword implements EnterSyntax {
	
	@Override
	public void handleSyntax(Player player, String input) {
		DialogueHandler.sendStatement(player, "Password has Been Set!");
		player.setPassword(input);
		
	}

	@Override
	public void handleSyntax(Player player, int input) {
		DialogueHandler.sendStatement(player, "Password has Been Set!");
		player.setPassword(input + "");
	}

}
