
package me.heldplayer.plugins.nei.mystcraft.client;

import java.util.logging.Level;

import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.DefaultOverlayHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NEIConfig implements IConfigureNEI {

    public static InkMixerRecipeHandler inkMixer;
    public static Class<? extends GuiContainer> guiInkMixerClass;

    public static WritingDeskRecipeHandler writingDesk;
    public static Class<? extends GuiContainer> guiWritingDeskClass;

    public static AgeExplorerRecipeHandler ageExplorer;

    public static MystTooltipHandler tooltipHandler;
    public static boolean tooltipsWritingDesk;
    public static boolean tooltipsInkMixer;

    @Override
    public void loadConfig() {
        Objects.log.log(Level.FINE, "Loading NEI config for Mystcraft");

        if (PluginNEIMystcraft.mystcraft == null) {
            Objects.log.log(Level.SEVERE, "Mystcraft is not installed or not found! This mod requires mystcraft to function!");
            return;
        }

        Integrator.initialize();

        tooltipsInkMixer = PluginNEIMystcraft.addInkMixerRecipes.getValue();

        if (tooltipsInkMixer) {
            Objects.log.log(Level.FINE, "Adding Ink Mixer recipe handler");

            tooltipsInkMixer = PluginNEIMystcraft.addInkMixerTooltips.getValue();

            NEIConfig.inkMixer = new InkMixerRecipeHandler();
            API.registerRecipeHandler(NEIConfig.inkMixer);
            API.registerUsageHandler(NEIConfig.inkMixer);

            Objects.log.log(Level.FINE, "Registering GUI overlay for Ink Mixer");

            API.registerGuiOverlay(NEIConfig.guiInkMixerClass, "inkmixer");
            API.registerGuiOverlayHandler(NEIConfig.guiInkMixerClass, new DefaultOverlayHandler(), "inkmixer");
        }

        tooltipsWritingDesk = PluginNEIMystcraft.addWritingDeskRecipes.getValue();

        if (tooltipsWritingDesk) {
            Objects.log.log(Level.FINE, "Adding Writing Desk recipe handler");

            tooltipsWritingDesk = PluginNEIMystcraft.addWritingDeskTooltips.getValue();

            NEIConfig.writingDesk = new WritingDeskRecipeHandler();
            API.registerRecipeHandler(NEIConfig.writingDesk);
            API.registerUsageHandler(NEIConfig.writingDesk);

            Objects.log.log(Level.FINE, "Registering GUI overlay for Writing Desk");

            API.registerGuiOverlay(NEIConfig.guiWritingDeskClass, "writingdesk");
            API.registerGuiOverlayHandler(NEIConfig.guiWritingDeskClass, new DefaultOverlayHandler(), "writingdesk");
        }

        if (tooltipsInkMixer || tooltipsWritingDesk) {
            Objects.log.log(Level.FINE, "Registering tooltip handler");

            NEIConfig.tooltipHandler = new MystTooltipHandler(PluginNEIMystcraft.addRecipesTooltips.getValue(), tooltipsWritingDesk, tooltipsInkMixer);
            GuiContainerManager.addTooltipHandler(NEIConfig.tooltipHandler);
            GuiContainerManager.addInputHandler(NEIConfig.tooltipHandler);
        }
        
        if(PluginNEIMystcraft.addAgeExplorer.getValue()){
            NEIConfig.ageExplorer = new AgeExplorerRecipeHandler();
            API.registerRecipeHandler(NEIConfig.ageExplorer);
            API.registerUsageHandler(NEIConfig.ageExplorer);
        }
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
