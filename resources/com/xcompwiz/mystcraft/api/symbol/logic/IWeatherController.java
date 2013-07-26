
package com.xcompwiz.mystcraft.api.symbol.logic;

import com.xcompwiz.mystcraft.api.internals.IStorageObject;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public interface IWeatherController {
    public abstract void setDataObject(IStorageObject infoObj);

    public abstract void updateRaining();

    public abstract void tick(World worldObj, Chunk chunk);

    public abstract void clear();

    public abstract void togglePrecipitation();

    public abstract float getRainingStrength();

    public abstract float getStormStrength();

    public abstract float getTemperatureAtHeight(float temp, int y);

    public abstract BiomeGenBase getSecondaryBiomeForCoords(BiomeGenBase saved, int par1, int par2);
}
