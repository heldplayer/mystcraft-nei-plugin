
package me.heldplayer.plugins.NEI.mystcraft;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOverlay extends Slot {

    public SlotOverlay(int id, int x, int y) {
        super(null, id, x, y);
    }

    @Override
    public ItemStack getStack() {
        return null;
    }

}
