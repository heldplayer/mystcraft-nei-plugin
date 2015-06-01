package me.heldplayer.plugins.nei.mystcraft.integration.mystcraft;

import com.xcompwiz.mystcraft.api.APIInstanceProvider;
import com.xcompwiz.mystcraft.api.exception.APIUndefined;
import com.xcompwiz.mystcraft.api.exception.APIVersionRemoved;
import com.xcompwiz.mystcraft.api.exception.APIVersionUndefined;
import me.heldplayer.plugins.nei.mystcraft.Objects;

public class MystcraftIntegration {

    public static void register(APIInstanceProvider provider) {
        Objects.log.info("Received the Mystcraft API provider");

        MystcraftIntegration.getLinkingAPI(provider);
        MystcraftIntegration.getLinkPropertyAPI(provider);
        MystcraftIntegration.getSymbolAPI(provider);
        MystcraftIntegration.getItemFactoryAPI(provider);
        MystcraftIntegration.getRenderAPI(provider);
        MystcraftIntegration.getPageAPI(provider);
    }

    private static void getLinkingAPI(APIInstanceProvider provider) {
        try {
            Object api = provider.getAPIInstance("linking-1");
            MystLinking.getAPI(api);
        } catch (APIUndefined e) {
            Objects.log.error("The Mystcraft linking API is missing", e);
        } catch (APIVersionUndefined e) {
            Objects.log.error("Mystcraft can't count to 1, missing the linking API", e);
        } catch (APIVersionRemoved e) {
            Objects.log.error("Version 1 of the Mystcraft linking API was removed, NEI Mystcraft Plugin needs to be updated", e);
        }
    }

    private static void getLinkPropertyAPI(APIInstanceProvider provider) {
        try {
            Object api = provider.getAPIInstance("linkingprop-1");
            MystLinkProperty.getAPI(api);
        } catch (APIUndefined e) {
            Objects.log.error("The Mystcraft linkingprop API is missing", e);
        } catch (APIVersionUndefined e) {
            Objects.log.error("Mystcraft can't count to 1, missing the linkingprop API", e);
        } catch (APIVersionRemoved e) {
            Objects.log.error("Version 1 of the Mystcraft linkingprop API was removed, NEI Mystcraft Plugin needs to be updated", e);
        }
    }

    private static void getSymbolAPI(APIInstanceProvider provider) {
        try {
            Object api = provider.getAPIInstance("symbol-1");
            MystSymbol.getAPI(api);
        } catch (APIUndefined e) {
            Objects.log.error("The Mystcraft symbol API is missing", e);
        } catch (APIVersionUndefined e) {
            Objects.log.error("Mystcraft can't count to 1, missing the symbol API", e);
        } catch (APIVersionRemoved e) {
            Objects.log.error("Version 1 of the Mystcraft symbol API was removed, NEI Mystcraft Plugin needs to be updated", e);
        }
    }

    private static void getItemFactoryAPI(APIInstanceProvider provider) {
        try {
            Object api = provider.getAPIInstance("itemfact-1");
            MystItemFactory.getAPI(api);
        } catch (APIUndefined e) {
            Objects.log.error("The Mystcraft itemfact API is missing", e);
        } catch (APIVersionUndefined e) {
            Objects.log.error("Mystcraft can't count to 1, missing the itemfact API", e);
        } catch (APIVersionRemoved e) {
            Objects.log.error("Version 1 of the Mystcraft itemfact API was removed, NEI Mystcraft Plugin needs to be updated", e);
        }
    }

    private static void getRenderAPI(APIInstanceProvider provider) {
        try {
            Object api = provider.getAPIInstance("render-1");
            MystRender.getAPI(api);
        } catch (APIUndefined e) {
            Objects.log.error("The Mystcraft render API is missing", e);
        } catch (APIVersionUndefined e) {
            Objects.log.error("Mystcraft can't count to 1, missing the render API", e);
        } catch (APIVersionRemoved e) {
            Objects.log.error("Version 1 of the Mystcraft render API was removed, NEI Mystcraft Plugin needs to be updated", e);
        }
    }

    private static void getPageAPI(APIInstanceProvider provider) {
        try {
            Object api = provider.getAPIInstance("page-1");
            MystPage.getAPI(api);
        } catch (APIUndefined e) {
            Objects.log.error("The Mystcraft page API is missing", e);
        } catch (APIVersionUndefined e) {
            Objects.log.error("Mystcraft can't count to 1, missing the page API", e);
        } catch (APIVersionRemoved e) {
            Objects.log.error("Version 1 of the Mystcraft page API was removed, NEI Mystcraft Plugin needs to be updated", e);
        }
    }
}
