package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.guihook.GuiContainerManager;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.plugins.nei.mystcraft.client.MystTooltipHandler;
import org.apache.logging.log4j.Level;

public class ModuleTooltips implements IModule {


    public static boolean inkMixerTooltips;
    public static boolean writingDeskTooltips;
    public static boolean recipesTooltips;
    private boolean enabled;

    @Override
    public void enable() {
        if (!this.enabled) {
            Objects.log.log(Level.DEBUG, "Registering tooltip handler");

            MystTooltipHandler tooltipHandler = new MystTooltipHandler();
            GuiContainerManager.addTooltipHandler(tooltipHandler);
            GuiContainerManager.addInputHandler(tooltipHandler);

            this.enabled = true;
        }

        ModuleTooltips.inkMixerTooltips = PluginNEIMystcraft.config.addInkMixerTooltips;
        ModuleTooltips.writingDeskTooltips = PluginNEIMystcraft.config.addWritingDeskTooltips;
        ModuleTooltips.recipesTooltips = PluginNEIMystcraft.config.addRecipesTooltips;
    }

    @Override
    public void disable() {
        ModuleTooltips.inkMixerTooltips = false;
        ModuleTooltips.writingDeskTooltips = false;
        ModuleTooltips.recipesTooltips = false;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
