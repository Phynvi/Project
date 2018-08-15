package com.overload.game.content.dialogues.impl.npc;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.dialogues.impl.Dialogue;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.container.shop.ShopManager;
import com.overload.util.ShopIdentifiers;

public class D_512 extends Dialogue {
    @Override
    public void execute(Player player, NPC npc) {
        if (npc == null)
            return;

        if (player.getNpcTalking() == npc.getId()) {
            switch (player.getDialogueState()) {
                default://Temporary
    	    		ShopManager.open(player, ShopIdentifiers.SUPPLY_STORE);
                    DialogueHandler.close(player, false);
                    break;
            }
        }
    }

    @Override
    public boolean handleActions(Player player, NPC npc) {
        if (player.getDialogueActionId() == -1)
            return false;
        
		switch (player.getDialogueActionType()) {
			case 1://Two Options
				return handleTwo(player, npc);
			case 2://Three Options
				return handleThree(player, npc);
			case 3://Four Options
				return handleFour(player, npc);
			case 4://Five Options
				return handleFive(player, npc);
		}
		return false;
    }

	@Override
	public boolean handleTwo(Player player, NPC npc) {
		return false;
	}

	@Override
	public boolean handleThree(Player player, NPC npc) {
        return false;
	}

	@Override
	public boolean handleFour(Player player, NPC npc) {
		return false;
	}

	@Override
	public boolean handleFive(Player player, NPC npc) {
		// TODO Auto-generated method stub
		return false;
	}
}
