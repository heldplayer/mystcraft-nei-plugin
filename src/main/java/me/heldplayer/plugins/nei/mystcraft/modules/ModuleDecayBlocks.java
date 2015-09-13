package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import java.util.ArrayList;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;

public class ModuleDecayBlocks implements IModule {

    private boolean enabled;

    @Override
    public void enable() {
        if (PluginNEIMystcraft.config.addDecaySubTypes) {
            Objects.log.log(Level.DEBUG, "Adding decay types to NEI view");

            ArrayList<Integer> damageVariants = new ArrayList<Integer>();
            damageVariants.add(0);
            damageVariants.add(1);

            damageVariants.add(3);
            damageVariants.add(4);

            damageVariants.add(6);

            for (Integer damage : damageVariants) {
                API.addItemListEntry(new ItemStack(MystObjs.decay, 1, damage));
            }

            this.enabled = true;
        }
    }

    @Override
    public void disable() {
        if (this.enabled) {
            Objects.log.log(Level.DEBUG, "Removing decay types from NEI view");

            ItemInfo.itemOverrides.removeAll(Item.getItemFromBlock(MystObjs.decay));

            this.enabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
