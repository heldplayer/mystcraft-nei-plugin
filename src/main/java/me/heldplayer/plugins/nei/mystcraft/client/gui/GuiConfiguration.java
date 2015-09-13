package me.heldplayer.plugins.nei.mystcraft.client.gui;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import java.util.ArrayList;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiConfiguration extends GuiConfig {

    public GuiConfiguration(GuiScreen parent) {
        super(parent, new ArrayList<IConfigElement>(PluginNEIMystcraft.configManager.categories.values()), Objects.MOD_ID, false, false, "NEIMystcraft Plugin Configuration");
    }

}
