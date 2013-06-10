
package me.heldplayer.plugins.NEI.mystcraft;

import java.io.File;

import me.heldplayer.util.HeldCore.Updater;
import me.heldplayer.util.HeldCore.UsageReporter;
import me.heldplayer.util.HeldCore.config.Config;
import me.heldplayer.util.HeldCore.config.ConfigValue;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.common.Configuration;
import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Main mod class
 * @author heldplayer
 *
 */
@Mod(modid = Objects.MOD_ID, name = Objects.MOD_NAME, version = Objects.MOD_VERSION, dependencies = Objects.MOD_DEPENCIES)
public class PluginNEIMystcraft {

    // HeldCore Objects
    private UsageReporter reporter;
    private Config config;
    public static ConfigValue<Boolean> silentUpdates;
    public static ConfigValue<Boolean> percentages;

    @Instance("Mystcraft")
    private Object mystcraft;

    private InkMixerRecipeHandler inkMixer;
    public static Class<? extends GuiContainer> guiInkMixerClass;

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        File file = new File(event.getModConfigurationDirectory(), "HeldCore");

        if (!file.exists()) {
            file.mkdirs();
        }

        Objects.log = event.getModLog();

        reporter = new UsageReporter(Objects.MOD_ID, Objects.MOD_VERSION, FMLCommonHandler.instance().getSide(), file);

        // Config
        silentUpdates = new ConfigValue<Boolean>("silentUpdates", Configuration.CATEGORY_GENERAL, null, Boolean.TRUE, "Set this to true to hide update messages in the main menu");
        percentages = new ConfigValue<Boolean>("percentages", Configuration.CATEGORY_GENERAL, null, Boolean.FALSE, "Config value to display percentages instead of colouring the ink");
        config = new Config(event.getSuggestedConfigurationFile());
        config.addConfigKey(silentUpdates);
        config.addConfigKey(percentages);
        config.load();
        config.saveOnChange();

        Updater.initializeUpdater(Objects.MOD_ID, Objects.MOD_VERSION, silentUpdates.getValue());
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
        Thread thread = new Thread(reporter, Objects.MOD_ID + " usage reporter");
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

        Integrator.initialize(mystcraft);

        this.inkMixer = new InkMixerRecipeHandler();
        API.registerRecipeHandler(inkMixer);
        API.registerUsageHandler(inkMixer);

        API.registerGuiOverlay(guiInkMixerClass, "inkmixer");
        API.registerGuiOverlayHandler(guiInkMixerClass, new DefaultOverlayHandler(), "inkmixer");
    }

}
