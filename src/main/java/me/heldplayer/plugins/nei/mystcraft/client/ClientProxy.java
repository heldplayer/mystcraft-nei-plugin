package me.heldplayer.plugins.nei.mystcraft.client;

import codechicken.nei.recipe.GuiRecipe;
import com.xcompwiz.lookingglass.api.view.IWorldView;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.heldplayer.plugins.nei.mystcraft.*;
import me.heldplayer.plugins.nei.mystcraft.packet.C01RequestAges;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.specialattack.forge.core.SpACore;
import net.specialattack.forge.core.client.texture.IconHolder;
import net.specialattack.forge.core.event.SyncEvent;
import org.apache.logging.log4j.Level;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static IIcon iconViewAge;
    public static Map<Integer, AgeInfo> clientAgesMap = new HashMap<Integer, AgeInfo>();
    public static List<IWorldView> views;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        SpACore.registerIconHolder(ClientProxy.iconViewAge = new IconHolder(Assets.DOMAIN + "view-age"));
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        FMLInterModComms.sendMessage("Mystcraft", "API", "me.heldplayer.plugins.nei.mystcraft.integration.mystcraft.MystcraftIntegration.register");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);

        try {
            Objects.log.log(Level.DEBUG, "Initializing temporary wrappers");
            MystObjs.initialize();
        } catch (Throwable ex) {
            Objects.log.log(Level.ERROR, "Failed initializing temporary wrappers", ex);
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
    public void onClientStartSyncing(SyncEvent.ClientServerInfoReceived event) {
        Integrator.allAges.clear();
        Integrator.reinitialize();
        NEIConfig.resetNEI();
        PluginNEIMystcraft.packetHandler.sendToServer(new C01RequestAges());
    }

    /**
     * Used to make sure all used LookingGlass views are released that were opened in NEI
     */
    @SubscribeEvent
    public void onInitGuiPost(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.gui instanceof GuiRecipe)) {
            if (ClientProxy.views != null) {
                for (IWorldView view : ClientProxy.views) {
                    view.release();
                }
                ClientProxy.views.clear();
                ClientProxy.views = null;
            }
        }
    }

}
