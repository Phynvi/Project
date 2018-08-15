package com.overload.game.content;

import com.overload.game.definition.NpcDefinition;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Item;
import com.overload.util.Misc;

import java.util.HashMap;

public class PlayerProperties {

    public static final String ATTRIBUTE_KEY = "PROPERTY_";

    private final Player player;

    public PlayerProperties(Player player) {
        this.player = player;
    }

    public void addProperty(String attributeSuffix, String name, int increment) {
        attributeSuffix = attributeSuffix.trim().toUpperCase().replaceAll(" ", "_");
        name = name.trim().toUpperCase().replaceAll(" ", "_");
        int current = player.getAttributes().getInt(ATTRIBUTE_KEY + attributeSuffix + "_" + name);

        if (!player.getAttributes().isSet(ATTRIBUTE_KEY + attributeSuffix + "_" + name)) {
            current = 0;
        }

        player.getAttributes().set(ATTRIBUTE_KEY + attributeSuffix + "_" + name, current + increment);
    }

    public void addProperty(String property, int increment) {
        addProperty("PLAYER", property, increment);
    }

    public void addProperty(NPC npc, int increment) {
        addProperty("NPC", npc.getDefinition().getName(), increment);
        player.sendMessage("Your " + npc.getDefinition().getName() + " kill count is: @red@" + Misc.format(player.getProperties().getPropertyValue("NPC_" + npc.getDefinition().getName())) + "</col>.");
    }

    public void addProperty(Item item, int increment) {
        addProperty("ITEM", item.getDefinition().getName(), increment);
    }

    public int getPropertyValue(String property) {
        property = property.trim().toUpperCase().replaceAll(" ", "_");

        int value = 0;

        for (Object attribute : player.getAttributes().getAttributes().keySet()) {
            if (String.valueOf(attribute).startsWith(ATTRIBUTE_KEY + property)) {
                value = player.getAttributes().getInt(attribute);
            }
        }

        return value;
    }

    public HashMap<String, Integer> getPropertyValues(String property) {
        property = property.trim().toUpperCase().replaceAll(" ", "_");

        HashMap<String, Integer> properties = new HashMap<>();

        for (Object attribute : player.getAttributes().getAttributes().keySet()) {
            if (String.valueOf(attribute).startsWith(ATTRIBUTE_KEY + property + "_")) {
                properties.put(String.valueOf(attribute).replace(ATTRIBUTE_KEY + property + "_", ""), player.getAttributes().getInt(attribute));
            }
        }

        return properties;
    }

    public void setDefaults(int npc) {
            player.getProperties().addProperty("NPC", NpcDefinition.forId(npc).getName(), 0);
        }
    }