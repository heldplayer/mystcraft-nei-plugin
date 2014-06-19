
package me.heldplayer.plugins.nei.mystcraft.wrap;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.xcompwiz.mystcraft.api.MystObjects;
import com.xcompwiz.mystcraft.core.InternalAPI;
import com.xcompwiz.mystcraft.symbol.IAgeSymbol;

@Deprecated
public class MystObjs {

    public static CreativeTabs creativeTab = null;
    public static CreativeTabs pageTab = null;
    public static String treasure_info = null;
    public static AchievementPage achivements;

    public static List<ItemStack> creative_notebooks = null;

    public static Block portal = null;
    public static Block crystal = null;
    public static Block crystal_receptacle = null;
    public static Block decay = null;
    public static Block bookstand = null;
    public static Block book_lectern = null;
    public static Block writing_desk_block = null;
    public static Block bookbinder = null;
    public static Block inkmixer = null;
    public static Block star_fissure = null;
    public static Block link_modifer = null;

    public static Item writing_desk = null;
    public static Item page = null;
    public static Item descriptive_book = null;
    public static Item linkbook_unlinked = null;
    public static Item linkbook = null;
    public static Item notebook = null;
    public static Item inkvial = null;

    public static Fluid black_ink = null;

    public static void initialize() {
        portal = getBlock(MystObjects.block_portal);
        crystal = getBlock(MystObjects.block_crystal);
        crystal_receptacle = getBlock(MystObjects.block_crystal_receptacle);
        decay = getBlock(MystObjects.block_decay);
        bookstand = getBlock(MystObjects.block_bookstand);
        book_lectern = getBlock(MystObjects.block_book_lectern);
        writing_desk_block = getBlock(MystObjects.block_writing_desk_block);
        bookbinder = getBlock(MystObjects.block_bookbinder);
        inkmixer = getBlock(MystObjects.block_inkmixer);
        star_fissure = getBlock(MystObjects.block_star_fissure);
        link_modifer = getBlock(MystObjects.block_link_modifer);

        writing_desk = getItem(MystObjects.item_writing_desk);
        page = getItem(MystObjects.item_page);
        descriptive_book = getItem(MystObjects.item_descriptive_book);
        linkbook_unlinked = getItem(MystObjects.item_linkbook_unlinked);
        linkbook = getItem(MystObjects.item_linkbook);
        notebook = getItem(MystObjects.item_notebook);
        inkvial = getItem(MystObjects.item_inkvial);

        black_ink = FluidRegistry.getFluid(MystObjects.fluid_black_ink);

        creative_notebooks = new ArrayList<ItemStack>();

        for (CreativeTabs tab : CreativeTabs.creativeTabArray) {
            if (tab == null)
                continue;
            if (tab.getTabLabel().equals("mystcraft.common")) {
                List<ItemStack> items = new ArrayList<ItemStack>();
                tab.displayAllReleventItems(items);
                for (ItemStack stack : items) {
                    if (stack == null)
                        continue;
                    if (stack.getItem() == MystObjs.notebook) {
                        creative_notebooks.add(stack);
                    }
                }
            }
        }
    }

    private static Item getItem(String name) {
        return (Item) Item.itemRegistry.getObject("Mystcraft:" + name);
    }

    private static Block getBlock(String name) {
        return Block.getBlockFromName("Mystcraft:" + name);
    }

    public static List<IAgeSymbol> getAllRegisteredSymbols() {
        return InternalAPI.symbol.getAllRegisteredSymbols();
    }

}
