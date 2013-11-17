
package me.heldplayer.plugins.nei.mystcraft.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.server.MinecraftServer;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class Packet1RequestAges extends HeldCorePacket {

    public Packet1RequestAges(int packetId) {
        super(packetId, null);
    }

    public Packet1RequestAges() {
        super(1, null);
    }

    @Override
    public Side getSendingSide() {
        return Side.CLIENT;
    }

    @Override
    public void read(ByteArrayDataInput in) throws IOException {}

    @Override
    public void write(DataOutputStream out) throws IOException {}

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        boolean addToNEI = PluginNEIMystcraft.addAgeExplorer.getValue();
        boolean listSymbols = PluginNEIMystcraft.allowSymbolExploring.getValue();
        boolean listPages = PluginNEIMystcraft.allowPageExploring.getValue();

        boolean playerOpped = MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(player.username);

        if (addToNEI && PluginNEIMystcraft.opOnlyAgeList.getValue()) {
            addToNEI = playerOpped;
        }
        if (listSymbols && PluginNEIMystcraft.opOnlySymbolExplorer.getValue()) {
            listSymbols = playerOpped;
        }
        if (listPages && PluginNEIMystcraft.opOnlyPageExploring.getValue()) {
            listPages = playerOpped;
        }

        for (AgeInfo info : CommonProxy.serverAgesMap.values()) {
            manager.addToSendQueue(PacketHandler.instance.createPacket(new Packet2AgeInfo(info, addToNEI, listSymbols, listPages)));
        }
    }

}
