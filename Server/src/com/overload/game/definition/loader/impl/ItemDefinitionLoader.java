package com.overload.game.definition.loader.impl;

import com.google.gson.Gson;
import com.overload.game.GameConstants;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.definition.loader.DefinitionLoader;

import java.io.FileReader;

public class ItemDefinitionLoader extends DefinitionLoader {

    @Override
    public void load() throws Throwable {
        FileReader reader = new FileReader(file());
        ItemDefinition[] defs = new Gson().fromJson(reader, ItemDefinition[].class);
        for (ItemDefinition def : defs) {
            ItemDefinition.definitions.put(def.getId(), def);
        }
        reader.close();
    }
    
    public static void reload() throws Throwable {
    	ItemDefinition.definitions.clear();
        FileReader reader = new FileReader(GameConstants.DEFINITIONS_DIRECTORY + "items.json");
        ItemDefinition[] defs = new Gson().fromJson(reader, ItemDefinition[].class);
        for (ItemDefinition def : defs) {
            ItemDefinition.definitions.put(def.getId(), def);
        }
        reader.close();
    }

    @Override
    public String file() {
        return GameConstants.DEFINITIONS_DIRECTORY + "items.json";
    }
}
