package me.heldplayer.plugins.nei.mystcraft.client;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import org.apache.logging.log4j.Level;

@SideOnly(Side.CLIENT)
public class NEIConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        Objects.log.log(Level.DEBUG, "Loading NEI config for Mystcraft");

        if (PluginNEIMystcraft.mystcraft == null) {
            Objects.log.log(Level.ERROR, "Mystcraft is not installed or not found! This mod requires mystcraft to function!");
            return;
        }

        Integrator.initialize();

        if (PluginNEIMystcraft.addAgeExplorer.getValue()) {
            AgeExplorerRecipeHandler ageExplorer = new AgeExplorerRecipeHandler();
            API.registerRecipeHandler(ageExplorer);
            API.registerUsageHandler(ageExplorer);
        }
    }

    @Override
    public String getName() {
        return Objects.MOD_NAME;
    }

    @Override
    public String getVersion() {
        return Objects.MOD_INFO.modVersion;
    }

    public static void resetNEI() {
        for (AgeInfo info : ClientProxy.clientAgesMap.values()) {
            if (info.symbols != null) {
                info.symbols.clear();
                info.symbols = null;
            }
            if (info.pages != null) {
                info.pages.clear();
                info.pages = null;
            }
        }

        ClientProxy.clientAgesMap.clear();
    }

}
