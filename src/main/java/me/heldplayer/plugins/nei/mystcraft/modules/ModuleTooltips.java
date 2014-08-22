package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.guihook.GuiContainerManager;
import cpw.mods.fml.relauncher.Side;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.client.MystTooltipHandler;
import net.specialattack.forge.core.config.ConfigValue;
import org.apache.logging.log4j.Level;

public class ModuleTooltips implements IModule {

    public static ConfigValue<Boolean> addInkMixerTooltips;
    public static ConfigValue<Boolean> addWritingDeskTooltips;
    public static ConfigValue<Boolean> addRecipesTooltips;
    public static boolean inkMixerTooltips;
    public static boolean writingDeskTooltips;
    public static boolean recipesTooltips;
    private boolean enabled;

    public ModuleTooltips() {
        addInkMixerTooltips = new ConfigValue<Boolean>("addInkMixerTooltips", "config.nei.mystcraft.key.addInkMixerTooltips", Side.CLIENT, Boolean.TRUE, "Should there be tooltips for the Ink Mixer?");
        addWritingDeskTooltips = new ConfigValue<Boolean>("addWritingDeskTooltips", "config.nei.mystcraft.key.addWritingDeskTooltips", Side.CLIENT, Boolean.TRUE, "Should there be tooltips for the Writing Desk?");
        addRecipesTooltips = new ConfigValue<Boolean>("addRecipesTooltips", "config.nei.mystcraft.key.addRecipesTooltips", Side.CLIENT, Boolean.TRUE, "Should there be clickable regions to show all recipes in a crafting station?");
    }

    @Override
    public void enable() {
        if (!enabled) {
            Objects.log.log(Level.DEBUG, "Registering tooltip handler");

            MystTooltipHandler tooltipHandler = new MystTooltipHandler();
            GuiContainerManager.addTooltipHandler(tooltipHandler);
            GuiContainerManager.addInputHandler(tooltipHandler);

            enabled = true;
        }

        inkMixerTooltips = addInkMixerTooltips.getValue();
        writingDeskTooltips = addWritingDeskTooltips.getValue();
        recipesTooltips = addRecipesTooltips.getValue();
    }

    @Override
    public void disable() {
        inkMixerTooltips = false;
        writingDeskTooltips = false;
        recipesTooltips = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public ConfigValue<?>[] getConfigEntries() {
        return new ConfigValue<?>[] { addInkMixerTooltips, addWritingDeskTooltips, addRecipesTooltips };
    }

}
