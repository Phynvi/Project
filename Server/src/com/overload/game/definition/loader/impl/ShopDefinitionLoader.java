package com.overload.game.definition.loader.impl;

import com.google.gson.Gson;
import com.overload.game.GameConstants;
import com.overload.game.definition.ShopDefinition;
import com.overload.game.definition.loader.DefinitionLoader;
import com.overload.game.model.container.shop.Shop;
import com.overload.game.model.container.shop.ShopManager;

import java.io.FileReader;

public class ShopDefinitionLoader extends DefinitionLoader {

    @Override
    public void load() throws Throwable {
        FileReader reader = new FileReader(file());
        ShopDefinition[] defs = new Gson().fromJson(reader, ShopDefinition[].class);
        for (ShopDefinition def : defs) {
            ShopManager.shops.put(def.getId(), new Shop(def.getId(), def.getName(), def.isIronmanOnly(), def.getCurrency(), def.getOriginalStock()));
        }
        reader.close();
    }
    
    public static void reload() throws Throwable {
        FileReader reader = new FileReader(GameConstants.DEFINITIONS_DIRECTORY + "shops.json");
        ShopDefinition[] defs = new Gson().fromJson(reader, ShopDefinition[].class);
        for (ShopDefinition def : defs) {
            ShopManager.shops.put(def.getId(), new Shop(def.getId(), def.getName(), def.isIronmanOnly(), def.getCurrency(), def.getOriginalStock()));
        }
        reader.close();
    }

    @Override
    public String file() {
        return GameConstants.DEFINITIONS_DIRECTORY + "shops.json";
    }
}
