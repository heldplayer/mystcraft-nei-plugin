
package com.xcompwiz.mystcraft.api.symbol.logic;

import net.minecraft.world.chunk.Chunk;

public interface IChunkProviderFinalization {

    void finalizeChunk(Chunk chunk, int chunkX, int chunkZ);

}
