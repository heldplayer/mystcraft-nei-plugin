
package com.xcompwiz.mystcraft.api.instability;

import com.xcompwiz.mystcraft.api.symbol.logic.IEnvironmentalEffect;

/**
 * Provides methods for generating boilerplate InstabilityProviders
 * 
 * @author xcompwiz
 * 
 */
public interface IInstabilityFactory {

    /**
     * Creates (but does not register!) a new instability provider for a given
     * effect
     * 
     * @param identifier
     *        The unique identifier for the provider to be created
     * @param effect
     *        The effect to use
     * @param stabilization
     *        The amount of stability this effects provides
     * @param rangeMin
     *        The minimum instability the world must have before this is can
     *        appear in the world
     * @param rangeMax
     *        The maximum instability the world may have before this stops being
     *        selectable to be added to the world
     * @param maxStack
     *        The maximum number of times this may be selected to be applied to
     *        the same world
     * @return The produced InstabilityProvider, ready to be registered
     */
    public IInstabilityProvider createProviderForEffect(String identifier, IEnvironmentalEffect effect, int stabilization, Integer rangeMin, Integer rangeMax, Integer maxStack);

}
