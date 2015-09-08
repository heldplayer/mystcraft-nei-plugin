package me.heldplayer.plugins.nei.mystcraft;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import java.util.Collection;
import me.heldplayer.plugins.nei.mystcraft.client.Integrator;
import me.heldplayer.plugins.nei.mystcraft.packet.C01RequestAges;
import me.heldplayer.plugins.nei.mystcraft.packet.MystNEIPacket;
import me.heldplayer.plugins.nei.mystcraft.packet.S01AgeInfo;
import net.minecraftforge.common.config.Configuration;
import net.specialattack.forge.core.ModInfo;
import net.specialattack.forge.core.SpACoreMod;
import net.specialattack.forge.core.SpACoreProxy;
import net.specialattack.forge.core.config.Config;
import net.specialattack.forge.core.config.ConfigCategory;
import net.specialattack.forge.core.config.ConfigValue;
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

    //// SpACore Objects
    // Integrator references
    // NEI Config references

    public static ConfigValue<Boolean> addAgeExplorer;
    public static ConfigValue<Boolean> allowAgeViewer;
    public static ConfigValue<Boolean> allowSymbolExploring;
    public static ConfigValue<Boolean> allowPageExploring;
    public static ConfigValue<Boolean> opOnlyAgeList;
    public static ConfigValue<Boolean> opOnlyAgeViewer;
    public static ConfigValue<Boolean> opOnlySymbolExplorer;
    public static ConfigValue<Boolean> opOnlyPageExploring;

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Objects.log = event.getModLog();

        PluginNEIMystcraft.packetHandler = new SpAPacketHandler<MystNEIPacket>("NEI-Mystcraft-Plugin", C01RequestAges.class, S01AgeInfo.class);

        // Config
        ConfigCategory<?> category = new ConfigCategory(Configuration.CATEGORY_GENERAL, "myst-nei:config.general", null);

        PluginNEIMystcraft.addAgeExplorer = new ConfigValue<Boolean>("addAgeExplorer", "myst-nei:config.general.addAgeExplorer", null, Boolean.TRUE);
        PluginNEIMystcraft.allowAgeViewer = new ConfigValue<Boolean>("allowAgeViewer", "myst-nei:config.general.allowAgeViewer", null, Boolean.TRUE);
        PluginNEIMystcraft.allowSymbolExploring = new ConfigValue<Boolean>("allowSymbolExploring", "myst-nei:config.general.allowSymbolExploring", null, Boolean.FALSE);
        PluginNEIMystcraft.allowPageExploring = new ConfigValue<Boolean>("allowPageExploring", "myst-nei:config.general.allowPageExploring", null, Boolean.TRUE);
        PluginNEIMystcraft.opOnlyAgeList = new ConfigValue<Boolean>("opOnlyAgeList", "myst-nei:config.general.opOnlyAgeList", null, Boolean.TRUE);
        PluginNEIMystcraft.opOnlyAgeViewer = new ConfigValue<Boolean>("opOnlyAgeViewer", "myst-nei:config.general.opOnlyAgeViewer", null, Boolean.TRUE);
        PluginNEIMystcraft.opOnlySymbolExplorer = new ConfigValue<Boolean>("opOnlySymbolExplorer", "myst-nei:config.general.opOnlySymbolExplorer", null, Boolean.TRUE);
        PluginNEIMystcraft.opOnlyPageExploring = new ConfigValue<Boolean>("opOnlyPageExploring", "myst-nei:config.general.opOnlyPageExploring", null, Boolean.TRUE);
        this.config = new Config(event.getSuggestedConfigurationFile());
        this.config.addCategory(category);
        category.addValue(PluginNEIMystcraft.addAgeExplorer);
        category.addValue(PluginNEIMystcraft.allowAgeViewer);
        category.addValue(PluginNEIMystcraft.allowSymbolExploring);
        category.addValue(PluginNEIMystcraft.allowPageExploring);
        category.addValue(PluginNEIMystcraft.opOnlyAgeList);
        category.addValue(PluginNEIMystcraft.opOnlyAgeViewer);
        category.addValue(PluginNEIMystcraft.opOnlySymbolExplorer);
        category.addValue(PluginNEIMystcraft.opOnlyPageExploring);
        Collection<ConfigValue<?>> values = Integrator.getAllConfigValues();
        for (ConfigValue<?> value : values) {
            category.addValue(value);
        }

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

    @Override
    public boolean configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        Integrator.reinitialize();

        return true;
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
