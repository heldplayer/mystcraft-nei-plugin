package me.heldplayer.plugins.nei.mystcraft.integration.mystcraft;

import com.xcompwiz.mystcraft.api.hook.LinkPropertyAPI;

public class MystLinkProperty {

    public static LinkPropertyAPI api;

    public static void getAPI(Object api) {
        MystLinkProperty.api = (LinkPropertyAPI) api;
    }

}
