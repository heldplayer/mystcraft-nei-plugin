package com.xcompwiz.mystcraft.api.symbol.logic;

import net.minecraft.world.World;

public interface ITerrainModifier {
    public abstract void affectTerrain(World worldObj, int chunkX, int chunkZ, short[] blocks, byte[] metadata);
}