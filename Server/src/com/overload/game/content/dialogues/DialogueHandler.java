package com.overload.game.content.dialogues;

import com.google.common.reflect.ClassPath;
import com.overload.game.content.RottenPotato;
import com.overload.game.content.bosses.zulrah.Zulrah;
import com.overload.game.content.dialogues.impl.Dialogue;
import com.overload.game.content.interactable.LadderHandler;
import com.overload.game.content.interactable.StairHandler;
import com.overload.game.content.minigames.barrows.Barrows;
import com.overload.game.definition.NpcDefinition;
import com.overload.game.definition.ObjectDefinition;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.MagicSpellbook;
import com.overload.game.model.rights.Right;
import com.overload.util.Misc;
import com.overload.util.NpcIdentifiers;
import com.overload.util.ObjectIdentifiers;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class DialogueHandler {

    public static boolean startDialogue(Player c, NPC npc) {
    	if (npcIgnored(npc.getId()))
    		return false;
    	String className;
        className = "D_" + npc.getId();
        String path = "com.overload.game.content.dialogues.impl.npc." + className;

        if (c.getDialogueState() == -1)
            c.setDialogueState(1);
        c.setNpcTalking(npc.getId());

        try {
            if (!DIALOGUE_MAP.containsKey(path)) {
                initialize(path);
            }
            DIALOGUE_MAP.get(path).execute(c, npc);
            return true;
        } catch (ClassNotFoundException e) {
            try {
                DIALOGUE_MAP.get("com.overload.game.content.dialogues.impl.npc.D_0").execute(c, npc);
            } catch (Exception e1) {
                c.getPacketSender().sendMessage("This NPC has no interest in talking to you.");
            }
        	return false;
        } catch (Exception e) {
            c.getPacketSender().sendMessage("This NPC has no interest in talking to you.");
            if (c.isDebug())
            	c.getPacketSender().sendMessage("Error while starting dialogue: D_" + className);
            e.printStackTrace();
            return true;
        }
    }

    private static boolean npcIgnored(int id) {
    	switch (id) {
	    	case NpcIdentifiers.VORKATH_ASLEEP:
	    		return true;
		}
		return false;
	}

	public static boolean proceed(Player c) {
        if (c.getNpcTalking() == -1 || c.getDialogueState() == -1) {
        	close(c, true);
            return false;
        }

        if (c.getNpcTalking() == -10) {
        	return handleGlobal(c, c.getDialogueClickType(), c.getDialogueClickId());
        }
        if (c.getNpcTalking() == 2147000000) {
        	switch (c.getDialogueState()) { 
	        	case 1:
	            	RottenPotato.eat(c);
	        		break;
	        	case 2:
	            	RottenPotato.slice(c);
	        		break;
	        	case 3:
	            	RottenPotato.peel(c);
	        		break;
	        	case 4:
	            	RottenPotato.mash(c);
	        		break;
        	}
        	return true;
        }
        NPC npc = new NPC(c.getNpcTalking(), c.getPosition());
        String className;
        className = "D_" + c.getNpcTalking();
        String path = "com.overload.game.content.dialogues.impl.npc." + className;
        try {

            if (!DIALOGUE_MAP.containsKey(path)) {
                initialize(path);
            }
            DIALOGUE_MAP.get(path).execute(c, npc);
            return true;
        } catch (ClassNotFoundException e) {
        	if (c.getRights().isOrInherits(Right.GAME_DEVELOPER)) {
        		c.getPacketSender().sendMessage("Couldn't find Dialogue:" + className);
            	c.getPacketSender().sendMessage(path);
        	}
            return false;
        } catch (Exception e) {
        	if (c.getRights().isOrInherits(Right.GAME_DEVELOPER)) {
        		c.getPacketSender().sendMessage("Error while starting dialogue: D_" + className);
        	}
            e.printStackTrace();
            return true;
        }
    }

    public static Map<String, Dialogue> DIALOGUE_MAP = new TreeMap<>();

    private static void initialize(String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class<?> commandClass = Class.forName(path);
        Object instance = commandClass.newInstance();
        if (instance instanceof Dialogue) {
            Dialogue dialogue = (Dialogue) instance;
            DIALOGUE_MAP.putIfAbsent(path, dialogue);
        }
    }

    public static void initialiseDialogues() throws IOException {
        ClassPath classPath = ClassPath.from(DialogueHandler.class.getClassLoader());
        String[] packages = {
                "com.overload.game.content.dialogues.impl.npc"
        };

        for (String pack : packages) {
            for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClasses(pack)) {
                try {
                    initialize(classInfo.getName());
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void close(Player player) {
    	close(player, true);
    }
    
    public static void close(Player player, boolean withRemoval) {
        player.setDialogueState(-1);
        player.setDialogueAction(-1, -1);
        player.setNpcTalking(-1);
        if (withRemoval)
        	player.getPacketSender().sendInterfaceRemoval();
    }
    
    private static final int[] NPC_DIALOGUE_ID = { 4885, 4890, 4896, 4903 };
	private static final int[] PLAYER_DIALOGUE_ID = { 971, 976, 982, 989 };
    private static final int[] OPTION_DIALOGUE_ID = { 13760, 2461, 2471, 2482, 2494, };
	private static final int[] STATEMENT_DIALOGUE_ID = { 356, 359, 363, 368, 374, };

    public static void sendOptions(Player player, String[] lines) {
        int firstChildId = OPTION_DIALOGUE_ID[lines.length - 1];
        player.getPacketSender().sendString(firstChildId - 1, "Select an option");
        for (int i = 0; i < lines.length; i++) {
            player.getPacketSender().sendString(firstChildId + i, lines[i]);
        }
        player.getPacketSender().sendChatboxInterface(firstChildId - 2);
    }
    public static void sendOptions(Player player, String title, String[] lines) {
        int firstChildId = OPTION_DIALOGUE_ID[lines.length - 1];
        player.getPacketSender().sendString(firstChildId - 1, title);
        for (int i = 0; i < lines.length; i++) {
            player.getPacketSender().sendString(firstChildId + i, lines[i]);
        }
        player.getPacketSender().sendChatboxInterface(firstChildId - 2);
    }

    public static void npcChat(Player player, int npc, String line) {
		String[] lines = Misc.wrap(line, 50, System.lineSeparator(), false).split(System.lineSeparator());
		npcChat(player, npc, lines);
    }
    
    public static void npcChat(Player player, int npc, String[] lines) {
    	npcChat(player, npc, lines, Expression.HAPPY);
    }

    public static void npcChat(Player player, int npc, String[] lines, int expression) {
        int startDialogueChildId = NPC_DIALOGUE_ID[lines.length - 1];
        int headChildId = startDialogueChildId - 2;
        player.getPacketSender().sendNpcHeadOnInterface(npc, headChildId);
        player.getPacketSender().sendInterfaceAnimation(headChildId, expression);
        player.getPacketSender().sendString(startDialogueChildId - 1,
                NpcDefinition.forId(npc) != null
                        ? NpcDefinition.forId(npc).getName().replaceAll("_", " ")
                        : "");
        for (int i = 0; i < lines.length; i++) {
            player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
        }
        player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
    }

    public static void playerChat(Player player, String line) {
		String[] lines = Misc.wrap(line, 50, System.lineSeparator(), false).split(System.lineSeparator());
		playerChat(player, lines);
    }
    
    public static void playerChat(Player player, String[] lines) {
    	playerChat(player, lines, Expression.HAPPY);
    }
    
    public static void playerChat(Player player, String[] lines, int expression) {
		int startDialogueChildId = PLAYER_DIALOGUE_ID[lines.length - 1];
		int headChildId = startDialogueChildId - 2;
		player.getPacketSender().sendPlayerHeadOnInterface(headChildId);
		player.getPacketSender().sendInterfaceAnimation(headChildId, expression);
		player.getPacketSender().sendString(startDialogueChildId - 1, player.getUsername());
		for (int i = 0; i < lines.length; i++) {
			player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
		}
		player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
    }
    
    public static void sendStatement(Player player, String str) {
		String[] lines = Misc.wrap(str, 50, System.lineSeparator(), false).split(System.lineSeparator());
		int chatboxInterface = STATEMENT_DIALOGUE_ID[lines.length - 1];
		for (int i = 0; i < lines.length; i++) {
			player.getPacketSender().sendString((chatboxInterface + 1) + i, lines[i]);
		}
		player.getPacketSender().sendChatboxInterface(chatboxInterface);
    }

	public static void sendStatement(Player player, String[] lines) {
		int chatboxInterface = STATEMENT_DIALOGUE_ID[lines.length - 1];
		for (int i = 0; i < lines.length; i++) {
			player.getPacketSender().sendString((chatboxInterface + 1) + i, lines[i]);
		}
		player.getPacketSender().sendChatboxInterface(chatboxInterface);
	}

	public static final int GLOBAL_OBJECT_CLICK = 1;
	public static final int GLOBAL_ITEM_CLICK = 2;
	public static final int GLOBAL_NPC_CLICK = 3;
    public static boolean handleGlobal(Player player, int type, int id) {
    	if (type == GLOBAL_OBJECT_CLICK) {
    		return handleObject(player, id);
    	} else if (type == GLOBAL_ITEM_CLICK) {
    		return handleItem(player, id);
    	} else if (type == GLOBAL_NPC_CLICK) {
    		return handleNPC(player, id);
    	}
    	return false;
    }
    
    public static boolean handleObject(Player player, int id) {
		int handle = player.getDialogueActionId();
        player.setDialogueAction(-1, -1);
        
        ObjectDefinition def = ObjectDefinition.forId(id);
        
        if (def.getName().toLowerCase().contains("ladder")) {

            switch (handle) {
	            case 1:
	                LadderHandler.climb(player, player.getLadderClicked(), Optional.of("up"));
	                close(player, true);
	                return true;
	            case 2:
	                LadderHandler.climb(player, player.getLadderClicked(), Optional.of("down"));
	                close(player, true);
	                return true;
                default:
                    close(player, true);
                    return true;
            }
        }
        
        if (def.getName().toLowerCase().contains("stair")) {

            switch (handle) {
	            case 1:
	            	StairHandler.climb(player, player.getLadderClicked(), Optional.of("up"));
	                close(player, true);
	                return true;
	            case 2:
	            	StairHandler.climb(player, player.getLadderClicked(), Optional.of("down"));
	                close(player, true);
	                return true;
                default:
                    close(player, true);
                    return true;
            }
        }
        
    	switch (id) {
            case 20770:
            case 20720:
            case 20722:
            case 20771:
            case 20721:
            case 20772:
                switch (handle) {
                    case 1:
                        player.moveTo(Barrows.CHEST_ENTRANCE);
                        close(player, true);
                        return true;
                    default:
                        close(player, true);
                        return true;
                }
	    	case ObjectIdentifiers.MAGICAL_ALTAR:
	            switch (handle) {
	            	case 1: // Normal spellbook option
            			close(player, true);
						MagicSpellbook.changeSpellbook(player, MagicSpellbook.NORMAL);
						return true;
					case 2: // Ancient spellbook option
            			close(player, true);
						MagicSpellbook.changeSpellbook(player, MagicSpellbook.ANCIENT);
						return true;
					case 3: // Lunar spellbook option
            			close(player, true);
						MagicSpellbook.changeSpellbook(player, MagicSpellbook.LUNAR);
						return true;
					case 4: // Lunar spellbook option
            			close(player, true);
						MagicSpellbook.changeSpellbook(player, MagicSpellbook.ARCEUUS);
						return true;
            		default:
            			close(player, true);
            			return true;	
	            }
	    	case ObjectIdentifiers.SACRIFICIAL_BOAT:
	            switch (handle) {
		            case 1:
            			close(player, true);
            			Zulrah.Enter(player);
            			return true;
        			default:
            			close(player, true);
            			return true;
	            }
    		default:
    			return false;
    	}
    }
    
    public static boolean handleItem(Player player, int id) {
    	switch (id) {
    		default:
    			return false;
    	}
    }
    
    public static boolean handleNPC(Player player, int id) {
    	switch (id) {
    		default:
    			return false;
    	}
    }
}
