package me.heldplayer.plugins.nei.mystcraft.client;

import java.util.List;

public class GrammarMapping {

    public final String name;
    public final List<String> children;
    public final float rarity;

    public GrammarMapping(String name, List<String> children, float rarity) {
        this.name = name;
        this.children = children;
        this.rarity = rarity;
    }

}
