package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;

public class ModuleCreativePortfolios implements IModule {

    private boolean enabled;

    @Override
    public void enable() {
        if (PluginNEIMystcraft.config.addCreativeNotebooks) {
            Objects.log.log(Level.DEBUG, "Adding creative notebooks to NEI view");

            // ItemStack notebook = new ItemStack(MystObjs.notebook, 1, 0);

            // Add a standard notebook first
            // API.addItemListEntry(notebook);

            for (ItemStack stack : MystObjs.creative_notebooks) {
                API.addItemListEntry(stack);
            }

            this.enabled = true;
        }
    }

    @Override
    public void disable() {
        if (this.enabled) {
            Objects.log.log(Level.DEBUG, "Removing creative notebooks from NEI view");

            ItemInfo.itemOverrides.removeAll(MystObjs.portfolio);

            this.enabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
