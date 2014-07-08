package me.heldplayer.plugins.nei.mystcraft;

import net.minecraft.item.ItemStack;

import java.util.List;

public class AgeInfo {

    public final int dimId;
    public String ageName;
    public List<String> symbols;
    public List<ItemStack> pages;

    public AgeInfo(int dimId) {
        this.dimId = dimId;
    }

}
