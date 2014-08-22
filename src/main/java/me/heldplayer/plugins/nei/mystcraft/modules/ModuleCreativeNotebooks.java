package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import cpw.mods.fml.relauncher.Side;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.ItemStack;
import net.specialattack.forge.core.config.ConfigValue;
import org.apache.logging.log4j.Level;

public class ModuleCreativeNotebooks implements IModule {

    public static ConfigValue<Boolean> addCreativeNotebooks;
    private boolean enabled;

    public ModuleCreativeNotebooks() {
        addCreativeNotebooks = new ConfigValue<Boolean>("addCreativeNotebooks", "config.nei.mystcraft.key.addCreativeNotebooks", Side.CLIENT, Boolean.TRUE, "Should creative notebooks be added to NEI?");
    }

    @Override
    public void enable() {
        if (addCreativeNotebooks.getValue()) {
            Objects.log.log(Level.DEBUG, "Adding creative notebooks to NEI view");

            // ItemStack notebook = new ItemStack(MystObjs.notebook, 1, 0);

            // Add a standard notebook first
            // API.addItemListEntry(notebook);

            for (ItemStack stack : MystObjs.creative_notebooks) {
                API.addItemListEntry(stack);
            }

            enabled = true;
        }
    }

    @Override
    public void disable() {
        if (enabled) {
            Objects.log.log(Level.DEBUG, "Removing creative notebooks from NEI view");

            ItemInfo.itemOverrides.removeAll(MystObjs.notebook);

            enabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public ConfigValue<?>[] getConfigEntries() {
        return new ConfigValue<?>[] { addCreativeNotebooks };
    }

}
