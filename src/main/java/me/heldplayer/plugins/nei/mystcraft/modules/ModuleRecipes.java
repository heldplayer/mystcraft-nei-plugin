package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.plugins.nei.mystcraft.client.InkMixerRecipeHandler;
import me.heldplayer.plugins.nei.mystcraft.client.Integrator;
import me.heldplayer.plugins.nei.mystcraft.client.WritingDeskRecipeHandler;
import org.apache.logging.log4j.Level;

public class ModuleRecipes implements IModule {

    public static boolean inkMixerEnabled;
    public static boolean writingDeskEnabled;
    private boolean enabled;

    @Override
    public void enable() {
        if (!this.enabled) {
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

            this.enabled = true;
        }

        ModuleRecipes.inkMixerEnabled = PluginNEIMystcraft.config.addInkMixerRecipes;
        ModuleRecipes.writingDeskEnabled = PluginNEIMystcraft.config.addWritingDeskRecipes;
    }

    @Override
    public void disable() {
        ModuleRecipes.inkMixerEnabled = false;
        ModuleRecipes.writingDeskEnabled = false;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
