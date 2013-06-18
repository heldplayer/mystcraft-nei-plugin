
package com.xcompwiz.mystcraft.api.symbol.words;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DrawableWord {

    private ArrayList<Integer> components = new ArrayList<Integer>();
    private ArrayList<Integer> colors = new ArrayList<Integer>();
    private String symbolSource = "/mods/mystcraft/textures/symbolcomponents.png";

    public DrawableWord() {}

    public DrawableWord(Integer[] components) {
        List<Integer> list = Arrays.asList(components);
        this.components.addAll(list);
    }

    public ArrayList<Integer> components() {
        return this.components;
    }

    public ArrayList<Integer> colors() {
        return this.colors;
    }

    public DrawableWord addDrawComponent(int slot, int color) {
        this.components.add(slot);
        this.colors.add(color);
        return this;
    }

    public DrawableWord addDrawComponent(int x, int y, int color) {
        return this.addDrawComponent(x + y * 8, color);
    }

    public DrawableWord addDrawComponents(int[] components, int color) {
        for (int i = 0; i < components.length; ++i) {
            this.addDrawComponent(components[i], color);
        }
        return this;
    }

    public DrawableWord addDrawComponents(int[] components, int[] colors) {
        int def = (colors.length > 0 ? colors[0] : 0);
        for (int i = 0; i < components.length; ++i) {
            this.addDrawComponent(components[i], (colors.length > i ? colors[i] : def));
        }
        return this;
    }

    public DrawableWord addDrawWord(int[][] word) {
        if (word.length == 2) {
            this.addDrawComponents(word[0], word[1]);
        }
        return this;
    }

    public String imageSource() {
        return this.symbolSource;
    }

    public DrawableWord setImageSource(String source) {
        this.symbolSource = source;
        return this;
    }
}
