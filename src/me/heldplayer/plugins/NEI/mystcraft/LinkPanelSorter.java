
package me.heldplayer.plugins.NEI.mystcraft;

import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class LinkPanelSorter implements Comparator {

    public int compare(String left, String right) {
        return left.compareTo(right);
    }

    @Override
    public int compare(Object left, Object right) {
        return this.compare((String) left, (String) right);
    }

}
