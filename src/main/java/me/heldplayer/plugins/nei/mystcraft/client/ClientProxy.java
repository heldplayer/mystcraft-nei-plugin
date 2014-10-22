package me.heldplayer.plugins.nei.mystcraft.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.plugins.nei.mystcraft.packet.Packet1RequestAges;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraftforge.common.MinecraftForge;
import net.specialattack.forge.core.event.SyncEvent;
import org.apache.logging.log4j.Level;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);

        try {
            Objects.log.log(Level.DEBUG, "Initializing temporary wrappers for 1.7");
            MystObjs.initialize();
        } catch (Throwable ex) {
            Objects.log.log(Level.ERROR, "Failed initializing temporary wrappers for 1.7", ex);
        }
    }

    @SubscribeEvent
    public void onClientDisconnectedFromServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        for (AgeInfo info : clientAgesMap.values()) {
            if (info.symbols != null) {
                info.symbols.clear();
                info.symbols = null;
            }
            if (info.pages != null) {
                info.pages.clear();
                info.pages = null;
            }
        }

        clientAgesMap.clear();
    }

    @SubscribeEvent
    public void clientLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        Integrator.reinitialize();
    }

    @SubscribeEvent
    public void onClientStartSyncing(SyncEvent.ClientStartSyncing event) {
        PluginNEIMystcraft.packetHandler.sendPacketToServer(new Packet1RequestAges());
    }

}
