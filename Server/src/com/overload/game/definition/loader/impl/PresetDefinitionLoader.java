package com.overload.game.definition.loader.impl;

import java.io.FileReader;
import java.security.InvalidParameterException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.overload.game.GameConstants;
import com.overload.game.content.presets.Presetable;
import com.overload.game.content.presets.Presetables;
import com.overload.game.definition.loader.DefinitionLoader;
import com.overload.game.model.Item;
import com.overload.game.model.MagicSpellbook;

public class PresetDefinitionLoader extends DefinitionLoader {

	public static int PRESETS_LOADED = 0;

	@Override
	public void load() throws Throwable {
		FileReader fileReader = new FileReader(file());
		JsonParser parser = new JsonParser();
		JsonArray array = (JsonArray) parser.parse(fileReader);
		Gson builder = new GsonBuilder().create();
		for (int i = 0; i < array.size(); i++) {
			JsonObject reader = (JsonObject) array.get(i);
			parse(reader, builder);
		}
		fileReader.close();
	}

	private void parse(JsonObject reader, Gson builder) {
		String name = reader.get("name").getAsString();
		String spellbook = reader.get("spellbook").getAsString();
		int[] stats = builder.fromJson(reader.get("stats").getAsJsonArray(), int[].class);
		Item[] inventory = builder.fromJson(reader.get("inventory").getAsJsonArray(), Item[].class);
		Item[] equipment = builder.fromJson(reader.get("equipment").getAsJsonArray(), Item[].class);

		if (inventory.length > 29) {
			throw new InvalidParameterException("Preset " + name + " has too many inventory items. Max is 28!");
		}

		if (equipment.length > 14) {
			throw new InvalidParameterException("Preset " + name + " has too many equipment items. Max is 14!");
		}

		Presetables.GLOBAL_PRESETS[PRESETS_LOADED] = new Presetable(name, PRESETS_LOADED, inventory, equipment, stats,
				MagicSpellbook.valueOf(spellbook), true);
		PRESETS_LOADED++;
	}

	@Override
	public String file() {
		return GameConstants.DEFINITIONS_DIRECTORY + "global_presets.json";
	}
}
