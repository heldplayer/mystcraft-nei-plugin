
package me.heldplayer.plugins.nei.mystcraft.client;

import com.xcompwiz.mystcraft.api.internals.ColorGradient;

/**
 * Class used to store information for an ink mixer recipe
 * 
 * @author Mitchel
 * 
 */
public class InkMixerRecipe {

    public ColorGradient gradient;
    public String[] modifiers;

    public InkMixerRecipe(ColorGradient value1, String[] value2) {
        this.gradient = value1;
        this.modifiers = value2;
    }

}
