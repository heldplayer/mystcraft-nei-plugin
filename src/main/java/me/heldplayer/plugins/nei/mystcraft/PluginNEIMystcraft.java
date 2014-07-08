package me.heldplayer.plugins.nei.mystcraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.Side;
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Objects.log = event.getModLog();

        PluginNEIMystcraft.packetHandler = new PacketHandler("NEI-Mystcraft-Plugin", Packet1RequestAges.class, Packet2AgeInfo.class);

        // Config
        ConfigCategory<?> category = new ConfigCategory(Configuration.CATEGORY_GENERAL, "config.nei.mystcraft.category.general", null, "General mod settings");
        hideTechnicalBlocks = new ConfigValue<Boolean>("hideTechnicalBlocks", "config.nei.mystcraft.key.hideTechnicalBlocks", Side.CLIENT, Boolean.TRUE, "Should technical blocks be hidden?");
        addDecaySubTypes = new ConfigValue<Boolean>("addDecaySubTypes", "config.nei.mystcraft.key.addDecaySubTypes", Side.CLIENT, Boolean.TRUE, "Should all decay types be visible in NEI?");
        addCreativeNotebooks = new ConfigValue<Boolean>("addCreativeNotebooks", "config.nei.mystcraft.key.addCreativeNotebooks", Side.CLIENT, Boolean.TRUE, "Should creative notebooks be added to NEI?");
        addSymbolPages = new ConfigValue<Boolean>("addSymbolPages", "config.nei.mystcraft.key.addSymbolPages", Side.CLIENT, Boolean.TRUE, "Should symbol pages be added to NEI?");
        addLinkPanels = new ConfigValue<Boolean>("addLinkPanels", "config.nei.mystcraft.key.addLinkPanels", Side.CLIENT, Boolean.TRUE, "Should link panels be added to NEI?");
        addLinkingBooks = new ConfigValue<Boolean>("addLinkingBooks", "config.nei.mystcraft.key.addLinkingBooks", Side.CLIENT, Boolean.TRUE, "Should unlinked linking books be added to NEI?");
        addItemRanges = new ConfigValue<Boolean>("addItemRanges", "config.nei.mystcraft.key.addItemRanges", Side.CLIENT, Boolean.TRUE, "Should item ranges be added to NEI?");
        showRecipeForLinkbooks = new ConfigValue<Boolean>("showRecipeForLinkbooks", "config.nei.mystcraft.key.showRecipeForLinkbooks", Side.CLIENT, Boolean.TRUE, "Should the recipe for linking books be added to NEI?");
        addInkMixerRecipes = new ConfigValue<Boolean>("addInkMixerRecipes", "config.nei.mystcraft.key.addInkMixerRecipes", Side.CLIENT, Boolean.TRUE, "Should ink mixer recipes be added to NEI?");
        addWritingDeskRecipes = new ConfigValue<Boolean>("addWritingDeskRecipes", "config.nei.mystcraft.key.addWritingDeskRecipes", Side.CLIENT, Boolean.TRUE, "Should writing desk recipes be added to NEI?");
        addInkMixerTooltips = new ConfigValue<Boolean>("addInkMixerTooltips", "config.nei.mystcraft.key.addInkMixerTooltips", Side.CLIENT, Boolean.TRUE, "Should there be tooltips for the Ink Mixer?");
        addWritingDeskTooltips = new ConfigValue<Boolean>("addWritingDeskTooltips", "config.nei.mystcraft.key.addWritingDeskTooltips", Side.CLIENT, Boolean.TRUE, "Should there be tooltips for the Writing Desk?");
        addRecipesTooltips = new ConfigValue<Boolean>("addRecipesTooltips", "config.nei.mystcraft.key.addRecipesTooltips", Side.CLIENT, Boolean.TRUE, "Should there be clickable regions to show all recipes in a crafting station?");
        addAgeExplorer = new ConfigValue<Boolean>("addAgeExplorer", "config.nei.mystcraft.key.addAgeExplorer", null, Boolean.TRUE, "Should ages be explorable?");
        addAgeList = new ConfigValue<Boolean>("addAgeList", "config.nei.mystcraft.key.addAgeList", null, Boolean.TRUE, "Should there be a list of items in NEI?");
        allowSymbolExploring = new ConfigValue<Boolean>("allowSymbolExploring", "config.nei.mystcraft.key.allowSymbolExploring", null, Boolean.FALSE, "Allow age symbols to be explored?");
        allowPageExploring = new ConfigValue<Boolean>("allowPageExploring", "config.nei.mystcraft.key.allowPageExploring", null, Boolean.TRUE, "Allow age pages to be explored?");
        opOnlyAgeList = new ConfigValue<Boolean>("opOnlyAgeList", "config.nei.mystcraft.key.opOnlyAgeList", null, Boolean.TRUE, "Only allow ops to see a list of ages in NEI?");
        opOnlySymbolExplorer = new ConfigValue<Boolean>("opOnlySymbolExplorer", "config.nei.mystcraft.key.opOnlySymbolExplorer", null, Boolean.TRUE, "Only allow ops to explore age symbols?");
        opOnlyPageExploring = new ConfigValue<Boolean>("opOnlyPageExploring", "config.nei.mystcraft.key.opOnlyPageExploring", null, Boolean.TRUE, "Only allow ops to explore age pages?");
        this.config = new Config(event.getSuggestedConfigurationFile());
        this.config.addCategory(category);
        category.addValue(hideTechnicalBlocks);
        category.addValue(addDecaySubTypes);
        category.addValue(addCreativeNotebooks);
        category.addValue(addSymbolPages);
        category.addValue(addLinkPanels);
        category.addValue(addLinkingBooks);
        category.addValue(addItemRanges);
        category.addValue(showRecipeForLinkbooks);
        category.addValue(addInkMixerRecipes);
        category.addValue(addWritingDeskRecipes);
        category.addValue(addInkMixerTooltips);
        category.addValue(addWritingDeskTooltips);
        category.addValue(addRecipesTooltips);
        category.addValue(addAgeExplorer);
        category.addValue(addAgeList);
        category.addValue(allowSymbolExploring);
        category.addValue(allowPageExploring);
        category.addValue(opOnlyAgeList);
        category.addValue(opOnlySymbolExplorer);
        category.addValue(opOnlyPageExploring);

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

}
