
package com.xcompwiz.mystcraft.api.internals;

/**
 * A basic algebra math formula interface
 * 
 * @author xcompwiz
 */
public interface IFormula {

    /**
     * Returns a value based on the value of x
     * 
     * @param x
     *        The x position
     * @return The y position
     */
    public abstract float calc(float x);

}
