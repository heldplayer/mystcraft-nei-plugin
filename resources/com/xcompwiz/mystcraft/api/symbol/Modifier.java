
package com.xcompwiz.mystcraft.api.symbol;

import java.util.List;

import com.xcompwiz.mystcraft.api.internals.BlockDescriptor;
import com.xcompwiz.mystcraft.api.internals.Color;
import com.xcompwiz.mystcraft.api.internals.ColorGradient;

public class Modifier {
    private Object value;
    public int dangling;

    public static final int dangling_default = 50;

    public Modifier() {
        this(null);
    }

    public Modifier(Object value) {
        this(value, dangling_default);
    }

    /**
     * 
     * @param value
     *        contained value
     * @param dangling
     *        The amount of instability to add if unused
     */
    public Modifier(Object value, int dangling) {
        this.value = value;
        this.dangling = dangling;
    }

    public Object asObject() {
        return this.value;
    }

    public Number asNumber() {
        if (this.value instanceof Number) {
            return (Number) this.value;
        }
        return null;
    }

    //Note: This will be replaced with an actual color class in the near future
    public Color asColor() {
        if (this.value instanceof Color) {
            return (Color) this.value;
        }
        return null;
    }

    public ColorGradient asGradient() {
        if (this.value instanceof ColorGradient) {
            return (ColorGradient) this.value;
        }
        return null;
    }

    public BlockDescriptor asBlockDescriptor() {
        if (this.value instanceof BlockDescriptor) {
            return (BlockDescriptor) this.value;
        }
        return null;
    }

    public List asList() {
        if (this.value instanceof List) {
            return (List) this.value;
        }
        return null;
    }
}
