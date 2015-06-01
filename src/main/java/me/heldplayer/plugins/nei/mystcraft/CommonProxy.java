package me.heldplayer.plugins.nei.mystcraft;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.DimensionManager;
import net.specialattack.forge.core.SpACoreProxy;
import org.apache.logging.log4j.Level;

public class CommonProxy extends SpACoreProxy {

    public static Map<Integer, AgeInfo> serverAgesMap = new HashMap<Integer, AgeInfo>();
    public static boolean lookingGlassLoaded;

    @Override
    public void init(FMLInitializationEvent event) {
        FMLInterModComms.sendMessage("LookingGlass", "API", "me.heldplayer.plugins.nei.mystcraft.integration.lookingglass.LookingGlassIntegration.register");
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        File dataFolder = new File(DimensionManager.getCurrentSaveRootDirectory(), "data");

        if (!dataFolder.exists() || !dataFolder.isDirectory()) {
            return;
        }

        File[] files = dataFolder.listFiles();

        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (file.isFile() && name.startsWith("agedata_") && name.endsWith(".dat")) {
                    try {
                        DataInputStream input = new DataInputStream(new FileInputStream(file));
                        NBTTagCompound compound = CompressedStreamTools.readCompressed(input);
                        NBTTagCompound data = compound.getCompoundTag("data");

                        ChunkCoordinates spawn = new ChunkCoordinates(data.getInteger("SpawnX"), data.getInteger("SpawnY"), data.getInteger("SpawnZ"));

                        int dimId = Integer.parseInt(name.substring(8, name.indexOf(".dat")));
                        AgeInfo info = new AgeInfo(dimId, spawn);

                        if (PluginNEIMystcraft.allowSymbolExploring.getValue()) {
                            NBTTagList symbols = data.getTagList("Symbols", 8);
                            info.symbols = new ArrayList<String>(symbols.tagCount());
                            for (int i = 0; i < symbols.tagCount(); i++) {
                                info.symbols.add(symbols.getStringTagAt(i));
                            }
                        }

                        if (PluginNEIMystcraft.allowPageExploring.getValue()) {
                            NBTTagList pages = data.getTagList("Pages", 10);
                            info.pages = new ArrayList<ItemStack>(pages.tagCount());
                            for (int i = 0; i < pages.tagCount(); i++) {
                                NBTTagCompound tag = pages.getCompoundTagAt(i);
                                ItemStack stack = new ItemStack(MystObjs.page);
                                stack.stackTagCompound = tag;
                                info.pages.add(stack);
                            }
                        }

                        info.ageName = data.getString("AgeName");

                        serverAgesMap.put(dimId, info);
                    } catch (Throwable e) {
                        Objects.log.log(Level.ERROR, "Failed reading agedata file " + file.getName(), e);
                    }
                }
            }
        }
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        for (AgeInfo info : serverAgesMap.values()) {
            if (info.symbols != null) {
                info.symbols.clear();
                info.symbols = null;
            }
            if (info.pages != null) {
                info.pages.clear();
                info.pages = null;
            }
        }

        serverAgesMap.clear();
    }

}
