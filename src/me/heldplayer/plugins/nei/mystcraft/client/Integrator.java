
package me.heldplayer.plugins.nei.mystcraft.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;

import me.heldplayer.plugins.nei.mystcraft.Assets;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.util.HeldCore.client.GuiHelper;
import me.heldplayer.util.HeldCore.reflection.RClass;
import me.heldplayer.util.HeldCore.reflection.RField;
import me.heldplayer.util.HeldCore.reflection.RMethod;
import me.heldplayer.util.HeldCore.reflection.ReflectionHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import org.lwjgl.opengl.GL11;

import codechicken.core.gui.GuiDraw;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;

import com.xcompwiz.mystcraft.api.MystAPI;
import com.xcompwiz.mystcraft.api.MystObjects;
import com.xcompwiz.mystcraft.api.internals.Color;
import com.xcompwiz.mystcraft.api.internals.ColorGradient;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;

/**
 * Class used for integrating into Mystcraft
 * 
 * @author heldplayer
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Integrator {

    // TODO: Linking book crafting

    private static RMethod<Object, Map<String, Float>> getItemEffectsMethod;
    private static RMethod<Object, Color> getColorForPropertyMethod;
    private static Map itemstack_bindings;
    private static Map oredict_bindings;
    private static Map itemId_bindings;
    private static List<ItemStack> allLinkpanels;

    private static Color defaultColor = new Color(1.0F, 1.0F, 1.0F);
    private static Color emptyColor = new Color(0.0F, 0.0F, 0.0F);

    /**
     * Initialize all NEI features for Mystcraft
     * 
     * @param mystcraft
     *        An instance of Mystcraft
     */
    public static void initialize(Object mystcraft) {
        if (mystcraft == null) {
            Objects.log.log(Level.SEVERE, "Mystcraft is not installed or not found! This mod requires mystcraft to function!");
            return;
        }

        try {
            hideTechnicalBlocks();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed hiding technical blocks from NEI", ex);
        }
        try {
            addDecayTypes();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed adding different kinds of decay to NEI", ex);
        }
        try {
            addCreativeNotebooks();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed adding a creative notebook to NEI", ex);
        }
        try {
            addPages();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed adding symbol pages to NEI", ex);
        }
        try {
            addLinkPanels();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed adding link panels to NEI", ex);
        }
        try {
            addLinkingbooks();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed adding linking books to NEI", ex);
        }
        try {
            addItemRanges();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed adding item ranges to NEI", ex);
        }
        try {
            getMethodsAndFields();
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed getting methods and fields", ex);
        }
        try {
            NEIConfig.guiInkMixerClass = (Class<? extends GuiContainer>) Class.forName("com.xcompwiz.mystcraft.client.gui.GuiInkMixer");
            NEIConfig.guiWritingDeskClass = (Class<? extends GuiContainer>) Class.forName("com.xcompwiz.mystcraft.client.gui.GuiWritingDesk");
            NEIConfig.recipeLinkingbookClass = (Class<? extends IRecipe>) Class.forName("com.xcompwiz.mystcraft.data.RecipeLinkingbook");
        }
        catch (Throwable ex) {
            Objects.log.log(Level.SEVERE, "Failed getting GUI classes", ex);
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

    /**
     * Add all pages to NEI
     * 
     * @throws Throwable
     */
    private static void addPages() throws Throwable {
        ItemStack page = new ItemStack(MystObjects.page, 1, 0);

        // Add a standard, empty page first!
        API.addNBTItem(page);

        // Add all the pages for all the symbols
        List<IAgeSymbol> allSymbols = MystAPI.symbol.getAllRegisteredSymbols();

        for (IAgeSymbol symbol : allSymbols) {
            API.addNBTItem(MystAPI.itemFact.buildSymbolPage(symbol.identifier()));
        }
    }

    /**
     * Add all link panels to NEI
     * 
     * @throws Throwable
     */
    private static void addLinkPanels() throws Throwable {
        RClass<Object> inkEffectsClass = (RClass<Object>) ReflectionHelper.getClass("com.xcompwiz.mystcraft.data.InkEffects");
        RField<Object, HashMap> colormapField = inkEffectsClass.getField("colormap");

        // Empty pages don't get added again, as this is already done in addPages()

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

            API.addNBTItem(is);

            allLinkpanels.add(is);
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

        MultiItemRange mystItems = new MultiItemRange();

        mystItems.add(MystObjects.writing_desk);
        mystItems.add(MystObjects.descriptive_book);
        mystItems.add(MystObjects.linkbook);
        mystItems.add(MystObjects.inkvial);

        MultiItemRange mystPages = new MultiItemRange();
        mystPages.add(MystObjects.page);

        MultiItemRange mystNotebooks = new MultiItemRange();
        mystNotebooks.add(MystObjects.notebook);

        MultiItemRange mystLinkbooks = new MultiItemRange();
        mystLinkbooks.add(MystObjects.linkbook_unlinked);

        API.addSetRange("Mystcraft.Blocks", mystBlocks);
        API.addSetRange("Mystcraft.Items", mystItems);
        API.addSetRange("Mystcraft.Items.Pages", mystPages);
        API.addSetRange("Mystcraft.Items.Notebooks", mystNotebooks);
        API.addSetRange("Mystcraft.Items.Linking Books", mystLinkbooks);
    }

    /**
     * Gets all methods and fields required by recipe handlers and such to
     * function
     * 
     * @throws Throwable
     */
    private static void getMethodsAndFields() throws Throwable {
        RClass<Object> inkEffectsClass = (RClass<Object>) ReflectionHelper.getClass("com.xcompwiz.mystcraft.data.InkEffects");

        getItemEffectsMethod = inkEffectsClass.getMethod("getItemEffects", ItemStack.class);
        getColorForPropertyMethod = inkEffectsClass.getMethod("getColorForProperty", String.class);

        RField<Object, Map> bindings = inkEffectsClass.getField("itemstack_bindings");
        itemstack_bindings = bindings.getStatic();

        bindings = inkEffectsClass.getField("oredict_bindings");
        oredict_bindings = bindings.getStatic();

        bindings = inkEffectsClass.getField("itemId_bindings");
        itemId_bindings = bindings.getStatic();
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
        if (stack == null) {
            return null;
        }

        if (getItemEffectsMethod == null) {
            return null;
        }

        Map<String, Float> properties = null;

        try {
            properties = (Map<String, Float>) getItemEffectsMethod.callStatic(stack);

            ColorGradient gradient = new ColorGradient();

            long max = 300L;
            int total = 0;

            if (properties == null) {
                return null;
            }

            for (Entry<String, Float> entry : properties.entrySet()) {
                Color color = (Color) getColorForPropertyMethod.callStatic(entry.getKey());
                if (entry.getKey().isEmpty()) {
                    color = emptyColor;
                }
                if (entry.getValue().floatValue() >= 0.01F) {
                    if (color == null) {
                        color = defaultColor;
                    }
                    long interval = (long) (entry.getValue().floatValue() * (float) max);
                    total = (int) (total + interval);
                    if (interval > 100L) {
                        gradient.pushColor(color, Long.valueOf(interval - 100L));
                        interval = 100L;
                    }
                    gradient.pushColor(color, Long.valueOf(interval));
                }
            }
            if (total < max - 1L) {
                long interval = max - total;
                if (interval > 100L) {
                    gradient.pushColor(emptyColor, Long.valueOf(interval - 100L));
                    interval = 100L;
                }
                gradient.pushColor(emptyColor, Long.valueOf(interval));
            }

            String[] modifiers = properties.keySet().toArray(new String[properties.size()]);

            return new InkMixerRecipe(gradient, modifiers);
        }
        catch (Exception e) {
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

}
