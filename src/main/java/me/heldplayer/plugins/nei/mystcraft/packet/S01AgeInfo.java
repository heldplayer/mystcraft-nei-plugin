package me.heldplayer.plugins.nei.mystcraft.packet;

import codechicken.nei.api.API;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import me.heldplayer.plugins.nei.mystcraft.client.ClientProxy;
import me.heldplayer.plugins.nei.mystcraft.client.Integrator;
import me.heldplayer.plugins.nei.mystcraft.modules.ModuleDescriptiveBooks;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ReportedException;

public class S01AgeInfo extends MystNEIPacket {

    public int dimId;
    public ChunkCoordinates spawn;
    public String ageName;
    public List<String> symbols;
    public List<ItemStack> pages;
    public boolean addToNEI;
    public boolean allowRendering;

    public S01AgeInfo() {
    }

    public S01AgeInfo(AgeInfo info, boolean addToNEI, boolean listSymbols, boolean listPages, boolean allowRendering) {
        this.dimId = info.dimId;
        this.spawn = info.spawn;
        this.ageName = info.ageName;
        if (listSymbols) {
            this.symbols = info.symbols;
        }
        if (listPages) {
            this.pages = info.pages;
        }
        this.addToNEI = addToNEI;
        this.allowRendering = allowRendering;
    }

    @Override
    public Side getSendingSide() {
        return Side.SERVER;
    }

    @Override
    public Side getReceivingSide() {
        return Side.SERVER;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.dimId = buf.readInt();
        this.spawn = new ChunkCoordinates(buf.readInt(), buf.readInt(), buf.readInt());
        this.addToNEI = buf.readBoolean();
        this.allowRendering = buf.readBoolean();

        byte[] ageName = new byte[buf.readInt()];
        buf.readBytes(ageName);
        this.ageName = new String(ageName);

        int symbols = buf.readInt();
        if (symbols >= 0) {
            this.symbols = new ArrayList<String>(symbols);
            for (int i = 0; i < symbols; i++) {
                byte[] bytes = new byte[buf.readInt()];
                buf.readBytes(bytes);
                this.symbols.add(new String(bytes));
            }
        }

        int pages = buf.readInt();
        if (pages >= 0) {
            this.pages = new ArrayList<ItemStack>(pages);
            for (int i = 0; i < pages; i++) {
                byte[] data = new byte[buf.readInt()];
                buf.readBytes(data);
                NBTTagCompound tag;
                try {
                    tag = CompressedStreamTools.func_152457_a(data, NBTSizeTracker.field_152451_a);
                } catch (IOException e) {
                    throw new ReportedException(new CrashReport("Exception trying to write packet", e));
                }
                ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
                this.pages.add(stack);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.dimId);
        buf.writeInt(this.spawn.posX);
        buf.writeInt(this.spawn.posY);
        buf.writeInt(this.spawn.posZ);
        buf.writeBoolean(this.addToNEI);
        buf.writeBoolean(this.allowRendering);

        byte[] ageName = this.ageName.getBytes();
        buf.writeInt(ageName.length);
        buf.writeBytes(ageName);

        if (this.symbols == null) {
            buf.writeInt(-1);
        } else {
            buf.writeInt(this.symbols.size());
            for (String symbol : this.symbols) {
                byte[] bytes = symbol.getBytes();
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
            }
        }

        if (this.pages == null) {
            buf.writeInt(-1);
        } else {
            buf.writeInt(this.pages.size());
            for (ItemStack page : this.pages) {
                NBTTagCompound tag = new NBTTagCompound();
                page.writeToNBT(tag);
                byte[] data;
                try {
                    data = CompressedStreamTools.compress(tag);
                } catch (IOException e) {
                    throw new ReportedException(new CrashReport("Exception trying to read packet", e));
                }
                buf.writeInt(data.length);
                buf.writeBytes(data);
            }
        }
    }

    @Override
    public void handle(MessageContext context, EntityPlayer player) {
        AgeInfo info = new AgeInfo(this.dimId, this.spawn);
        info.ageName = this.ageName;
        info.symbols = this.symbols;
        info.pages = this.pages;
        info.allowRendering = this.allowRendering && CommonProxy.lookingGlassLoaded;
        putClientInfo(this.dimId, info);

        ItemStack stack = new ItemStack(MystObjs.descriptiveBook);
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

    @SideOnly(Side.CLIENT)
    private void putClientInfo(int dimId, AgeInfo info) {
        ClientProxy.clientAgesMap.put(this.dimId, info);
    }

}
