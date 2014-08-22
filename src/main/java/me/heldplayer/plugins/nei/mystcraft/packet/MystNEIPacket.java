package me.heldplayer.plugins.nei.mystcraft.packet;

import net.minecraft.world.World;
import net.specialattack.forge.core.packet.SpACorePacket;

public abstract class MystNEIPacket extends SpACorePacket {

    public MystNEIPacket(World world) {
        super(world);
    }

}
