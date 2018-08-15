package com.overload.draw;

import static com.overload.Client.tick;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.overload.Client;
import com.overload.Configuration;
import com.overload.cache.FileArchive;
import com.overload.cache.def.ItemDefinition;
import com.overload.cache.def.NpcDefinition;
import com.overload.cache.def.ObjectDefinition;
import com.overload.cache.graphics.GameFont;
import com.overload.cache.graphics.RSFont;
import com.overload.cache.graphics.widget.Widget;
import com.overload.io.jaggrab.JagGrab;
import com.overload.io.packets.outgoing.impl.Command;

public class Console {

    public static boolean consoleOpen;
    public final String[] consoleMessages;
    public String consoleInput;
    public String previousMessage;

    public Console() {
        consoleInput = "";
        consoleOpen = false;
        consoleMessages = new String[50];
        consoleMessages[0] = "This is the developer console. To close, press the ` key on your keyboard.";
    }

    public static Client client() {
        return Client.instance;
    }

    public void drawConsole() {
        if (consoleOpen) {
            Rasterizer2D.drawTransparentBox(0, 0, client().getGameComponent().getWidth(), 334, 5320850, 97);
            Rasterizer2D.drawPixels(1, 315, 0, 16777215, client().getGameComponent().getWidth());
            client().newBoldFont.drawBasicString("-->", 11, 328, 16777215, 0);
            if (tick % 20 < 10) {
                client().newBoldFont.drawBasicString(consoleInput + "|", 38, 328, 16777215, 0);
            } else {
                client().newBoldFont.drawBasicString(consoleInput, 38, 328, 16777215, 0);
            }
        }
    }

    public void drawConsoleArea() {
        if (consoleOpen) {
            for (int i = 0, j = 308; i < 17; i++, j -= 18) {
                if (consoleMessages[i] != null) {
                    client().newRegularFont.drawBasicString(consoleMessages[i], 9, j, 0xFFFFFF, 0);
                }
            }
        }
    }


    public void printMessage(String s, int i) {
        if (client().backDialogueId == -1) {
            client().updateChatbox = true;
        }
        for (int j = 16; j > 0; j--) {
            consoleMessages[j] = consoleMessages[j - 1];
        }
        if (i == 0) {
            consoleMessages[0] = date() + ": --> " + s;
            previousMessage = s;
        } else {
            consoleMessages[0] = date() + ": " + s;
        }
    }

    public String date() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Server time
        return sdf.format(date);
    }

    public void sendCommandPacket(String cmd) {
    	if (cmd.startsWith("findnpc")) {
            String name = cmd.substring(("findnpc").length() + 1);
            for (int i = 0; i < NpcDefinition.TOTAL_NPCS; i++) {
                NpcDefinition def = NpcDefinition.lookup(i);
                if (def == null || def.name == null ||
                        !def.name.toLowerCase().contains(name)) {
                    continue;
                }
                printMessage("NPC " + i + ", name: " + def.name + ", stand anim: " + def.standAnim, 1);
            }
            return;
        } else if (cmd.startsWith("finditem")) {
        	String name = cmd.substring(("finditem").length() + 1);
            for (int i = 0; i < ItemDefinition.TOTAL_ITEMS; i++) {
                ItemDefinition def = ItemDefinition.lookup(i);
                if (def == null || def.name == null ||
                        !def.name.toLowerCase().contains(name)) {
                    continue;
                }
                printMessage("Item " + i + ", name: " + def.name + ", modelId: " + def.inventory_model, 1);
            }
            return;
        } else if (cmd.startsWith("findobject")) {
        	String name = cmd.substring(("findobject").length() + 1);
            for (int i = 0; i < ObjectDefinition.TOTAL_OBJECTS; i++) {
                ObjectDefinition def = ObjectDefinition.lookup(i);
                if (def == null || def.name == null ||
                        !def.name.toLowerCase().contains(name)) {
                    continue;
                }
                printMessage("Object " + i + ", name: " + def.name, 1);
            }
            return;
        } else if (cmd.equalsIgnoreCase("cls") || cmd.equalsIgnoreCase("clear")) {
            for (int j = 0; j < 17; j++) {
                consoleMessages[j] = null;
            }
            return;
        }
        switch (cmd.toLowerCase()) {
            case "help":
                client().console.printMessage("Type 'commands' to see a list of available commands.", 1);
                break;
            case "commands":
                showCommands();
                break;
            case "data":
                Configuration.clientData = !Configuration.clientData;
                client().console.printMessage("Data " + (Configuration.clientData ? "on" : "off"), 1);
                break;
            case "childids":
                for (int i = 0; i < 50000; i++) {
                    client().sendString("" + i, i);
                }
                break;
            case "finterface":
                try {
                    String[] args = client().inputString.split(" ");
                    int id1 = Integer.parseInt(args[1]);
                    int id2 = Integer.parseInt(args[2]);
                    client().fullscreenInterfaceID = id1;
                    client().openInterfaceId = id2;
                    client().console.printMessage("Opened interface " + id1 + " " + id2 + ".", 1);
                } catch (Exception e) {
                    client().console.printMessage("Failed to open interface.", 1);
                }
                break;
            case "music":
                Configuration.enableMusic = !Configuration.enableMusic;
                break;
            case "rint":
                GameFont gameFont = new GameFont(true, "q8_full", client().titleArchive);
                GameFont fonts[] = {client().smallText, client().regularText, client().boldText, gameFont};
                FileArchive interfaces = client().createArchive(3, "interface", "interface", JagGrab.CRCs[JagGrab.INTERFACE_CRC]);
                FileArchive graphics = client().createArchive(4, "2d graphics", "media", JagGrab.CRCs[JagGrab.MEDIA_CRC]);
                Widget.load(interfaces, fonts, graphics, new RSFont[]{client().newSmallFont, client().newRegularFont, client().newBoldFont, client().newFancyFont});
                break;
            case "grid":
                Client.enableGridOverlay = !Client.enableGridOverlay;
                break;
        }

        /** Server commands **/
        if (Client.loggedIn) {
            client().sendPacket(new Command(cmd).create());
        }
    }

    public final void showCommands() {
        client().console.printMessage("commands - This command", 1);
        client().console.printMessage("cls - Clear console", 1);
        client().console.printMessage("displayfps - Toggle FPS", 1);

        // Mod commands
        if (client().getMyPrivilege() >= 1 && client().getMyPrivilege() <= 4) {
            String[] cmds = new String[]{"mute $plr - Mute player",
                    "unmute $plr - Unmute player",
                    "kick $plr - Kick player"};
            for (String cmd : cmds) {
                client().console.printMessage(cmd, 1);
            }
        }
    }
}
