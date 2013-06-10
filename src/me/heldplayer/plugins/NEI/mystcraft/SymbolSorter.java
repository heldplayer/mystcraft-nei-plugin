
package me.heldplayer.plugins.NEI.mystcraft;

import java.util.Comparator;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;

/**
 * Sorter for symbols
 * 
 * @author heldplayer
 * 
 */
@SuppressWarnings("rawtypes")
public class SymbolSorter implements Comparator {

    public int compare(IAgeSymbol left, IAgeSymbol right) {
        return left.displayName().compareTo(right.displayName());
    }

    @Override
    public int compare(Object left, Object right) {
        return this.compare((IAgeSymbol) left, (IAgeSymbol) right);
    }

}
