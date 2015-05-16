package me.heldplayer.plugins.nei.mystcraft.wrap;

import com.xcompwiz.mystcraft.api.MystObjects;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

@GameRegistry.ObjectHolder(MystObjects.MystcraftModId)
public class MystObjs {

    public static CreativeTabs creativeTab = null;
    public static CreativeTabs pageTab = null;
    public static String treasure_info = null;
    public static AchievementPage achivements;

    public static List<ItemStack> creative_notebooks = null;

    // Items
    @GameRegistry.ObjectHolder(MystObjects.Items.writing_desk)
    public static final Item writingDesk = null;

    @GameRegistry.ObjectHolder(MystObjects.Items.page)
    public static final Item page = null;

    @GameRegistry.ObjectHolder(MystObjects.Items.descriptive_book)
    public static final Item descriptiveBook = null;

    @GameRegistry.ObjectHolder(MystObjects.Items.linkbook_unlinked)
    public static final Item linkingBookUnlinked = null;

    @GameRegistry.ObjectHolder(MystObjects.Items.linkbook)
    public static final Item linkingBook = null;

    @GameRegistry.ObjectHolder(MystObjects.Items.folder)
    public static final Item folder = null;

    @GameRegistry.ObjectHolder(MystObjects.Items.portfolio)
    public static final Item portfolio = null;

    @GameRegistry.ObjectHolder(MystObjects.Items.booster)
    public static final Item booster = null;

    @GameRegistry.ObjectHolder(MystObjects.Items.inkvial)
    public static final Item inkVial = null;

    // Blocks
    @GameRegistry.ObjectHolder(MystObjects.Blocks.portal)
    public static final Block portal = null;

    @GameRegistry.ObjectHolder(MystObjects.Blocks.crystal)
    public static final Block crystal = null;

    @GameRegistry.ObjectHolder(MystObjects.Blocks.crystal_receptacle)
    public static final Block crystalReceptacle = null;

    @GameRegistry.ObjectHolder(MystObjects.Blocks.decay)
    public static final Block decay = null;

    @GameRegistry.ObjectHolder(MystObjects.Blocks.bookstand)
    public static final Block bookstand = null;

    @GameRegistry.ObjectHolder(MystObjects.Blocks.book_lectern)
    public static final Block lectern = null;

    @GameRegistry.ObjectHolder(MystObjects.Blocks.writing_desk_block)
    public static final Block writingDeskBlock = null;

    @GameRegistry.ObjectHolder(MystObjects.Blocks.bookbinder)
    public static final Block bookBinder = null;

    @GameRegistry.ObjectHolder(MystObjects.Blocks.inkmixer)
    public static final Block inkMixer = null;

    @GameRegistry.ObjectHolder(MystObjects.Blocks.star_fissure)
    public static final Block starFissure = null;

    @GameRegistry.ObjectHolder(MystObjects.Blocks.link_modifer)
    public static final Block linkModifier = null;

    @GameRegistry.ObjectHolder("BlockFluidMyst")
    public static final Block blackInkBlock = null;

    // Fluids
    public static Fluid black_ink = null;

    public static void initialize() {
        black_ink = FluidRegistry.getFluid(MystObjects.Fluids.black_ink);

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
                    if (stack.getItem() == MystObjs.portfolio) {
                        creative_notebooks.add(stack);
                    }
                }
            }
        }
    }

}
