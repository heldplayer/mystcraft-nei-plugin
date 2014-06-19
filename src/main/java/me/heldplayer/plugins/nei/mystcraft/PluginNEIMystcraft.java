
package me.heldplayer.plugins.nei.mystcraft;

import me.heldplayer.plugins.nei.mystcraft.packet.Packet1RequestAges;
import me.heldplayer.plugins.nei.mystcraft.packet.Packet2AgeInfo;
import net.minecraftforge.common.config.Configuration;
import net.specialattack.forge.core.ModInfo;
import net.specialattack.forge.core.SpACoreMod;
import net.specialattack.forge.core.SpACoreProxy;
import net.specialattack.forge.core.config.Config;
import net.specialattack.forge.core.config.ConfigValue;
import net.specialattack.forge.core.packet.PacketHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.relauncher.Side;

/**
 * Main mod class
 * 
 * @author heldplayer
 * 
 */
@Mod(modid = Objects.MOD_ID, name = Objects.MOD_NAME, dependencies = Objects.MOD_DEPENCIES)
public class PluginNEIMystcraft extends SpACoreMod {

    @Instance(value = Objects.MOD_ID)
    public static PluginNEIMystcraft instance;
    @SidedProxy(clientSide = Objects.CLIENT_PROXY, serverSide = Objects.SERVER_PROXY)
    public static CommonProxy proxy;

    @Instance("Mystcraft")
    public static Object mystcraft;
    public static PacketHandler packetHandler;

    //// SpACore Objects
    // Integrator references
    public static ConfigValue<Boolean> hideTechnicalBlocks;
    public static ConfigValue<Boolean> addDecaySubTypes;
    public static ConfigValue<Boolean> addCreativeNotebooks;
    public static ConfigValue<Boolean> addSymbolPages;
    public static ConfigValue<Boolean> addLinkPanels;
    public static ConfigValue<Boolean> addLinkingBooks;
    public static ConfigValue<Boolean> addItemRanges;
    public static ConfigValue<Boolean> showRecipeForLinkbooks;
    // NEI Config references
    public static ConfigValue<Boolean> addInkMixerRecipes;
    public static ConfigValue<Boolean> addWritingDeskRecipes;
    public static ConfigValue<Boolean> addInkMixerTooltips;
    public static ConfigValue<Boolean> addWritingDeskTooltips;
    public static ConfigValue<Boolean> addRecipesTooltips;

    public static ConfigValue<Boolean> addAgeExplorer;
    public static ConfigValue<Boolean> addAgeList;
    public static ConfigValue<Boolean> allowSymbolExploring;
    public static ConfigValue<Boolean> allowPageExploring;
    public static ConfigValue<Boolean> opOnlyAgeList;
    public static ConfigValue<Boolean> opOnlySymbolExplorer;
    public static ConfigValue<Boolean> opOnlyPageExploring;

    @Override
    @SuppressWarnings("unchecked")
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Objects.log = event.getModLog();

        PluginNEIMystcraft.packetHandler = new PacketHandler("NEI-Mystcraft-Plugin", Packet1RequestAges.class, Packet2AgeInfo.class);

