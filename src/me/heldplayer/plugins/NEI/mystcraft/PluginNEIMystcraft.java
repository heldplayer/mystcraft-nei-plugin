
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
 * 
 * @author heldplayer
 * 
 */
@Mod(modid = Objects.MOD_ID, name = Objects.MOD_NAME, version = Objects.MOD_VERSION, dependencies = Objects.MOD_DEPENCIES)
public class PluginNEIMystcraft {

    @Instance("Mystcraft")
    private Object mystcraft;

    private InkMixerRecipeHandler inkMixer;
    public static Class<? extends GuiContainer> guiInkMixerClass;

    // HeldCore Objects
    private UsageReporter reporter;
    private Config config;
    // Config values for HeldCore
    public static ConfigValue<Boolean> silentUpdates;
    public static ConfigValue<Boolean> optOut;
    public static ConfigValue<String> modPack;

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        File file = new File(event.getModConfigurationDirectory(), "HeldCore");

        if (!file.exists()) {
            file.mkdirs();
        }

        Objects.log = event.getModLog();

        // Config
        silentUpdates = new ConfigValue<Boolean>("silentUpdates", Configuration.CATEGORY_GENERAL, null, Boolean.TRUE, "Set this to true to hide update messages in the main menu");
        optOut = new ConfigValue<Boolean>("optOut", Configuration.CATEGORY_GENERAL, null, Boolean.FALSE, "Set this to true to opt-out from statistics gathering. If you are configuring this mod for a modpack, please leave it set to false");
        modPack = new ConfigValue<String>("modPack", Configuration.CATEGORY_GENERAL, null, "", "If this mod is running in a modpack, please set this config value to the name of the modpack");
        this.config = new Config(event.getSuggestedConfigurationFile());
        this.config.addConfigKey(silentUpdates);
        this.config.addConfigKey(optOut);
        this.config.addConfigKey(modPack);
        this.config.load();
        this.config.saveOnChange();

        this.reporter = new UsageReporter(Objects.MOD_ID, Objects.MOD_VERSION, modPack.getValue(), FMLCommonHandler.instance().getSide(), file);

        Updater.initializeUpdater(Objects.MOD_ID, Objects.MOD_VERSION, silentUpdates.getValue());
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
        if (optOut.getValue()) {
            Thread thread = new Thread(this.reporter, Objects.MOD_ID + " usage reporter");
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }

        Integrator.initialize(this.mystcraft);

        this.inkMixer = new InkMixerRecipeHandler();
        API.registerRecipeHandler(this.inkMixer);
        API.registerUsageHandler(this.inkMixer);

        API.registerGuiOverlay(guiInkMixerClass, "inkmixer");
        API.registerGuiOverlayHandler(guiInkMixerClass, new DefaultOverlayHandler(), "inkmixer");
    }

}
