
package me.heldplayer.plugins.nei.mystcraft.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.specialattack.forge.core.packet.SpACorePacket;
import codechicken.nei.api.API;
import cpw.mods.fml.relauncher.Side;

public class Packet2AgeInfo extends SpACorePacket {

    public int dimId;
    public String ageName;
    public List<String> symbols;
    public List<ItemStack> pages;
    public boolean addToNEI;

    public Packet2AgeInfo() {
        super(null);
    }

    public Packet2AgeInfo(AgeInfo info, boolean addToNEI, boolean listSymbols, boolean listPages) {
        super(null);
        this.dimId = info.dimId;
        this.ageName = info.ageName;
        if (listSymbols) {
            this.symbols = info.symbols;
        }
        if (listPages) {
            this.pages = info.pages;
        }
        this.addToNEI = addToNEI;
    }

    @Override
    public Side getSendingSide() {
        return Side.SERVER;
    }

    @Override
    public void read(ChannelHandlerContext context, ByteBuf in) throws IOException {
        this.dimId = in.readInt();
        this.addToNEI = in.readBoolean();

        byte[] ageName = new byte[in.readInt()];
        in.readBytes(ageName);
        this.ageName = new String(ageName);

        int symbols = in.readInt();
        if (symbols >= 0) {
            this.symbols = new ArrayList<String>(symbols);
            for (int i = 0; i < symbols; i++) {
                byte[] bytes = new byte[in.readInt()];
                in.readBytes(bytes);
                this.symbols.add(new String(bytes));
            }
        }

        int pages = in.readInt();
        if (pages >= 0) {
            this.pages = new ArrayList<ItemStack>(pages);
            for (int i = 0; i < pages; i++) {
                byte[] data = new byte[in.readInt()];
                in.readBytes(data);
                NBTTagCompound tag = CompressedStreamTools.decompress(data);
                ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
                this.pages.add(stack);
            }
        }
    }

    @Override
    public void write(ChannelHandlerContext context, ByteBuf out) throws IOException {
        out.writeInt(this.dimId);
        out.writeBoolean(this.addToNEI);

        byte[] ageName = this.ageName.getBytes();
        out.writeInt(ageName.length);
        out.writeBytes(ageName);

        if (this.symbols == null) {
            out.writeInt(-1);
        }
        else {
            out.writeInt(this.symbols.size());
            for (String symbol : this.symbols) {
                byte[] bytes = symbol.getBytes();
                out.writeInt(bytes.length);
                out.writeBytes(bytes);
            }
        }

        if (this.pages == null) {
            out.writeInt(-1);
        }
        else {
            out.writeInt(this.pages.size());
            for (ItemStack page : this.pages) {
                NBTTagCompound tag = new NBTTagCompound();
                page.writeToNBT(tag);
                byte[] data = CompressedStreamTools.compress(tag);
                out.writeInt(data.length);
                out.writeBytes(data);
            }
        }
    }

    @Override
    public void onData(ChannelHandlerContext context, EntityPlayer player) {
        AgeInfo info = new AgeInfo(this.dimId);
        info.ageName = this.ageName;
        info.symbols = this.symbols;
        info.pages = this.pages;
        CommonProxy.clientAgesMap.put(Integer.valueOf(this.dimId), info);

        ItemStack stack = new ItemStack(MystObjs.descriptive_book);
        NBTTagCompound tag = stack.stackTagCompound = new NBTTagCompound();
        tag.setInteger("Dimension", this.dimId);
        tag.setString("agename", this.ageName);

        if (this.addToNEI && PluginNEIMystcraft.addAgeList.getValue()) {
            API.addItemListEntry(stack);
        }
    }

}
