
package com.xcompwiz.mystcraft.api.instability;

/**
 * Provides methods for registering providers to and interacting with the
 * instability system
 * The implementation of this is provided by MystAPI
 * Do NOT implement this yourself!
 * 
 * @author xcompwiz
 */
public interface IInstabilityAPI {

    /**
     * Registers an instability provider to the instability system
     * This makes it available for selection when a world is unstable as
     * governed by its internal
     * stability values
     */
    void registerInstability(IInstabilityProvider provider);
}
