package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.specialattack.forge.core.config.ConfigValue;
import org.apache.logging.log4j.Level;

public class ModuleDecayBlocks implements IModule {

    public static ConfigValue<Boolean> addDecaySubTypes;
    private boolean enabled;

    public ModuleDecayBlocks() {
        addDecaySubTypes = new ConfigValue<Boolean>("addDecaySubTypes", "config.nei.mystcraft.key.addDecaySubTypes", Side.CLIENT, Boolean.TRUE, "Should all decay types be visible in NEI?");
    }

    @Override
    public void enable() {
        if (addDecaySubTypes.getValue()) {
            Objects.log.log(Level.DEBUG, "Adding decay types to NEI view");

            ArrayList<Integer> damageVariants = new ArrayList<Integer>();
            damageVariants.add(0);
            damageVariants.add(1);

            damageVariants.add(3);
            damageVariants.add(4);

            damageVariants.add(6);

            for (Integer damage : damageVariants) {
                API.addItemListEntry(new ItemStack(MystObjs.decay.getBlock(), 1, damage));
            }

            enabled = true;
        }
    }

    @Override
    public void disable() {
        if (enabled) {
            Objects.log.log(Level.DEBUG, "Removing decay types from NEI view");

            ItemInfo.itemOverrides.removeAll(Item.getItemFromBlock(MystObjs.decay.getBlock()));

            enabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public ConfigValue<?>[] getConfigEntries() {
        return new ConfigValue<?>[] { addDecaySubTypes };
    }

}
