
package me.heldplayer.plugins.nei.mystcraft.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import me.heldplayer.plugins.nei.mystcraft.Assets;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldProvider;
import net.specialattack.forge.core.client.GuiHelper;
import net.specialattack.forge.core.crafting.ICraftingResultHandler;
import net.specialattack.forge.core.crafting.ISpACoreRecipe;
import net.specialattack.forge.core.crafting.ShapelessSpACoreRecipe;
import net.specialattack.forge.core.reflection.RClass;
import net.specialattack.forge.core.reflection.RField;
import net.specialattack.forge.core.reflection.ReflectionHelper;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.api.API;

import com.xcompwiz.mystcraft.core.InternalAPI;
import com.xcompwiz.mystcraft.symbol.ColorGradient;
import com.xcompwiz.mystcraft.symbol.IAgeSymbol;

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

    private static List<ItemStack> allLinkpanels;

    /**
     * Initialize all NEI features for Mystcraft
     * 
     */
    public static void initialize() {
        Objects.log.log(Level.DEBUG, "Initializing Mystcraft Integrator");

        if (PluginNEIMystcraft.hideTechnicalBlocks.getValue()) {
            try {
                Objects.log.log(Level.DEBUG, "Hiding technical blocks from NEI");
                hideTechnicalBlocks();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.ERROR, "Failed hiding technical blocks from NEI", ex);
            }
        }

        if (PluginNEIMystcraft.addDecaySubTypes.getValue()) {
            try {
                Objects.log.log(Level.DEBUG, "Adding decay types to NEI");
                addDecayTypes();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.ERROR, "Failed adding decay types to NEI", ex);
            }
        }

        if (PluginNEIMystcraft.addCreativeNotebooks.getValue()) {
            try {
                Objects.log.log(Level.DEBUG, "Adding creative notebooks to NEI view");
                addCreativeNotebooks();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.ERROR, "Failed adding creative notebooks to NEI", ex);
            }
        }

        if (PluginNEIMystcraft.addSymbolPages.getValue()) {
            try {
                Objects.log.log(Level.DEBUG, "Adding symbol pages to NEI view");
                addPages();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.ERROR, "Failed adding symbol pages to NEI", ex);
            }
        }

        try {
            Objects.log.log(Level.DEBUG, "Getting all link panels");
            prepareLinkPanels();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.ERROR, "Failed getting all link panels", ex);
        }

        if (PluginNEIMystcraft.addLinkPanels.getValue()) {
            try {
                Objects.log.log(Level.DEBUG, "Adding link panels to NEI");
                addLinkPanels();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.ERROR, "Failed adding link panels to NEI", ex);
            }
        }

        if (PluginNEIMystcraft.addLinkingBooks.getValue()) {
            try {
                Objects.log.log(Level.DEBUG, "Adding linking books to NEI");
                addLinkingbooks();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.ERROR, "Failed adding linking books to NEI", ex);
            }
        }

        if (PluginNEIMystcraft.addItemRanges.getValue()) {
            try {
                Objects.log.log(Level.DEBUG, "Adding item ranges to NEI");
                addItemRanges();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.ERROR, "Failed adding item ranges to NEI", ex);
            }
        }

        try {
            Objects.log.log(Level.DEBUG, "Getting methods and fields");
            getMethodsAndFields();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.ERROR, "Failed getting methods and fields", ex);
        }

        try {
            Objects.log.log(Level.DEBUG, "Getting GUI classes");
            NEIConfig.guiInkMixerClass = (Class<? extends GuiContainer>) Class.forName("com.xcompwiz.mystcraft.client.gui.GuiInkMixer");
            NEIConfig.guiWritingDeskClass = (Class<? extends GuiContainer>) Class.forName("com.xcompwiz.mystcraft.client.gui.GuiWritingDesk");
        }
        catch (Throwable ex) {
            Objects.log.log(Level.ERROR, "Failed getting GUI classes", ex);
        }

        if (PluginNEIMystcraft.showRecipeForLinkbooks.getValue()) {
            try {
                Objects.log.log(Level.DEBUG, "Adding 'fake' recipes");
                addFakeRecipes();
            }
            catch (Throwable ex) {
                Objects.log.log(Level.ERROR, "Failed adding 'fake' recipes", ex);
            }
        }
    }

    /**
     * Hide technical blocks and items from the NEI item list
     * 
     * @throws Throwable
     */
    private static void hideTechnicalBlocks() throws Throwable {
        API.hideItem(new ItemStack(MystObjs.portal));
        API.hideItem(new ItemStack(MystObjs.writing_desk_block));
        API.hideItem(new ItemStack(MystObjs.star_fissure));
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

        for (Integer damage : damageVariants) {
            API.addItemListEntry(new ItemStack(MystObjs.decay, 1, damage));
        }
    }

    /**
     * Add all creative notebooks to NEI
     * 
     * @throws Throwable
     */
    private static void addCreativeNotebooks() throws Throwable {
        // ItemStack notebook = new ItemStack(MystObjs.notebook, 1, 0);

        // Add a standard notebook first
        // API.addItemListEntry(notebook);

        for (ItemStack stack : MystObjs.creative_notebooks) {
            API.addItemListEntry(stack);
        }
    }

    /**
     * Add all pages to NEI
     * 
     * @throws Throwable
     */
    private static void addPages() throws Throwable {
        List<IAgeSymbol> allSymbols = InternalAPI.symbol.getAllRegisteredSymbols();

        for (IAgeSymbol symbol : allSymbols) {
            API.addItemListEntry(InternalAPI.itemFact.buildSymbolPage(symbol.identifier()));
        }
    }

    private static void prepareLinkPanels() throws Throwable {
        RClass<Object> inkEffectsClass = (RClass<Object>) ReflectionHelper.getClass("com.xcompwiz.mystcraft.data.InkEffects");
        RField<Object, HashMap> colormapField = inkEffectsClass.getField("colormap");

        // Add all modifiers known to have a colour, this includes mod added modifiers
        HashMap colormap = colormapField.getStatic();

        TreeMap map = new TreeMap(new LinkPanelSorter());
        map.putAll(colormap);
        if (!map.containsKey("Following")) {
            map.put("Following", null);
        }

        Object[] keys = map.keySet().toArray(new Object[0]);

        int bin = binary(keys.length);

        allLinkpanels = new ArrayList<ItemStack>();

        for (int i = 0; i <= bin; i++) {
            ItemStack is = new ItemStack(MystObjs.page, 1, 0);

            NBTTagCompound compound = new NBTTagCompound();
            NBTTagCompound linkPanelCompound = new NBTTagCompound();

            NBTTagList list = new NBTTagList();

            for (int j = 0; j < keys.length; j++) {
                if (((i >> j) & 0x1) == 1) {
                    list.appendTag(new NBTTagString((String) keys[j]));
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
            API.addItemListEntry(stack);
        }
    }

    private static void addLinkingbooks() throws Throwable {
        for (ItemStack panel : allLinkpanels) {
            ItemStack book = new ItemStack(MystObjs.linkbook_unlinked);

            book.stackTagCompound = (NBTTagCompound) panel.stackTagCompound.copy();

            API.addItemListEntry(book);
        }
    }

    /**
     * Add item ranges to the NEI interface
     * 
     * @throws Throwable
     */
    private static void addItemRanges() throws Throwable {
        // FIXME
        //        MultiItemRange mystBlocks = new MultiItemRange();
        //
        //        mystBlocks.add(MystObjects.portal);
        //        mystBlocks.add(MystObjects.crystal);
        //        mystBlocks.add(MystObjects.crystal_receptacle);
        //        mystBlocks.add(MystObjects.decay);
        //        mystBlocks.add(MystObjects.bookstand);
        //        mystBlocks.add(MystObjects.book_lectern);
        //        mystBlocks.add(MystObjects.writing_desk_block);
        //        mystBlocks.add(MystObjects.bookbinder);
        //        mystBlocks.add(MystObjects.inkmixer);
        //        mystBlocks.add(MystObjects.star_fissure);
        //
        //        mystBlocks.add(MystObjects.link_modifer);
        //
        //        API.addSetRange("Mystcraft.Blocks", mystBlocks);
        //
        //        MultiItemRange mystItems = new MultiItemRange();
        //
        //        mystItems.add(MystObjects.writing_desk);
        //        mystItems.add(MystObjects.linkbook);
        //        mystItems.add(MystObjects.inkvial);
        //
        //        if (PluginNEIMystcraft.addSymbolPages.getValue() || PluginNEIMystcraft.addLinkPanels.getValue()) {
        //            MultiItemRange mystPages = new MultiItemRange();
        //            mystPages.add(MystObjects.page);
        //
        //            API.addSetRange("Mystcraft.Items.Pages", mystPages);
        //        }
        //        else {
        //            mystItems.add(MystObjects.page);
        //        }
        //
        //        if (PluginNEIMystcraft.addCreativeNotebooks.getValue()) {
        //            MultiItemRange mystNotebooks = new MultiItemRange();
        //            mystNotebooks.add(MystObjects.notebook);
        //
        //            API.addSetRange("Mystcraft.Items.Notebooks", mystNotebooks);
        //        }
        //        else {
        //            mystItems.add(MystObjects.notebook);
        //        }
        //
        //        if (PluginNEIMystcraft.addLinkingBooks.getValue()) {
        //            MultiItemRange mystLinkbooks = new MultiItemRange();
        //            mystLinkbooks.add(MystObjects.linkbook_unlinked);
        //
        //            API.addSetRange("Mystcraft.Items.Linking Books", mystLinkbooks);
        //        }
        //        else {
        //            mystItems.add(MystObjects.linkbook_unlinked);
        //        }
        //
        //        if (PluginNEIMystcraft.addAgeList.getValue()) {
        //            MultiItemRange mystDescriptiveBooks = new MultiItemRange();
        //            mystDescriptiveBooks.add(MystObjects.descriptive_book);
        //
        //            API.addSetRange("Mystcraft.Items.Descriptive Books", mystDescriptiveBooks);
        //        }
        //        else {
        //            mystItems.add(MystObjects.linkbook_unlinked);
        //        }
        //
        //        API.addSetRange("Mystcraft.Items", mystItems);
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

            Map<String, Float> properties = InternalAPI.linkProperties.getPropertiesForItem(stack);

            if (properties == null) {
                return null;
            }

            ColorGradient gradient = InternalAPI.linkProperties.getPropertiesGradient(properties);

            String[] modifiers = properties.keySet().toArray(new String[properties.size()]);

            return new InkMixerRecipe(gradient, modifiers);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        catch (Throwable e) {
            Objects.log.log(Level.WARN, "Failed getting gradient", e);
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

        InternalAPI.render.drawSymbol(x, y + (height + 1.0F - width) / 2.0F, z, width - 1.0F, symbol);
    }

    private static void addFakeRecipes() {
        ICraftingResultHandler handler = new ICraftingResultHandler() {
            @Override
            public ItemStack getOutput(ISpACoreRecipe recipe, List<ItemStack> input) {
                ItemStack result = recipe.getOutput();

                for (ItemStack stack : input) {
                    if (stack != null && stack.getItem() == MystObjs.page) {
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
                if (input != null && input.getItem() == MystObjs.page) {
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

        ShapelessSpACoreRecipe recipe = new ShapelessSpACoreRecipe(handler, new ItemStack(MystObjs.linkbook_unlinked), stacks, new ItemStack(Items.leather));

        GameRegistry.addRecipe(recipe);
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
