
package me.heldplayer.plugins.nei.mystcraft.client;

import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.api.API;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.DefaultOverlayHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static InkMixerRecipeHandler inkMixer;
    public static Class<? extends GuiContainer> guiInkMixerClass;
    public static WritingDeskRecipeHandler writingDesk;
    public static Class<? extends GuiContainer> guiWritingDeskClass;
    public static MystTooltipeHandler tooltipHandler;

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        Integrator.initialize(PluginNEIMystcraft.mystcraft);

        ClientProxy.inkMixer = new InkMixerRecipeHandler();
        API.registerRecipeHandler(ClientProxy.inkMixer);
        API.registerUsageHandler(ClientProxy.inkMixer);

        API.registerGuiOverlay(ClientProxy.guiInkMixerClass, "inkmixer");
        API.registerGuiOverlayHandler(ClientProxy.guiInkMixerClass, new DefaultOverlayHandler(), "inkmixer");

        ClientProxy.writingDesk = new WritingDeskRecipeHandler();
        API.registerRecipeHandler(ClientProxy.writingDesk);
        API.registerUsageHandler(ClientProxy.writingDesk);

        API.registerGuiOverlay(ClientProxy.guiWritingDeskClass, "writingdesk");
        API.registerGuiOverlayHandler(ClientProxy.guiWritingDeskClass, new DefaultOverlayHandler(), "writingdesk");

        tooltipHandler = new MystTooltipeHandler();
        GuiContainerManager.addTooltipHandler(tooltipHandler);
        GuiContainerManager.addInputHandler(tooltipHandler);
    }
}
