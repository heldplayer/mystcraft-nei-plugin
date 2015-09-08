package me.heldplayer.plugins.nei.mystcraft.packet;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.plugins.nei.mystcraft.modules.ModuleDescriptiveBooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class C01RequestAges extends MystNEIPacket {

    public C01RequestAges() {
    }

    @Override
    public Side getSendingSide() {
        return Side.CLIENT;
    }

    @Override
    public Side getReceivingSide() {
        return Side.SERVER;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public void handle(MessageContext context, EntityPlayer player) {
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
            PluginNEIMystcraft.packetHandler.sendTo(new S01AgeInfo(info, addToNEI, listSymbols, listPages, allowRendering), (EntityPlayerMP) player);
        }
    }
}
