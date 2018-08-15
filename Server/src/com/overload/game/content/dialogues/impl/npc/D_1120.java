package com.overload.game.content.dialogues.impl.npc;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.dialogues.impl.Dialogue;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;

public class D_1120 extends Dialogue {

	@Override
	public void execute(Player player, NPC npc) {
		if (npc == null)
            return;

        if (player.getNpcTalking() == npc.getId()) {
            switch (player.getDialogueState()) {
                case 1:
                    DialogueHandler.sendOptions(player, new String[] {
                            "Practice",
                            "Endurance",
                            "Rumble",
                            "Previous: " + (player.getNightmareZone().hasCustomRumble() ? "Customisable Rumble (" + player.getNightmareZone().getCustomDifficulty() + ")"
                            															: "No Previous...")
                    });
                    player.setDialogueState(500);
                    break;
                case 2:
                	DialogueHandler.npcChat(player, npc.getId(), new String[] {
                            "For a customisable Rumble dream, normal mode, I'll",
                            "want 22,000 coins.",
                            "I'll deduct the money from the coffer when you start",
                            "the dream."
                    });
                    player.setDialogueState(3);
                	break;
                case 3:
                    DialogueHandler.sendOptions(player, "Agree to pay 22,000 coins?", new String[] {
                            "Yes",
                            "No",
                    });
                    player.setDialogueState(500);
                    break;
                case 500:
                	handleActions(player, npc);
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
		int handle = player.getDialogueActionId();
        player.setDialogueAction(-1, -1);

        switch (handle) {
            case 1:
                player.sendMessage("You must have 22,000 GP in Dominics Coffer");
                DialogueHandler.close(player, true);
                return true;
            default:
                DialogueHandler.close(player, true);
                return false;
        }
	}

	@Override
	public boolean handleThree(Player player, NPC npc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleFour(Player player, NPC npc) {
		int handle = player.getDialogueActionId();
        player.setDialogueAction(-1, -1);

        switch (handle) {
            case 3:
                player.setDialogueState(2);
                return true;
            default:
                DialogueHandler.close(player, true);
                return false;
        }
	}

	@Override
	public boolean handleFive(Player player, NPC npc) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
