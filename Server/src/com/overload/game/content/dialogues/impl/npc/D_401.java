package com.overload.game.content.dialogues.impl.npc;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.dialogues.impl.Dialogue;
import com.overload.game.content.skill.slayer.SlayerTask;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.container.shop.ShopManager;
import com.overload.util.ItemIdentifiers;
import com.overload.util.ShopIdentifiers;

public class D_401 extends Dialogue {
    @Override
    public void execute(Player player, NPC npc) {
        if (npc == null)
            return;

        if (player.getNpcTalking() == npc.getId()) {
            switch (player.getDialogueState()) {
            	case 1:
            		DialogueHandler.npcChat(player, npc.getId(), "'Ello, and what are you after then?");
            		player.setDialogueState(2);
            		break;
            	case 2:
            		if (!player.getSlayer().getTask().isPresent()) {
            			DialogueHandler.sendOptions(player, new String[] {
                                "Who are you?",
                                "Do you have anything to trade?",
                                "View Rewards",
                                "Er...nothing..."
                        });
            		} else {
            			DialogueHandler.sendOptions(player, new String[] {
                                "I need another assignment.",
                                "Do you have anything to trade?",
                                "View Rewards",
                                "Er...nothing..."
                        });
            		}
            		player.setDialogueState(999);
            		break;
            	case 3:
            		DialogueHandler.npcChat(player, npc.getId(), new String[] {
            				"I'm a slayer master. I train adventurers to learn",
            				"the weaknesses of seemingly invunerable monsters.",
            				"To learn how, you need to kill specific..."
            		});
            		player.setDialogueState(4);
            		break;
            	case 4:
            		DialogueHandler.npcChat(player, npc.getId(), new String[] {
            				"monsters. I'll identify suitable targets and",
            				"assign you a quota."
            		});
            		player.setDialogueState(5);
            		break;
            	case 5:
            		DialogueHandler.playerChat(player, new String[] {
            				"What's first?"
            		});
            		player.setDialogueState(6);
            		break;
            	case 6:
        			player.getSlayer().createNewTask(npc.getId());
    				SlayerTask task = player.getSlayer().getTask().get();
    				String taskName = task.getPrimaryName();
            		DialogueHandler.npcChat(player, npc.getId(), new String[] {
        				"We'll start you off hunting " + taskName + (player.getSlayer().getTaskAmount() > 1 && (taskName.substring(0, taskName.length() - 1) == "s") ? "" : "s") + ". You'll need to",
        				"kill " + player.getSlayer().getTaskAmount() + " of them. You'll also need",
        				"this enchanted gem - it allows Slayer Masters..."
            		});
            		if (player.getInventory().getFreeSlots() >= 0) {
            			player.getInventory().add(ItemIdentifiers.ENCHANTED_GEM, 1);
            		} else {
            			player.getBank(0).add(ItemIdentifiers.ENCHANTED_GEM, 1);
            			player.sendMessage("@dre@Your Inventory was full an Enchanted Gem has been sent to your bank.");
            		}
            		player.setDialogueState(7);
            		break;
            	case 7:
            		DialogueHandler.npcChat(player, npc.getId(), new String[] {
        				"...like myself to contact you and update you on",
        				"your progress. Don't worry if you lose it;",
        				"you can buy another from any Slayer Master."
            		});
            		player.setDialogueState(8);
            		break;
            	case 8:
            		DialogueHandler.playerChat(player, new String[] {
            				"Got any tips for me?"
            		});
            		player.setDialogueState(9);
            		break;
            	case 9:
    				task = player.getSlayer().getTask().get();
            		DialogueHandler.npcChat(player, npc.getId(), new String[] {
            				"This creature is weak against most weapons,",
            				"but they can be found in or around",
            				(task.getLocations()[0] != null ? task.getLocations()[0] : "")
            		});
            		player.setDialogueState(10);
            		break;
            	case 10:
            		DialogueHandler.playerChat(player, new String[] {
            				"Okay, great!"
            		});
            		player.setDialogueState(-1);
            		break;
            	case 11:
            		if (player.getSlayer().isOnTuraelTask()) {
                		DialogueHandler.npcChat(player, npc.getId(), new String[] {
                				"You're still on an assignment.",
                				"You need to finish that one first."
                		});
                		player.setDialogueState(12);
            		} else {
        				task = player.getSlayer().getTask().get();
        				taskName = task.getPrimaryName();
                		DialogueHandler.npcChat(player, npc.getId(), new String[] {
                				"You're still hunting " + taskName + (player.getSlayer().getTaskAmount() > 1 && (taskName.substring(0, taskName.length() - 1) == "s") ? "" : "s") + ", although it's a",
                				"tougher assignment than I'd normally give... I guess I could give you a new assignment,",
                				"if you'd like. If you do get a new one, then you will lose your current slayer target",
                				"and reset your task streak. Is that ok?"
                		});
                		player.setDialogueState(13);
            		}
            		break;
            	case 12:
    				task = player.getSlayer().getTask().get();
    				taskName = task.getPrimaryName();
            		DialogueHandler.sendStatement(player, "You need to kill " + player.getSlayer().getTaskAmount() + " " + taskName + (player.getSlayer().getTaskAmount() > 1 && (taskName.substring(0, taskName.length() - 1) == "s") ? "" : "s"));
            		player.setDialogueState(-1);
            		break;
            	case 13:
        			DialogueHandler.sendOptions(player, new String[] {
                            "Yes, please.",
                            "No, thanks."
                    });
        			player.setDialogueState(999);
            		break;
            	case 14:
        			player.getSlayer().createNewTask(npc.getId());
    				task = player.getSlayer().getTask().get();
    				taskName = task.getPrimaryName();
            		DialogueHandler.npcChat(player, npc.getId(), new String[] {
            				"Your new assignment is " + (player.getSlayer().getTaskAmount() > 1 && (taskName.substring(0, taskName.length() - 1) == "s") ? "" : "s") + ".",
            				"You'll need to kill " + player.getSlayer().getTaskAmount() + " of them. You'll also"
            		});
            		player.setDialogueState(-1);
            	case 999:
                	handleActions(player, npc);
                	break;
                default:
                    DialogueHandler.close(player, true);
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
        		player.setDialogueState(14);
	            return true;
	        case 2:
                DialogueHandler.close(player, true);
                return true;
            default:
                return false;
        }
	}

	@Override
	public boolean handleThree(Player player, NPC npc) {
		return false;
	}

	@Override
	public boolean handleFour(Player player, NPC npc) {
        int handle = player.getDialogueActionId();
        player.setDialogueAction(-1, -1);

        switch (handle) {
	        case 1:
        		if (!player.getSlayer().getTask().isPresent()) {
            		player.setDialogueState(3);
        		} else {
            		player.setDialogueState(11);
        		}
	            return true;
	        case 2:
	    		ShopManager.open(player, ShopIdentifiers.SLAYER_STORE);
                DialogueHandler.close(player, false);
	            return true;
	        case 3:
	        	player.getSlayer().openInterface(0);
	        	return true;
            case 4:
                DialogueHandler.close(player, true);
                return true;
            default:
                return false;
        }
	}

	@Override
	public boolean handleFive(Player player, NPC npc) {
		// TODO Auto-generated method stub
		return false;
	}
}
