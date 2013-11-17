
package me.heldplayer.plugins.nei.mystcraft;

import java.util.List;

import net.minecraft.item.ItemStack;

public class AgeInfo {

    public final int dimId;
    public String ageName;
    public List<String> symbols;
    public List<ItemStack> pages;

    public AgeInfo(int dimId) {
        this.dimId = dimId;
    }

}
