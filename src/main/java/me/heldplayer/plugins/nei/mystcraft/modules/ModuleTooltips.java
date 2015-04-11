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
        addInkMixerTooltips = new ConfigValue<Boolean>("addInkMixerTooltips", "myst-nei:config.general.addInkMixerTooltips", Side.CLIENT, Boolean.TRUE);
        addWritingDeskTooltips = new ConfigValue<Boolean>("addWritingDeskTooltips", "myst-nei:config.general.addWritingDeskTooltips", Side.CLIENT, Boolean.TRUE);
        addRecipesTooltips = new ConfigValue<Boolean>("addRecipesTooltips", "myst-nei:config.general.addRecipesTooltips", Side.CLIENT, Boolean.TRUE);
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
