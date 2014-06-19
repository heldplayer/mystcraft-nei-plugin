
package me.heldplayer.plugins.nei.mystcraft.client;

import java.util.ArrayList;

import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import me.heldplayer.plugins.nei.mystcraft.packet.Packet1RequestAges;
import me.heldplayer.plugins.nei.mystcraft.packet.PacketHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import codechicken.nei.api.ItemInfo;

import com.xcompwiz.mystcraft.api.MystObjects;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IConnectionHandler {

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        NetworkRegistry.instance().registerConnectionHandler(this);
    }

    @Override
    public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {}

    @Override
    public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
        return null;
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {}

    @Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {}

    @Override
    public void connectionClosed(INetworkManager manager) {
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

    @Override
    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
        // TODO: add config option to disable + prevent on servers

        ArrayList<ItemStack> variants = ItemInfo.getItemCompounds(MystObjects.descriptive_book.itemID);

        if (variants == null) {
            variants = new ArrayList<ItemStack>();
            ItemInfo.itemcompounds.put(Integer.valueOf(MystObjects.descriptive_book.itemID), variants);
        }

        variants.clear();
        variants.add(new ItemStack(MystObjects.descriptive_book));

        manager.addToSendQueue(PacketHandler.instance.createPacket(new Packet1RequestAges()));
    }

}
