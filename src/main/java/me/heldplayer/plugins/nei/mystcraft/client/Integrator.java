
package me.heldplayer.plugins.nei.mystcraft.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import me.heldplayer.plugins.nei.mystcraft.Assets;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.util.HeldCore.client.GuiHelper;
import me.heldplayer.util.HeldCore.crafting.ICraftingResultHandler;
import me.heldplayer.util.HeldCore.crafting.IHeldCoreRecipe;
import me.heldplayer.util.HeldCore.crafting.ShapelessHeldCoreRecipe;
import me.heldplayer.util.HeldCore.reflection.RClass;
import me.heldplayer.util.HeldCore.reflection.RField;
import me.heldplayer.util.HeldCore.reflection.ReflectionHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldProvider;

import org.lwjgl.opengl.GL11;

import codechicken.core.gui.GuiDraw;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;

import com.xcompwiz.mystcraft.api.MystAPI;
import com.xcompwiz.mystcraft.api.MystObjects;
import com.xcompwiz.mystcraft.api.internals.ColorGradient;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Class used for integrating into Mystcraft
 * 
 * @author heldplayer
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Integrator {

    private static RField<Object, Object> agedataField;
    private static RField<Object, List<String>> symbolsField;
    private static RField<Object, List<ItemStack>> pagesField;
    private static Class worldProviderMystClass;

    private static Map itemstack_bindings;
    private static Map oredict_bindings;
    private static Map itemId_bindings;
    private static HashMap<String, List> grammarMappingsRaw;

    private static List<ItemStack> allLinkpanels;
    private static List<GrammarMapping> grammarMappings;
    private static HashMap<String, List<GrammarMapping>> grammarMappingsMapped;

    /**
     * Initialize all NEI features for Mystcraft
     * 
     */
    public static void initialize() {
        Objects.log.log(Level.FINE, "Initializing Mystcraft Integrator");

        if (PluginNEIMystcraft.hideTechnicalBlocks.getValue()) {
            try {
                Objects.log.log(Level.FINE, "Hiding technical blocks from NEI");
                hideTechnicalBlocks();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.SEVERE, "Failed hiding technical blocks from NEI", ex);
            }
        }

        if (PluginNEIMystcraft.addDecaySubTypes.getValue()) {
            try {
                Objects.log.log(Level.FINE, "Adding decay types to NEI");
                addDecayTypes();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.SEVERE, "Failed adding decay types to NEI", ex);
            }
        }

        if (PluginNEIMystcraft.addCreativeNotebooks.getValue()) {
            try {
                Objects.log.log(Level.FINE, "Adding creative notebooks to NEI view");
                addCreativeNotebooks();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.SEVERE, "Failed adding creative notebooks to NEI", ex);
            }
        }

        try {
            Objects.log.log(Level.FINE, "Adding empty page to NEI");
            addEmptyPage();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed adding empty page to NEI", ex);
        }

        if (PluginNEIMystcraft.addSymbolPages.getValue()) {
            try {
                Objects.log.log(Level.FINE, "Adding symbol pages to NEI view");
                addPages();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.SEVERE, "Failed adding symbol pages to NEI", ex);
            }
        }

        try {
            Objects.log.log(Level.FINE, "Getting all link panels");
            prepareLinkPanels();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed getting all link panels", ex);
        }

        if (PluginNEIMystcraft.addLinkPanels.getValue()) {
            try {
                Objects.log.log(Level.FINE, "Adding link panels to NEI");
                addLinkPanels();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.SEVERE, "Failed adding link panels to NEI", ex);
            }
        }

        if (PluginNEIMystcraft.addLinkingBooks.getValue()) {
            try {
                Objects.log.log(Level.FINE, "Adding linking books to NEI");
                addLinkingbooks();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.SEVERE, "Failed adding linking books to NEI", ex);
            }
        }

        if (PluginNEIMystcraft.addItemRanges.getValue()) {
            try {
                Objects.log.log(Level.FINE, "Adding item ranges to NEI");
                addItemRanges();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.SEVERE, "Failed adding item ranges to NEI", ex);
            }
        }

        try {
            Objects.log.log(Level.FINE, "Getting methods and fields");
            getMethodsAndFields();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed getting methods and fields", ex);
        }

        try {
            Objects.log.log(Level.FINE, "Getting GUI classes");
            NEIConfig.guiInkMixerClass = (Class<? extends GuiContainer>) Class.forName("com.xcompwiz.mystcraft.client.gui.GuiInkMixer");
            NEIConfig.guiWritingDeskClass = (Class<? extends GuiContainer>) Class.forName("com.xcompwiz.mystcraft.client.gui.GuiWritingDesk");
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed getting GUI classes", ex);
        }

        if (PluginNEIMystcraft.showRecipeForLinkbooks.getValue()) {
            try {
                Objects.log.log(Level.FINE, "Adding 'fake' recipes");
                addFakeRecipes();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.SEVERE, "Failed adding 'fake' recipes", ex);
            }
        }

        // TODO: Config setting?
        try {
            Objects.log.log(Level.FINE, "Parsing grammar rules");
            parseGrammarRules();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed parsing grammar rules", ex);
        }
    }

    /**
     * Hide technical blocks and items from the NEI item list
     * 
     * @throws Throwable
     */
    private static void hideTechnicalBlocks() throws Throwable {
        API.hideItem(MystObjects.portal.blockID);
        API.hideItem(MystObjects.writing_desk_block.blockID);
        API.hideItem(MystObjects.star_fissure.blockID);
    }

    /**
     * Add all decay types that are standard Mystcraft to the NEI item list
     * 
     * @throws Throwable
     */
    private static void addDecayTypes() throws Throwable {
        ArrayList<Integer> damageVariants = new ArrayList<Integer>();
        damageVariants.add(0);
        damageVariants.add(1);

        damageVariants.add(3);
        damageVariants.add(4);

        damageVariants.add(6);

        API.setItemDamageVariants(MystObjects.decay.blockID, damageVariants);
    }

    /**
     * Add all creative notebooks to NEI
     * 
     * @throws Throwable
     */
    private static void addCreativeNotebooks() throws Throwable {
        ItemStack notebook = new ItemStack(MystObjects.notebook, 1, 0);

        // Add a standard notebook first
        API.addNBTItem(notebook);

        for (ItemStack stack : MystObjects.creative_notebooks) {
            API.addNBTItem(stack);
        }
    }

    private static void addEmptyPage() throws Throwable {
        ItemStack page = new ItemStack(MystObjects.page, 1, 0);

        API.addNBTItem(page);
    }

    /**
     * Add all pages to NEI
     * 
     * @throws Throwable
     */
    private static void addPages() throws Throwable {
        // Add all the pages for all the symbols
        List<IAgeSymbol> allSymbols = MystAPI.symbol.getAllRegisteredSymbols();

        for (IAgeSymbol symbol : allSymbols) {
            API.addNBTItem(MystAPI.itemFact.buildSymbolPage(symbol.identifier()));
        }
    }

    private static void prepareLinkPanels() throws Throwable {
        RClass<Object> inkEffectsClass = (RClass<Object>) ReflectionHelper.getClass("com.xcompwiz.mystcraft.data.InkEffects");
        RField<Object, HashMap> colormapField = inkEffectsClass.getField("colormap");

        // Add all modifiers known to have a colour, this includes mod added modifiers
        HashMap colormap = colormapField.getStatic();

        TreeMap map = new TreeMap(new LinkPanelSorter());
        map.putAll(colormap);

        Object[] keys = map.keySet().toArray(new Object[0]);

        int bin = binary(keys.length);

        allLinkpanels = new ArrayList<ItemStack>();

        for (int i = 0; i <= bin; i++) {
            ItemStack is = new ItemStack(MystObjects.page, 1, 0);

            NBTTagCompound compound = new NBTTagCompound("tag");
            NBTTagCompound linkPanelCompound = new NBTTagCompound("linkpanel");

            NBTTagList list = new NBTTagList("properties");

            for (int j = 0; j < keys.length; j++) {
                if (((i >> j) & 0x1) == 1) {
                    list.appendTag(new NBTTagString("", (String) keys[j]));
                }
            }

            linkPanelCompound.setTag("properties", list);

            compound.setTag("linkpanel", linkPanelCompound);

            is.setTagCompound(compound);

            allLinkpanels.add(is);
        }
    }

    /**
     * Add all link panels to NEI
     * 
     * @throws Throwable
     */
    private static void addLinkPanels() throws Throwable {
        for (ItemStack stack : allLinkpanels) {
            API.addNBTItem(stack);
        }
    }

    private static void addLinkingbooks() throws Throwable {
        for (ItemStack panel : allLinkpanels) {
            ItemStack book = new ItemStack(MystObjects.linkbook_unlinked);

            book.stackTagCompound = (NBTTagCompound) panel.stackTagCompound.copy();

            API.addNBTItem(book);
        }
    }

    /**
     * Add item ranges to the NEI interface
     * 
     * @throws Throwable
     */
    private static void addItemRanges() throws Throwable {
        MultiItemRange mystBlocks = new MultiItemRange();

        mystBlocks.add(MystObjects.portal);
        mystBlocks.add(MystObjects.crystal);
        mystBlocks.add(MystObjects.crystal_receptacle);
        mystBlocks.add(MystObjects.decay);
        mystBlocks.add(MystObjects.bookstand);
        mystBlocks.add(MystObjects.book_lectern);
        mystBlocks.add(MystObjects.writing_desk_block);
        mystBlocks.add(MystObjects.bookbinder);
        mystBlocks.add(MystObjects.inkmixer);
        mystBlocks.add(MystObjects.star_fissure);

        mystBlocks.add(MystObjects.link_modifer);

        API.addSetRange("Mystcraft.Blocks", mystBlocks);

        MultiItemRange mystItems = new MultiItemRange();

        mystItems.add(MystObjects.writing_desk);
        mystItems.add(MystObjects.linkbook);
        mystItems.add(MystObjects.inkvial);

        if (PluginNEIMystcraft.addSymbolPages.getValue() || PluginNEIMystcraft.addLinkPanels.getValue()) {
            MultiItemRange mystPages = new MultiItemRange();
            mystPages.add(MystObjects.page);

            API.addSetRange("Mystcraft.Items.Pages", mystPages);
        }
        else {
            mystItems.add(MystObjects.page);
        }

        if (PluginNEIMystcraft.addCreativeNotebooks.getValue()) {
            MultiItemRange mystNotebooks = new MultiItemRange();
            mystNotebooks.add(MystObjects.notebook);

            API.addSetRange("Mystcraft.Items.Notebooks", mystNotebooks);
        }
        else {
            mystItems.add(MystObjects.notebook);
        }

        if (PluginNEIMystcraft.addLinkingBooks.getValue()) {
            MultiItemRange mystLinkbooks = new MultiItemRange();
            mystLinkbooks.add(MystObjects.linkbook_unlinked);

            API.addSetRange("Mystcraft.Items.Linking Books", mystLinkbooks);
        }
        else {
            mystItems.add(MystObjects.linkbook_unlinked);
        }

        if (PluginNEIMystcraft.addAgeList.getValue()) {
            MultiItemRange mystDescriptiveBooks = new MultiItemRange();
            mystDescriptiveBooks.add(MystObjects.descriptive_book);

            API.addSetRange("Mystcraft.Items.Descriptive Books", mystDescriptiveBooks);
        }
        else {
            mystItems.add(MystObjects.linkbook_unlinked);
        }

        API.addSetRange("Mystcraft.Items", mystItems);
    }

    /**
     * Gets all methods and fields required by recipe handlers and such to
     * function
     * 
     * @throws Throwable
     */
    private static void getMethodsAndFields() throws Throwable {
        RClass<Object> inkEffectsClass = (RClass<Object>) ReflectionHelper.getClass("com.xcompwiz.mystcraft.data.InkEffects");

        RField<Object, Map> bindings = inkEffectsClass.getField("itemstack_bindings");
        itemstack_bindings = bindings.getStatic();

        bindings = inkEffectsClass.getField("oredict_bindings");
        oredict_bindings = bindings.getStatic();

        bindings = inkEffectsClass.getField("itemId_bindings");
        itemId_bindings = bindings.getStatic();

        RClass<Object> grammarGeneratorClass = new RClass(Class.forName("com.xcompwiz.mystcraft.symbols.GrammarGenerator"));
        RField<Object, HashMap<String, List>> mappingsField = grammarGeneratorClass.getField("mappings");
        grammarMappingsRaw = mappingsField.get(null);

        RClass<Object> worldProviderMystClass = new RClass(Integrator.worldProviderMystClass = Class.forName("com.xcompwiz.mystcraft.world.WorldProviderMyst"));
        agedataField = worldProviderMystClass.getField("agedata");

        RClass<Object> ageDataClass = new RClass(Class.forName("com.xcompwiz.mystcraft.world.agedata.AgeData"));
        symbolsField = ageDataClass.getField("symbols");
        pagesField = ageDataClass.getField("pages");
    }

    /**
     * Utility method used in {@link #addLinkPanels()}
     * 
     * @param bits
     *        The amount of bits to turn to 1
     * @return Returns an int that has bits set to 1 equal to {@link bits}
     */
    private static int binary(int bits) {
        int result = 0;
        for (int i = 0; i < bits; i++) {
            result |= 1 << i;
        }
        return result;
    }

    /**
     * Returns an ink mixer recipe that uses the given stack as ingredient
     * 
     * @param stack
     * @return
     */
    public static InkMixerRecipe getInkMixerRecipe(ItemStack stack) {
        try {
            if (stack == null) {
                return null;
            }

            Map<String, Float> properties = MystAPI.linkProperties.getPropertiesForItem(stack);

            if (properties == null) {
                return null;
            }

            ColorGradient gradient = MystAPI.linkProperties.getPropertiesGradient(properties);

            String[] modifiers = properties.keySet().toArray(new String[properties.size()]);

            return new InkMixerRecipe(gradient, modifiers);
        }
        catch (Throwable e) {
            Objects.log.log(Level.WARNING, "Failed getting gradient", e);
            return null;
        }
    }

    /**
     * Returns all possible ink mixer recipes as an ArrayList of Objects
     * 
     * @return
     */
    public static ArrayList getALlInkMixerRecipes() {
        ArrayList result = new ArrayList();

        result.addAll(itemstack_bindings.keySet());
        result.addAll(oredict_bindings.keySet());
        result.addAll(itemId_bindings.keySet());

        return result;
    }

    public static List<ItemStack> getAllLinkpanels() {
        return allLinkpanels;
    }

    /**
     * Renders a symbol
     * 
     * @param symbol
     *        The symbol to render
     * @param x
     *        X-position to render at
     * @param y
     *        Y-position to render at
     * @param z
     *        Z-index to render at
     * @param width
     *        Width to render
     * @param height
     *        Height to render
     */
    public static void renderPage(IAgeSymbol symbol, float x, float y, float z, float width, float height) {
        GuiDraw.changeTexture(Assets.bookPageLeft);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiHelper.drawTexturedModalRect((int) x, (int) y, (int) width, (int) height, z, 0.609375F, 0.0F, 0.7294117647058824F, 0.15625F);

        MystAPI.render.drawSymbol(x, y + (height + 1.0F - width) / 2.0F, z, width - 1.0F, symbol);
    }

    private static void addFakeRecipes() {
        ICraftingResultHandler handler = new ICraftingResultHandler() {
            @Override
            public ItemStack getOutput(IHeldCoreRecipe recipe, List<ItemStack> input) {
                ItemStack result = recipe.getOutput();

                for (ItemStack stack : input) {
                    if (stack != null && stack.getItem() == MystObjects.page) {
                        if (stack.stackTagCompound != null) {
                            result.stackTagCompound = stack.stackTagCompound;
                            break;
                        }
                    }
                }

                return result;
            }

            @Override
            public String getOwningModName() {
                return "Mystcraft";
            }

            @Override
            public String getOwningModId() {
                return "Mystcraft";
            }

            @Override
            public boolean isValidRecipeInput(ItemStack input) {
                if (input != null && input.getItem() == MystObjects.page) {
                    if (input.stackTagCompound == null) {
                        return false;
                    }

                    NBTTagCompound tag = input.stackTagCompound;
                    if (!tag.hasKey("linkpanel")) {
                        return false;
                    }

                    return true;
                }
                return true;
            }

            @Override
            public String getNEIOverlayText() {
                return StatCollector.translateToLocal("nei.mystcraft.linkbook.activate");
            }
        };

        List<ItemStack> linkPanels = getAllLinkpanels();

        ItemStack[] stacks = new ItemStack[linkPanels.size()];
        stacks = linkPanels.toArray(stacks);

        ShapelessHeldCoreRecipe recipe = new ShapelessHeldCoreRecipe(handler, new ItemStack(MystObjects.linkbook_unlinked), stacks, new ItemStack(Item.leather));

        GameRegistry.addRecipe(recipe);
    }

    private static void parseGrammarRules() throws Throwable {
        RClass<Object> Rule = new RClass(Class.forName("com.xcompwiz.mystcraft.symbols.GrammarGenerator$Rule"));
        RField values = Rule.getField("values");
        RField rarity = Rule.getField("rarity");

        grammarMappings = new ArrayList<GrammarMapping>();
        grammarMappingsMapped = new HashMap<String, List<GrammarMapping>>();

        for (String key : grammarMappingsRaw.keySet()) {
            List<?> rules = grammarMappingsRaw.get(key);
            List<GrammarMapping> mappings = new ArrayList<GrammarMapping>();
            grammarMappingsMapped.put(key, mappings);

            for (Object rule : rules) {
                GrammarMapping mapping = new GrammarMapping(key, (List<String>) values.get(rule), (Float) rarity.get(rule));
                grammarMappings.add(mapping);
                mappings.add(mapping);
            }
        }
    }

    public static List<String> getAgeSymbols(WorldProvider provider) {
        if (!worldProviderMystClass.isAssignableFrom(provider.getClass())) {
            return null;
        }

        Object ageData = agedataField.get(provider);

        return symbolsField.get(ageData);
    }

    public static List<ItemStack> getAgePages(WorldProvider provider) {
        if (!worldProviderMystClass.isAssignableFrom(provider.getClass())) {
            return null;
        }

        Object ageData = agedataField.get(provider);

        return pagesField.get(ageData);
    }

}
