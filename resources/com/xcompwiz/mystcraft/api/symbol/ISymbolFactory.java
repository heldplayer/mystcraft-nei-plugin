
package com.xcompwiz.mystcraft.api.symbol;

import net.minecraft.world.gen.feature.WorldGenerator;

import com.xcompwiz.mystcraft.api.internals.BlockDescriptor;

/**
 * Provides methods for generating boilerplate IAgeSymbols
 * These methods do not register the symbol directly. Use the SymbolAPI for
 * that.
 * The implementation of this is provided by MystAPI
 * Do NOT implement this yourself!
 * 
 * @author xcompwiz
 */
public interface ISymbolFactory {

    /**
     * Creates (but does not register!) a new 'Terrain Feature' symbol using a
     * basic WorldGenerator object
     * The WorldGenerator class must provide a default constructor (no
     * parameters)
     * It is not recommended that you use this method. This is meant primarily
     * for testing.
     * 
     * @param id
     *        The unique identifier for the symbol
     * @param gen
     *        The generator to use
     * @return The produced AgeSymbol, ready to be registered
     */
    public IAgeSymbol createSymbol(String id, Class<WorldGenerator> gen);

    /**
     * Creates (but does not register!) a new 'Block Modifier' symbol
     * The resultant symbol, if registered, will generate its own grammar rules
     * 
     * @param block
     *        The block descriptor to use
     * @param thirdword
     *        The third DrawableWord reference for the symbol. Should be
     *        something characteristic (ex. Terrain, Ore, Sea)
     * @param grammarWeight
     *        The weighting for the generated grammar rules. See
     *        {@link IGrammarAPI}
     * @param itemRarity
     *        The rarity score for finding the item as loot. 1 is common, 0 is
     *        impossible.
     * @return a new, unregistered modifier symbol that pushes a block to the
     *         modifier stack
     */
    public IAgeSymbol createSymbol(BlockDescriptor block, String thirdword, float grammarWeight, float itemRarity);
}
