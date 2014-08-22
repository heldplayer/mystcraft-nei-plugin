package me.heldplayer.plugins.nei.mystcraft.modules;

import net.specialattack.forge.core.config.ConfigValue;

public interface IModule {

    void enable();

    void disable();

    boolean isEnabled();

    ConfigValue<?>[] getConfigEntries();

}
