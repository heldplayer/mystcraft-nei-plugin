
package me.heldplayer.plugins.nei.mystcraft.packet;

import me.heldplayer.plugins.nei.mystcraft.Objects;

public class PacketHandler extends me.heldplayer.util.HeldCore.packet.PacketHandler {

    public static PacketHandler instance;

    public PacketHandler() {
        super(Objects.MOD_CHANNEL);
        this.registerPacket(1, Packet1RequestAges.class);
        this.registerPacket(2, Packet2AgeInfo.class);
        instance = this;
    }

}
