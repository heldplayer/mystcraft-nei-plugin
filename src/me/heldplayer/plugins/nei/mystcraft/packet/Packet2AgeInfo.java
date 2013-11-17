
package me.heldplayer.plugins.nei.mystcraft.packet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import codechicken.nei.api.API;

import com.google.common.io.ByteArrayDataInput;
import com.xcompwiz.mystcraft.api.MystObjects;

import cpw.mods.fml.relauncher.Side;

public class Packet2AgeInfo extends HeldCorePacket {

    public int dimId;
    public String ageName;
    public List<String> symbols;
    public List<ItemStack> pages;

    public Packet2AgeInfo(int packetId) {
        super(packetId, null);
    }

    public Packet2AgeInfo(AgeInfo info) {
        super(2, null);
        this.dimId = info.dimId;
        this.ageName = info.ageName;
        this.symbols = info.symbols;
        this.pages = info.pages;
    }

    @Override
    public Side getSendingSide() {
        return Side.SERVER;
    }

    @Override
    public void read(ByteArrayDataInput in) throws IOException {
        this.dimId = in.readInt();

        byte[] ageName = new byte[in.readInt()];
        in.readFully(ageName);
        this.ageName = new String(ageName);

        int symbols = in.readInt();
        if (symbols >= 0) {
            this.symbols = new ArrayList<String>(symbols);
            for (int i = 0; i < symbols; i++) {
                byte[] bytes = new byte[in.readInt()];
                in.readFully(bytes);
                this.symbols.add(new String(bytes));
            }
        }

        int pages = in.readInt();
        if (pages >= 0) {
            this.pages = new ArrayList<ItemStack>(pages);
            for (int i = 0; i < pages; i++) {
                NBTTagCompound tag = CompressedStreamTools.read(in);
                ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
                this.pages.add(stack);
            }
        }
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.dimId);

        byte[] ageName = this.ageName.getBytes();
        out.writeInt(ageName.length);
        out.write(ageName);

        if (this.symbols == null) {
            out.writeInt(-1);
        }
        else {
            out.writeInt(this.symbols.size());
            for (String symbol : this.symbols) {
                byte[] bytes = symbol.getBytes();
                out.writeInt(bytes.length);
                out.write(bytes);
            }
        }

        if (this.pages == null) {
            out.writeInt(-1);
        }
        else {
            out.writeInt(this.pages.size());
            for (ItemStack page : this.pages) {
                NBTTagCompound tag = new NBTTagCompound("tag");
                page.writeToNBT(tag);
                CompressedStreamTools.write(tag, out);
            }
        }
    }

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        AgeInfo info = new AgeInfo(this.dimId);
        info.ageName = this.ageName;
        info.symbols = this.symbols;
        info.pages = this.pages;
        CommonProxy.clientAgesMap.put(Integer.valueOf(this.dimId), info);

        ItemStack stack = new ItemStack(MystObjects.descriptive_book);
        NBTTagCompound tag = stack.stackTagCompound = new NBTTagCompound("tag");
        tag.setInteger("Dimension", this.dimId);
        tag.setString("agename", this.ageName);

        API.addNBTItem(stack);
    }

}
