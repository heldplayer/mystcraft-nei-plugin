package me.heldplayer.plugins.nei.mystcraft.packet;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
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
        boolean addToNEI = PluginNEIMystcraft.config.addAgeList;
        boolean listSymbols = PluginNEIMystcraft.config.addAgeExplorer && PluginNEIMystcraft.config.allowSymbolExploring;
        boolean listPages = PluginNEIMystcraft.config.addAgeExplorer && PluginNEIMystcraft.config.allowPageExploring;
        boolean allowRendering = CommonProxy.lookingGlassLoaded && PluginNEIMystcraft.config.addAgeExplorer && PluginNEIMystcraft.config.allowAgeViewer;

        boolean playerOpped = MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile());

        if (addToNEI && PluginNEIMystcraft.config.opOnlyAgeList) {
            addToNEI = playerOpped;
        }
        if (listSymbols && PluginNEIMystcraft.config.opOnlySymbolExplorer) {
            listSymbols = playerOpped;
        }
        if (listPages && PluginNEIMystcraft.config.opOnlyPageExploring) {
            listPages = playerOpped;
        }
        if (allowRendering && PluginNEIMystcraft.config.opOnlyAgeViewer) {
            allowRendering = playerOpped;
        }

        for (AgeInfo info : CommonProxy.serverAgesMap.values()) {
            PluginNEIMystcraft.packetHandler.sendTo(new S01AgeInfo(info, addToNEI, listSymbols, listPages, allowRendering), (EntityPlayerMP) player);
        }
    }
}