        // Config
        hideTechnicalBlocks = new ConfigValue<Boolean>("hideTechnicalBlocks", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Should technical blocks be hidden?");
        addDecaySubTypes = new ConfigValue<Boolean>("addDecaySubTypes", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Should all decay types be visible in NEI?");
        addCreativeNotebooks = new ConfigValue<Boolean>("addCreativeNotebooks", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Should creative notebooks be added to NEI?");
        addSymbolPages = new ConfigValue<Boolean>("addSymbolPages", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Should symbol pages be added to NEI?");
        addLinkPanels = new ConfigValue<Boolean>("addLinkPanels", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Should link panels be added to NEI?");
        addLinkingBooks = new ConfigValue<Boolean>("addLinkingBooks", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Should unlinked linking books be added to NEI?");
        addItemRanges = new ConfigValue<Boolean>("addItemRanges", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Should item ranges be added to NEI?");
        showRecipeForLinkbooks = new ConfigValue<Boolean>("showRecipeForLinkbooks", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Should the recipe for linking books be added to NEI?");
        addInkMixerRecipes = new ConfigValue<Boolean>("addInkMixerRecipes", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Should ink mixer recipes be added to NEI?");
        addWritingDeskRecipes = new ConfigValue<Boolean>("addWritingDeskRecipes", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Should writing desk recipes be added to NEI?");
        addInkMixerTooltips = new ConfigValue<Boolean>("addInkMixerTooltips", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Should there be tooltips for the Ink Mixer?");
        addWritingDeskTooltips = new ConfigValue<Boolean>("addWritingDeskTooltips", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Should there be tooltips for the Writing Desk?");
        addRecipesTooltips = new ConfigValue<Boolean>("addRecipesTooltips", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Should there be clickable regions to show all recipes in a crafting station?");
        addAgeExplorer = new ConfigValue<Boolean>("addAgeExplorer", Configuration.CATEGORY_GENERAL, null, Boolean.TRUE, "Should ages be explorable?");
        addAgeList = new ConfigValue<Boolean>("addAgeList", Configuration.CATEGORY_GENERAL, null, Boolean.TRUE, "Should there be a list of items in NEI?");
        allowSymbolExploring = new ConfigValue<Boolean>("allowSymbolExploring", Configuration.CATEGORY_GENERAL, null, Boolean.FALSE, "Allow age symbols to be explored?");
        allowPageExploring = new ConfigValue<Boolean>("allowPageExploring", Configuration.CATEGORY_GENERAL, null, Boolean.TRUE, "Allow age pages to be explored?");
        opOnlyAgeList = new ConfigValue<Boolean>("opOnlyAgeList", Configuration.CATEGORY_GENERAL, null, Boolean.TRUE, "Only allow ops to see a list of ages in NEI?");
        opOnlySymbolExplorer = new ConfigValue<Boolean>("opOnlySymbolExplorer", Configuration.CATEGORY_GENERAL, null, Boolean.TRUE, "Only allow ops to explore age symbols?");
        opOnlyPageExploring = new ConfigValue<Boolean>("opOnlyPageExploring", Configuration.CATEGORY_GENERAL, null, Boolean.TRUE, "Only allow ops to explore age pages?");
        this.config = new Config(event.getSuggestedConfigurationFile());
        this.config.addConfigKey(hideTechnicalBlocks);
        this.config.addConfigKey(addDecaySubTypes);
        this.config.addConfigKey(addCreativeNotebooks);
        this.config.addConfigKey(addSymbolPages);
        this.config.addConfigKey(addLinkPanels);
        this.config.addConfigKey(addLinkingBooks);
        this.config.addConfigKey(addItemRanges);
        this.config.addConfigKey(showRecipeForLinkbooks);
        this.config.addConfigKey(addInkMixerRecipes);
        this.config.addConfigKey(addWritingDeskRecipes);
        this.config.addConfigKey(addInkMixerTooltips);
        this.config.addConfigKey(addWritingDeskTooltips);
        this.config.addConfigKey(addRecipesTooltips);
        this.config.addConfigKey(addAgeExplorer);
        this.config.addConfigKey(addAgeList);
        this.config.addConfigKey(allowSymbolExploring);
        this.config.addConfigKey(allowPageExploring);
        this.config.addConfigKey(opOnlyAgeList);
        this.config.addConfigKey(opOnlySymbolExplorer);
        this.config.addConfigKey(opOnlyPageExploring);

        super.preInit(event);
    }

    @Override
    @EventHandler
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        proxy.serverStarted(event);
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        proxy.serverStopped(event);
    }

    @Override
    public ModInfo getModInfo() {
        return Objects.MOD_INFO;
    }

    @Override
    public SpACoreProxy getProxy() {
        return proxy;
    }

}
