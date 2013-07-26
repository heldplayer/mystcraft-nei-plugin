
package com.xcompwiz.mystcraft.api.symbol;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.biome.BiomeGenBase;

import com.xcompwiz.mystcraft.api.internals.BlockCategory;
import com.xcompwiz.mystcraft.api.internals.BlockDescriptor;

/**
 * A collection of helper functions for dealing with more complex modifier
 * types, such as blocks and biomes
 * 
 * @author xcompwiz
 */
public final class ModifierUtils {

    /**
     * Provides a block of a particular generation category if one is in the
     * queue
     * Will pop the first block satisfying the generation category found from
     * the top of the queue
     * 
     * @param controller
     *        The controller passed to the symbol during logic registration
     * @param type
     *        The generation category to attempt to retrieve
     * @return A block descriptor, if one satisfying the category is found.
     *         Otherwise null
     */
    public static BlockDescriptor popBlockOfType(IAgeController controller, BlockCategory type) {
        Modifier modifier = controller.popModifier("blocklist");
        List<BlockDescriptor> list = modifier.asList();
        if (list == null)
            return null;
        controller.setModifier("blocklist", modifier);
        for (int i = 0; i < list.size(); ++i) {
            BlockDescriptor block = list.get(i);
            if (block.isUsable(type)) {
                list.remove(i);
                modifier.dangling -= dangling_block;
                return block;
            }
        }
        return null;
    }

    /**
     * Adds a block descriptor to the top of the queue
     * 
     * @param controller
     *        The controller passed to the symbol during logic registration
     * @param block
     *        The block descriptor to push to the queue
     */
    public static void pushBlock(IAgeController controller, BlockDescriptor block) {
        Modifier modifier = controller.popModifier("blocklist");
        List<BlockDescriptor> list = modifier.asList();
        if (list == null) {
            list = new ArrayList<BlockDescriptor>();
            modifier = new Modifier(list, 0);
        }
        list.add(0, block);
        modifier.dangling += dangling_block;
        controller.setModifier("blocklist", modifier);
    }

    public static void pushBiome(IAgeController controller, BiomeGenBase biome) {
        Modifier modifier = controller.popModifier("biomelist");
        List<BiomeGenBase> list = modifier.asList();
        if (list == null) {
            list = new ArrayList<BiomeGenBase>();
            modifier = new Modifier(list, 0);
        }
        list.add(biome);
        modifier.dangling += dangling_biome;
        controller.setModifier("biomelist", modifier);
    }

    public static BiomeGenBase popBiome(IAgeController controller) {
        Modifier modifier = controller.popModifier("biomelist");
        List<BiomeGenBase> list = modifier.asList();
        if (list == null || list.size() == 0)
            return null;
        controller.setModifier("biomelist", modifier);
        BiomeGenBase biome = list.remove(list.size() - 1);
        modifier.dangling -= dangling_biome;
        return biome;
    }

    public static final int dangling_block = 50;
    public static final int dangling_biome = 100;
}
