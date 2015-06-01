package me.heldplayer.plugins.nei.mystcraft.integration.mystcraft;

import com.xcompwiz.mystcraft.api.hook.LinkingAPI;

public class MystLinking {

    public static LinkingAPI api;

    public static void getAPI(Object api) {
        MystLinking.api = (LinkingAPI) api;
    }

}
