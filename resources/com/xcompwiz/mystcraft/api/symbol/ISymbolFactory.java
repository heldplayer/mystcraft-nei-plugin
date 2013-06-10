
package com.xcompwiz.mystcraft.api.symbol;

import net.minecraft.world.gen.feature.WorldGenerator;

import com.xcompwiz.mystcraft.api.internals.BlockDescriptor;

/**
 * Provides methods for generating boilerplate IAgeSymbols
 * These methods do not register the symbol directly. Use the SymbolAPI for
 * that.
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
     * 
     * @param block
     *        The block descriptor to use
     * @param thirdword
     *        The third DrawableWord reference for the symbol. Should be
     *        something characteristic (ex. Terrain, Ore, Sea)
     * @return a new, unregistered modifier symbol that pushes a block to the
     *         modifier stack
     */
    public IAgeSymbol createSymbol(BlockDescriptor block, String thirdword);
}
