package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import cpw.mods.fml.relauncher.Side;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.ItemStack;
import net.specialattack.forge.core.config.ConfigValue;
import org.apache.logging.log4j.Level;

public class ModuleTechnicalBlocks implements IModule {

    public static ConfigValue<Boolean> hideTechnicalBlocks;
    private boolean enabled;

    public ModuleTechnicalBlocks() {
        hideTechnicalBlocks = new ConfigValue<Boolean>("hideTechnicalBlocks", "myst-nei:config.general.hideTechnicalBlocks", Side.CLIENT, Boolean.TRUE);
    }

    @Override
    public void enable() {
        if (hideTechnicalBlocks.getValue()) {
            Objects.log.log(Level.DEBUG, "Hiding technical blocks from NEI");

            API.hideItem(new ItemStack(MystObjs.portal.getBlock()));
            API.hideItem(new ItemStack(MystObjs.writing_desk_block.getBlock()));
            API.hideItem(new ItemStack(MystObjs.star_fissure.getBlock()));

            enabled = true;
        }
    }

    @Override
    public void disable() {
        if (enabled) {
            Objects.log.log(Level.DEBUG, "Unhiding technical blocks from NEI");

            ItemInfo.hiddenItems.remove(new ItemStack(MystObjs.portal.getBlock()));
            ItemInfo.hiddenItems.remove(new ItemStack(MystObjs.writing_desk_block.getBlock()));
            ItemInfo.hiddenItems.remove(new ItemStack(MystObjs.star_fissure.getBlock()));

            enabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public ConfigValue<?>[] getConfigEntries() {
        return new ConfigValue<?>[] { hideTechnicalBlocks };
    }

}
