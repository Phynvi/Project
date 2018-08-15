package com.overload.game.model.syntax.impl;

import com.overload.game.content.presets.Presetables;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.syntax.EnterSyntax;
import com.overload.util.Misc;

public class ChangePresetName implements EnterSyntax {
	
	private int presetIndex;
	
	public ChangePresetName(final int presetIndex) {
		this.presetIndex = presetIndex;
	}

	@Override
	public void handleSyntax(Player player, String input) {
		
		player.getPacketSender().sendInterfaceRemoval();
		
		input = Misc.formatText(input);
		
		if(!Misc.isValidName(input)) {
			player.getPacketSender().sendMessage("Invalid name for preset. Please enter characters only.");
			player.setCurrentPreset(null);
			Presetables.open(player);
			return;
		}
		
		if(player.getPresets()[presetIndex] != null) {
			
			player.getPresets()[presetIndex].setName(input);
			player.getPacketSender().sendMessage("The preset's name has been updated.");
			
			Presetables.open(player);
		}
	}

	@Override
	public void handleSyntax(Player player, int input) {
	}

}
