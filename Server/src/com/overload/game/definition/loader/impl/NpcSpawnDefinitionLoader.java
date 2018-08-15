package com.overload.game.definition.loader.impl;

import com.google.gson.Gson;
import com.overload.Server;
import com.overload.game.GameConstants;
import com.overload.game.World;
import com.overload.game.definition.NpcSpawnDefinition;
import com.overload.game.definition.loader.DefinitionLoader;
import com.overload.game.entity.impl.npc.NPC;

import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;

public class NpcSpawnDefinitionLoader extends DefinitionLoader {

    @Override
    public void load() throws Throwable {
    	for (String s : files()) {
	        FileReader reader = new FileReader(s);
	        NpcSpawnDefinition[] defs = new Gson().fromJson(reader, NpcSpawnDefinition[].class);
            Server.getLogger().log(Level.INFO, "Npc Spawns Loaded: " + s);
	        for (NpcSpawnDefinition def : defs) {
	        	if (def == null) {
	                Server.getLogger().log(Level.WARNING, "NULL: " + s);
	        		continue;
	        	}
	            NPC npc = new NPC(def.getId(), def.getPosition());
	            npc.getMovementCoordinator().setRadius(def.getRadius());
	            npc.setFace(def.getFacing());
	            World.getNpcs().add(npc);
	        }
	        reader.close();
    	}
    }

    public String[] files() {
    	File[] listOfFiles = new File(file()).listFiles();
    	String[] ret = new String[listOfFiles.length];
    	for (int i = 0; i < listOfFiles.length; i++) {
    	      if (listOfFiles[i].isFile())
    	          ret[i] = file() + listOfFiles[i].getName();
    	}
        return ret;
    }

	@Override
	public String file() {
		return GameConstants.DEFINITIONS_DIRECTORY + "spawns/";
	}

}
