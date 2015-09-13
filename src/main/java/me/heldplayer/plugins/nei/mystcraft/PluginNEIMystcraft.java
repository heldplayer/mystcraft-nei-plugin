package me.heldplayer.plugins.nei.mystcraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import me.heldplayer.plugins.nei.mystcraft.client.Integrator;
import me.heldplayer.plugins.nei.mystcraft.packet.C01RequestAges;
import me.heldplayer.plugins.nei.mystcraft.packet.MystNEIPacket;
import me.heldplayer.plugins.nei.mystcraft.packet.S01AgeInfo;
import net.specialattack.forge.core.ModInfo;
import net.specialattack.forge.core.SpACoreMod;
import net.specialattack.forge.core.SpACoreProxy;
import net.specialattack.forge.core.config.ConfigManager;
import net.specialattack.forge.core.config.Configuration;
import net.specialattack.forge.core.packet.SpAPacketHandler;

/**
 * Main mod class
 *
 * @author heldplayer
 */
@Mod(modid = Objects.MOD_ID, name = Objects.MOD_NAME, dependencies = Objects.MOD_DEPENCIES, guiFactory = Objects.GUI_FACTORY)
public class PluginNEIMystcraft extends SpACoreMod {

    @Instance(value = Objects.MOD_ID)
    public static PluginNEIMystcraft instance;
    @SidedProxy(clientSide = Objects.CLIENT_PROXY, serverSide = Objects.SERVER_PROXY)
    public static CommonProxy proxy;

    @Instance("Mystcraft")
    public static Object mystcraft;
    public static SpAPacketHandler<MystNEIPacket> packetHandler;

    public static Config config;
    public static ConfigManager configManager;

    @Configuration("NEI-Mystcraft-Plugin.cfg")
    public static class Config {

        @Configuration.Option(category = "general")
        public boolean addAgeExplorer = true;

        @Configuration.Option(category = "general")
        public boolean addAgeList = true;

        @Configuration.Option(category = "general")
        public boolean allowAgeViewer = true;

        @Configuration.Option(category = "general")
        public boolean allowSymbolExploring = false;

        @Configuration.Option(category = "general")
        public boolean allowPageExploring = true;

        @Configuration.Option(category = "general")
        public boolean opOnlyAgeList = true;

        @Configuration.Option(category = "general")
        public boolean opOnlyAgeViewer = true;

        @Configuration.Option(category = "general")
        public boolean opOnlySymbolExplorer = true;

        @Configuration.Option(category = "general")
        public boolean opOnlyPageExploring = true;

        @Configuration.Option(category = "client", side = Configuration.CSide.CLIENT)
        @Configuration.Alias(name = "hideTechnicalBlocks", category = "general")
        public boolean hideTechnicalBlocks = true;

        @Configuration.Option(category = "client", side = Configuration.CSide.CLIENT)
        @Configuration.Alias(name = "addDecaySubTypes", category = "general")
        public boolean addDecaySubTypes = true;

        @Configuration.Option(category = "client", side = Configuration.CSide.CLIENT)
        @Configuration.Alias(name = "addCreativeNotebooks", category = "general")
        public boolean addCreativeNotebooks = true;

        @Configuration.Option(category = "client", side = Configuration.CSide.CLIENT)
        @Configuration.Alias(name = "addSymbolPages", category = "general")
        public boolean addSymbolPages = true;

        @Configuration.Option(category = "client", side = Configuration.CSide.CLIENT)
        @Configuration.Alias(name = "addLinkPanels", category = "general")
        public boolean addLinkPanels = true;

        @Configuration.Option(category = "client", side = Configuration.CSide.CLIENT)
        @Configuration.Alias(name = "addLinkingBooks", category = "general")
        public boolean addLinkingBooks = true;

        @Configuration.Option(category = "client", side = Configuration.CSide.CLIENT)
        @Configuration.Alias(name = "addItemRanges", category = "general")
        public boolean addItemRanges = true;

        @Configuration.Option(category = "client", side = Configuration.CSide.CLIENT)
        @Configuration.Alias(name = "showRecipeForLinkbooks", category = "general")
        public boolean showRecipeForLinkbooks = true;

        @Configuration.Option(category = "client", side = Configuration.CSide.CLIENT)
        @Configuration.Alias(name = "addInkMixerRecipes", category = "general")
        public boolean addInkMixerRecipes = true;

        @Configuration.Option(category = "client", side = Configuration.CSide.CLIENT)
        @Configuration.Alias(name = "addWritingDeskRecipes", category = "general")
        public boolean addWritingDeskRecipes = true;

        @Configuration.Option(category = "client", side = Configuration.CSide.CLIENT)
        @Configuration.Alias(name = "addInkMixerTooltips", category = "general")
        public boolean addInkMixerTooltips = true;

        @Configuration.Option(category = "client", side = Configuration.CSide.CLIENT)
        @Configuration.Alias(name = "addWritingDeskTooltips", category = "general")
        public boolean addWritingDeskTooltips = true;

        @Configuration.Option(category = "client", side = Configuration.CSide.CLIENT)
        @Configuration.Alias(name = "addRecipesTooltips", category = "general")
        public boolean addRecipesTooltips = true;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Objects.log = event.getModLog();

        PluginNEIMystcraft.configManager = ConfigManager.registerConfig(PluginNEIMystcraft.config = new Config());
        PluginNEIMystcraft.configManager.setReloadListener(new Runnable() {
            @Override
            public void run() {
                Integrator.reinitialize();
            }
        });

        PluginNEIMystcraft.packetHandler = new SpAPacketHandler<MystNEIPacket>("NEI-Mystcraft-Plugin", C01RequestAges.class, S01AgeInfo.class);

        super.preInit(event);
    }

    @Override
    public ModInfo getModInfo() {
        return Objects.MOD_INFO;
    }

    @Override
    public SpACoreProxy getProxy() {
        return PluginNEIMystcraft.proxy;
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        PluginNEIMystcraft.proxy.serverStarted(event);
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        PluginNEIMystcraft.proxy.serverStopped(event);
    }
}
