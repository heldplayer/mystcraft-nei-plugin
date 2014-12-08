package me.heldplayer.plugins.nei.mystcraft.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.Map;
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

    public static Map<Integer, AgeInfo> clientAgesMap = new HashMap<Integer, AgeInfo>();

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        FMLInterModComms.sendMessage("Mystcraft", "register", "me.heldplayer.plugins.nei.mystcraft.client.Integrator.setMystAPI");
    }

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
        NEIConfig.resetNEI();
    }

    @SubscribeEvent
    public void clientLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        Integrator.reinitialize();
    }

    @SubscribeEvent
    public void onClientStartSyncing(SyncEvent.ClientStartSyncing event) {
        Integrator.allAges.clear();
        Integrator.reinitialize();
        NEIConfig.resetNEI();
        PluginNEIMystcraft.packetHandler.sendPacketToServer(new Packet1RequestAges());
    }

}
