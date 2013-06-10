
package com.xcompwiz.mystcraft.api.symbol.logic;

import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public interface ITerrainFeatureLocator {
    public abstract ChunkPosition locate(World world, String s, int i, int j, int k);
}
