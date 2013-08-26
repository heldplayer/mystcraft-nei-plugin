
package me.heldplayer.plugins.nei.mystcraft.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;

import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.util.HeldCore.reflection.RClass;
import me.heldplayer.util.HeldCore.reflection.RField;
import me.heldplayer.util.HeldCore.reflection.RMethod;
import me.heldplayer.util.HeldCore.reflection.ReflectionHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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

    private static RMethod<Object, Map<String, Float>> getItemEffectsMethod;
    private static RMethod<Object, Color> getColorForPropertyMethod;
    private static Map itemstack_bindings;
    private static Map oredict_bindings;
    private static Map itemId_bindings;

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

        MystAPI.linkProperties.addPropertyToItem("stairWood", "Following", 0.7F);

        try {
            hideTechnicalBlocks();
        }
        catch (Exception ex) {
            Objects.log.log(Level.SEVERE, "Failed hiding technical blocks from NEI", ex);
        }
        catch (Error ex) {
            Objects.log.log(Level.SEVERE, "Failed hiding technical blocks from NEI, you don't need to report this as I'll probably already know about this", ex);
        }
        try {
            addDecayTypes();
        }
        catch (Exception ex) {
            Objects.log.log(Level.SEVERE, "Failed adding different kinds of decay to NEI", ex);
        }
        catch (Error ex) {
            Objects.log.log(Level.SEVERE, "Failed adding different kinds of decay to NEI, you don't need to report this as I'll probably already know about this", ex);
        }
        try {
            addCreativeNotebook();
        }
        catch (Exception ex) {
            Objects.log.log(Level.SEVERE, "Failed adding a creative notebook to NEI", ex);
        }
        catch (Error ex) {
            Objects.log.log(Level.SEVERE, "Failed adding a creative notebook to NEI, you don't need to report this as I'll probably already know about this", ex);
        }
        try {
            addPages();
        }
        catch (Exception ex) {
            Objects.log.log(Level.SEVERE, "Failed adding symbol pages to NEI", ex);
        }
        catch (Error ex) {
            Objects.log.log(Level.SEVERE, "Failed adding symbol pages to NEI, you don't need to report this as I'll probably already know about this", ex);
        }
        try {
            addLinkPanels();
        }
        catch (Exception ex) {
            Objects.log.log(Level.SEVERE, "Failed adding link panels to NEI", ex);
        }
        catch (Error ex) {
            Objects.log.log(Level.SEVERE, "Failed adding link panels to NEI, you don't need to report this as I'll probably already know about this", ex);
        }
        try {
            addItemRanges();
        }
        catch (Exception ex) {
            Objects.log.log(Level.SEVERE, "Failed adding item ranges to NEI", ex);
        }
        catch (Error ex) {
            Objects.log.log(Level.SEVERE, "Failed adding item ranges to NEI, you don't need to report this as I'll probably already know about this", ex);
        }
        try {
            getMethodsAndFields();
        }
        catch (Exception ex) {
            Objects.log.log(Level.SEVERE, "Failed getting methods and fields", ex);
        }
        catch (Error ex) {
            Objects.log.log(Level.SEVERE, "Failed getting methods and fields, you don't need to report this as I'll probably already know about this", ex);
        }
        try {
            PluginNEIMystcraft.guiInkMixerClass = (Class<? extends GuiContainer>) Class.forName("com.xcompwiz.mystcraft.client.gui.GuiInkMixer");
        }
        catch (Exception ex) {
            Objects.log.log(Level.SEVERE, "Failed getting GUI classes", ex);
        }
        catch (Error ex) {
            Objects.log.log(Level.SEVERE, "Failed getting GUI classes, you don't need to report this as I'll probably already know about this", ex);
        }
    }

    /**
     * Hide technical blocks and items from the NEI item list
     * 
     * @throws Exception
     * @throws Error
     */
    private static void hideTechnicalBlocks() throws Exception, Error {
        API.hideItem(MystObjects.writing_desk_block.blockID);
        API.hideItem(MystObjects.portal.blockID);
        API.hideItem(MystObjects.star_fissure.blockID);
    }

    /**
     * Add all decay types that are standard Mystcraft to the NEI item list
     * 
     * @throws Exception
     * @throws Error
     */
    private static void addDecayTypes() throws Exception, Error {
        ArrayList<Integer> damageVariants = new ArrayList<Integer>();
        damageVariants.add(0);
        damageVariants.add(1);

        damageVariants.add(3);
        damageVariants.add(4);

        damageVariants.add(6);

        API.setItemDamageVariants(MystObjects.decay.blockID, damageVariants);
    }

    /**
     * Add a creative notebook to NEI
     * 
     * @throws Exception
     * @throws Error
     */
    private static void addCreativeNotebook() throws Exception, Error {
        ItemStack notebook = new ItemStack(MystObjects.notebook, 1, 0);

        // Add a standard notebook, or NEI will use the creative one
        API.addNBTItem(notebook);

        for (ItemStack stack : MystObjects.creative_notebooks) {
            API.addNBTItem(stack);
        }
    }

    /**
     * Add all pages to NEI
     * 
     * @throws Exception
     * @throws Error
     */
    private static void addPages() throws Exception, Error {
        RClass<Object> symbolManagerClass = (RClass<Object>) ReflectionHelper.getClass("com.xcompwiz.mystcraft.symbols.SymbolManager");
        RMethod<Object, ArrayList<IAgeSymbol>> getAgeSymbols = symbolManagerClass.getMethod("getAgeSymbols");

        ItemStack page = new ItemStack(MystObjects.page, 1, 0);

        // Add a standard, empty page first!
        API.addNBTItem(page);

        // Add all the pages for all the symbols
        ArrayList<IAgeSymbol> symbols = getAgeSymbols.callStatic();

        Collections.sort(symbols, new SymbolSorter());

        for (IAgeSymbol symbol : symbols) {
            ItemStack is = new ItemStack(MystObjects.page, 1, 0);

            NBTTagCompound compound = new NBTTagCompound("tag");
            compound.setString("symbol", symbol.identifier());

            is.setTagCompound(compound);

            API.addNBTItem(is);
        }
    }

    /**
     * Add all link panels to NEI
     * 
     * @throws Exception
     * @throws Error
     */
    private static void addLinkPanels() throws Exception, Error {
        RClass<Object> inkEffectsClass = (RClass<Object>) ReflectionHelper.getClass("com.xcompwiz.mystcraft.data.InkEffects");
        //Class inkEffectsClass = Class.forName("com.xcompwiz.mystcraft.data.InkEffects");
        RField<Object, HashMap> colormapField = inkEffectsClass.getField("colormap");
        //Field colormapField = inkEffectsClass.getDeclaredField("colormap");

        // Empty pages don't get added again, as this is already done in addPages()

        // Add all modifiers known to have a colour, this includes mod added modifiers
        HashMap colormap = colormapField.getStatic();

        TreeMap map = new TreeMap(new LinkPanelSorter());
        map.putAll(colormap);

        Object[] keys = map.keySet().toArray(new Object[0]);

        int bin = binary(keys.length);

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
        }
    }

    /**
     * Add item ranges to the NEI interface
     * 
     * @throws Exception
     * @throws Error
     */
    private static void addItemRanges() throws Exception, Error {
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
        mystItems.add(MystObjects.linkbook_unlinked);
        mystItems.add(MystObjects.linkbook);
        mystItems.add(MystObjects.notebook);
        mystItems.add(MystObjects.inkvial);

        MultiItemRange mystPages = new MultiItemRange();
        mystPages.add(MystObjects.page);

        API.addSetRange("Mystcraft.Blocks", mystBlocks);
        API.addSetRange("Mystcraft.Items", mystItems);
        API.addSetRange("Mystcraft.Pages", mystPages);
    }

    /**
     * Gets all methods and fields required by recipe handlers and such to
     * function
     * 
     * @throws Exception
     * @throws Error
     */
    private static void getMethodsAndFields() throws Exception, Error {
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

            String[] modifiers = properties.keySet().toArray(new String[0]);
            Float[] percentages = properties.values().toArray(new Float[0]);

            return new InkMixerRecipe(gradient, modifiers, percentages);
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

}
