package me.heldplayer.plugins.nei.mystcraft.packet;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.plugins.nei.mystcraft.modules.ModuleDescriptiveBooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.specialattack.forge.core.packet.Attributes;

public class Packet1RequestAges extends MystNEIPacket {

    public Packet1RequestAges() {
        super(null);
    }

    @Override
    public Side getSendingSide() {
        return Side.CLIENT;
    }

    @Override
    public void read(ChannelHandlerContext context, ByteBuf in) throws IOException {
    }

    @Override
    public void write(ChannelHandlerContext context, ByteBuf out) throws IOException {
    }

    @Override
    public void onData(ChannelHandlerContext context) {
        this.requireAttribute(Attributes.SENDING_PLAYER);

        EntityPlayer player = this.attr(Attributes.SENDING_PLAYER).get();

        boolean addToNEI = ModuleDescriptiveBooks.addAgeList.getValue();
        boolean listSymbols = PluginNEIMystcraft.addAgeExplorer.getValue() && PluginNEIMystcraft.allowSymbolExploring.getValue();
        boolean listPages = PluginNEIMystcraft.addAgeExplorer.getValue() && PluginNEIMystcraft.allowPageExploring.getValue();
        boolean allowRendering = CommonProxy.lookingGlassLoaded && PluginNEIMystcraft.addAgeExplorer.getValue() && PluginNEIMystcraft.allowAgeViewer.getValue();

        boolean playerOpped = MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile());

        if (addToNEI && PluginNEIMystcraft.opOnlyAgeList.getValue()) {
            addToNEI = playerOpped;
        }
        if (listSymbols && PluginNEIMystcraft.opOnlySymbolExplorer.getValue()) {
            listSymbols = playerOpped;
        }
        if (listPages && PluginNEIMystcraft.opOnlyPageExploring.getValue()) {
            listPages = playerOpped;
        }
        if (allowRendering && PluginNEIMystcraft.opOnlyAgeViewer.getValue()) {
            allowRendering = playerOpped;
        }

        for (AgeInfo info : CommonProxy.serverAgesMap.values()) {
            PluginNEIMystcraft.packetHandler.sendPacketToPlayer(new Packet2AgeInfo(info, addToNEI, listSymbols, listPages, allowRendering), player);
        }
    }

}
