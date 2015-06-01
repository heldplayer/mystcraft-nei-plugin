package me.heldplayer.plugins.nei.mystcraft.integration.mystcraft;

import com.xcompwiz.mystcraft.api.hook.PageAPI;

public class MystPage {

    public static PageAPI api;

    public static void getAPI(Object api) {
        MystPage.api = (PageAPI) api;
    }

}
