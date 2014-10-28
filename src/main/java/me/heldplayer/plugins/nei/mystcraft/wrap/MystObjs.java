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

    public static ObjectReference portal = new ObjectReference(MystObjects.block_portal, true);
    public static ObjectReference crystal = new ObjectReference(MystObjects.block_crystal, true);
    public static ObjectReference crystal_receptacle = new ObjectReference(MystObjects.block_crystal_receptacle, true);
    public static ObjectReference decay = new ObjectReference(MystObjects.block_decay, true);
    public static ObjectReference bookstand = new ObjectReference(MystObjects.block_bookstand, true);
    public static ObjectReference book_lectern = new ObjectReference(MystObjects.block_book_lectern, true);
    public static ObjectReference writing_desk_block = new ObjectReference(MystObjects.block_writing_desk_block, true);
    public static ObjectReference bookbinder = new ObjectReference(MystObjects.block_bookbinder, true);
    public static ObjectReference inkmixer = new ObjectReference(MystObjects.block_inkmixer, true);
    public static ObjectReference star_fissure = new ObjectReference(MystObjects.block_star_fissure, true);
    public static ObjectReference link_modifer = new ObjectReference(MystObjects.block_link_modifer, true);

    public static ObjectReference writing_desk = new ObjectReference(MystObjects.item_writing_desk, false);
    public static ObjectReference page = new ObjectReference(MystObjects.item_page, false);
    public static ObjectReference descriptive_book = new ObjectReference(MystObjects.item_descriptive_book, false);
    public static ObjectReference linkbook_unlinked = new ObjectReference(MystObjects.item_linkbook_unlinked, false);
    public static ObjectReference linkbook = new ObjectReference(MystObjects.item_linkbook, false);
    public static ObjectReference notebook = new ObjectReference(MystObjects.item_notebook, false);
    public static ObjectReference inkvial = new ObjectReference(MystObjects.item_inkvial, false);

    public static Fluid black_ink = null;

    public static void initialize() {
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
