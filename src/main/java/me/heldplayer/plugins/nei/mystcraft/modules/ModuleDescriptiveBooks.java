package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.client.Integrator;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.ItemStack;
import net.specialattack.forge.core.config.ConfigValue;
import org.apache.logging.log4j.Level;

public class ModuleDescriptiveBooks implements IModule {

    public static ConfigValue<Boolean> addAgeList;
    private boolean enabled;

    public ModuleDescriptiveBooks() {
        addAgeList = new ConfigValue<Boolean>("addAgeList", "myst-nei:config.general.addAgeList", null, Boolean.TRUE);
    }

    @Override
    public void enable() {
        if (addAgeList.getValue()) {
            Objects.log.log(Level.DEBUG, "Adding descriptive books to NEI view");

            for (ItemStack age : Integrator.allAges) {
                API.addItemListEntry(age);
            }

            enabled = true;
        }
    }

    @Override
    public void disable() {
        if (enabled) {
            Objects.log.log(Level.DEBUG, "Removing descriptive books from NEI view");

            ItemInfo.itemOverrides.removeAll(MystObjs.descriptiveBook);

            enabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public ConfigValue<?>[] getConfigEntries() {
        return new ConfigValue<?>[] { addAgeList };
    }

}
