package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.plugins.nei.mystcraft.client.Integrator;
import me.heldplayer.plugins.nei.mystcraft.integration.mystcraft.MystItemFactory;
import me.heldplayer.plugins.nei.mystcraft.integration.mystcraft.MystSymbol;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;

public class ModulePages implements IModule {

    private boolean symbolsEnabled;
    private boolean linkpanelsEnabled;

    @Override
    public void enable() {
        if (PluginNEIMystcraft.config.addSymbolPages) {
            Objects.log.log(Level.DEBUG, "Adding symbol pages to NEI view");

            Set<IAgeSymbol> symbols = new TreeSet<IAgeSymbol>(new Comparator<IAgeSymbol>() {
                @Override
                public int compare(IAgeSymbol o1, IAgeSymbol o2) {
                    return o1.displayName().compareTo(o2.displayName());
                }
            });
            symbols.addAll(MystSymbol.api.getAllRegisteredSymbols());

            for (IAgeSymbol symbol : symbols) {
                API.addItemListEntry(MystItemFactory.api.buildSymbolPage(symbol.identifier()));
            }

            this.symbolsEnabled = true;
        }

        if (PluginNEIMystcraft.config.addLinkPanels) {
            Objects.log.log(Level.DEBUG, "Adding link panels to NEI view");

            for (ItemStack stack : Integrator.getAllLinkpanels()) {
                API.addItemListEntry(stack);
            }

            this.symbolsEnabled = true;
        }
    }

    @Override
    public void disable() {
        if (this.symbolsEnabled || this.linkpanelsEnabled) {
            Objects.log.log(Level.DEBUG, "Removing symbol pages and link panels from NEI view");

            ItemInfo.itemOverrides.removeAll(MystObjs.page);

            this.symbolsEnabled = false;
            this.linkpanelsEnabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return this.symbolsEnabled || this.linkpanelsEnabled;
    }
}
