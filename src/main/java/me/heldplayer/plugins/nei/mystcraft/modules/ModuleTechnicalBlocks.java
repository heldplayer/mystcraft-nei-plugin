package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;

public class ModuleTechnicalBlocks implements IModule {

    private boolean enabled;

    @Override
    public void enable() {
        if (PluginNEIMystcraft.config.hideTechnicalBlocks) {
            Objects.log.log(Level.DEBUG, "Hiding technical blocks from NEI");

            API.hideItem(new ItemStack(MystObjs.portal));
            API.hideItem(new ItemStack(MystObjs.writingDeskBlock));
            API.hideItem(new ItemStack(MystObjs.starFissure));
            API.hideItem(new ItemStack(MystObjs.blackInkBlock));

            this.enabled = true;
        }
    }

    @Override
    public void disable() {
        if (this.enabled) {
            Objects.log.log(Level.DEBUG, "Unhiding technical blocks from NEI");

            ItemInfo.hiddenItems.remove(new ItemStack(MystObjs.portal));
            ItemInfo.hiddenItems.remove(new ItemStack(MystObjs.writingDeskBlock));
            ItemInfo.hiddenItems.remove(new ItemStack(MystObjs.starFissure));
            ItemInfo.hiddenItems.remove(new ItemStack(MystObjs.blackInkBlock));

            this.enabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
