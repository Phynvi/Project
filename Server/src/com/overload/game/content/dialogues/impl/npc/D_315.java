package com.overload.game.content.dialogues.impl.npc;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.dialogues.impl.Dialogue;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.container.shop.ShopManager;
import com.overload.util.ShopIdentifiers;

public class D_315 extends Dialogue {

    @Override
    public void execute(Player player, NPC npc) {
        if (npc == null)
            return;

        if (player.getNpcTalking() == npc.getId()) {
            switch (player.getDialogueState()) {
                case 1:
                    DialogueHandler.npcChat(player, npc.getId(), "Hello, What are u seeking for?");
                    player.setDialogueState(2);
                    break;
                case 2:
                    if (!player.getSlayer().getTask().isPresent()) {
                        DialogueHandler.sendOptions(player, new String[]{
                                "Who are you?",
                                "What is Bounty Hunter?",
                                "Do you have anything to trade?",
                                "Er...nothing..."
                        });
                    } else {
                        DialogueHandler.sendOptions(player, new String[]{
                                "Do you have anything to trade?",
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
            }
        }

    }

    @Override
    public boolean handleActions(Player player, NPC npc) {
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
                ShopManager.open(player, ShopIdentifiers.PVP_SHOP);
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
        return false;
    }
}
