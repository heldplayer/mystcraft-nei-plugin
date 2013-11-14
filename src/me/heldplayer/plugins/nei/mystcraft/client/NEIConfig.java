
package me.heldplayer.plugins.nei.mystcraft.client;

import java.util.logging.Level;

import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.DefaultOverlayHandler;

public class NEIConfig implements IConfigureNEI {

    public static InkMixerRecipeHandler inkMixer;
    public static Class<? extends GuiContainer> guiInkMixerClass;

    public static WritingDeskRecipeHandler writingDesk;
    public static Class<? extends GuiContainer> guiWritingDeskClass;

    public static MystTooltipHandler tooltipHandler;

    @Override
    public void loadConfig() {
        Objects.log.log(Level.FINE, "Loading NEI config for Mystcraft");

        if (PluginNEIMystcraft.mystcraft == null) {
            Objects.log.log(Level.SEVERE, "Mystcraft is not installed or not found! This mod requires mystcraft to function!");
            return;
        }

        Integrator.initialize();

        Objects.log.log(Level.FINE, "Adding Ink Mixer recipe handler");

        NEIConfig.inkMixer = new InkMixerRecipeHandler();
        API.registerRecipeHandler(NEIConfig.inkMixer);
        API.registerUsageHandler(NEIConfig.inkMixer);

        Objects.log.log(Level.FINE, "Registering GUI overlay for Ink Mixer");

        API.registerGuiOverlay(NEIConfig.guiInkMixerClass, "inkmixer");
        API.registerGuiOverlayHandler(NEIConfig.guiInkMixerClass, new DefaultOverlayHandler(), "inkmixer");

        Objects.log.log(Level.FINE, "Adding Writing Desk recipe handler");

        NEIConfig.writingDesk = new WritingDeskRecipeHandler();
        API.registerRecipeHandler(NEIConfig.writingDesk);
        API.registerUsageHandler(NEIConfig.writingDesk);

        Objects.log.log(Level.FINE, "Registering GUI overlay for Writing Desk");

        API.registerGuiOverlay(NEIConfig.guiWritingDeskClass, "writingdesk");
        API.registerGuiOverlayHandler(NEIConfig.guiWritingDeskClass, new DefaultOverlayHandler(), "writingdesk");

        Objects.log.log(Level.FINE, "Registering tooltip handler");

        NEIConfig.tooltipHandler = new MystTooltipHandler();
        GuiContainerManager.addTooltipHandler(NEIConfig.tooltipHandler);
        GuiContainerManager.addInputHandler(NEIConfig.tooltipHandler);
    }

    @Override
    public String getName() {
        return Objects.MOD_NAME;
    }

    @Override
    public String getVersion() {
        return "@VERSION@";
    }

}
