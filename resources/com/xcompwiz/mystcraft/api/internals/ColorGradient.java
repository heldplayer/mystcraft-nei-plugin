
package com.xcompwiz.mystcraft.api.internals;

import java.util.ArrayList;
import java.util.List;

public class ColorGradient {
    private List<Color> colors = new ArrayList<Color>();
    private List<Long> intervals = new ArrayList<Long>();
    private long length = 0;

    public int getColorCount() {
        return colors.size();
    }

    public void appendGradient(ColorGradient other) {
        for (int i = 0; i < other.colors.size(); ++i) {
            pushColor(other.colors.get(i), other.intervals.get(i));
        }
    }

    public void pushColor(Color color) {
        pushColor(color, null);
    }

    public void pushColor(Color color, Long interval) {
        if (color == null)
            return;
        if (interval == null || interval <= 0)
            interval = 12000L;
        colors.add((Color) color);
        intervals.add((long) (interval.longValue()));
        length += interval.longValue();
    }

    public Color getColor(long time) {
        //Case: No colors
        if (colors.size() == 0)
            throw new RuntimeException("Whoops, empty gradient!");

        //Case: Only one color
        if (colors.size() == 1)
            return colors.get(0);

        if (length <= 0)
            return colors.get(0);
        time = time % length;

        //Get first color
        int colorcounter = 0;
        while (time >= intervals.get(colorcounter)) {
            time -= intervals.get(colorcounter);
            colorcounter = (++colorcounter) % colors.size();
        }
        //Get second color
        int secondcolor = (colorcounter + 1) % colors.size();
        //Interpolate
        Color color1 = colors.get(colorcounter);
        Color color2 = colors.get(secondcolor);
        float interp = (float) time / (float) (intervals.get(colorcounter));
        Color colorout = new Color(interpolate(interp, color1.r, color2.r), interpolate(interp, color1.g, color2.g), interpolate(interp, color1.b, color2.b));
        return colorout;
    }

    private float interpolate(float interp, float val1, float val2) {
        return (val2 * interp) + (val1 * (1 - interp));
    }

    public long getLength() {
        return length;
    }

}
