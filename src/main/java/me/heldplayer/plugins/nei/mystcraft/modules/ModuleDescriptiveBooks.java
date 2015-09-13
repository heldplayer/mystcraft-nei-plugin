package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.plugins.nei.mystcraft.client.Integrator;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;

public class ModuleDescriptiveBooks implements IModule {

    private boolean enabled;

    @Override
    public void enable() {
        if (PluginNEIMystcraft.config.addAgeList) {
            Objects.log.log(Level.DEBUG, "Adding descriptive books to NEI view");

            for (ItemStack age : Integrator.allAges) {
                API.addItemListEntry(age);
            }

            this.enabled = true;
        }
    }

    @Override
    public void disable() {
        if (this.enabled) {
            Objects.log.log(Level.DEBUG, "Removing descriptive books from NEI view");

            ItemInfo.itemOverrides.removeAll(MystObjs.descriptiveBook);

            this.enabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
