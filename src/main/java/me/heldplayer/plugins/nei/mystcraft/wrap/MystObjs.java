package me.heldplayer.plugins.nei.mystcraft.wrap;

import com.xcompwiz.mystcraft.api.MystObjects;
import com.xcompwiz.mystcraft.core.InternalAPI;
import com.xcompwiz.mystcraft.symbol.IAgeSymbol;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class MystObjs {

    public static CreativeTabs creativeTab = null;
    public static CreativeTabs pageTab = null;
    public static String treasure_info = null;
    public static AchievementPage achivements;

    public static List<ItemStack> creative_notebooks = null;

    public static ObjectReference portal = null;
    public static ObjectReference crystal = null;
    public static ObjectReference crystal_receptacle = null;
    public static ObjectReference decay = null;
    public static ObjectReference bookstand = null;
    public static ObjectReference book_lectern = null;
    public static ObjectReference writing_desk_block = null;
    public static ObjectReference bookbinder = null;
    public static ObjectReference inkmixer = null;
    public static ObjectReference star_fissure = null;
    public static ObjectReference link_modifer = null;

    public static ObjectReference writing_desk = null;
    public static ObjectReference page = null;
    public static ObjectReference descriptive_book = null;
    public static ObjectReference linkbook_unlinked = null;
    public static ObjectReference linkbook = null;
    public static ObjectReference notebook = null;
    public static ObjectReference inkvial = null;

    public static Fluid black_ink = null;

    public static void initialize() {
        portal = new ObjectReference(MystObjects.block_portal, true);
        crystal = new ObjectReference(MystObjects.block_crystal, true);
        crystal_receptacle = new ObjectReference(MystObjects.block_crystal_receptacle, true);
        decay = new ObjectReference(MystObjects.block_decay, true);
        bookstand = new ObjectReference(MystObjects.block_bookstand, true);
        book_lectern = new ObjectReference(MystObjects.block_book_lectern, true);
        writing_desk_block = new ObjectReference(MystObjects.block_writing_desk_block, true);
        bookbinder = new ObjectReference(MystObjects.block_bookbinder, true);
        inkmixer = new ObjectReference(MystObjects.block_inkmixer, true);
        star_fissure = new ObjectReference(MystObjects.block_star_fissure, true);
        link_modifer = new ObjectReference(MystObjects.block_link_modifer, true);

        writing_desk = new ObjectReference(MystObjects.item_writing_desk, false);
        page = new ObjectReference(MystObjects.item_page, false);
        descriptive_book = new ObjectReference(MystObjects.item_descriptive_book, false);
        linkbook_unlinked = new ObjectReference(MystObjects.item_linkbook_unlinked, false);
        linkbook = new ObjectReference(MystObjects.item_linkbook, false);
        notebook = new ObjectReference(MystObjects.item_notebook, false);
        inkvial = new ObjectReference(MystObjects.item_inkvial, false);

        black_ink = FluidRegistry.getFluid(MystObjects.fluid_black_ink);

        creative_notebooks = new ArrayList<ItemStack>();

        for (CreativeTabs tab : CreativeTabs.creativeTabArray) {
            if (tab == null) {
                continue;
            }
            if (tab.getTabLabel().equals("mystcraft.common")) {
                List<ItemStack> items = new ArrayList<ItemStack>();
                tab.displayAllReleventItems(items);
                for (ItemStack stack : items) {
                    if (stack == null) {
                        continue;
                    }
                    if (stack.getItem() == MystObjs.notebook.getItem()) {
                        creative_notebooks.add(stack);
                    }
                }
            }
        }
    }

    public static List<IAgeSymbol> getAllRegisteredSymbols() {
        return InternalAPI.symbol.getAllRegisteredSymbols();
    }

    public static class ObjectReference {

        public final String name;
        public final boolean isBlock;

        private ObjectReference(String name, boolean isBlock) {
            this.name = name;
            this.isBlock = isBlock;
        }

        public Item getItem() {
            return (Item) Item.itemRegistry.getObject(MystObjects.MystcraftModId + ":" + name);
        }

        public Block getBlock() {
            return Block.getBlockFromName(MystObjects.MystcraftModId + ":" + name);
        }

    }

}
