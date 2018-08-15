package com.overload.game.definition.loader.impl;

import java.io.FileReader;

import com.google.gson.Gson;
import com.overload.game.GameConstants;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.definition.MusicDefinition;
import com.overload.game.definition.loader.DefinitionLoader;

public class MusicDefinitionLoader extends DefinitionLoader {
	
	@Override
    public void load() throws Throwable {
        FileReader reader = new FileReader(file());
        MusicDefinition[] defs = new Gson().fromJson(reader, MusicDefinition[].class);
        for (MusicDefinition def : defs) {
        	MusicDefinition.definitions.put(def.getFrame(), def);
        }
        reader.close();
    }
    
    public static void reload() throws Throwable {
    	ItemDefinition.definitions.clear();
        FileReader reader = new FileReader(GameConstants.DEFINITIONS_DIRECTORY + "music.json");
        MusicDefinition[] defs = new Gson().fromJson(reader, MusicDefinition[].class);
        for (MusicDefinition def : defs) {
        	MusicDefinition.definitions.put(def.getFrame(), def);
        }
        reader.close();
    }

    @Override
    public String file() {
        return GameConstants.DEFINITIONS_DIRECTORY + "music.json";
    }
}