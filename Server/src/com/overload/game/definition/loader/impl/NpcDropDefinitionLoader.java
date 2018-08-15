package com.overload.game.definition.loader.impl;

import com.google.gson.Gson;
import com.overload.game.GameConstants;
import com.overload.game.definition.NpcDropDefinition;
import com.overload.game.definition.loader.DefinitionLoader;

import java.io.FileReader;

public class NpcDropDefinitionLoader extends DefinitionLoader {

    @Override
    public void load() throws Throwable {
        FileReader reader = new FileReader(file());
        NpcDropDefinition[] defs = new Gson().fromJson(reader, NpcDropDefinition[].class);
        for (NpcDropDefinition def : defs) {
            for (int npcId : def.getNpcIds()) {
                NpcDropDefinition.definitions.put(npcId, def);
            }
        }
        reader.close();
    }
    
    public static void reload() throws Throwable {
    	NpcDropDefinition.definitions.clear();
        FileReader reader = new FileReader(GameConstants.DEFINITIONS_DIRECTORY + "npc_drops.json");
        NpcDropDefinition[] defs = new Gson().fromJson(reader, NpcDropDefinition[].class);
        for (NpcDropDefinition def : defs) {
            for (int npcId : def.getNpcIds()) {
                NpcDropDefinition.definitions.put(npcId, def);
            }
        }
        reader.close();
    }

    @Override
    public String file() {
        return GameConstants.DEFINITIONS_DIRECTORY + "npc_drops.json";
    }
}
