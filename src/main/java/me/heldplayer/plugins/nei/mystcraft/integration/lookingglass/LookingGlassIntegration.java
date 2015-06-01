package me.heldplayer.plugins.nei.mystcraft.integration.lookingglass;

import com.xcompwiz.lookingglass.api.APIInstanceProvider;
import com.xcompwiz.lookingglass.api.APIUndefined;
import com.xcompwiz.lookingglass.api.APIVersionRemoved;
import com.xcompwiz.lookingglass.api.APIVersionUndefined;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import me.heldplayer.plugins.nei.mystcraft.Objects;

public class LookingGlassIntegration {

    public static void register(APIInstanceProvider provider) {
        Objects.log.info("Received the LookingGlass API provider");

        LookingGlassIntegration.getViewAPI(provider);
    }

    private static void getViewAPI(APIInstanceProvider provider) {
        try {
            Object api = provider.getAPIInstance("view-1");
            CommonProxy.lookingGlassLoaded = true;
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                LGView.getAPI(api);
            }
        } catch (APIUndefined e) {
            Objects.log.error("The LookingGlass view API is missing", e);
        } catch (APIVersionUndefined e) {
            Objects.log.error("LookingGlass can't count to 1, missing the view API", e);
        } catch (APIVersionRemoved e) {
            Objects.log.error("Version 1 of the LookingGlass view API was removed, NEI Mystcraft Plugin needs to be updated", e);
        }
    }
}
