
package me.heldplayer.plugins.nei.mystcraft;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import me.heldplayer.util.HeldCore.HeldCoreProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;

import com.xcompwiz.mystcraft.api.MystObjects;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;

public class CommonProxy extends HeldCoreProxy {

    public static HashMap<Integer, AgeInfo> serverAgesMap = new HashMap<Integer, AgeInfo>();
    public static HashMap<Integer, AgeInfo> clientAgesMap = new HashMap<Integer, AgeInfo>();

    @Override
    public void preInit(FMLPreInitializationEvent event) {}

    @Override
    public void init(FMLInitializationEvent event) {}

    @Override
    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarted(FMLServerStartedEvent event) {
        File dataFolder = new File(MinecraftServer.getServer().anvilFile, MinecraftServer.getServer().worldServers[0].getSaveHandler().getWorldDirectoryName() + File.separator + "data");

        if (!dataFolder.exists() || !dataFolder.isDirectory()) {
            return;
        }

        File[] files = dataFolder.listFiles();

        for (File file : files) {
            String name = file.getName();
            if (file.isFile() && name.startsWith("agedata_") && name.endsWith(".dat")) {
                try {
                    DataInputStream input = new DataInputStream(new FileInputStream(file));
                    NBTTagCompound compound = CompressedStreamTools.readCompressed(input);
                    NBTTagCompound data = compound.getCompoundTag("data");

                    int dimId = Integer.parseInt(name.substring(8, name.indexOf(".dat")));
                    AgeInfo info = new AgeInfo(dimId);

                    NBTTagList symbols = data.getTagList("Symbols");
                    info.symbols = new ArrayList<String>(symbols.tagCount());
                    for (int i = 0; i < symbols.tagCount(); i++) {
                        NBTTagString symbol = (NBTTagString) symbols.tagAt(i);
                        info.symbols.add(symbol.data);
                    }

                    NBTTagList pages = data.getTagList("Pages");
                    info.pages = new ArrayList<ItemStack>(pages.tagCount());
                    for (int i = 0; i < pages.tagCount(); i++) {
                        NBTTagCompound tag = (NBTTagCompound) pages.tagAt(i);
                        tag.setName("tag");
                        ItemStack stack = new ItemStack(MystObjects.page);
                        stack.stackTagCompound = tag;
                        info.pages.add(stack);
                    }

                    info.ageName = data.getString("AgeName");

                    serverAgesMap.put(Integer.valueOf(dimId), info);
                }
                catch (Throwable e) {
                    Objects.log.log(Level.SEVERE, "Failed reading agedata file " + file.getName(), e);
                }
            }
        }
    }

    public void serverStopped(FMLServerStoppedEvent event) {
        for (AgeInfo info : serverAgesMap.values()) {
            info.symbols.clear();
            info.symbols = null;
            info.pages.clear();
            info.pages = null;
        }

        serverAgesMap.clear();
    }

}
