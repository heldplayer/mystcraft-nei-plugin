package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import cpw.mods.fml.relauncher.Side;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.client.Integrator;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.ItemStack;
import net.specialattack.forge.core.config.ConfigValue;
import org.apache.logging.log4j.Level;

public class ModulePages implements IModule {

    public static ConfigValue<Boolean> addSymbolPages;
    public static ConfigValue<Boolean> addLinkPanels;
    private boolean symbolsEnabled;
    private boolean linkpanelsEnabled;

    public ModulePages() {
        addSymbolPages = new ConfigValue<Boolean>("addSymbolPages", "config.nei.mystcraft.key.addSymbolPages", Side.CLIENT, Boolean.TRUE, "Should symbol pages be added to NEI?");
        addLinkPanels = new ConfigValue<Boolean>("addLinkPanels", "config.nei.mystcraft.key.addLinkPanels", Side.CLIENT, Boolean.TRUE, "Should link panels be added to NEI?");
    }

    @Override
    public void enable() {
        if (addSymbolPages.getValue()) {
            Objects.log.log(Level.DEBUG, "Adding symbol pages to NEI view");

            Set<IAgeSymbol> symbols = new TreeSet<IAgeSymbol>(new Comparator<IAgeSymbol>() {
                @Override
                public int compare(IAgeSymbol o1, IAgeSymbol o2) {
                    return o1.displayName().compareTo(o2.displayName());
                }
            });
            symbols.addAll(Integrator.mystAPI.getSymbolAPI().getAllRegisteredSymbols());

            for (IAgeSymbol symbol : symbols) {
                API.addItemListEntry(Integrator.mystAPI.getItemFactory().buildSymbolPage(symbol.identifier()));
            }

            symbolsEnabled = true;
        }

        if (addLinkPanels.getValue()) {
            Objects.log.log(Level.DEBUG, "Adding link panels to NEI view");

            for (ItemStack stack : Integrator.getAllLinkpanels()) {
                API.addItemListEntry(stack);
            }

            symbolsEnabled = true;
        }
    }

    @Override
    public void disable() {
        if (symbolsEnabled || linkpanelsEnabled) {
            Objects.log.log(Level.DEBUG, "Removing symbol pages and link panels from NEI view");

            ItemInfo.itemOverrides.removeAll(MystObjs.page.getItem());

            symbolsEnabled = false;
            linkpanelsEnabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return symbolsEnabled || linkpanelsEnabled;
    }

    @Override
    public ConfigValue<?>[] getConfigEntries() {
        return new ConfigValue<?>[] { addSymbolPages, addLinkPanels };
    }

}
