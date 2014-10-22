package me.heldplayer.plugins.nei.mystcraft.packet;

import codechicken.nei.api.API;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import me.heldplayer.plugins.nei.mystcraft.client.Integrator;
import me.heldplayer.plugins.nei.mystcraft.modules.ModuleDescriptiveBooks;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

public class Packet2AgeInfo extends MystNEIPacket {

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
                NBTTagCompound tag = CompressedStreamTools.func_152457_a(data, NBTSizeTracker.field_152451_a);
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
        } else {
            out.writeInt(this.symbols.size());
            for (String symbol : this.symbols) {
                byte[] bytes = symbol.getBytes();
                out.writeInt(bytes.length);
                out.writeBytes(bytes);
            }
        }

        if (this.pages == null) {
            out.writeInt(-1);
        } else {
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
        CommonProxy.clientAgesMap.put(this.dimId, info);

        ItemStack stack = new ItemStack(MystObjs.descriptive_book.getItem());
        NBTTagCompound tag = stack.stackTagCompound = new NBTTagCompound();
        tag.setInteger("Dimension", this.dimId);
        tag.setString("agename", this.ageName);

        if (this.addToNEI) {
            Integrator.allAges.add(stack);
            if (ModuleDescriptiveBooks.addAgeList.getValue()) {
                API.addItemListEntry(stack);
            }
        }
    }

}
