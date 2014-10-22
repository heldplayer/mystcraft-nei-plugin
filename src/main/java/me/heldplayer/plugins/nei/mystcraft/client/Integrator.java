package me.heldplayer.plugins.nei.mystcraft.client;

import codechicken.lib.gui.GuiDraw;
import com.xcompwiz.mystcraft.core.InternalAPI;
import com.xcompwiz.mystcraft.symbol.ColorGradient;
import com.xcompwiz.mystcraft.symbol.IAgeSymbol;
import me.heldplayer.plugins.nei.mystcraft.Assets;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.modules.*;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.WorldProvider;
import net.specialattack.forge.core.client.GuiHelper;
import net.specialattack.forge.core.config.ConfigValue;
import net.specialattack.forge.core.reflection.RClass;
import net.specialattack.forge.core.reflection.RField;
import net.specialattack.forge.core.reflection.ReflectionHelper;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Class used for integrating into Mystcraft
 *
 * @author heldplayer
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class Integrator {

    public static List<ItemStack> allAges = new ArrayList<ItemStack>();
    public static Class<? extends GuiContainer> guiInkMixerClass;
    public static Class<? extends GuiContainer> guiWritingDeskClass;

    private static List<ItemStack> allLinkpanels;
    private static RField<Object, Object> agedataField;
    private static RField<Object, List<String>> symbolsField;
    private static RField<Object, List<ItemStack>> pagesField;
    private static Class worldProviderMystClass;
    private static Map itemstack_bindings;
    private static Map oredict_bindings;
    private static Map itemId_bindings;
    private static boolean initialized = false;

    private static Set<IModule> modules = new HashSet<IModule>();

    static {
        modules.add(new ModuleTechnicalBlocks());
        modules.add(new ModuleDecayBlocks());
        modules.add(new ModuleCreativeNotebooks());
        modules.add(new ModulePages());
        modules.add(new ModuleLinkingBooks());
        modules.add(new ModuleDescriptiveBooks());
        modules.add(new ModuleItemSubsets());
        modules.add(new ModuleTooltips());
        modules.add(new ModuleRecipes());
    }

    private Integrator() {
    }

    public static Collection<ConfigValue<?>> getAllConfigValues() {
        HashSet<ConfigValue<?>> result = new HashSet<ConfigValue<?>>();

        for (IModule module : modules) {
            Collections.addAll(result, module.getConfigEntries());
        }

        return result;
    }

    public static void reinitialize() {
        if (!initialized) {
            return;
        }

        for (IModule module : modules) {
            try {
                Objects.log.log(Level.DEBUG, "Disabling module " + module.getClass().getName());

                module.disable();
            } catch (Exception e) {
                Objects.log.log(Level.ERROR, "Failed disabling module " + module.getClass().getName(), e);
            }
        }

        initialized = false;

        try {
            Objects.log.log(Level.DEBUG, "Getting all link panels");
            prepareLinkPanels();
        } catch (Exception ex) {
            Objects.log.log(Level.ERROR, "Failed getting all link panels", ex);
        }

        for (IModule module : modules) {
            try {
                Objects.log.log(Level.DEBUG, "Enabling module " + module.getClass().getName());

                module.enable();
            } catch (Exception e) {
                Objects.log.log(Level.ERROR, "Failed enabling module " + module.getClass().getName(), e);
            }
        }

        initialized = true;
    }

    private static void prepareLinkPanels() {
        RClass<Object> inkEffectsClass = (RClass<Object>) ReflectionHelper.getClass("com.xcompwiz.mystcraft.data.InkEffects");
        RField<Object, HashMap> colormapField = inkEffectsClass.getField("colormap");

        // Add all modifiers known to have a colour, this includes mod added modifiers
        HashMap colormap = colormapField.getStatic();

        TreeMap map = new TreeMap(new LinkPanelSorter());
        map.putAll(colormap);
        if (!map.containsKey("Following")) {
            map.put("Following", null);
        }

        Object[] keys = map.keySet().toArray(new Object[map.size()]);

        int bin = binary(keys.length);

        allLinkpanels = new ArrayList<ItemStack>();

        for (int i = 0; i <= bin; i++) {
            ItemStack is = new ItemStack(MystObjs.page.getItem(), 1, 0);

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

    private static int binary(int bits) {
        int result = 0;
        for (int i = 0; i < bits; i++) {
            result |= 1 << i;
        }
        return result;
    }

    /**
     * Initialize all NEI features for Mystcraft
     */
    public static void initialize() {
        if (initialized) {
            return;
        }

        Objects.log.log(Level.DEBUG, "Initializing Mystcraft Integrator");

        try {
            Objects.log.log(Level.DEBUG, "Getting all link panels");
            prepareLinkPanels();
        } catch (Exception ex) {
            Objects.log.log(Level.ERROR, "Failed getting all link panels", ex);
        }

        try {
            Objects.log.log(Level.DEBUG, "Getting methods and fields");
            getMethodsAndFields();
        } catch (Exception ex) {
            Objects.log.log(Level.ERROR, "Failed getting methods and fields", ex);
        }

        try {
            Objects.log.log(Level.DEBUG, "Getting GUI classes");
            guiInkMixerClass = (Class<? extends GuiContainer>) Class.forName("com.xcompwiz.mystcraft.client.gui.GuiInkMixer");
            guiWritingDeskClass = (Class<? extends GuiContainer>) Class.forName("com.xcompwiz.mystcraft.client.gui.GuiWritingDesk");
        } catch (Exception ex) {
            Objects.log.log(Level.ERROR, "Failed getting GUI classes", ex);
        }

        for (IModule module : modules) {
            try {
                Objects.log.log(Level.DEBUG, "Enabling module " + module.getClass().getName());

                module.enable();
            } catch (Exception e) {
                Objects.log.log(Level.ERROR, "Failed enabling module " + module.getClass().getName(), e);
            }
        }

        initialized = true;
    }

    /**
     * Gets all methods and fields required by recipe handlers and such to
     * function
     */
    private static void getMethodsAndFields() throws ClassNotFoundException {
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

    public static List<ItemStack> getAllLinkpanels() {
        return allLinkpanels;
    }

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
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        } catch (Throwable e) {
            Objects.log.log(Level.WARN, "Failed getting gradient", e);
            return null;
        }
    }

    public static ArrayList getALlInkMixerRecipes() {
        ArrayList result = new ArrayList();

        result.addAll(itemstack_bindings.keySet());
        result.addAll(oredict_bindings.keySet());
        result.addAll(itemId_bindings.keySet());

        return result;
    }

    public static void renderPage(IAgeSymbol symbol, float x, float y, float z, float width, float height) {
        GuiDraw.changeTexture(Assets.bookPageLeft);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiHelper.drawTexturedModalRect((int) x, (int) y, (int) width, (int) height, z, 0.609375F, 0.0F, 0.7294117647058824F, 0.15625F);

        InternalAPI.render.drawSymbol(x, y + (height + 1.0F - width) / 2.0F, z, width - 1.0F, symbol);
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
