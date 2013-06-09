package com.xcompwiz.mystcraft.api.symbol.logic;

public interface ITerrainGenerator {
    public abstract void generateTerrain(int chunkX, int chunkZ, short[] blocks, byte[] metadata);
}