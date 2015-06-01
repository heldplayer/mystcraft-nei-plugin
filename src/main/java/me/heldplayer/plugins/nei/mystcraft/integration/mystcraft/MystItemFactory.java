package me.heldplayer.plugins.nei.mystcraft.integration.mystcraft;

import com.xcompwiz.mystcraft.api.hook.ItemFactory;

public class MystItemFactory {

    public static ItemFactory api;

    public static void getAPI(Object api) {
        MystItemFactory.api = (ItemFactory) api;
    }

}
