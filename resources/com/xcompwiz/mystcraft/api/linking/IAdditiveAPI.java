
package com.xcompwiz.mystcraft.api.linking;

import net.minecraft.item.ItemStack;

/**
 * Supplies functions for adding effects to ink mixtures through item additives
 * 
 * @author xcompwiz
 * 
 */
public interface IAdditiveAPI {

    /*
     * These functions are used for the ink mixing system.
     * When a link panel is crafted from a pool of ink, each effect in the ink
     * has its probability chance of being on the link panel.
     * Adding items to the pool change the probabilities.
     * Each additive has some set of effects with a paired probability. When it
     * is added to the ink, those probabilities are added to the ink after
     * scaling the existing probabilities.
     * The effect probabilities in the ink are scaled based on the "free"
     * probability of the additive, or the inverse of the sum of the
     * probabilities of all effects on the additive.
     * 
     * A concrete example:
     * The ink pool currently has a 20% chance of adding disarm to the
     * linkpanel.
     * An additive is added with a 10% chance of intra-linking and a 10% chance
     * of disarm.
     * The sum of the additive's probabilities is 20%. The inverse of which is
     * 80%.
     * This means the ink pool's probabilities are scaled to "fit" in the 80%
     * (.2*.8 = .16)
     * The additives probabilities are then added straight, resulting in the
     * pool having a 26% chance of disarm and a 10% chance of intra-linking.
     * Note that these effects are selected independently when crafting, so
     * multiple effects are possible.
     * Also note that the maximum total probability is 100%.
     * Additives cannot add more than 100% probability total.
     */

    /**
     * Used to add an effect probability to an item
     * Note that this adds the effect along side any other effects already on
     * the item
     * The total probability of the effects on the item cannot exceed 1
     * When the item is added to the mixture the probabilities in the ink will
     * scale to fit in the "free" portion of the probability
     * 
     * @param itemstack
     *        The itemstack to match (exactly). Will use a stackSize=1 copy.
     * @param property
     *        The id of the property to add
     * @param probability
     *        The probability strength of the property to add to the mixture
     */
    public void addPropertyToItem(ItemStack itemstack, String property, float probability);

    /**
     * Used to add an effect probability to an item
     * Note that this adds the effect along side any other effects already on
     * the item
     * The total probability of the effects on the item cannot exceed 1
     * When the item is added to the mixture the probabilities in the ink will
     * scale to fit in the "free" portion of the probability
     * 
     * @param name
     *        The ore dictionary name to match
     * @param property
     *        The id of the property to add
     * @param probability
     *        The probability strength of the property to add to the mixture
     */
    public void addPropertyToItem(String name, String property, float probability);

    /**
     * Used to add an effect probability to an item
     * Note that this adds the effect along side any other effects already on
     * the item
     * The total probability of the effects on the item cannot exceed 1
     * When the item is added to the mixture the probabilities in the ink will
     * scale to fit in the "free" portion of the probability
     * 
     * @param itemId
     *        The item id to match
     * @param property
     *        The id of the property to add
     * @param probability
     *        The probability strength of the property to add to the mixture
     */
    public void addPropertyToItem(int itemId, String property, float probability);
}
