package com.overload.game.content.dialogues.impl.npc;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.dialogues.impl.Dialogue;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;

public class D_1617 extends Dialogue {
    @Override
    public void execute(Player player, NPC npc) {
        if (npc == null)
            return;

        if (player.getNpcTalking() == npc.getId()) {
        	if (!player.getZulrahLostItems().isEmpty()) {
	            switch (player.getDialogueState()) {
	                case 1:
	                    DialogueHandler.npcChat(player, npc.getId(), new String[] {
	                            "Hello, i have some items for you,",
	                            "Would you like to claim them back?"
	                    });
	                    player.setDialogueState(2);
	                    break;
	                case 2:
	                    DialogueHandler.sendOptions(player, new String[] {
	                            "Sure!",
	                            "Maybe another time."
	                    });
	            		player.setDialogueState(999);
	                    break;
	                case 3:
	                    DialogueHandler.npcChat(player, npc.getId(), new String[] {
	                            "Ok, but please remember if you log out",
	                            "or die anywhere else your items will be",
	                            "gone forever!"
	                    });
	            		player.setDialogueState(-1);
	                	break;
	                case 4:
	                    DialogueHandler.npcChat(player, npc.getId(), new String[] {
	                            "Excellent, that will cost you",
	                            player.getZulrahLostItems().getItemsValue() + " Coins",
	                            "are you sure you want to continue?"
	                    });
	            		player.setDialogueState(5);
	                	break;
	                case 5:
	                    DialogueHandler.sendOptions(player, "Pay " + player.getZulrahLostItems().getItemsValue() + " Coins?", new String[] {
	                            "Yes please.",
	                            "No thank you.",
	                            "How did you work out that value?"
	                    });
	            		player.setDialogueState(999);
	                	break;
	                case 6:
	                    DialogueHandler.npcChat(player, npc.getId(), new String[] {
	                            "The cost you have to pay is worked out by",
	                            "your lost items value combined; however",
	                            "has a limit of 500k Coins."
	                    });
	            		player.setDialogueState(5);
	                	break;
	                default://Temporary
	                    if (handleActions(player, npc))
	                        return;
	                    break;
	            }
        	} else {
                DialogueHandler.npcChat(player, npc.getId(), "I have nothing for you.");
        		player.setDialogueState(-1);
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
        		player.setDialogueState(4);
                return true;
            case 2:
        		player.setDialogueState(3);
                return true;
            default:
                return false;
        }
	}

	@Override
	public boolean handleThree(Player player, NPC npc) {
        int handle = player.getDialogueActionId();
        player.setDialogueAction(-1, -1);

        switch (handle) {
            case 1:
            	player.getZulrahLostItems().retain();
                DialogueHandler.close(player, true);
                return true;
            case 2:
        		player.setDialogueState(3);
                return true;
            case 3:
        		player.setDialogueState(6);
        		return true;
            default:
                return false;
        }
	}

	@Override
	public boolean handleFour(Player player, NPC npc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleFive(Player player, NPC npc) {
		// TODO Auto-generated method stub
		return false;
	}
}
