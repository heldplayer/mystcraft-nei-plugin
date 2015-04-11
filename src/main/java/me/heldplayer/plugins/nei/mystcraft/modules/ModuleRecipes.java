package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;
import cpw.mods.fml.relauncher.Side;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.client.InkMixerRecipeHandler;
import me.heldplayer.plugins.nei.mystcraft.client.Integrator;
import me.heldplayer.plugins.nei.mystcraft.client.WritingDeskRecipeHandler;
import net.specialattack.forge.core.config.ConfigValue;
import org.apache.logging.log4j.Level;

public class ModuleRecipes implements IModule {

    public static ConfigValue<Boolean> addInkMixerRecipes;
    public static ConfigValue<Boolean> addWritingDeskRecipes;
    public static boolean inkMixerEnabled;
    public static boolean writingDeskEnabled;
    private boolean enabled;

    public ModuleRecipes() {
        addInkMixerRecipes = new ConfigValue<Boolean>("addInkMixerRecipes", "myst-nei:config.general.addInkMixerRecipes", Side.CLIENT, Boolean.TRUE);
        addWritingDeskRecipes = new ConfigValue<Boolean>("addWritingDeskRecipes", "myst-nei:config.general.addWritingDeskRecipes", Side.CLIENT, Boolean.TRUE);
    }

    @Override
    public void enable() {
        if (!enabled) {
            {
                Objects.log.log(Level.DEBUG, "Adding Ink Mixer recipe handler");

                InkMixerRecipeHandler inkMixer = new InkMixerRecipeHandler();
                API.registerRecipeHandler(inkMixer);
                API.registerUsageHandler(inkMixer);

                Objects.log.log(Level.DEBUG, "Registering GUI overlay for Ink Mixer");

                API.registerGuiOverlay(Integrator.guiInkMixerClass, "inkmixer");
                API.registerGuiOverlayHandler(Integrator.guiInkMixerClass, new DefaultOverlayHandler(), "inkmixer");
            }
            {
                Objects.log.log(Level.DEBUG, "Adding Writing Desk recipe handler");

                WritingDeskRecipeHandler writingDesk = new WritingDeskRecipeHandler();
                API.registerRecipeHandler(writingDesk);
                API.registerUsageHandler(writingDesk);

                Objects.log.log(Level.DEBUG, "Registering GUI overlay for Writing Desk");

                API.registerGuiOverlay(Integrator.guiWritingDeskClass, "writingdesk");
                API.registerGuiOverlayHandler(Integrator.guiWritingDeskClass, new DefaultOverlayHandler(), "writingdesk");
            }

            enabled = true;
        }

        inkMixerEnabled = addInkMixerRecipes.getValue();
        writingDeskEnabled = addWritingDeskRecipes.getValue();
    }

    @Override
    public void disable() {
        inkMixerEnabled = false;
        writingDeskEnabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public ConfigValue<?>[] getConfigEntries() {
        return new ConfigValue<?>[] { addInkMixerRecipes, addWritingDeskRecipes };
    }

}
