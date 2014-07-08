package me.heldplayer.plugins.nei.mystcraft.client;

import java.util.Comparator;

/**
 * Sorter for link panels
 *
 * @author heldplayer
 */
@SuppressWarnings("rawtypes")
public class LinkPanelSorter implements Comparator {

    @Override
    public int compare(Object left, Object right) {
        return this.compare((String) left, (String) right);
    }

    public int compare(String left, String right) {
        return left.compareTo(right);
    }

}
