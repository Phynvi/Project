package com.overload.game.content.dialogues.impl.npc;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.dialogues.impl.Dialogue;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;

public class D_5036 extends Dialogue {

    @Override
    public void execute(Player player, NPC npc) {
        if (npc == null)
            return;

        if (player.getNpcTalking() == npc.getId()) {
            switch (player.getDialogueState()) {
                case 1:
                    DialogueHandler.npcChat(player, npc.getId(), new String[] {
                            "Hello, i offer free potions would u like one?"
                    });
                    player.setDialogueState(2);
                    break;
                default://Temporary
                    if (handleActions(player, npc))
                        return;
                    DialogueHandler.sendOptions(player, new String[] {
                            "Yes",
                            "No"
                    });
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
                player.getInventory().add(113, 1);
                DialogueHandler.close(player, true);
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
        // TODO Auto-generated method stub
        return false;
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