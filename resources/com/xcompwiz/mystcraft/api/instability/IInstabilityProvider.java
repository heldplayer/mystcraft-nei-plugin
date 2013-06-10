
package com.xcompwiz.mystcraft.api.instability;

/**
 * Provides an instability mechanic description and registers effects using the
 * same approach as the symbols
 * Implement and register this through the InstabilityAPI to add your own
 * instability providers to Mystcraft
 */
public interface IInstabilityProvider {

    /**
     * Provides a unique string identifier for the provider
     */
    public String identifier();

    /**
     * Provides the amount of stability the mechanic provides
     */
    public int stabilization();

    /**
     * Returns the low end of the interval in which this instability mechanic
     * can be added
     * The mechanic will only be added to a world if the world's instability
     * score is contained within the described interval
     * Can return null (negative infinity)
     */
    public Integer minimumInstability();

    /**
     * Returns the high end of the interval in which this instability mechanic
     * can be added
     * The mechanic will only be added to a world if the world's instability
     * score is contained within the described interval
     * Can return null (positive infinity)
     */
    public Integer maximumInstability();

    /**
     * Called when the provider should register its effects to the passed
     * controller
     * This will be called only once, no matter what level the provider is for
     * the world (regardless of the number of times 'stacked')
     * 
     * @param level
     *        The level (stack count) of the provider [1-inf)
     */
    public void addEffects(IInstabilityController controller, Integer level);

    /**
     * Should return the maximum level of the mechanic or the number of times
     * the provider can stack
     * Can return null to indicate no maximum
     * 
     * @return the maximum level the provider can handle
     */
    public Integer maxLevel();
}
