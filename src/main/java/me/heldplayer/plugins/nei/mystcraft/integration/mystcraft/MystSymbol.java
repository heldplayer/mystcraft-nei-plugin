package me.heldplayer.plugins.nei.mystcraft.integration.mystcraft;

import com.xcompwiz.mystcraft.api.hook.SymbolAPI;

public class MystSymbol {

    public static SymbolAPI api;

    public static void getAPI(Object api) {
        MystSymbol.api = (SymbolAPI) api;
    }

}
