
package com.xcompwiz.mystcraft.api;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fluids.Fluid;

/**
 * Provides various literals, items, blocks, etc of Mystcraft for external use
 * These are not guaranteed to be set. Be sure to check for nulls.
 * These are set during Mystcraft's pre-init phase
 * 
 * @author XCompWiz
 * 
 */
public class MystObjects {
    /** For use with ChestGenHooks */
    public static String treasure_info = null;
    /** For registering Mystcraft related achievements */
    public static AchievementPage achivements;

    public static ArrayList<ItemStack> creative_notebooks;

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

    /** Remember that this is a debug block! */
    public static Block link_modifer = null;

    public static Item writing_desk = null;
    public static Item page = null;
    public static Item descriptive_book = null;
    public static Item linkbook_unlinked = null;
    public static Item linkbook = null;
    public static Item notebook = null;
    public static Item inkvial = null;
    public static Item firemarble = null;

    public static Fluid black_ink = null;

    /** For Jaded */
    public static Item purple_page = null;
}
