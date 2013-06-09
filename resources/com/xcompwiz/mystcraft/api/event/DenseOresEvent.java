package com.xcompwiz.mystcraft.api.event;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraftforge.event.Event;

public class DenseOresEvent extends Event {
	public final World worldObj;
	public final Random random;
	public final int xPos;
	public final int zPos;

	public DenseOresEvent(World worldObj, Random rand, int xPos, int zPos) {
		this.worldObj = worldObj;
		this.random = rand;
		this.xPos = xPos;
		this.zPos = zPos;
	}

}
