package com.overload.game.content.dialogues.impl.npc;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.dialogues.impl.Dialogue;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.util.Misc;

public class D_0 extends Dialogue {

	String[][] msg = {
			{"Uhm, can we talk later? My cat is dieing!"},
			{"Oh god, not this person again...."},
			{"I feel like someone is trying to talk to me,", "but I dont see anyone"},
			{"You again? Didn't I tell you I'm busy?"},
			{"No, I'm not giving you a party hat. Good day!"},
			{"I'm not talking to you, scammer!"},
			{"Hello " + (Misc.random(2) == 1 ? "Christ-" : "Jowc-") + " wait you're not him! Never mind,", "I'm too busy to talk"},
			{"Did you know that the staff are always watching?", "Be careful..."},
			{"Damn! I lost my bronze axe to Tzhaar Jad.", "Can you get it for me?"},
			{"Another adventurer... Stop wasting your time,", "and quit talking to me!"},
			{"Psst... You there. The best money making method", "is selling mind bombs.. Mind blown..."	}
	};
	
	@Override
	public void execute(Player player, NPC npc) {
		if (npc == null)
            return;

        if (player.getNpcTalking() == npc.getId()) {
        	String[] s = msg[Misc.random(msg.length-1)];
            switch (player.getDialogueState()) {
                default:
                	DialogueHandler.npcChat(player, npc.getId(), s);
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
		return false;
	}
	
}
