package me.heldplayer.plugins.nei.mystcraft;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;

public class AgeInfo {

    public final int dimId;
    public final ChunkCoordinates spawn;
    public String ageName;
    public List<String> symbols;
    public List<ItemStack> pages;
    public boolean allowRendering;

    public AgeInfo(int dimId, ChunkCoordinates spawn) {
        this.dimId = dimId;
        this.spawn = spawn;
    }

}
