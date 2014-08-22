package me.heldplayer.plugins.nei.mystcraft;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import me.heldplayer.plugins.nei.mystcraft.client.Integrator;
import me.heldplayer.plugins.nei.mystcraft.packet.MystNEIPacket;
import me.heldplayer.plugins.nei.mystcraft.packet.Packet1RequestAges;
import me.heldplayer.plugins.nei.mystcraft.packet.Packet2AgeInfo;
import net.minecraftforge.common.config.Configuration;
import net.specialattack.forge.core.ModInfo;
import net.specialattack.forge.core.SpACoreMod;
import net.specialattack.forge.core.SpACoreProxy;
import net.specialattack.forge.core.config.Config;
import net.specialattack.forge.core.config.ConfigCategory;
import net.specialattack.forge.core.config.ConfigValue;
import net.specialattack.forge.core.packet.PacketHandler;

import java.util.Collection;

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
    public static PacketHandler<MystNEIPacket> packetHandler;

    //// SpACore Objects
    // Integrator references
    // NEI Config references

    public static ConfigValue<Boolean> addAgeExplorer;
    public static ConfigValue<Boolean> allowSymbolExploring;
    public static ConfigValue<Boolean> allowPageExploring;
    public static ConfigValue<Boolean> opOnlyAgeList;
    public static ConfigValue<Boolean> opOnlySymbolExplorer;
    public static ConfigValue<Boolean> opOnlyPageExploring;

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Objects.log = event.getModLog();

        PluginNEIMystcraft.packetHandler = new PacketHandler<MystNEIPacket>("NEI-Mystcraft-Plugin", Packet1RequestAges.class, Packet2AgeInfo.class);

        // Config
        ConfigCategory<?> category = new ConfigCategory(Configuration.CATEGORY_GENERAL, "config.nei.mystcraft.category.general", null, "General mod settings");

        addAgeExplorer = new ConfigValue<Boolean>("addAgeExplorer", "config.nei.mystcraft.key.addAgeExplorer", null, Boolean.TRUE, "Should ages be explorable?");
        allowSymbolExploring = new ConfigValue<Boolean>("allowSymbolExploring", "config.nei.mystcraft.key.allowSymbolExploring", null, Boolean.FALSE, "Allow age symbols to be explored?");
        allowPageExploring = new ConfigValue<Boolean>("allowPageExploring", "config.nei.mystcraft.key.allowPageExploring", null, Boolean.TRUE, "Allow age pages to be explored?");
        opOnlyAgeList = new ConfigValue<Boolean>("opOnlyAgeList", "config.nei.mystcraft.key.opOnlyAgeList", null, Boolean.TRUE, "Only allow ops to see a list of ages in NEI?");
        opOnlySymbolExplorer = new ConfigValue<Boolean>("opOnlySymbolExplorer", "config.nei.mystcraft.key.opOnlySymbolExplorer", null, Boolean.TRUE, "Only allow ops to explore age symbols?");
        opOnlyPageExploring = new ConfigValue<Boolean>("opOnlyPageExploring", "config.nei.mystcraft.key.opOnlyPageExploring", null, Boolean.TRUE, "Only allow ops to explore age pages?");
        this.config = new Config(event.getSuggestedConfigurationFile());
        this.config.addCategory(category);
        category.addValue(addAgeExplorer);
        category.addValue(allowSymbolExploring);
        category.addValue(allowPageExploring);
        category.addValue(opOnlyAgeList);
        category.addValue(opOnlySymbolExplorer);
        category.addValue(opOnlyPageExploring);
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
        return proxy;
    }

    @Override
    public boolean configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        Integrator.reinitialize();

        return true;
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        proxy.serverStarted(event);
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        proxy.serverStopped(event);
    }
}
