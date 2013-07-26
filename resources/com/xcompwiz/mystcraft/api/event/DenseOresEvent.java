
package com.xcompwiz.mystcraft.api.event;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraftforge.event.Event;

public class DenseOresEvent extends Event {
    /** The world in which we're generating */
    public final World worldObj;
    /** The random object to use when generating */
    public final Random random;
    /** The block position of the chunk (chunkX * 16) */
    public final int xPos;
    /** The block position of the chunk (chunkZ * 16) */
    public final int zPos;

    /**
     * Listen for this event via Forge in order to do generation after Dense
     * Ores does for every dense ores symbol
     * The provided variables are exactly like that of the standard generation.
     * See the documentation of each variable for more information.
     */
    public DenseOresEvent(World worldObj, Random rand, int xPos, int zPos) {
        this.worldObj = worldObj;
        this.random = rand;
        this.xPos = xPos;
        this.zPos = zPos;
    }

}
