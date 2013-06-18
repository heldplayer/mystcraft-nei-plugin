
package com.xcompwiz.mystcraft.api.internals;

public class Color {
    public final float r;
    public final float g;
    public final float b;

    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color average(Color other) {
        Color color = new Color((this.r + other.r) / 2, (this.g + other.g) / 2, (this.b + other.b) / 2);
        return color;
    }

    public Color average(float red, float green, float blue) {
        Color color = new Color((this.r + red) / 2, (this.g + green) / 2, (this.b + blue) / 2);
        return color;
    }

    public int asInt() {
        int iColor = ((int) (this.r * 255) << 16);
        iColor += ((int) (this.g * 255) << 8);
        iColor += ((int) (this.b * 255));
        return iColor;
    }
}
