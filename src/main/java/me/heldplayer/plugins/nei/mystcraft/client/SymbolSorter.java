package me.heldplayer.plugins.nei.mystcraft.client;

import com.xcompwiz.mystcraft.symbol.IAgeSymbol;
import java.util.Comparator;

/**
 * Sorter for symbols
 *
 * @author heldplayer
 */
@SuppressWarnings("rawtypes")
public class SymbolSorter implements Comparator {

    @Override
    public int compare(Object left, Object right) {
        return this.compare((IAgeSymbol) left, (IAgeSymbol) right);
    }

    public int compare(IAgeSymbol left, IAgeSymbol right) {
        return left.displayName().compareTo(right.displayName());
    }

}
