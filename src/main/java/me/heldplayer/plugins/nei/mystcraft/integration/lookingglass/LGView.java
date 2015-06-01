package me.heldplayer.plugins.nei.mystcraft.integration.lookingglass;

import com.xcompwiz.lookingglass.api.IWorldViewAPI;

public class LGView {

    public static IWorldViewAPI api;

    public static void getAPI(Object api) {
        LGView.api = (IWorldViewAPI) api;
    }

}
