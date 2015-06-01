package me.heldplayer.plugins.nei.mystcraft.integration.mystcraft;

import com.xcompwiz.mystcraft.api.hook.RenderAPI;

public class MystRender {

    public static RenderAPI api;

    public static void getAPI(Object api) {
        MystRender.api = (RenderAPI) api;
    }

}
